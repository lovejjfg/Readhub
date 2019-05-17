package com.lovejjfg.readhub.data.topic.detail

import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.data.topic.NewsArrayItem
import com.lovejjfg.readhub.view.widget.ConnectorView
import com.lovejjfg.readhub.view.widget.ConnectorView.Type.NODE

/**
 * Created by joe on 2017/9/13.
 * lovejjfg@gmail.com
 */
class DetailItems {
    var detail: TopicDetail? = null
    var newsItem: NewsArrayItem? = null
    var timeLine: TopicsItem? = null
    var type: Int = 0
    var timeLineType: ConnectorView.Type = NODE
    var img: String? = null
    var alt: String? = null
    var text: String? = null
    var href: String? = null
    var gravity: String? = null
    var isBlockquote: Boolean = false
    var flag: Int = 0
    var map: HashMap<String, String> = HashMap()

    constructor(detail: TopicDetail) {
        this.detail = detail
        type = Constants.TYPE_HEADER
    }

    constructor(newsItem: NewsArrayItem) {
        this.newsItem = newsItem
        this.type = Constants.TYPE_NEWS
    }

    constructor(timeLine: TopicsItem) {
        this.timeLine = timeLine
        this.type = Constants.TYPE_TIMELINE
    }

    constructor() {
        this.type = Constants.TYPE_DIVIDER
    }

    constructor(img: String, alt: String?) {
        this.img = img
        this.alt = alt
        this.type = Constants.TYPE_PARSE_IMG
    }

    constructor(type: Int) {
        this.type = type
    }
}
