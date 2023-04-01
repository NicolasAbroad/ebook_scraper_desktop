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
public class KakuyomuScraperTest {

    private static KakuyomuScraper scraper = new KakuyomuScraper();

    private static Document noVolumeDocument;

    private static Document volumeDocument;

    private static Document chapter;

    /**
     * Set up html documents.
     * @throws IOException
     */
    @BeforeClass
    public static void setUp() throws IOException {
        noVolumeDocument = scraper.parseHTMLDocument("https://kakuyomu.jp/works/16816700426011043454");
        volumeDocument = scraper.parseHTMLDocument("https://kakuyomu.jp/works/1177354054891792326");
        chapter = scraper
                .parseHTMLDocument("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891799922");
    }

    /** Test parse author method on page without author link. */
    @Test
    public void testParseAuthor01() {
        String expected = "木の芽";
        String actual = scraper.parseAuthor(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /** Test parse author method on page with author link. */
    @Test
    public void testParseAuthor02() {
        String expected = "此見えこ";
        String actual = scraper.parseAuthor(volumeDocument);
        assertEquals(expected, actual);
    }

    /** Test parse story title method. */
    @Test
    public void testParseStoryTitle() {
        String expected = "勇者パーティーをクビになったので故郷に帰ろうとしたら、パーティーメンバー全員がついてきたんだが～追いかけてきたみんなと始める結婚スローライフ～";
        String actual = scraper.parseStoryTitle(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /** Test if html has volumes. */
    @Test
    public void testHasVolumes01() {
        assertFalse(scraper.hasVolumes(noVolumeDocument));
    }

    /** Test if html has volumes. */
    @Test
    public void testHasVolumes02() {
        assertTrue(scraper.hasVolumes(volumeDocument));
    }

    /** Test volume title parsing. */
    @Test
    public void testParseVolumeTitles01() {
        List<String> expected = new ArrayList<String>();
        assertEquals(expected, scraper.parseVolumeTitles(noVolumeDocument));
    }

    /** Test volume title parsing. */
    @Test
    public void testParseVolumeTitle02() {
        List<String> expected = new ArrayList<String>();
        expected.add("本編");
        expected.add("後日談");
        assertEquals(expected, scraper.parseVolumeTitles(volumeDocument));
    }

    /** Test chapter title parsing. */
    @Test
    public void testParseChapterTitle() {
        String expected = "第3話";
        String actual = scraper.parseChapterTitle(chapter);
        assertEquals(expected, actual);
    }

    /** Test chapter text parsing. */
    @Test
    public void testParseChapterText() {
        String expected = "<header id=\"contentMain-header\">\r\n" + "\r\n"
                + "        <p class=\"widget-episodeTitle js-vertical-composition-item\">第3話</p>\r\n"
                + "      </header><div class=\"widget-episode js-episode-body-container\">\r\n"
                + "        <div class=\"widget-episode-inner\">\r\n"
                + "          <div class=\"widget-episodeBody js-episode-body\" data-viewer-history-path=\"/works/1177354054891792326/episodes/1177354054891799922/history\" data-viewer-reading-quantity-path=\"/works/1177354054891792326/episodes/1177354054891799922/reading_quantity\"><p id=\"p1\">「こたちゃん、大ニュース！」</p>\r\n"
                + "<p id=\"p2\"> 　僕の部屋に駆け込んできた千紗ちゃんが開口一番にそう言ったのは、豚丼に使うもやしとピーマンを炒めていたときだった。突進するような勢いで僕の真横まで興奮気味に駆け寄ってきた彼女に、僕はあわててコンロの火を止める。横を向けば、吐息がかかるほど近くまで顔を寄せた千紗ちゃんの大きな目と目が合った。</p>\r\n"
                + "<p id=\"p3\"> 「突き止めたの！」</p>\r\n" + "<p id=\"p4\"> 「なにを？」</p>\r\n" + "<p id=\"p5\"> 「例の彼の職場！」</p>\r\n"
                + "<p id=\"p6\"> 「え」</p>\r\n"
                + "<p id=\"p7\"> 　走ってきたのか、頬を紅潮させた彼女からは、いつもと同じ砂糖菓子みたいな甘い匂いがした。千紗ちゃんがつけている香水の匂い。ふわふわして甘ったるいその匂いは、千紗ちゃんにぴったりだといつも思う。</p>\r\n"
                + "<p id=\"p8\"> 「会えたの？」</p>\r\n" + "<p id=\"p9\"> 「うん！　今日のバイト前に見つけたの！」</p>\r\n"
                + "<p id=\"p10\"> 　弾けるような笑顔でそう言って、千紗ちゃんはその場でぴょんぴょん飛び跳ねる。そんな様子は、同い年とは思えないほど子どもっぽくて、「よかったね」と僕もつられるように笑顔になりながら</p>\r\n"
                + "<p id=\"p11\"> 「職場ってことは、社会人だったんだ？」</p>\r\n" + "<p id=\"p12\"> 「わかんない。バイト先かも。話してはないもん」</p>\r\n"
                + "<p id=\"p13\"> 「え、なんで？　声かけなかったの？」</p>\r\n"
                + "<p id=\"p14\"> 　探していた人をようやく見つけて、千紗ちゃんはそこで臆するような子じゃないはずだ。恋する千紗ちゃんは、基本的にいつも猪突猛進だった。</p>\r\n"
                + "<p id=\"p15\"> 「うん、かけたかったんだけどね。その人、道路の反対側にいたの。横断歩道も赤だったし、駆け寄れなくて。だからとりあえず見失わないように後をつけたの。そうしたら西通りにある喫茶店に入っていったんだ。それも正面からじゃなくて裏口から。だからお客さんじゃなくて働いてるんだろうなって」</p>\r\n"
                + "<p id=\"p16\"> 　西通りにある喫茶店。言われて、たしかにあのあたりに、一軒ちょっと大きめな喫茶店があったのを思い出す。</p>\r\n"
                + "<p id=\"p17\"> 　それにしても、千紗ちゃんがその人が探していた交差点から西通りまでは、けっこうな距離がある。歩いたら二十分ほどかかるのではないだろうか。その距離を追いかけたということか。</p>\r\n"
                + "<p id=\"p18\"> 　萩原あたりが聞いたら、間違いなくストーカーとか言ってきそうだな、なんて頭の隅で思いながら</p>\r\n"
                + "<p id=\"p19\"> 「千紗ちゃんは、その喫茶店には入らなかったの？」</p>\r\n"
                + "<p id=\"p20\"> 「うん。バイトの時間だったから、今日は入れなかったの。いちおうバイト終わりにも行ってみて中覗いて来たんだけど、彼いないみたいだったから、また明日行ってみようと思って」</p>\r\n"
                + "<p id=\"p21\"> 「そっか」</p>\r\n" + "<p id=\"p22\"> 「だからこたちゃん、今日は帰るね！」</p>\r\n"
                + "<p id=\"p23\"> 「へ？」</p>\r\n"
                + "<p id=\"p24\"> 　間の抜けた声を上げる僕にかまわず、千紗ちゃんはさっさと踵を返して玄関のほうへ歩き出していたので</p>\r\n"
                + "<p id=\"p25\"> 「ご飯は？　食べて行かないの？」</p>\r\n" + "<p id=\"p26\"> 「うん、今日はいい。早く帰って明日の準備したいもん」</p>\r\n"
                + "<p id=\"p27\"> 「準備？」</p>\r\n" + "<p id=\"p28\"> 「明日着ていく服を選んだりー、お顔のパックもしなきゃ！」</p>\r\n"
                + "<p id=\"p29\"> 「じゃあおかずタッパーに詰めるから、持って帰りなよ。どうせ帰ってもなにも食べるものないんでしょ」</p>\r\n"
                + "<p id=\"p30\"> 　そう言うと、千紗ちゃんは足を止めて少しだけ考えてから</p>\r\n"
                + "<p id=\"p31\"> 「んー、いいや。今日はなんだか胸がいっぱいで、なにも入りそうにないから」</p>\r\n"
                + "<p id=\"p32\"> 「いや駄目だって、ちゃんと食べなきゃ」</p>\r\n" + "<p id=\"p33\"> 「心配してくれてありがとね、こたちゃん」</p>\r\n"
                + "<p id=\"p34\"> 　僕の言葉は耳に入っていない様子で、千紗ちゃんはうきうきと弾む声で言って、玄関に脱ぎ散らかしていたスニーカーを拾う。そうしてそれを履き始めた彼女の背中を眺めながら、僕はふと首を傾げると</p>\r\n"
                + "<p id=\"p35\"> 「え、じゃあなんで」</p>\r\n" + "<p id=\"p36\"> 「ん？」</p>\r\n"
                + "<p id=\"p37\"> 「何しにここに来たの？」</p>\r\n"
                + "<p id=\"p38\"> 　僕の住むアパートは、千紗ちゃんのバイト先とも千紗ちゃんの住むアパートとも方向が違う。千紗ちゃんが帰りしなにふらっと立ち寄れる場所にはない。ここへ来るには、わざわざ僕のアパートのほうへ足を伸ばさなければならないはずだ。</p>\r\n"
                + "<p id=\"p39\"> 　僕の質問に、千紗ちゃんはきょとんとした顔でこちらを振り向いて</p>\r\n"
                + "<p id=\"p40\"> 「そりゃあ、こたちゃんに報告しに来たんだよ」</p>\r\n" + "<p id=\"p41\"> 　そう言って、あっけらかんと笑った。</p>\r\n"
                + "<p id=\"p42\"> 「報告」</p>\r\n"
                + "<p id=\"p43\"> 「うん。だってこたちゃん、応援してくれてたでしょ。例の彼とのこと。だからちゃんと報告しとかなきゃと思って」</p>\r\n"
                + "<p id=\"p44\"> 「……そっか」</p>\r\n" + "<p id=\"p45\"> 「明日、彼とちゃんと会えたらまた報告するね。じゃあ、おやすみこたちゃん！」</p>\r\n"
                + "<p id=\"p46\"> 　おやすみ、という僕の言葉が終わらないうちに、ドアが閉まって千紗ちゃんの姿は消えた。</p>\r\n"
                + "<p id=\"p47\"> 　なんだか嵐が去ったみたいに、途端に部屋がしんと静まりかえる。僕は中断していた料理を再開しようと、台所に戻った。そうしてそこに残された二人分の豚肉を眺めて、どうしたものかと考えた。</p>\r\n"
                + "\r\n" + "          </div>\r\n" + "        </div>\r\n" + "      </div>";
        String actual = scraper.parseChapterText(chapter);
        assertEquals(expected, actual);
    }

    /** Test parsing all chapters' urls. */
    @Test
    public void testParseAllChapterUrls() {
        List<String> expected = new ArrayList<String>();
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16816700426011111223");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16816700426011137753");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16816700427121810041");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330653865591874");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330653917353277");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330653964862783");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330654030011963");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330654092232190");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330654136576117");
        expected.add("https://kakuyomu.jp/works/16816700426011043454/episodes/16817330654183452303");
        List<String> actual = scraper.parseAllChapterUrls(noVolumeDocument);
        assertEquals(expected, actual);
    }

    /** Test parsing chapters' urls by volume. */
    @Test
    public void testParseChaptersByVolume() {
        // prepare expected map
        Map<Integer, List<String>> expected = new HashMap<Integer, List<String>>();
        List<String> volume1 = new ArrayList<String>();
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891792335");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891799890");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891799922");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891799936");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800064");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800078");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800089");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800105");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800118");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800139");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891800332");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806353");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806407");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806511");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806546");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806580");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806602");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806649");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806707");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806769");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891806794");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891814290");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891896979");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891896991");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891897003");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891897011");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891897018");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891917822");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891917824");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891917852");
        volume1.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891917861");
        List<String> volume2 = new ArrayList<String>();
        volume2.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054892091168");
        volume2.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054892091420");
        volume2.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054892954660");
        volume2.add("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054892091532");

        expected.put(1, volume1);
        expected.put(2, volume2);

        Map<Integer, List<String>> actual = scraper.parseChapterUrlsByVolume(volumeDocument);
        assertEquals(expected, actual);
    }

    /** Test parsing chapter's number. */
    @Test
    public void testParseChapterNumber() {
        assertEquals(scraper.parseChapterNumber(chapter), -1);
    }
}
