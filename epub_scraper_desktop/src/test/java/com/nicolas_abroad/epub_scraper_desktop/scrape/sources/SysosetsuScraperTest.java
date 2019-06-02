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
        noVolumeDocument = scraper.parseHTMLDocument("https://ncode.syosetu.com/n0286ee/");
        volumeDocument = scraper.parseHTMLDocument("https://ncode.syosetu.com/n1419y/");
        chapter = scraper.parseHTMLDocument("https://ncode.syosetu.com/n7594ct/1/");
    }

    /**
     * Test parse author method on page without author link.
     */
    @Test
    public void testParseAuthor01() {
        String expected = "磯部勝彦";
        String actual = scraper.parseAuthor(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /**
     * Test parse author method on page with author link.
     */
    @Test
    public void testParseAuthor02() {
        String expected = "天笠恭介";
        String actual = scraper.parseAuthor(volumeDocument);
        assertEquals(expected, actual);
    }

    /**
     * Test parse story title method.
     */
    @Test
    public void testParseStoryTitle() {
        String expected = "あんずの輝き～舞浜発ファンタジーランド～";
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
        expected.add("第一章　雨・見合い・橙空の飛翔");
        expected.add("第二章　学校・友人・正体見たり");
        expected.add("第三章　訪問・衝撃・妖怪の住処にて");
        expected.add("第四章　決意・事故・悲しい決別");
        expected.add("第五章　勝負・互いの距離・そして――");
        expected.add("第六章　目覚め・繋がり・確かな約束");
        expected.add("終章");
        assertEquals(expected, scraper.parseVolumeTitles(volumeDocument));
    }

    /**
     * Test chapter title parsing.
     */
    @Test
    public void testParseChapterTitle() {
        String expected = "『プロローグ』";
        String actual = scraper.parseChapterTitle(chapter);
        assertEquals(expected, actual);
    }

    /**
     * Test chapter text parsing.
     */
    @Test
    public void testParseChapterText() {
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

    /**
     * Test parsing all chapters' urls.
     */
    @Test
    public void testParseAllChapterUrls() {
        // TODO finish test
        List<String> expected = new ArrayList<String>();
        expected.add("https://ncode.syosetu.com/n0286ee/1/");
        expected.add("https://ncode.syosetu.com/n0286ee/2/");
        expected.add("https://ncode.syosetu.com/n0286ee/3/");
        expected.add("https://ncode.syosetu.com/n0286ee/4/");
        expected.add("https://ncode.syosetu.com/n0286ee/5/");
        expected.add("https://ncode.syosetu.com/n0286ee/6/");
        expected.add("https://ncode.syosetu.com/n0286ee/7/");
        List<String> actual = scraper.parseAllChapterUrls(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /**
     * Test parsing chapters' urls by volume.
     */
    @Test
    public void testParseChaptersByVolume() {
        // prepare expected map
        Map<Integer, List<String>> expected = new HashMap<Integer, List<String>>();
        List<String> volume1 = new ArrayList<String>();
        volume1.add("https://ncode.syosetu.com/n1419y/1/");
        List<String> volume2 = new ArrayList<String>();
        volume2.add("https://ncode.syosetu.com/n1419y/2/");
        volume2.add("https://ncode.syosetu.com/n1419y/3/");
        volume2.add("https://ncode.syosetu.com/n1419y/4/");
        volume2.add("https://ncode.syosetu.com/n1419y/5/");
        List<String> volume3 = new ArrayList<String>();
        volume3.add("https://ncode.syosetu.com/n1419y/6/");
        volume3.add("https://ncode.syosetu.com/n1419y/7/");
        volume3.add("https://ncode.syosetu.com/n1419y/8/");
        volume3.add("https://ncode.syosetu.com/n1419y/9/");
        List<String> volume4 = new ArrayList<String>();
        volume4.add("https://ncode.syosetu.com/n1419y/10/");
        volume4.add("https://ncode.syosetu.com/n1419y/11/");
        volume4.add("https://ncode.syosetu.com/n1419y/12/");
        volume4.add("https://ncode.syosetu.com/n1419y/13/");
        volume4.add("https://ncode.syosetu.com/n1419y/14/");
        volume4.add("https://ncode.syosetu.com/n1419y/15/");
        volume4.add("https://ncode.syosetu.com/n1419y/16/");
        volume4.add("https://ncode.syosetu.com/n1419y/17/");
        List<String> volume5 = new ArrayList<String>();
        volume5.add("https://ncode.syosetu.com/n1419y/18/");
        volume5.add("https://ncode.syosetu.com/n1419y/19/");
        volume5.add("https://ncode.syosetu.com/n1419y/20/");
        volume5.add("https://ncode.syosetu.com/n1419y/21/");
        volume5.add("https://ncode.syosetu.com/n1419y/22/");
        List<String> volume6 = new ArrayList<String>();
        volume6.add("https://ncode.syosetu.com/n1419y/23/");
        volume6.add("https://ncode.syosetu.com/n1419y/24/");
        volume6.add("https://ncode.syosetu.com/n1419y/25/");
        volume6.add("https://ncode.syosetu.com/n1419y/26/");
        volume6.add("https://ncode.syosetu.com/n1419y/27/");
        List<String> volume7 = new ArrayList<String>();
        volume7.add("https://ncode.syosetu.com/n1419y/28/");
        volume7.add("https://ncode.syosetu.com/n1419y/29/");
        volume7.add("https://ncode.syosetu.com/n1419y/30/");
        List<String> volume8 = new ArrayList<String>();
        volume8.add("https://ncode.syosetu.com/n1419y/31/");
        expected.put(1, volume1);
        expected.put(2, volume2);
        expected.put(3, volume3);
        expected.put(4, volume4);
        expected.put(5, volume5);
        expected.put(6, volume6);
        expected.put(7, volume7);
        expected.put(8, volume8);

        Map<Integer, List<String>> actual = scraper.parseChaptersByVolume(volumeDocument);
        assertEquals(expected, actual);
    }

}
