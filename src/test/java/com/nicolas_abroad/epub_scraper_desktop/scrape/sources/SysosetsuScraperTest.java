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
public class SysosetsuScraperTest {

	private static final SyosetsuScraper scraper = new SyosetsuScraper();

	private static Document noVolumeDocument;

	private static Document volumeDocument;

	private static Document chapter;

	/**
	 * Set up html documents.
	 *
	 * @throws IOException
	 */
	@BeforeClass
	public static void setUp() throws IOException {
		noVolumeDocument = scraper.parseHTMLDocument("https://ncode.syosetu.com/n0286ee/");
		volumeDocument = scraper.parseHTMLDocument("https://ncode.syosetu.com/n1419y/");
		chapter = scraper.parseHTMLDocument("https://ncode.syosetu.com/n7594ct/1/");
	}

	/** Test parse author method on page without author link. */
	@Test
	public void testParseAuthor01() {
		String expected = "磯部勝彦";
		String actual = scraper.parseAuthor(noVolumeDocument);
		assertEquals(expected, actual);
	}

	/** Test parse author method on page with author link. */
	@Test
	public void testParseAuthor02() {
		String expected = "天笠恭介";
		String actual = scraper.parseAuthor(volumeDocument);
		assertEquals(expected, actual);
	}

	/** Test parse story title method. */
	@Test
	public void testParseStoryTitle() {
		String expected = "あんずの輝き～夢の国からファンタジー～";
		String actual = scraper.parseStoryTitle(noVolumeDocument);
		assertEquals(expected, actual);
	}

	/**
	 * Test if html has volumes.
	 *
	 * @throws Exception
	 */
	@Test
	public void testHasVolumes01() throws Exception {
		assertFalse(scraper.hasVolumes(noVolumeDocument));
	}

	/**
	 * Test if html has volumes.
	 *
	 * @throws Exception
	 */
	@Test
	public void testHasVolumes02() throws Exception {
		assertTrue(scraper.hasVolumes(volumeDocument));
	}

	/** Test volume title parsing. */
	@Test
	public void testParseVolumeTitles01() {
		List<String> expected = new ArrayList<>();
		assertEquals(expected, scraper.parseVolumeTitles(noVolumeDocument));
	}

