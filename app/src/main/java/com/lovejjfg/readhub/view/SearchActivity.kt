package com.lovejjfg.readhub.view

import android.app.SearchManager
import android.app.SharedElementCallback
import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.TransitionRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.toast
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.base.BaseAdapter
import com.lovejjfg.readhub.base.BaseViewHolder
import com.lovejjfg.readhub.data.DataManager
import com.lovejjfg.readhub.data.search.SearchItem
import com.lovejjfg.readhub.transitions.CircularReveal
import com.lovejjfg.readhub.utils.DateUtil
import com.lovejjfg.readhub.utils.FirebaseUtils
import com.lovejjfg.readhub.utils.ImeUtils
import com.lovejjfg.readhub.utils.JumpUitl
import com.lovejjfg.readhub.utils.TransitionUtils
import com.lovejjfg.readhub.utils.inflate
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_search.empty
import kotlinx.android.synthetic.main.activity_search.goTop
import kotlinx.android.synthetic.main.activity_search.noResultLayout
import kotlinx.android.synthetic.main.activity_search.parentContainer
import kotlinx.android.synthetic.main.activity_search.resultsContainer
import kotlinx.android.synthetic.main.activity_search.searchContent
import kotlinx.android.synthetic.main.activity_search.searchView
import kotlinx.android.synthetic.main.activity_search.searchback
import kotlinx.android.synthetic.main.holder_search.view.searchDes
import kotlinx.android.synthetic.main.holder_search.view.searchTime
import kotlinx.android.synthetic.main.holder_search.view.searchTitle
import kotlinx.android.synthetic.main.layout_no_search_result.noSearchResult

/**
 * Created by joe on 2018/10/29.
 * Email: lovejjfg@gmail.com
 */
