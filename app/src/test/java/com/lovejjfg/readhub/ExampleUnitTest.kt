package com.lovejjfg.readhub

import org.jsoup.Jsoup
import org.junit.Test
import java.util.Calendar
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        val test = "</em><strong><em>《好奇心日报（www.qdaily.com）》发布，即使我们允许了也不许转载＊&nbsp;</em></strong></span>"

        val child = Jsoup.parse(test).body().child(0)
        println(child.outerHtml())
    }
}
