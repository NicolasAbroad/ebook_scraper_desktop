package com.nicolas_abroad.epub_scraper_desktop.scrape.sources;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test class for the SyosetsuScraper class.
 *
 * @author Nicolas
 */
public class KakuyomuScraperTest {

	private static KakuyomuScraper scraper = new KakuyomuScraper();

	private static Document noVolumeDocument;

	private static Document volumeDocument;

	private static Document volumeWithSubtitlesDocument;

	private static Document chapter;

	/**
	 * Set up html documents.
	 *
	 * @throws IOException
	 */
	@BeforeClass
	public static void setUp() throws IOException {
		noVolumeDocument = scraper.parseHTMLDocument("https://kakuyomu.jp/works/1177354054882078516");
		volumeDocument = scraper.parseHTMLDocument("https://kakuyomu.jp/works/1177354054891792326");
		volumeWithSubtitlesDocument = scraper.parseHTMLDocument("https://kakuyomu.jp/works/1177354054884850859");
		chapter = scraper
				.parseHTMLDocument("https://kakuyomu.jp/works/1177354054891792326/episodes/1177354054891799922");
	}

	/** Test parse author method on page without author link. */
	@Test
	public void testParseAuthor01() throws Exception {
		String expected = "尾岡れき@猫部";
		String actual = scraper.parseAuthor(noVolumeDocument);
		assertEquals(expected, actual);
	}

	/** Test parse author method on page with author link. */
	@Test
	public void testParseAuthor02() throws Exception {
		String expected = "此見えこ";
		String actual = scraper.parseAuthor(volumeDocument);
		assertEquals(expected, actual);
	}

	/** Test parse story title method. */
	@Test
	public void testParseStoryTitle() throws Exception {
		String expected = "スイーツをあなたに";
		String actual = scraper.parseStoryTitle(noVolumeDocument);
		assertEquals(expected, actual);
	}

	/** Test if html has volumes. */
	@Test
	public void testHasVolumes01() throws Exception {
		assertFalse(scraper.hasVolumes(noVolumeDocument));
	}

	/** Test if html has volumes. (Without subtitles) */
	@Test
	public void testHasVolumes02() throws Exception {
		assertTrue(scraper.hasVolumes(volumeDocument));
	}

	/** Test if html has volumes. (With subtitles) */
	@Test
	public void testHasVolumes03() throws Exception {
		assertTrue(scraper.hasVolumes(volumeWithSubtitlesDocument));
	}

	/** Test volume title parsing. */
	@Test
	public void testParseVolumeTitles01() throws Exception {
		List<String> expected = new ArrayList<String>();
		List<String> actual = scraper.parseVolumeTitles(noVolumeDocument);
		assertEquals(expected, actual);
	}

	/** Test volume title parsing. (Without subtitles) */
	@Test
	public void testParseVolumeTitle02() throws Exception {
		List<String> expected = new ArrayList<String>();
		expected.add("本編");
		expected.add("後日談");
		assertEquals(expected, scraper.parseVolumeTitles(volumeDocument));
	}

	/** Test volume title parsing. (With subtitles) */
	@Test
	public void testParseVolumeTitle03() throws Exception {
		List<String> expected = new ArrayList<String>();
		expected.add("本編");
		expected.add("付録");
		assertEquals(expected, scraper.parseVolumeTitles(volumeWithSubtitlesDocument));
	}

	/** Test chapter title parsing. */
	@Test
	public void testParseChapterTitle01() {
		String expected = "第3話";
		String actual = scraper.parseChapterTitle(chapter);
		assertEquals(expected, actual);
	}

	/**
	 * Test chapter title parsing when chapter title contains "<" and ">".
	 *
	 * @throws IOException
	 */
	@Test
	public void testParseChapterTitle02() throws IOException {
		Document document = scraper
				.parseHTMLDocument("https://kakuyomu.jp/works/1177354054889370224/episodes/1177354054890562756");
		// 第96話 レギオン・ミーティング <上>
		String expected = "第96話　レギオン・ミーティング　&lt;上&gt;";
		String actual = scraper.parseChapterTitle(document);
		assertEquals(expected, actual);
	}