open class SearchActivity : BaseActivity() {
    private lateinit var adapter: SearchAdapter
    private var currentPage: Int = 1
    private var searchKey: String? = null
    private val transitions = SparseArray<Transition>()
    private var emptyText: TextView? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_search
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        toast(getString(R.string.search_api_disable))
        val manager = LinearLayoutManager(this)
        searchContent.layoutManager = manager
        adapter = SearchAdapter()
        adapter.setOnItemClickListener { _, _, item ->
            JumpUitl.jumpTimeLine(this, item.topicId)
        }
        adapter.setLoadMoreListener {
            loadMore()
        }
        searchContent.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (Math.abs(dy) > 100) {
                    ImeUtils.hideIme(searchView)
                    hideNavigation()
                }
                if (!goTop.isShown && manager.findLastVisibleItemPosition() >= 20)
                    goTop.show()
                else if (goTop.isShown && manager.findLastVisibleItemPosition() < 20)
                    goTop.hide()
            }
        })

        searchContent.adapter = adapter
        goTop.setOnClickListener {
            searchContent.scrollToPosition(0)
            goTop.hide()
        }
        setupTransitions()
        setupSearchView()
    }

    private fun hideNavigation() {
        parentContainer.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE //保证view稳定
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun setupSearchView() {
        searchback.setOnClickListener {
            onBackPressed()
        }
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        // hint, inputType & ime options seem to be ignored from XML! Set in code
        searchView.isSoundEffectsEnabled = false
        searchView.queryHint = getString(R.string.search_hint)
        searchView.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        searchView.imeOptions = searchView.imeOptions or EditorInfo.IME_ACTION_SEARCH or
            EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (checkKeyFirst(query)) {
                    FirebaseUtils.logEggText(this@SearchActivity, query)
                } else {
                    startSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (TextUtils.isEmpty(query)) {
                    clearResults()
                }
                return true
            }
        })
    }

    private fun getTransition(@TransitionRes transitionId: Int): Transition {
        var transition: Transition? = transitions.get(transitionId)
        if (transition == null) {
            transition = TransitionInflater.from(this).inflateTransition(transitionId)
            transitions.put(transitionId, transition)
        }
        return transition!!
    }

    private fun clearResults() {
        unSubscribe()
        TransitionManager.beginDelayedTransition(
            searchContent,
            getTransition(R.transition.auto)
        )
        searchContent.scrollToPosition(0)
        adapter.clearList()
        searchContent.post {
            resultsContainer.isGone = true
            searchContent.isGone = true
        }
        empty.isGone = true
        setNoResultsVisibility(View.GONE)
    }

    private fun startSearch(nearKey: String?) {
        if (nearKey == null) {
            return
        }
        setNoResultsVisibility(View.GONE)
        FirebaseUtils.logSearchEvent(this, nearKey)
        ImeUtils.hideIme(searchView)
        searchView.clearFocus()
        searchContent.scrollToPosition(0)
        adapter.clearList()
        searchContent.post {
            resultsContainer.isVisible = true
            searchContent.isGone = true
        }
        this.searchKey = nearKey
        empty.visibility = View.VISIBLE
        DataManager.subscribe(this, DataManager.initSearch()
            .hotTopic(mapOf("page" to "$currentPage", "size" to "20", "ner_name" to nearKey, "type" to "hot"))
            , Consumer {
                empty.visibility = View.GONE
                val data = it.data
                if (data != null && data.items.isNotEmpty()) {
                    adapter.totalCount = data.totalItems
                    adapter.setList(data.items)
                    if (!searchContent.isVisible) {
                        TransitionManager.beginDelayedTransition(
                            resultsContainer,
                            getTransition(R.transition.search_show_results)
                        )
                        searchContent.visibility = View.VISIBLE
                    }
                    currentPage = data.pageIndex
                } else {
                    setNoResultsVisibility(View.VISIBLE)
                }
            }, Consumer {
                empty.visibility = View.GONE
                toast(R.string.search_api_disable, Toast.LENGTH_LONG)
                it.printStackTrace()
            })
    }

    private fun checkKeyFirst(nearKey: String): Boolean {
        return try {
            if (nearKey.contains("冯大") || nearKey.toLowerCase().contains("fenng")) {
                JumpUitl.jumpWeb(this, "https://weibo.com/fenng")
                true
            } else if (nearKey.contains("lovejjfg") || nearKey.contains("俊锅锅")) {
                JumpUitl.jumpWeb(this, "https://github.com/lovejjfg/")
                true
            } else if (nearKey.toLowerCase() == "readhub") {
                JumpUitl.jumpWeb(this, "https://readhub.cn")
                true
            } else if (nearKey.contains("哈哈") || nearKey.toLowerCase().contains("haha")) {
                EggsHelper.showCenterScaleView(this@SearchActivity)
                true
            } else
                false
        } catch (e: Exception) {
            CrashReport.postCatchedException(e)
            false
        }
    }

    private fun loadMore() {
        val nearKey = searchKey ?: return
        currentPage++
        DataManager.subscribe(this, DataManager.initSearch()
            .hotTopic(mapOf("page" to "$currentPage", "size" to "20", "ner_name" to nearKey, "type" to "hot"))
            , Consumer {
                val data = it.data
                if (data != null) {
                    adapter.totalCount = data.totalItems
                    adapter.appendList(data.items)
                    currentPage = data.pageIndex
                } else {
                    adapter.showEmpty()
                }
            }, Consumer {
                --currentPage
                it.printStackTrace()
            })
    }

    private fun setNoResultsVisibility(visibility: Int) {
        if (visibility == View.VISIBLE) {
            if (emptyText == null) {
                emptyText = noResultLayout.inflate() as TextView
                emptyText?.setOnClickListener {
                    searchView.setQuery("", false)
                    searchView.requestFocus()
                    ImeUtils.showIme(searchView)
                }
            }
            val message = String.format(
                getString(R.string.no_search_results), searchView.query.toString()
            )
            val ssb = SpannableStringBuilder(message)
            ssb.setSpan(
                StyleSpan(Typeface.ITALIC),
                message.indexOf('“') + 1,
                message.length - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            noSearchResult.text = ssb
        }
        emptyText?.visibility = visibility
    }

    override fun onBackPressed() {
        // clear the background else the touch ripple moves with the translation which looks bad
        searchback.background = null
        finishAfterTransition()
    }

    private fun setupTransitions() {
        // grab the position that the search icon transitions in *from*
        // & use it to configure the return transition
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementStart(
                sharedElementNames: List<String>,
                sharedElements: List<View>?,
                sharedElementSnapshots: List<View>
            ) {
                if (sharedElements != null && !sharedElements.isEmpty()) {
                    val searchIcon = sharedElements[0]
                    if (searchIcon.id != R.id.searchback) return
                    val centerX = (searchIcon.left + searchIcon.right) / 2
                    val transitionSet = window.returnTransition as? TransitionSet ?: return
                    val hideResults = TransitionUtils.findTransition(
                        transitionSet,
                        CircularReveal::class.java, R.id.resultsContainer
                    ) as? CircularReveal
                    hideResults?.setCenter(Point(centerX, 0))
                }
            }
        })
    }

    class SearchAdapter : BaseAdapter<SearchItem>() {
        override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<SearchItem> {
            return SearchHolder(parent.inflate(R.layout.holder_search))
        }

        override fun onViewHolderBind(holder: PowerHolder<SearchItem>, position: Int) {
            holder.onBind(list[position])
        }

        class SearchHolder(itemView: View) : BaseViewHolder<SearchItem>(itemView) {

            override fun onBind(t: SearchItem) {
                itemView.searchTitle.text = t.topicTitle?.trim()
                itemView.searchDes.text = t.topicSummary?.trim()
                itemView.searchTime.text = DateUtil.parseTime(t.topicCreateAt)
            }
        }
    }
}