	/** Test volume title parsing. */
	@Test
	public void testParseVolumeTitle02() {
		List<String> expected = new ArrayList<>();
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

	/** Test chapter title parsing. */
	@Test
	public void testParseChapterTitle01() {
		String expected = "『プロローグ』";
		String actual = scraper.parseChapterTitle(chapter);
		assertEquals(expected, actual);
	}

	/**
	 * Test chapter title parsing when chapter title contains "…".
	 *
	 * @throws IOException
	 */
	@Test
	public void testParseChapterTitle02() throws IOException {
		Document document = scraper.parseHTMLDocument("https://ncode.syosetu.com/n1217et/212/");
		// 210話 高月マコトは、……
		String expected = "210話　高月マコトは、……";
		String actual = scraper.parseChapterTitle(document);
		assertEquals(expected, actual);
	}

	/**
	 * Test chapter title parsing when chapter title contains "&" surrounded by
	 * words.
	 *
	 * @throws IOException
	 */
	@Test
	public void testParseChapterTitle03() throws IOException {
		Document document = scraper.parseHTMLDocument("https://ncode.syosetu.com/n0610eg/283/");
		// 『書籍化記念&一周年』学園都市でのやりとり『閑話』
		String expected = "『書籍化記念&amp;一周年』学園都市でのやりとり『閑話』";
		String actual = scraper.parseChapterTitle(document);
		assertEquals(expected, actual);
	}

	/**
	 * Test chapter title parsing when chapter title contains "&" surrounded by
	 * spaces.
	 *
	 * @throws IOException
	 */
	@Test
	public void testParseChapterTitle04() throws IOException {
		Document document = scraper.parseHTMLDocument("https://ncode.syosetu.com/n6681fa/57/");
		// 57 第6話06：Chase & AVENGE!!
		String expected = "57　第6話06：Chase &amp; AVENGE!!";
		String actual = scraper.parseChapterTitle(document);
		assertEquals(expected, actual);
	}

	/** Test chapter text parsing. */
	@Test
	public void testParseChapterText01() {
		String expected = "<div class=\"c-pager c-pager--center\">\n" +
				"<a class=\"c-pager__item c-pager__item--next\">次へ</a></div><div class=\"p-novel__number js-siori\">1/537</div><h1 class=\"p-novel__title p-novel__title--rensai\">『プロローグ』</h1><div class=\"p-novel__body\">\n" +
				"<div class=\"js-novel-text p-novel__text p-novel__text--preface\">\n" +
				"<p id=\"Lp1\">初投稿、というか初小説です。</p>\n" +
				"<p id=\"Lp2\">よろしくお願いします。</p>\n" +
				"</div>\n" + "\n" +
				"<div class=\"js-novel-text p-novel__text\">\n" +
				"<p id=\"L1\">　人はあっけなく死ぬものだ。</p>\n" +
				"<p id=\"L2\"><br></br></p>\n" +
				"<p id=\"L3\">　そんなことはわかっていた。</p>\n" +
				"<p id=\"L4\"><br></br></p>\n" +
				"<p id=\"L5\">　それでも、衝撃だった。</p>\n" +
				"<p id=\"L6\"><br></br></p>\n" +
				"<p id=\"L7\">　父と母が死んだのだという。</p>\n" +
				"<p id=\"L8\"><br></br></p>\n" +
				"<p id=\"L9\">　交通事故だったと。</p>\n" +
				"<p id=\"L10\"><br></br></p>\n" +
				"<p id=\"L11\">　ふたりが外に出る前、最後に交わした言葉はなんだったか。</p>\n" +
				"<p id=\"L12\"><br></br></p>\n" +
				"<p id=\"L13\">　病院にも葬式にも行ったらしいが、いまいち覚えていない。</p>\n" +
				"<p id=\"L14\"><br></br></p>\n" +
				"<p id=\"L15\">　いや、覚えているんだけれども、靄がかかったように曖昧で。</p>\n" +
				"<p id=\"L16\"><br></br></p>\n" +
				"<p id=\"L17\">　3年ぶりに妹にも会ったはずなのに、いまいち覚えていない。</p>\n" +
				"<p id=\"L18\"><br></br></p>\n" +
				"<p id=\"L19\">　そういえば、家の外に出たのは10年ぶりだったはずだ。</p>\n" +
				"<p id=\"L20\"><br></br></p>\n" +
				"<p id=\"L21\">　10年ぶりの外出だったのに、あまり覚えていないというのももったいない気がする。</p>\n" +
				"<p id=\"L22\"><br></br></p>\n" +
				"<p id=\"L23\">　あっさり出られたのは、父と母からの最後の贈り物なのかもしれない。</p>\n" +
				"<p id=\"L24\"><br></br></p>\n" +
				"<p id=\"L25\">　引き換えがふたりの命なら、特にうれしくはないけれども。</p>\n" +
				"<p id=\"L26\"><br></br></p>\n" +
				"<p id=\"L27\">　外に出よう。</p>\n" +
				"<p id=\"L28\"><br></br></p>\n" +
				"<p id=\"L29\">　10年、引きこもりのニートだった俺に、温かく接してくれた父と母。</p>\n" +
				"<p id=\"L30\"><br></br></p>\n" +
				"<p id=\"L31\">　このまま引きこもりを続けたら、ふたりの死が無駄になった気がするから。</p>\n" +
				"<p id=\"L32\"><br></br></p>\n" +
				"<p id=\"L33\">　外に出よう。</p>\n" +
				"</div>\n" +
				"</div><div class=\"c-pager c-pager--center\">\n" +
				"<a class=\"c-pager__item c-pager__item--next\">次へ</a><a class=\"c-pager__item\">目次</a></div>";
		String actual = scraper.parseChapterText(chapter);
		assertEquals(expected, actual);
	}

	/**
	 * Test chapter text parsing when chapter text contains images.
	 *
	 * @throws IOException
	 */
	@Test
	public void testParseChapterText02() throws IOException {
		Document document = scraper.parseHTMLDocument("https://ncode.syosetu.com/n5464di/5/");
		String expected = "<div class=\"c-pager c-pager--center\">\n" +
				"<a class=\"c-pager__item c-pager__item--before\">前へ</a><a class=\"c-pager__item c-pager__item--next\">次へ</a></div><div class=\"p-novel__number js-siori\">5/7</div><h1 class=\"p-novel__title p-novel__title--rensai\">共通⑤ 彼女は幼馴染み？</h1><div class=\"p-novel__body\">\n" +
				"\n" + "<div class=\"js-novel-text p-novel__text\">\n" +
				"<p id=\"L1\">「マグナダリアさん!!」</p>\n" +
				"<p id=\"L2\">ウラミルヂィが小部屋のドアを蹴破る。</p>\n" +
				"<p id=\"L3\"><br></br></p>\n" +
				"<p id=\"L4\">室内には筋トレ器具のようなものが無数にある。</p>\n" +
				"<p id=\"L5\">その中に一人の赤髪の女がいた。</p>\n" +
				"<p id=\"L6\"><br></br></p>\n" +
				"<p id=\"L7\">結われていない長い髪、鎧、背中に剣を背負っている。</p>\n" +
				"<p id=\"L8\"><br></br></p>\n" +
				"<p id=\"L9\">（なんかこいつどっかで見たことあるな）</p>\n" +
				"<p id=\"L10\">新斗は彼女の顔を見て、何かを思い出した。</p>\n" +
				"<p id=\"L11\"><br></br></p>\n" +
				"<p id=\"L12\">「あんた、俺の幼馴染みじゃないよな？」</p>\n" +
				"<p id=\"L13\">彼女の顔が幼馴染みに似ており、念のため確認した新斗。</p>\n" +
				"<p id=\"L14\"><br></br></p>\n" +
				"<p id=\"L15\">「はぁ!?」</p>\n" +
				"<p id=\"L16\"><br></br></p>\n" +
				"<p id=\"L17\">（違ったか</p>\n" +
				"<p id=\"L18\">…今頃あいつは大企業のOLやってんだろうな）</p>\n" +
				"<p id=\"L19\"><br></br></p>\n" +
				"<p id=\"L20\">「ちょっとウラミルヂィ！誰なのよこいつら!」</p>\n" +
				"<p id=\"L21\">マグナダリアは新斗達に剣を向けた。</p>\n" +
				"<p id=\"L22\"><br></br></p>\n" +
				"<p id=\"L23\">「剣士マグナダリア殿</p>\n" +
				"<p id=\"L24\">私の顔に免じて、許してくれ」</p>\n" +
				"<p id=\"L25\">ディレスタントが前に出た。</p>\n" +
				"<p id=\"L26\"><br></br></p>\n" +
				"<p id=\"L27\">「ディレスタント女史……」</p>\n" +
				"<p id=\"L28\">（よかった…落ち着いてくれた）</p>\n" +
				"<p id=\"L29\"><br></br></p>\n" +
				"<p id=\"L30\">「…って尚更ダメよ!!」</p>\n" +
				"<p id=\"L31\">マグナダリアは剣を振りかざした。</p>\n" +
				"<p id=\"L32\"><br></br></p>\n" +
				"<p id=\"L33\">「あぶねっ」</p>\n" +
				"<p id=\"L34\">「姫様を盾にするなんて、とんだ悪党ね！」</p>\n" +
				"<p id=\"L35\"><br></br></p>\n" +
				"<p id=\"L36\">「あらあら～どうしたんでしょう？」</p>\n" +
				"<p id=\"L37\">「姫様、危ないので我々は離れていましょう」</p>\n" +
				"<p id=\"L38\"><a></a></p>\n" +
				"<p id=\"L39\"><br></br></p>\n" +
				"<p id=\"L40\">ディレスタントはエカドリーユを連れて、外へ避難した。</p>\n" +
				"<p id=\"L41\"><br></br></p>\n" +
				"<p id=\"L42\">「ハアアアアアア」</p>\n" +
				"<p id=\"L43\">あえて避けられていたマグナダリアの剣は確実に新斗を狙う。</p>\n" +
				"<p id=\"L44\"><br></br></p>\n" +
				"<p id=\"L45\">「やめなさい!」</p>\n" +
				"<p id=\"L46\"><a></a></p>\n" +
				"<p id=\"L47\"><br></br></p>\n" +
				"<p id=\"L48\">ウラミルヂィが新斗の前に出て、足で剣の中心を蹴り、無数のナイフを投げる。</p>\n" +
				"<p id=\"L49\">マグナダリアの腕にはあたらずに、無数のナイフは剣を打撃した。</p>\n" +
				"<p id=\"L50\">マグナダリアの手から剣が落ちる。</p>\n" +
				"<p id=\"L51\"><br></br></p>\n" +
				"<p id=\"L52\">マグナダリアはようやく話を聞く体制になった。</p>\n" +
				"</div>\n" + "</div><div class=\"c-pager c-pager--center\">\n" +
				"<a class=\"c-pager__item c-pager__item--before\">前へ</a><a class=\"c-pager__item c-pager__item--next\">次へ</a><a class=\"c-pager__item\">目次</a></div>";
		String actual = scraper.parseChapterText(document);
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	/** Test parsing all chapters' urls. */
	@Test
	public void testParseAllChapterUrls() {
		List<String> expected = new ArrayList<>();
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

	/** Test parsing chapters' urls by volume. */
	@Test
	public void testParseChaptersByVolume() {
		// prepare expected map
		Map<Integer, List<String>> expected = new HashMap<>();
		List<String> volume1 = new ArrayList<>();
		volume1.add("https://ncode.syosetu.com/n1419y/1/");
		List<String> volume2 = new ArrayList<>();
		volume2.add("https://ncode.syosetu.com/n1419y/2/");
		volume2.add("https://ncode.syosetu.com/n1419y/3/");
		volume2.add("https://ncode.syosetu.com/n1419y/4/");
		volume2.add("https://ncode.syosetu.com/n1419y/5/");
		List<String> volume3 = new ArrayList<>();
		volume3.add("https://ncode.syosetu.com/n1419y/6/");
		volume3.add("https://ncode.syosetu.com/n1419y/7/");
		volume3.add("https://ncode.syosetu.com/n1419y/8/");
		volume3.add("https://ncode.syosetu.com/n1419y/9/");
		List<String> volume4 = new ArrayList<>();
		volume4.add("https://ncode.syosetu.com/n1419y/10/");
		volume4.add("https://ncode.syosetu.com/n1419y/11/");
		volume4.add("https://ncode.syosetu.com/n1419y/12/");
		volume4.add("https://ncode.syosetu.com/n1419y/13/");
		volume4.add("https://ncode.syosetu.com/n1419y/14/");
		volume4.add("https://ncode.syosetu.com/n1419y/15/");
		volume4.add("https://ncode.syosetu.com/n1419y/16/");
		volume4.add("https://ncode.syosetu.com/n1419y/17/");
		List<String> volume5 = new ArrayList<>();
		volume5.add("https://ncode.syosetu.com/n1419y/18/");
		volume5.add("https://ncode.syosetu.com/n1419y/19/");
		volume5.add("https://ncode.syosetu.com/n1419y/20/");
		volume5.add("https://ncode.syosetu.com/n1419y/21/");
		volume5.add("https://ncode.syosetu.com/n1419y/22/");
		List<String> volume6 = new ArrayList<>();
		volume6.add("https://ncode.syosetu.com/n1419y/23/");
		volume6.add("https://ncode.syosetu.com/n1419y/24/");
		volume6.add("https://ncode.syosetu.com/n1419y/25/");
		volume6.add("https://ncode.syosetu.com/n1419y/26/");
		volume6.add("https://ncode.syosetu.com/n1419y/27/");
		List<String> volume7 = new ArrayList<>();
		volume7.add("https://ncode.syosetu.com/n1419y/28/");
		volume7.add("https://ncode.syosetu.com/n1419y/29/");
		volume7.add("https://ncode.syosetu.com/n1419y/30/");
		List<String> volume8 = new ArrayList<>();
		volume8.add("https://ncode.syosetu.com/n1419y/31/");
		expected.put(1, volume1);
		expected.put(2, volume2);
		expected.put(3, volume3);
		expected.put(4, volume4);
		expected.put(5, volume5);
		expected.put(6, volume6);
		expected.put(7, volume7);
		expected.put(8, volume8);

		Map<Integer, List<String>> actual = scraper.parseChapterUrlsByVolume(volumeDocument);
		assertEquals(expected, actual);
	}

	/** Test parsing chapter's number. */
	@Test
	public void testParseChapterNumber() {
		assertEquals(scraper.parseChapterNumber(chapter), 1);
	}

}
