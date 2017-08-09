/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.data

import android.os.Parcel
import android.os.Parcelable

/**
 * ReadHub
 * Created by Joe at 2017/7/27.
 */

class HotTopic : Parcelable {
    /**
     */

    var pageSize: Int = 0

    var totalItems: Int = 0

    var totalPages: Int = 0

    var data: List<Data>? = null

    class Data : Parcelable {
        /**
         */
        var isExband: Boolean? = false

        var id: String? = null

        var createdAt: String? = null

        var order: Int = 0

        var publishDate: String? = null

        var summary: String? = null

        var title: String? = null

        var updatedAt: String? = null

        var nelData: NelDataBean? = null

        var newsArray: List<NewsArrayBean>? = null

        var relatedTopicArray: List<*>? = null

        var entityRelatedTopics: List<EntityRelatedTopics>? = null

        class NelDataBean {
            var result: List<ResultBean>? = null

            class ResultBean {
                /**
                 */

                var entityId: Int = 0
                var entityName: String? = null
                var eventType: Int = 0
                var eventTypeLabel: String? = null
                var data: List<DataBean>? = null

                class DataBean {
                    /**
                     */

                    var id: String? = null
                    var title: String? = null
                    var url: String? = null
                    var mobileUrl: String? = null
                    var sources: List<SourcesBean>? = null

                    class SourcesBean {
                        /**
                         * name : 爱范儿
                         * url : http://www.ifanr.com/879251
                         * mobileUrl : http://www.ifanr.com/879251
                         */

                        var name: String? = null
                        var url: String? = null
                        var mobileUrl: String? = null
                    }
                }
            }
        }

        class NewsArrayBean {
            /**
             * id : 14944759
             * url : http://www.tuoniao.fm/breaking/12336.html
             * title : 小米获得10亿美元三年期再融资
             * groupId : 1
             * siteName : 鸵鸟电台
             * mobileUrl : http://www.tuoniao.fm/breaking/12336.html
             * authorName : 杜祎
             * duplicateId : 1
             * publishDate : 2017-07-26 14:07:12
             */

            var id: Int = 0
            var url: String? = null
            var title: String? = null
            var groupId: Int = 0
            var siteName: String? = null
            var mobileUrl: String? = null
            var authorName: String? = null
            var duplicateId: Int = 0
            var publishDate: String? = null
        }

        class EntityRelatedTopics {
            /**
             * entityId : 54
             * entityName : 小米
             * eventType : 0
             * eventTypeLabel :
             * data : [{"id":"3riHOS6wfge","title":"2017 年第二季度：华为、OPPO、vivo 稳居中国市场前三，小米逆袭，三星掉队","url":"http://www.ifanr.com/879251","mobileUrl":"http://www.ifanr.com/879251","sources":[{"name":"爱范儿","url":"http://www.ifanr.com/879251","mobileUrl":"http://www.ifanr.com/879251"},{"name":"199IT","url":"http://www.199it.com/archives/616777.html","mobileUrl":"http://www.199it.com/archives/616777.html"},{"name":"艾瑞网","url":"http://news.iresearch.cn/content/2017/07/269434.shtml","mobileUrl":"http://news.iresearch.cn/content/2017/07/269434.shtml"}]},{"id":"5iIaaIsFCii","title":"谷歌Pixel 2将首发高通骁龙836芯片 小米一加随后跟进","url":"http://www.cnbeta.com/articles/634847.htm","mobileUrl":"http://m.cnbeta.com/view/634847.htm","sources":[{"name":"CNBeta","url":"http://www.cnbeta.com/articles/634847.htm","mobileUrl":"http://m.cnbeta.com/view/634847.htm"},{"name":"新浪科技","url":"http://tech.sina.com.cn/mobile/n/n/2017-07-25/doc-ifyihrit1369124.shtml","mobileUrl":"https://tech.sina.cn/mobile/xp/2017-07-25/detail-ifyihrit1369124.d.html?pos=18"}]}]
             */

            var entityId: Int = 0
            var entityName: String? = null
            var eventType: Int = 0
            var eventTypeLabel: String? = null
            var data: List<DataBean>? = null

            class DataBean {
                /**
                 * id : 3riHOS6wfge
                 * title : 2017 年第二季度：华为、OPPO、vivo 稳居中国市场前三，小米逆袭，三星掉队
                 * url : http://www.ifanr.com/879251
                 * mobileUrl : http://www.ifanr.com/879251
                 * sources : [{"name":"爱范儿","url":"http://www.ifanr.com/879251","mobileUrl":"http://www.ifanr.com/879251"},{"name":"199IT","url":"http://www.199it.com/archives/616777.html","mobileUrl":"http://www.199it.com/archives/616777.html"},{"name":"艾瑞网","url":"http://news.iresearch.cn/content/2017/07/269434.shtml","mobileUrl":"http://news.iresearch.cn/content/2017/07/269434.shtml"}]
                 */

                var id: String? = null
                var title: String? = null
                var url: String? = null
                var mobileUrl: String? = null
                var sources: List<SourcesBean>? = null

                class SourcesBean {
                    /**
                     * name : 爱范儿
                     * url : http://www.ifanr.com/879251
                     * mobileUrl : http://www.ifanr.com/879251
                     */

                    var name: String? = null
                    var url: String? = null
                    var mobileUrl: String? = null
                }
            }
        }

        override fun toString(): String {
            return "DataBeanXX(id=$id, createdAt=$createdAt, order=$order, publishDate=$publishDate, summary=$summary, title=$title, updatedAt=$updatedAt, nelData=$nelData, newsArray=$newsArray, relatedTopicArray=$relatedTopicArray, entityRelatedTopics=$entityRelatedTopics)"
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<Data> = object : Parcelable.Creator<Data> {
                override fun createFromParcel(source: Parcel): Data = Data(source)
                override fun newArray(size: Int): Array<Data?> = arrayOfNulls(size)
            }
        }

        constructor()

        constructor(source: Parcel) : this(
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {}
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<HotTopic> = object : Parcelable.Creator<HotTopic> {
            override fun createFromParcel(source: Parcel): HotTopic = HotTopic(source)
            override fun newArray(size: Int): Array<HotTopic?> = arrayOfNulls(size)
        }
    }

    constructor()

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {}
}