	/** Test chapter text parsing. */
	@Test
	public void testParseChapterText() {
		String expected = "<header id=\"contentMain-header\">\n" + "\n"
				+ "        <p class=\"widget-episodeTitle js-vertical-composition-item\">第3話</p>\n"
				+ "      </header><div class=\"widget-episode js-episode-body-container\">\n"
				+ "        <div class=\"widget-episode-inner\">\n"
				+ "          <div class=\"widget-episodeBody js-episode-body\" data-viewer-history-path=\"/works/1177354054891792326/episodes/1177354054891799922/history\" data-viewer-reading-quantity-path=\"/works/1177354054891792326/episodes/1177354054891799922/reading_quantity\"><p id=\"p1\">「こたちゃん、大ニュース！」</p>\n"
				+ "<p id=\"p2\"> 　僕の部屋に駆け込んできた千紗ちゃんが開口一番にそう言ったのは、豚丼に使うもやしとピーマンを炒めていたときだった。突進するような勢いで僕の真横まで興奮気味に駆け寄ってきた彼女に、僕はあわててコンロの火を止める。横を向けば、吐息がかかるほど近くまで顔を寄せた千紗ちゃんの大きな目と目が合った。</p>\n"
				+ "<p id=\"p3\"> 「突き止めたの！」</p>\n" + "<p id=\"p4\"> 「なにを？」</p>\n" + "<p id=\"p5\"> 「例の彼の職場！」</p>\n"
				+ "<p id=\"p6\"> 「え」</p>\n"
				+ "<p id=\"p7\"> 　走ってきたのか、頬を紅潮させた彼女からは、いつもと同じ砂糖菓子みたいな甘い匂いがした。千紗ちゃんがつけている香水の匂い。ふわふわして甘ったるいその匂いは、千紗ちゃんにぴったりだといつも思う。</p>\n"
				+ "<p id=\"p8\"> 「会えたの？」</p>\n" + "<p id=\"p9\"> 「うん！　今日のバイト前に見つけたの！」</p>\n"
				+ "<p id=\"p10\"> 　弾けるような笑顔でそう言って、千紗ちゃんはその場でぴょんぴょん飛び跳ねる。そんな様子は、同い年とは思えないほど子どもっぽくて、「よかったね」と僕もつられるように笑顔になりながら</p>\n"
				+ "<p id=\"p11\"> 「職場ってことは、社会人だったんだ？」</p>\n" + "<p id=\"p12\"> 「わかんない。バイト先かも。話してはないもん」</p>\n"
				+ "<p id=\"p13\"> 「え、なんで？　声かけなかったの？」</p>\n"
				+ "<p id=\"p14\"> 　探していた人をようやく見つけて、千紗ちゃんはそこで臆するような子じゃないはずだ。恋する千紗ちゃんは、基本的にいつも猪突猛進だった。</p>\n"
				+ "<p id=\"p15\"> 「うん、かけたかったんだけどね。その人、道路の反対側にいたの。横断歩道も赤だったし、駆け寄れなくて。だからとりあえず見失わないように後をつけたの。そうしたら西通りにある喫茶店に入っていったんだ。それも正面からじゃなくて裏口から。だからお客さんじゃなくて働いてるんだろうなって」</p>\n"
				+ "<p id=\"p16\"> 　西通りにある喫茶店。言われて、たしかにあのあたりに、一軒ちょっと大きめな喫茶店があったのを思い出す。</p>\n"
				+ "<p id=\"p17\"> 　それにしても、千紗ちゃんがその人が探していた交差点から西通りまでは、けっこうな距離がある。歩いたら二十分ほどかかるのではないだろうか。その距離を追いかけたということか。</p>\n"
				+ "<p id=\"p18\"> 　萩原あたりが聞いたら、間違いなくストーカーとか言ってきそうだな、なんて頭の隅で思いながら</p>\n"
				+ "<p id=\"p19\"> 「千紗ちゃんは、その喫茶店には入らなかったの？」</p>\n"
				+ "<p id=\"p20\"> 「うん。バイトの時間だったから、今日は入れなかったの。いちおうバイト終わりにも行ってみて中覗いて来たんだけど、彼いないみたいだったから、また明日行ってみようと思って」</p>\n"
				+ "<p id=\"p21\"> 「そっか」</p>\n" + "<p id=\"p22\"> 「だからこたちゃん、今日は帰るね！」</p>\n" + "<p id=\"p23\"> 「へ？」</p>\n"
				+ "<p id=\"p24\"> 　間の抜けた声を上げる僕にかまわず、千紗ちゃんはさっさと踵を返して玄関のほうへ歩き出していたので</p>\n"
				+ "<p id=\"p25\"> 「ご飯は？　食べて行かないの？」</p>\n" + "<p id=\"p26\"> 「うん、今日はいい。早く帰って明日の準備したいもん」</p>\n"
				+ "<p id=\"p27\"> 「準備？」</p>\n" + "<p id=\"p28\"> 「明日着ていく服を選んだりー、お顔のパックもしなきゃ！」</p>\n"
				+ "<p id=\"p29\"> 「じゃあおかずタッパーに詰めるから、持って帰りなよ。どうせ帰ってもなにも食べるものないんでしょ」</p>\n"
				+ "<p id=\"p30\"> 　そう言うと、千紗ちゃんは足を止めて少しだけ考えてから</p>\n"
				+ "<p id=\"p31\"> 「んー、いいや。今日はなんだか胸がいっぱいで、なにも入りそうにないから」</p>\n"
				+ "<p id=\"p32\"> 「いや駄目だって、ちゃんと食べなきゃ」</p>\n" + "<p id=\"p33\"> 「心配してくれてありがとね、こたちゃん」</p>\n"
				+ "<p id=\"p34\"> 　僕の言葉は耳に入っていない様子で、千紗ちゃんはうきうきと弾む声で言って、玄関に脱ぎ散らかしていたスニーカーを拾う。そうしてそれを履き始めた彼女の背中を眺めながら、僕はふと首を傾げると</p>\n"
				+ "<p id=\"p35\"> 「え、じゃあなんで」</p>\n" + "<p id=\"p36\"> 「ん？」</p>\n" + "<p id=\"p37\"> 「何しにここに来たの？」</p>\n"
				+ "<p id=\"p38\"> 　僕の住むアパートは、千紗ちゃんのバイト先とも千紗ちゃんの住むアパートとも方向が違う。千紗ちゃんが帰りしなにふらっと立ち寄れる場所にはない。ここへ来るには、わざわざ僕のアパートのほうへ足を伸ばさなければならないはずだ。</p>\n"
				+ "<p id=\"p39\"> 　僕の質問に、千紗ちゃんはきょとんとした顔でこちらを振り向いて</p>\n" + "<p id=\"p40\"> 「そりゃあ、こたちゃんに報告しに来たんだよ」</p>\n"
				+ "<p id=\"p41\"> 　そう言って、あっけらかんと笑った。</p>\n" + "<p id=\"p42\"> 「報告」</p>\n"
				+ "<p id=\"p43\"> 「うん。だってこたちゃん、応援してくれてたでしょ。例の彼とのこと。だからちゃんと報告しとかなきゃと思って」</p>\n"
				+ "<p id=\"p44\"> 「……そっか」</p>\n" + "<p id=\"p45\"> 「明日、彼とちゃんと会えたらまた報告するね。じゃあ、おやすみこたちゃん！」</p>\n"
				+ "<p id=\"p46\"> 　おやすみ、という僕の言葉が終わらないうちに、ドアが閉まって千紗ちゃんの姿は消えた。</p>\n"
				+ "<p id=\"p47\"> 　なんだか嵐が去ったみたいに、途端に部屋がしんと静まりかえる。僕は中断していた料理を再開しようと、台所に戻った。そうしてそこに残された二人分の豚肉を眺めて、どうしたものかと考えた。</p>\n"
				+ "\n" + "          </div>\n" + "        </div>\n" + "      </div>";
		String actual = scraper.parseChapterText(chapter);
		assertEquals(expected, actual);
	}

