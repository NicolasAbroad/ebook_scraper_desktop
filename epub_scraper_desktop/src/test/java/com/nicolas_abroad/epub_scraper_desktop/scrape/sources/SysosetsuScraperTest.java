package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test class for the SyosetsuScraper class.
 * @author Nicolas
 */
public class SysosetsuScraperTest {

    private static SyosetsuScraper scraper = new SyosetsuScraper();

    private static Document noVolumeDocument;

    private static Document volumeDocument;

    private static Document chapter;

    /**
     * Set up html documents.
     * @throws IOException
     */
    @BeforeClass
    public static void setUp() throws IOException {
        noVolumeDocument = scraper.parseHTMLDocument("https://ncode.syosetu.com/n8802bq/");
        volumeDocument = scraper.parseHTMLDocument("https://ncode.syosetu.com/n6316bn/");
        chapter = scraper.parseHTMLDocument("https://ncode.syosetu.com/n7594ct/1/");
    }

    /**
     * Test parse author method.
     */
    @Test
    public void testParseAuthor() {
        String expected = "Ｙ．Ａ";
        String actual = scraper.parseAuthor(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /**
     * Test parse story title method.
     */
    @Test
    public void testParseStoryTitle() {
        String expected = "八男って、それはないでしょう！　";
        String actual = scraper.parseStoryTitle(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /**
     * Test if html has volumes.
     */
    @Test
    public void testHasVolumes01() {
        assertFalse(scraper.hasVolumes(noVolumeDocument));
    }

    /**
     * Test if html has volumes.
     */
    @Test
    public void testHasVolumes02() {
        assertTrue(scraper.hasVolumes(volumeDocument));
    }

    /**
     * Test volume title parsing.
     */
    @Test
    public void testParseVolumeTitles01() {
        List<String> expected = new ArrayList<String>();
        assertEquals(expected, scraper.parseVolumeTitles(noVolumeDocument));
    }

    /**
     * Test volume title parsing.
     */
    @Test
    public void testParseVolumeTitle02() {
        List<String> expected = new ArrayList<String>();
        expected.add("序章");
        expected.add("地位向上編");
        expected.add("森の騒乱編");
        expected.add("王都生活編");
        expected.add("魔王誕生編");
        expected.add("聖魔対立編");
        expected.add("魔都開国編");
        expected.add("魔人暗躍編");
        expected.add("帝国侵攻編");
        expected.add("竜魔激突編");
        expected.add("天魔大戦編");
        expected.add("色々番外編");
        assertEquals(expected, scraper.parseVolumeTitles(volumeDocument));
    }

    @Test
    public void testParseChapterTitle() {
        String expected = "『プロローグ』";
        String actual = scraper.parseChapterTitle(chapter);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseChapterText() throws IOException {
        String expected = "<div class=\"novel_bn\">\n"
                + "<a href=\"/n7594ct/2/\">次へ&#xa0;&gt;&gt;</a></div><div id=\"novel_no\">1/519</div><p class=\"novel_subtitle\">『プロローグ』</p><div id=\"novel_p\" class=\"novel_view\">\n"
                + "<p id=\"Lp1\">初投稿、というか初小説です。</p>\n" + "<p id=\"Lp2\">よろしくお願いします。</p>\n"
                + "</div><div id=\"novel_honbun\" class=\"novel_view\">\n" + "<p id=\"L1\">　人はあっけなく死ぬものだ。</p>\n"
                + "<p id=\"L2\"><br></p>\n" + "<p id=\"L3\">　そんなことはわかっていた。</p>\n" + "<p id=\"L4\"><br></p>\n"
                + "<p id=\"L5\">　それでも、衝撃だった。</p>\n" + "<p id=\"L6\"><br></p>\n" + "<p id=\"L7\">　父と母が死んだのだという。</p>\n"
                + "<p id=\"L8\"><br></p>\n" + "<p id=\"L9\">　交通事故だったと。</p>\n" + "<p id=\"L10\"><br></p>\n"
                + "<p id=\"L11\">　ふたりが外に出る前、最後に交わした言葉はなんだったか。</p>\n" + "<p id=\"L12\"><br></p>\n"
                + "<p id=\"L13\">　病院にも葬式にも行ったらしいが、いまいち覚えていない。</p>\n" + "<p id=\"L14\"><br></p>\n"
                + "<p id=\"L15\">　いや、覚えているんだけれども、靄がかかったように曖昧で。</p>\n" + "<p id=\"L16\"><br></p>\n"
                + "<p id=\"L17\">　3年ぶりに妹にも会ったはずなのに、いまいち覚えていない。</p>\n" + "<p id=\"L18\"><br></p>\n"
                + "<p id=\"L19\">　そういえば、家の外に出たのは10年ぶりだったはずだ。</p>\n" + "<p id=\"L20\"><br></p>\n"
                + "<p id=\"L21\">　10年ぶりの外出だったのに、あまり覚えていないというのももったいない気がする。</p>\n" + "<p id=\"L22\"><br></p>\n"
                + "<p id=\"L23\">　あっさり出られたのは、父と母からの最後の贈り物なのかもしれない。</p>\n" + "<p id=\"L24\"><br></p>\n"
                + "<p id=\"L25\">　引き換えがふたりの命なら、特にうれしくはないけれども。</p>\n" + "<p id=\"L26\"><br></p>\n"
                + "<p id=\"L27\">　外に出よう。</p>\n" + "<p id=\"L28\"><br></p>\n"
                + "<p id=\"L29\">　10年、引きこもりのニートだった俺に、温かく接してくれた父と母。</p>\n" + "<p id=\"L30\"><br></p>\n"
                + "<p id=\"L31\">　このまま引きこもりを続けたら、ふたりの死が無駄になった気がするから。</p>\n" + "<p id=\"L32\"><br></p>\n"
                + "<p id=\"L33\">　外に出よう。</p>\n" + "</div><div class=\"novel_bn\">\n"
                + "<a href=\"/n7594ct/2/\">次へ&#xa0;&gt;&gt;</a><a href=\"https://ncode.syosetu.com/n7594ct/\">目次</a></div>";
        String actual = scraper.parseChapterText(chapter);
        assertEquals(expected, actual);
    }

    @Test
    public void testSortChaptersByVolume() {
        Map<Integer, List<String>> volumesMap = new HashMap<Integer, List<String>>();
        // TODO finish test
    }

}
