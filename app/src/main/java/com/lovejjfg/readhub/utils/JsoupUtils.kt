package com.lovejjfg.readhub.utils

import android.text.TextUtils
import android.util.Log
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.detail.DetailItems
import io.reactivex.ObservableEmitter
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.regex.Pattern

/**
 * Created by Joe on 2017/4/19.
 * Email lovejjfg@gmail.com
 */

object JsoupUtils {

    private val FLAG_BLOCK = 1
    val FLAG_H1 = FLAG_BLOCK shl 1
    val FLAG_H6 = FLAG_BLOCK shl 2
    private val FLAG_UNDERLINE = FLAG_BLOCK shl 3
    private val FLAG_COLOR = FLAG_BLOCK shl 4
    val FLAG_HREF = FLAG_BLOCK shl 5
    var currentFlag = 0

    @JvmOverloads
    fun parse(p: Elements, subscriber: ObservableEmitter<in DetailItems>, attr: String, flag: Int = 0) {
        Log.i("TAG", "parse: " + p.outerHtml())
        for (e in p) {
            currentFlag = flag
            var gravity: String? = attr
            if (TextUtils.isEmpty(attr)) {
                gravity = getGravity(e)
            }
            val s1 = e.tagName()
            //如果是引用标签 或者包含引用标签
            if (TextUtils.equals("blockquote", s1)) {
                Log.i("TAG", "parse: 引用样式")
                currentFlag = currentFlag or FLAG_BLOCK
                parse(e.children(), subscriber, attr, currentFlag)
                continue
            }
            if (TextUtils.equals("h1", e.tagName())) {
                Log.i("TAG", "parse:这是一个h1 " + e.html())
                currentFlag = currentFlag or FLAG_H1
            }
            if (TextUtils.equals("h6", e.tagName())) {
                Log.i("TAG", "parse:这是一个h6 " + e.html())
                currentFlag = currentFlag or FLAG_H6
            }

            val img = e.select("img,iframe")
            var html = e.html()
            if (img != null && !img.isEmpty()) {
                val size = img.size
                for (i in 0 until size) {
                    val ee = img[i]
                    val s = ee.outerHtml()//图片的标签地址
                    val substring =
                        html.split(Pattern.quote(s).toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()//图片见面的text
                    if (substring.isEmpty()) {//没有可以切割的
                        addImage(subscriber, ee)
                        continue
                    }
                    if (i != size - 1) {
                        addText(subscriber, gravity, substring[0], currentFlag)
                        addImage(subscriber, ee)
                        if (substring.size == 2) {
                            html = substring[1]
                        }
                    } else {
                        if (substring.size == 2) {
                            addText(subscriber, gravity, substring[0], currentFlag)
                            addImage(subscriber, ee)
                            addText(subscriber, gravity, substring[1], currentFlag)
                        } else {
                            addText(subscriber, gravity, substring[0], currentFlag)
                            addImage(subscriber, ee)
                        }
                    }
                }
            } else {
                //p 标签只含有 br标签
                html = if (TextUtils.equals(e.tagName(), "p")) {
                    e.html()
                } else {
                    e.outerHtml()
                }
                if (TextUtils.isEmpty(e.text())) {
//                    if (!TextUtils.isEmpty(html) && html.contains("<br>")) {
//                        addEmptyText(subscriber)
//                    }
                    continue
                }
                addText(subscriber, gravity, html, currentFlag)
            }
        }
        //        Logger.e("----------");
        //        }
    }

    private fun addText(subscriber: ObservableEmitter<in DetailItems>, gravity: String?, html: String, flag: Int) {
        if (TextUtils.isEmpty(html)) {
            return
        }
        Log.i("TAG", "parse:Text:: " + html)
        //过滤文末的<br>标签
        val select = Jsoup.parse(html).select("br")
        val size = select.size
        if (size > 0) {
            val substring =
                html.split(Pattern.quote("<br>").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()//图片见面的text
            if (substring.isEmpty()) {//没有可以切割的
                Log.i("TAG", "文字解析结果: 没有可以切割的")
                addEmptyText(subscriber)
                return
            }
            val join = TextUtils.join("<br>", substring)
            addContent(subscriber, gravity, join, flag)
        } else {
            addContent(subscriber, gravity, html, flag)
        }
    }

    private fun addContent(html1: String, flag: Int, sb: StringBuilder, addSpan: Boolean) {
        var html = html1
        if (!html.startsWith("<span") || addSpan) {
            html = "<span>$html</span>"
        }
        Log.i("TAG", "parseContent: " + html)
        val span = Jsoup.parse(html).select("body > *")
        for (s in span) {
            //            boolean isClor = haveFlag(FLAG_COLOR, flag);
            //            boolean isUnderline = haveFlag(FLAG_UNDERLINE, flag);
            var currentFlag = flag
            val style = s.attr("style")
            if (style.contains("color")) {
                //                isClor = true;
                currentFlag = addFlag(FLAG_COLOR, currentFlag)
            }
            //            "style" -> "font-size: 14px; text-align: justify; text-decoration: underline; color: rgb(250, 192, 143);"
            if (style.contains("text-decoration: underline") || style.contains("text-decoration:underline")) {
                //                isUnderline = true;
                currentFlag = addFlag(FLAG_UNDERLINE, currentFlag)
            }

            val childSpan = s.select("* > span")

            if (!childSpan.isEmpty()) {
                //                int size = childSpan.size();
                //                for (int i = 0; i < size; i++) {
                val point = childSpan[0].outerHtml()
                val split = s.outerHtml().split(Pattern.quote(point).toRegex(), 2).toTypedArray()
                when {
                    split.size == 2 -> {
                        addText(sb, currentFlag, split[0])
                        addContent(point, currentFlag, sb, false)
                        addContent(split[1], currentFlag, sb, false)
                        //                        addText(sb, currentFlag, split[1]);
                        Log.i("TAG", "addContent:[0]: " + split[0])
                        Log.i("TAG", "addContent:[1]: " + split[1])
                    }
                    split.size == 1 -> {
                        Log.e("TAG", "addContent: 只有一个没发切割：：" + s.outerHtml())
                        addContent(point, currentFlag, sb, false)
                        addContent(split[0], currentFlag, sb, false)
                    }
                    else -> Log.e("TAG", "addContent: 没发切割：：" + s.outerHtml())
                }
                //                }
                Log.i("TAG", "addContent: 嵌套span标签：" + s.html())
            } else {//没有自标签
                addText(sb, currentFlag, s)
            }
        }
    }

    private fun addText(sb: StringBuilder, flag: Int, element: Element) {

        val img = element.select("a")
        val html = element.outerHtml()
        if (img != null && !img.isEmpty()) {
            val ee = img.get(0)
            val s = ee.outerHtml()//图片的标签地址
            val substring = html.split(Pattern.quote(s).toRegex(), 2).toTypedArray()//图片见面的text
            if (substring.size == 0) {//没有可以切割的
                addATag(sb, ee, flag)
            }
            val s0 = substring[0]
            if (substring.size == 2) {
                //拆掉的时候可能会丢失一些属性
                if (!TextUtils.isEmpty(s0)) {
                    addContent(s0, flag, sb, false)
                }
                addATag(sb, ee, flag)
                var s1 = substring[1]
                if (!TextUtils.isEmpty(s1)) {
                    s1 = correctHtml(s1)
                    addContent(s1, flag, sb, false)
                }
            } else {
                if (!TextUtils.isEmpty(s0)) {
                    addContent(s0, flag, sb, false)
                }
                addATag(sb, ee, flag)
            }
        } else {
            addText(sb, flag, element.outerHtml())
        }
    }

    private fun correctHtml(s1: String): String {
        var s: String = s1
        if (s1.contains("</strong>") && !s1.contains("<strong>")) {
            s = "<strong>" + s1
        }
        if (s1.contains("</b>") && !s1.contains("<b>")) {
            s = "<b>" + s1
        }
        if (s1.contains("</u>") && !s1.contains("<u>")) {
            s = "<u>" + s1
        }
        return s
    }

    var map = HashMap<String, String>()

    private fun addATag(sb: StringBuilder, ee: Element, flag: Int) {

        val colorStart = "<font color=\"#68AEFF\">"
        val colorEnd = "</font>"
        val href = ee.attr("href")
        //        Log.i("TAG", "addContent:href:: " + href);
        var text = ee.text()
        map[text] = href
        text = String.format("%s<u>%s</u>%s", colorStart, text, colorEnd)
        sb.append(text)
        currentFlag = addFlag(currentFlag, FLAG_HREF)
    }

    private fun addText(sb: StringBuilder, flag: Int, s: String) {

        val colorStart = "<font color=\"#8b8b8b\">"
        val colorEnd = "</font>"

        val isClor = haveFlag(FLAG_COLOR, flag)
        val isUnderline = haveFlag(FLAG_UNDERLINE, flag)
        if (isClor) {
            sb.append(colorStart)
            if (isUnderline) {
                sb.append("<u>")
            }
            sb.append(s)
            if (isUnderline) {
                sb.append("</u>")
            }
            sb.append(colorEnd)
        } else {
            if (isUnderline) {
                sb.append("<u>")
            }
            sb.append(s)
            if (isUnderline) {
                sb.append("</u>")
            }
        }
    }

    private fun addContent(subscriber: ObservableEmitter<in DetailItems>, gravity: String?, html: String, flag: Int) {
        if (TextUtils.isEmpty(html)) {
            return
        }
        //        String colorStart = "<font color=\"#ffff00ff\">";
        val sb = StringBuilder()
        addContent(html, flag, sb, true)
        Log.i("TAG", "addContent: 最后输出结果：：" + sb.toString())
        addRealText(subscriber, gravity, sb.toString(), flag)
    }

    private fun addRealText(
        subscriber: ObservableEmitter<in DetailItems>,
        gravity: String?,
        texts: String,
        flag1: Int
    ) {
        var text = texts
        var flag = flag1
        Log.i("TAG", "addRealText: " + text)
        if (TextUtils.isEmpty(text)) {
            return
        }
        val text1 = Jsoup.parse(text).body().text()
        // maybe you should call addEmptyText() when text was empty.
        if (TextUtils.isEmpty(text1)) {
            return
        }

        val gravity1 = getGravity(text)
        val item = DetailItems(Constants.TYPE_PARSE_TEXT)
        //新增超链接解析
        if (haveFlag(currentFlag, FLAG_HREF)) {
            item.map = HashMap(map)
        }
        Log.i("TAG", "文字解析结果: " + text)
        item.isBlockquote = haveFlag(FLAG_BLOCK, flag)
        item.flag = flag
        item.text = text
        item.gravity = if (TextUtils.isEmpty(gravity1)) gravity else gravity1
        subscriber.onNext(item)
        currentFlag = 0
        map.clear()
    }

    private fun addEmptyText(subscriber: ObservableEmitter<in DetailItems>) {
        Log.i("TAG", "addEmptyText")
        val item = DetailItems(Constants.TYPE_PARSE_TEXT)
        item.text = null
        subscriber.onNext(item)
    }

    private fun addImage(subscriber: ObservableEmitter<in DetailItems>, first: Element) {
        val item: DetailItems
        val aClass = first.attr("class").trim { it <= ' ' }
        if (TextUtils.isEmpty(aClass)) {//
            var href: String? = null
            if (TextUtils.equals(first.parent().tagName(), "a")) {
                href = first.parent().attr("href")
                Log.i("TAG", "addImage: 图片被a嵌套了：$href")
            }
            val src = first.attr("src")
            val alt = first.attr("alt")
            item = DetailItems(src, alt)
            item.href = href
            subscriber.onNext(item)
            //            Logger.e("最后输出结果：这是图片 " + src + "    相关参数" + width + " * " + height + ";" + alt);
        }
    }

    private fun getGravity(e: Element): String? {
        val style = e.attr("style")
        var gravity: String? = null
        if (style != null && style.contains("text-align")) {
            gravity = style.substring(style.indexOf("text-align:") + 11).trim { it <= ' ' }
            //            Logger.e("gravite:" + gravity);
        }
        return gravity
    }

    private fun getGravity(style: String?): String? {
        //        String style = e.attr("style");
        var gravity: String? = null
        if (style != null && style.contains("text-align")) {
            gravity = style.substring(style.indexOf("text-align:") + 11).trim { it <= ' ' }
            //            Logger.e("gravite:" + gravity);
        }
        return gravity
    }

    fun haveFlag(flag: Int, value: Int): Boolean {
        return flag and value != 0
    }

    private fun addFlag(flag: Int, value: Int): Int {
        return if (haveFlag(flag, value)) {
            value
        } else value or flag
    }

    private fun removeFlag(flag: Int, value: Int): Int {
        return if (!haveFlag(flag, value)) {
            value
        } else value xor flag
    }
}
