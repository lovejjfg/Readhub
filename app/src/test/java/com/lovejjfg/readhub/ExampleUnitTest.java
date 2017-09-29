package com.lovejjfg.readhub;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String test = "</em><strong><em>《好奇心日报（www.qdaily.com）》发布，即使我们允许了也不许转载＊&nbsp;</em></strong></span>";

        Element child = Jsoup.parse(test).body().child(0);
        System.out.println(child.outerHtml());

    }
}