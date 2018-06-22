package com.lovejjfg.readhub

import org.jsoup.Jsoup
import org.junit.Test

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

    @Test
    fun testTranslate() {
        var s1 = "xxxx"
        var s2 = s1
        println(s2)
        s1 = "xxxx2"
        println(s1)
        println(s2)

        var t1 = TestModel("test")
        val t2 = t1
        println(t2.name)
        t1 = TestModel("test changed")
        println(t1.name)
        println(t2.name)
    }

    @Test
    fun testHash() {
        val key = 7
        var h: Int
        h = key.hashCode()
        println("before:$h")
        println("向右移动:${h.ushr(16)}")

        println(h xor h.ushr(16))
        val message = h xor h.ushr(16)
        println(message and 15)
    }

    @Test
    fun testReplace() {
        val str1 = "xxx"
        val str2 = null
        val str3 = "oooo"
        val format = String.format("%s%s%s", str1, str2, str3)
        println(format)
    }

    data class TestModel(var name: String? = null)
}