	/** Test parsing all chapters' urls. */
	@Test
	public void testParseAllChapterUrls() throws Exception {
		List<String> expected = new ArrayList<String>();
		expected.add("https://kakuyomu.jp/works/1177354054882078516/episodes/1177354054882078526");
		expected.add("https://kakuyomu.jp/works/1177354054882078516/episodes/1177354054882078613");
		expected.add("https://kakuyomu.jp/works/1177354054882078516/episodes/1177354054882078620");
		expected.add("https://kakuyomu.jp/works/1177354054882078516/episodes/1177354054882078635");
		List<String> actual = scraper.parseAllChapterUrls(noVolumeDocument);
		assertEquals(expected, actual);
	}

	/** Test parsing chapters' urls by volume. (Without subtitles) */
	@Test
	public void testParseChaptersByVolume01() throws Exception {
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

	/** Test parsing chapters' urls by volume. (With subtitles) */
	@Test
	public void testParseChaptersByVolume02() throws Exception {
		// prepare expected map
		Map<Integer, List<String>> expected = new HashMap<Integer, List<String>>();
		List<String> volume1 = new ArrayList<String>();
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884851015");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884853763");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884862727");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884878794");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884886288");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884903372");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884903945");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884920743");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884938794");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884947825");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884949855");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884954647");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884973119");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884980784");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884987687");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054884999842");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885006577");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885017919");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885032088");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885048963");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885078077");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885132567");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885153605");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885253650");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885278688");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885291381");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885358346");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885382126");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885406061");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885420799");
		volume1.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885431348");

		List<String> volume2 = new ArrayList<String>();
		volume2.add("https://kakuyomu.jp/works/1177354054884850859/episodes/1177354054885438179");

		expected.put(1, volume1);
		expected.put(2, volume2);

		Map<Integer, List<String>> actual = scraper.parseChapterUrlsByVolume(volumeWithSubtitlesDocument);
		assertEquals(expected, actual);
	}

	/** Test parsing chapter's number. */
	@Test
	public void testParseChapterNumber() {
		assertEquals(scraper.parseChapterNumber(chapter), -1);
	}
}
