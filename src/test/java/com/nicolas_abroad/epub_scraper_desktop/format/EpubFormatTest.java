package com.nicolas_abroad.epub_scraper_desktop.format;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Chapter;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.SyosetsuScraper;

/**
 * Unit test class for the EpubFormat class.
 * @author Nicolas
 */
public class EpubFormatTest {

    private EpubFormat epubFormat = new EpubFormat();

    private static Volume volume;

    /** Temporary folder used to ensure no files are left after tests */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        // set up volume
        EbookScraper scraper = new SyosetsuScraper();
        List<String> chapterUrls = new ArrayList<String>();
        chapterUrls.add("https://ncode.syosetu.com/n1443bp/1/");
        chapterUrls.add("https://ncode.syosetu.com/n1443bp/2/");
        chapterUrls.add("https://ncode.syosetu.com/n1443bp/3/");
        chapterUrls.add("https://ncode.syosetu.com/n1443bp/4/");
        volume = new Volume(scraper, chapterUrls);
        volume.setTitle("test");
        volume.setAuthor("nicolas");
        volume.generate();
    }

    /**
     * Read zip file.
     * @param file
     * @return zip file content
     * @throws IOException
     */
    public String readZipFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (ZipInputStream zipInStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            while (true) {
                ZipEntry entry = zipInStream.getNextEntry();
                if (entry == null)
                    break;
                if (entry.isDirectory()) {
                    sb.append(entry.getName() + System.lineSeparator());
                } else {
                    sb.append(entry.getName() + System.lineSeparator());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    while (true) {
                        int input = zipInStream.read();
                        if (input < 0)
                            break;
                        byteArrayOutputStream.write(input);
                    }
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                    sb.append(new String(byteArrayOutputStream.toByteArray()));
                    sb.append(System.lineSeparator());
                }
                zipInStream.closeEntry();
            }
        }
        return sb.toString();
    }

    @Test
    public void tesWriteFileToZip() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        String filePath = "testTitle";
        String fileContent = "testContent";
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.writeFileToZip(zipOutputStream, filePath, fileContent);
        }

        String expected = filePath + System.lineSeparator() + fileContent + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateMimetypeContent() {
        String expected = "application/epub+zip";
        String actual = epubFormat.generateMimetypeContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateMimetypePath() {
        String expected = "mimetype";
        String actual = epubFormat.generateMimetypePath();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateMimetype() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateMimetype(zipOutputStream);
        }

        String expected = "mimetype" + System.lineSeparator() + "application/epub+zip" + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateContainerContent() {
        String expected = "<?xml version=\"1.0\"?>" + System.lineSeparator()
                + "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">"
                + System.lineSeparator() + "    <rootfiles>" + System.lineSeparator()
                + "        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>"
                + System.lineSeparator() + "    </rootfiles>" + System.lineSeparator() + "</container>";
        String actual = epubFormat.generateContainerContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateContainerPath() {
        String expected = "META-INF/container.xml";
        String actual = epubFormat.generateContainerPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateContainer() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateContainer(zipOutputStream);
        }

        String expected = "META-INF/container.xml" + System.lineSeparator() + "<?xml version=\"1.0\"?>"
                + System.lineSeparator()
                + "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">"
                + System.lineSeparator() + "    <rootfiles>" + System.lineSeparator()
                + "        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>"
                + System.lineSeparator() + "    </rootfiles>" + System.lineSeparator() + "</container>"
                + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateContentContent() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
                + "<package prefix=\"cc: http://creativecommons.org/ns#\" unique-identifier=\"uid\" version=\"3.0\" xml:lang=\"en\" xmlns=\"http://www.idpf.org/2007/opf\">"
                + System.lineSeparator() + "    <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
                + System.lineSeparator() + "        <meta property=\"dcterms:modified\">2010-02-17T04:39:13Z</meta>"
                + System.lineSeparator() + "        <dc:title>test</dc:title>" + System.lineSeparator()
                + "        <dc:creator id=\"creator\">nicolas</dc:creator>" + System.lineSeparator()
                + "        <dc:identifier id=\"uid\">nicolas nicolas test</dc:identifier>" + System.lineSeparator()
                + "        <dc:language>ja-JP</dc:language>" + System.lineSeparator()
                + "        <dc:rights>Public Domain</dc:rights>" + System.lineSeparator()
                + "        <dc:publisher>Nicolas</dc:publisher>" + System.lineSeparator() + "    </metadata>"
                + System.lineSeparator() + "    <manifest >" + System.lineSeparator()
                + "        <item id=\"ncx\" href=\"toc.ncx\" media-type=\"application/x-dtbncx+xml\"/>"
                + System.lineSeparator()
                + "        <item href=\"nav.xhtml\" id=\"nav\" media-type=\"application/xhtml+xml\" properties=\"nav\"/>"
                + System.lineSeparator()
                + "        <item id=\"hstyle\" href=\"horizontal.css\" media-type=\"text/css\"/>"
                + System.lineSeparator()
                + "        <item id=\"pagetemplate\" href=\"page-template.xpgt\" media-type=\"application/vnd.adobe-page-template+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c1\" href=\"c1.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c2\" href=\"c2.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c3\" href=\"c3.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c4\" href=\"c4.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator() + "    </manifest>" + System.lineSeparator()
                + "    <spine toc=\"ncx\" page-progression-direction=\"rtl\">" + System.lineSeparator()
                + "        <itemref idref=\"nav\"/>" + System.lineSeparator() + "        <itemref idref =\"c1\"/>"
                + System.lineSeparator() + "        <itemref idref =\"c2\"/>" + System.lineSeparator()
                + "        <itemref idref =\"c3\"/>" + System.lineSeparator() + "        <itemref idref =\"c4\"/>"
                + System.lineSeparator() + "    </spine>" + System.lineSeparator() + "</package>";
        String actual = epubFormat.generateContentContent(volume);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateContentPath() {
        String expected = "OEBPS/content.opf";
        String actual = epubFormat.generateContentPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateContent() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateContent(zipOutputStream, volume);
        }

        String expected = "OEBPS/content.opf" + System.lineSeparator() + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + System.lineSeparator()
                + "<package prefix=\"cc: http://creativecommons.org/ns#\" unique-identifier=\"uid\" version=\"3.0\" xml:lang=\"en\" xmlns=\"http://www.idpf.org/2007/opf\">"
                + System.lineSeparator() + "    <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
                + System.lineSeparator() + "        <meta property=\"dcterms:modified\">2010-02-17T04:39:13Z</meta>"
                + System.lineSeparator() + "        <dc:title>test</dc:title>" + System.lineSeparator()
                + "        <dc:creator id=\"creator\">nicolas</dc:creator>" + System.lineSeparator()
                + "        <dc:identifier id=\"uid\">nicolas nicolas test</dc:identifier>" + System.lineSeparator()
                + "        <dc:language>ja-JP</dc:language>" + System.lineSeparator()
                + "        <dc:rights>Public Domain</dc:rights>" + System.lineSeparator()
                + "        <dc:publisher>Nicolas</dc:publisher>" + System.lineSeparator() + "    </metadata>"
                + System.lineSeparator() + "    <manifest >" + System.lineSeparator()
                + "        <item id=\"ncx\" href=\"toc.ncx\" media-type=\"application/x-dtbncx+xml\"/>"
                + System.lineSeparator()
                + "        <item href=\"nav.xhtml\" id=\"nav\" media-type=\"application/xhtml+xml\" properties=\"nav\"/>"
                + System.lineSeparator()
                + "        <item id=\"hstyle\" href=\"horizontal.css\" media-type=\"text/css\"/>"
                + System.lineSeparator()
                + "        <item id=\"pagetemplate\" href=\"page-template.xpgt\" media-type=\"application/vnd.adobe-page-template+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c1\" href=\"c1.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c2\" href=\"c2.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c3\" href=\"c3.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c4\" href=\"c4.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator() + "    </manifest>" + System.lineSeparator()
                + "    <spine toc=\"ncx\" page-progression-direction=\"rtl\">" + System.lineSeparator()
                + "        <itemref idref=\"nav\"/>" + System.lineSeparator() + "        <itemref idref =\"c1\"/>"
                + System.lineSeparator() + "        <itemref idref =\"c2\"/>" + System.lineSeparator()
                + "        <itemref idref =\"c3\"/>" + System.lineSeparator() + "        <itemref idref =\"c4\"/>"
                + System.lineSeparator() + "    </spine>" + System.lineSeparator() + "</package>"
                + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);

    }

    @Test
    public void testTocPath() {
        String expected = "OEBPS/toc.ncx";
        String actual = epubFormat.generateTocPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testTocContent() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
                + "    <ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">" + System.lineSeparator()
                + "        <head>" + System.lineSeparator() + "            <meta name=\"dtb:depth\" content=\"1\"/>"
                + System.lineSeparator() + "            <meta name=\"dtb:totalPageCount\" content=\"0\"/>"
                + System.lineSeparator() + "            <meta name=\"dtb:maxPageNumber\" content=\"0\"/>"
                + System.lineSeparator() + "            <meta name=\"dtb:uid\" content=\"nicolas nicolas test\"/>"
                + System.lineSeparator() + "        </head>" + System.lineSeparator() + "        <docTitle>"
                + System.lineSeparator() + "        <text>null - test</text>" + System.lineSeparator()
                + "        </docTitle>" + System.lineSeparator() + "        <navMap>" + System.lineSeparator()
                + "        <navPoint id=\"c1\" playOrder =\"0\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c1\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c1.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c2\" playOrder =\"1\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c2\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c2.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c3\" playOrder =\"2\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c3\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c3.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c4\" playOrder =\"3\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c4\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c4.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator() + "</navMap>"
                + System.lineSeparator() + "</ncx>";
        String actual = epubFormat.generateTocContent(volume);
        assertEquals(expected, actual);
    }

    @Test
    public void testToc() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateToc(zipOutputStream, volume);
        }

        String expected = "OEBPS/toc.ncx" + System.lineSeparator() + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + System.lineSeparator() + "    <ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">"
                + System.lineSeparator() + "        <head>" + System.lineSeparator()
                + "            <meta name=\"dtb:depth\" content=\"1\"/>" + System.lineSeparator()
                + "            <meta name=\"dtb:totalPageCount\" content=\"0\"/>" + System.lineSeparator()
                + "            <meta name=\"dtb:maxPageNumber\" content=\"0\"/>" + System.lineSeparator()
                + "            <meta name=\"dtb:uid\" content=\"nicolas nicolas test\"/>" + System.lineSeparator()
                + "        </head>" + System.lineSeparator() + "        <docTitle>" + System.lineSeparator()
                + "        <text>null - test</text>" + System.lineSeparator() + "        </docTitle>"
                + System.lineSeparator() + "        <navMap>" + System.lineSeparator()
                + "        <navPoint id=\"c1\" playOrder =\"0\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c1\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c1.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c2\" playOrder =\"1\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c2\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c2.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c3\" playOrder =\"2\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c3\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c3.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c4\" playOrder =\"3\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c4\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c4.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator() + "</navMap>"
                + System.lineSeparator() + "</ncx>" + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateChapterPath() {
        Chapter chapter = volume.getChapters().get(0);
        String expected = "OEBPS/c1.xhtml";
        String actual = epubFormat.generateChapterPath(chapter);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateChapterContent() {
        Chapter chapter = volume.getChapters().get(0);
        String expected = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\r\n" + "<!DOCTYPE html>\r\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "<head>\r\n" + "<meta charset=\"utf-8\" />\r\n"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"horizontal.css\" class=\"horizontal\" title=\"horizontal\" />\r\n"
                + "<title>＃１　死亡、そして復活。</title>\r\n" + "</head>\r\n" + "<body>\r\n" + "<div class=\"novel_bn\">\n"
                + "<a href=\"/n1443bp/2/\">次へ&#xa0;&gt;&gt;</a></div><div id=\"novel_no\">1/500</div><p class=\"novel_subtitle\">＃１　死亡、そして復活。</p><div id=\"novel_honbun\" class=\"novel_view\">\n"
                + "<p id=\"L1\"><br></br></p>\n" + "<p id=\"L2\">「というわけで、お前さんは死んでしまった。本当に申し訳ない」</p>\n"
                + "<p id=\"L3\">「はあ」</p>\n" + "<p id=\"L4\"><br></br></p>\n"
                + "<p id=\"L5\">　深々と頭を下げるご老人。その背後に広がるは輝く雲海。どこまでも雲の絨毯が広がり、果てが見えない。でも、自分たちが座っているのは畳の上。質素な四畳半の部屋が（部屋と言っても壁も天井もないが）雲の上に浮いている。ちゃぶ台に茶箪笥、レトロ調なテレビに黒電話。古めかしいが味のある家具類が並ぶ。</p>\n"
                + "<p id=\"L6\">　そして目の前にいるのは神様。少なくとも本人はそう言ってる。神様が言うには、間違って僕を死なせてしまったらしいが、死んだという実感がいまいち自分には無い。</p>\n"
                + "<p id=\"L7\">　確か下校中、突然降り出した雨に僕は家路を急いでいた。近くの公園を横切って近道をしようとした瞬間、襲ってきたのはまぶしい光と轟音。</p>\n"
                + "<p id=\"L8\"><br></br></p>\n"
                + "<p id=\"L9\">「雷を落とした先に人がいるか確認を怠った。本当に申し訳ない。落雷で死ぬ人間もけっこういるが、今回のケースは予定外じゃった」</p>\n"
                + "<p id=\"L10\">「雷が直撃して僕は死んだわけですか…。なるほど。するとここは天国？」</p>\n"
                + "<p id=\"L11\">「いや、天国よりさらに上、神様たちのいる世界……そうじゃな、神界とでも言うかな。人間が来ることは本当は出来ん。君は特別にワシが呼んだんじゃよ、えーっと……も…もちづき…」</p>\n"
                + "<p id=\"L12\">「とうや。望月冬夜です」</p>\n" + "<p id=\"L13\">「そうそう望月冬夜君」</p>\n"
                + "<p id=\"L14\"><br></br></p>\n"
                + "<p id=\"L15\">　神様はそう言いながら傍のヤカンから急須にお湯を注ぎ、湯呑みにお茶をいれてくれた。あ、茶柱立ってる。</p>\n"
                + "<p id=\"L16\"><br></br></p>\n"
                + "<p id=\"L17\">「しかし、君は少し落ち着き過ぎやせんかね？　自分が死んだんじゃ、もっとこう慌てたりするもんだと思っていたが」</p>\n"
                + "<p id=\"L18\">「あまり現実感が無いからですかね？　どこか夢の中のような感じですし。起こってしまったことをどうこう言っても仕方ないですよ」</p>\n"
                + "<p id=\"L19\">「達観しとるのう」</p>\n" + "<p id=\"L20\"><br></br></p>\n"
                + "<p id=\"L21\">　さすがに15で死ぬとは思っていなかったが。ズズズ…とお茶を飲む。美味い。</p>\n" + "<p id=\"L22\"><br></br></p>\n"
                + "<p id=\"L23\">「で、これから僕はどうなるんでしょうか？　天国か地獄、どちらかに？」</p>\n"
                + "<p id=\"L24\">「いやいや、君はワシの落ち度から死んでしまったのじゃから、すぐ生き返らせることができる。ただのう…」</p>\n"
                + "<p id=\"L25\"><br></br></p>\n" + "<p id=\"L26\">　言いよどむ神様。なんだろう、何か問題があるんだろうか。</p>\n"
                + "<p id=\"L27\"><br></br></p>\n"
                + "<p id=\"L28\">「君の元いた世界に生き返らせるわけにはいかんのじゃよ。すまんがそういうルールでな。こちらの都合で本当に申し訳ない。で、じゃ」</p>\n"
                + "<p id=\"L29\">「はい」</p>\n"
                + "<p id=\"L30\">「お前さんには別の世界で蘇ってもらいたい。そこで第二の人生をスタート、というわけじゃ。納得出来ない気持ちもわかる、だが」</p>\n"
                + "<p id=\"L31\">「いいですよ」</p>\n" + "<p id=\"L32\">「……いいのか？」</p>\n" + "<p id=\"L33\"><br></br></p>\n"
                + "<p id=\"L34\">　言葉を遮って僕が即答すると、神様がポカンとした顔でこちらを見ている。</p>\n" + "<p id=\"L35\"><br></br></p>\n"
                + "<p id=\"L36\">「そちらの事情は分かりましたし、無理強いをする気もありません。生き返るだけでありがたいですし。それでけっこうです」</p>\n"
                + "<p id=\"L37\">「…本当にお前さんは人格が出来とるのう。あの世界で生きていれは大人物になれたろうに…本当に申し訳ない」</p>\n"
                + "<p id=\"L38\"><br></br></p>\n"
                + "<p id=\"L39\">　しょんぼりとする神様。僕はいわゆるおじいちゃん子だったので、なんだかいたたまれない気持ちになる。そんなに気にしないでいいのに。</p>\n"
                + "<p id=\"L40\"><br></br></p>\n" + "<p id=\"L41\">「罪ほろぼしにせめて何かさせてくれんか。ある程度のことなら叶えてやれるぞ？」</p>\n"
                + "<p id=\"L42\">「うーん、そう言われましても…」</p>\n" + "<p id=\"L43\"><br></br></p>\n"
                + "<p id=\"L44\">　一番は元の世界での復活だが、それは無理。で、あるならば、これから行く世界で役立つものがいいのだろうが…。</p>\n"
                + "<p id=\"L45\"><br></br></p>\n" + "<p id=\"L46\">「これから僕が行く世界って、どんなところですか？」</p>\n"
                + "<p id=\"L47\">「君が元いた世界と比べると、まだまだ発展途上の世界じゃな。ほれ、君の世界でいうところの中世時代、半分くらいはあれに近い。まあ、全部が全部あのレベルではないが」</p>\n"
                + "<p id=\"L48\"><br></br></p>\n"
                + "<p id=\"L49\">　うーん、だいぶ生活レベルは下がるらしいなあ。そんなとこでやっていけるか不安だ。何の知識もない自分がそんな世界に飛び込んで大丈夫だろうか。あ。</p>\n"
                + "<p id=\"L50\"><br></br></p>\n" + "<p id=\"L51\">「あの、ひとつお願いが」</p>\n"
                + "<p id=\"L52\">「お、なんじゃなんじゃ。なんでも叶えてやるぞ？」</p>\n" + "<p id=\"L53\">「これ、向こうの世界でも使えるようにできませんかね？」</p>\n"
                + "<p id=\"L54\"><br></br></p>\n"
                + "<p id=\"L55\">　そう言って僕が制服の内ポケットから出したもの。小さな金属の板のような万能携帯電話。いわゆるスマートフォン。</p>\n"
                + "<p id=\"L56\"><br></br></p>\n" + "<p id=\"L57\">「これをか？　まあ可能じゃが…。いくつか制限されるぞ。それでもいいなら…」</p>\n"
                + "<p id=\"L58\">「例えば？」</p>\n"
                + "<p id=\"L59\">「君からの直接干渉はほぼ出来ん。通話やメール、サイトへの書き込み等じゃな。見るだけ読むだけなら問題ない。そうじゃな…ワシに電話くらいはできるようにしとこう」</p>\n"
                + "<p id=\"L60\">「充分ですよ」</p>\n" + "<p id=\"L61\"><br></br></p>\n"
                + "<p id=\"L62\">　元いた世界の情報が引き出せれば、それはかなりの武器になる。何をするにしても役立つには違いない。</p>\n"
                + "<p id=\"L63\"><br></br></p>\n" + "<p id=\"L64\">「バッテリーは君の魔力で充電できるようにしとこうかの。これで電池切れは心配あるまい」</p>\n"
                + "<p id=\"L65\">「魔力？　向こうの世界にはそんな力があるんですか？　じゃあ魔法とかも？」</p>\n"
                + "<p id=\"L66\">「あるよ。なに、君ならすぐに使えるようになる」</p>\n" + "<p id=\"L67\"><br></br></p>\n"
                + "<p id=\"L68\">　魔法が使えるようになるのか。それは面白そうだ。異世界へ行く楽しみができた。</p>\n" + "<p id=\"L69\"><br></br></p>\n"
                + "<p id=\"L70\">「さて、そろそろ蘇ってもらうとするか」</p>\n" + "<p id=\"L71\">「いろいろお世話になりました」</p>\n"
                + "<p id=\"L72\">「いや、元はといえば悪いのはこっちじゃから。おっと最後にひとつ」</p>\n" + "<p id=\"L73\"><br></br></p>\n"
                + "<p id=\"L74\">　神様が軽く手をかざすと暖かな光が僕の周りを包む。</p>\n" + "<p id=\"L75\"><br></br></p>\n"
                + "<p id=\"L76\">「蘇ってまたすぐ死んでしまっては意味ないからのう。基礎能力、身体能力、その他諸々底上げしとこう。これでよほどのことがなければ死ぬことはない。間抜けな神様が雷でも落とさん限りはな」</p>\n"
                + "<p id=\"L77\"><br></br></p>\n" + "<p id=\"L78\">　そう言って神様は自虐的に笑った。つられて僕も笑う。</p>\n"
                + "<p id=\"L79\"><br></br></p>\n" + "<p id=\"L80\">「一度送り出してしまうと、もうワシは干渉できんのでな。最後のプレゼントじゃ」</p>\n"
                + "<p id=\"L81\">「ありがとうございます」</p>\n" + "<p id=\"L82\">「手出しはできんが、相談に乗るぐらいはできる。困ったらいつでもそれで連絡しなさい」</p>\n"
                + "<p id=\"L83\"><br></br></p>\n"
                + "<p id=\"L84\">　神様は僕の手の中にあるスマホを指差しそう言った。気安く神様に電話ってのもなかなかできないと思うけど、本当に困ったら力を借りるとしよう。</p>\n"
                + "<p id=\"L85\"><br></br></p>\n" + "<p id=\"L86\">「では、またな」</p>\n" + "<p id=\"L87\"><br></br></p>\n"
                + "<p id=\"L88\">　神様が微笑んだ次の瞬間、僕の意識はフッと途絶えた。</p>\n" + "</div><div class=\"novel_bn\">\n"
                + "<a href=\"/n1443bp/2/\">次へ&#xa0;&gt;&gt;</a><a href=\"https://ncode.syosetu.com/n1443bp/\">目次</a></div>"
                + System.lineSeparator() + "</body>\r\n" + "</html>";
        String actual = epubFormat.generateChapterContent(chapter);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateChapter() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        Chapter chapter = volume.getChapters().get(0);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateChapter(zipOutputStream, chapter);
        }

        String expected = "OEBPS/c1.xhtml" + System.lineSeparator()
                + "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\r\n" + "<!DOCTYPE html>\r\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "<head>\r\n" + "<meta charset=\"utf-8\" />\r\n"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"horizontal.css\" class=\"horizontal\" title=\"horizontal\" />\r\n"
                + "<title>＃１　死亡、そして復活。</title>\r\n" + "</head>\r\n" + "<body>\r\n" + "<div class=\"novel_bn\">\n"
                + "<a href=\"/n1443bp/2/\">次へ&#xa0;&gt;&gt;</a></div><div id=\"novel_no\">1/500</div><p class=\"novel_subtitle\">＃１　死亡、そして復活。</p><div id=\"novel_honbun\" class=\"novel_view\">\n"
                + "<p id=\"L1\"><br></br></p>\n" + "<p id=\"L2\">「というわけで、お前さんは死んでしまった。本当に申し訳ない」</p>\n"
                + "<p id=\"L3\">「はあ」</p>\n" + "<p id=\"L4\"><br></br></p>\n"
                + "<p id=\"L5\">　深々と頭を下げるご老人。その背後に広がるは輝く雲海。どこまでも雲の絨毯が広がり、果てが見えない。でも、自分たちが座っているのは畳の上。質素な四畳半の部屋が（部屋と言っても壁も天井もないが）雲の上に浮いている。ちゃぶ台に茶箪笥、レトロ調なテレビに黒電話。古めかしいが味のある家具類が並ぶ。</p>\n"
                + "<p id=\"L6\">　そして目の前にいるのは神様。少なくとも本人はそう言ってる。神様が言うには、間違って僕を死なせてしまったらしいが、死んだという実感がいまいち自分には無い。</p>\n"
                + "<p id=\"L7\">　確か下校中、突然降り出した雨に僕は家路を急いでいた。近くの公園を横切って近道をしようとした瞬間、襲ってきたのはまぶしい光と轟音。</p>\n"
                + "<p id=\"L8\"><br></br></p>\n"
                + "<p id=\"L9\">「雷を落とした先に人がいるか確認を怠った。本当に申し訳ない。落雷で死ぬ人間もけっこういるが、今回のケースは予定外じゃった」</p>\n"
                + "<p id=\"L10\">「雷が直撃して僕は死んだわけですか…。なるほど。するとここは天国？」</p>\n"
                + "<p id=\"L11\">「いや、天国よりさらに上、神様たちのいる世界……そうじゃな、神界とでも言うかな。人間が来ることは本当は出来ん。君は特別にワシが呼んだんじゃよ、えーっと……も…もちづき…」</p>\n"
                + "<p id=\"L12\">「とうや。望月冬夜です」</p>\n" + "<p id=\"L13\">「そうそう望月冬夜君」</p>\n"
                + "<p id=\"L14\"><br></br></p>\n"
                + "<p id=\"L15\">　神様はそう言いながら傍のヤカンから急須にお湯を注ぎ、湯呑みにお茶をいれてくれた。あ、茶柱立ってる。</p>\n"
                + "<p id=\"L16\"><br></br></p>\n"
                + "<p id=\"L17\">「しかし、君は少し落ち着き過ぎやせんかね？　自分が死んだんじゃ、もっとこう慌てたりするもんだと思っていたが」</p>\n"
                + "<p id=\"L18\">「あまり現実感が無いからですかね？　どこか夢の中のような感じですし。起こってしまったことをどうこう言っても仕方ないですよ」</p>\n"
                + "<p id=\"L19\">「達観しとるのう」</p>\n" + "<p id=\"L20\"><br></br></p>\n"
                + "<p id=\"L21\">　さすがに15で死ぬとは思っていなかったが。ズズズ…とお茶を飲む。美味い。</p>\n" + "<p id=\"L22\"><br></br></p>\n"
                + "<p id=\"L23\">「で、これから僕はどうなるんでしょうか？　天国か地獄、どちらかに？」</p>\n"
                + "<p id=\"L24\">「いやいや、君はワシの落ち度から死んでしまったのじゃから、すぐ生き返らせることができる。ただのう…」</p>\n"
                + "<p id=\"L25\"><br></br></p>\n" + "<p id=\"L26\">　言いよどむ神様。なんだろう、何か問題があるんだろうか。</p>\n"
                + "<p id=\"L27\"><br></br></p>\n"
                + "<p id=\"L28\">「君の元いた世界に生き返らせるわけにはいかんのじゃよ。すまんがそういうルールでな。こちらの都合で本当に申し訳ない。で、じゃ」</p>\n"
                + "<p id=\"L29\">「はい」</p>\n"
                + "<p id=\"L30\">「お前さんには別の世界で蘇ってもらいたい。そこで第二の人生をスタート、というわけじゃ。納得出来ない気持ちもわかる、だが」</p>\n"
                + "<p id=\"L31\">「いいですよ」</p>\n" + "<p id=\"L32\">「……いいのか？」</p>\n" + "<p id=\"L33\"><br></br></p>\n"
                + "<p id=\"L34\">　言葉を遮って僕が即答すると、神様がポカンとした顔でこちらを見ている。</p>\n" + "<p id=\"L35\"><br></br></p>\n"
                + "<p id=\"L36\">「そちらの事情は分かりましたし、無理強いをする気もありません。生き返るだけでありがたいですし。それでけっこうです」</p>\n"
                + "<p id=\"L37\">「…本当にお前さんは人格が出来とるのう。あの世界で生きていれは大人物になれたろうに…本当に申し訳ない」</p>\n"
                + "<p id=\"L38\"><br></br></p>\n"
                + "<p id=\"L39\">　しょんぼりとする神様。僕はいわゆるおじいちゃん子だったので、なんだかいたたまれない気持ちになる。そんなに気にしないでいいのに。</p>\n"
                + "<p id=\"L40\"><br></br></p>\n" + "<p id=\"L41\">「罪ほろぼしにせめて何かさせてくれんか。ある程度のことなら叶えてやれるぞ？」</p>\n"
                + "<p id=\"L42\">「うーん、そう言われましても…」</p>\n" + "<p id=\"L43\"><br></br></p>\n"
                + "<p id=\"L44\">　一番は元の世界での復活だが、それは無理。で、あるならば、これから行く世界で役立つものがいいのだろうが…。</p>\n"
                + "<p id=\"L45\"><br></br></p>\n" + "<p id=\"L46\">「これから僕が行く世界って、どんなところですか？」</p>\n"
                + "<p id=\"L47\">「君が元いた世界と比べると、まだまだ発展途上の世界じゃな。ほれ、君の世界でいうところの中世時代、半分くらいはあれに近い。まあ、全部が全部あのレベルではないが」</p>\n"
                + "<p id=\"L48\"><br></br></p>\n"
                + "<p id=\"L49\">　うーん、だいぶ生活レベルは下がるらしいなあ。そんなとこでやっていけるか不安だ。何の知識もない自分がそんな世界に飛び込んで大丈夫だろうか。あ。</p>\n"
                + "<p id=\"L50\"><br></br></p>\n" + "<p id=\"L51\">「あの、ひとつお願いが」</p>\n"
                + "<p id=\"L52\">「お、なんじゃなんじゃ。なんでも叶えてやるぞ？」</p>\n" + "<p id=\"L53\">「これ、向こうの世界でも使えるようにできませんかね？」</p>\n"
                + "<p id=\"L54\"><br></br></p>\n"
                + "<p id=\"L55\">　そう言って僕が制服の内ポケットから出したもの。小さな金属の板のような万能携帯電話。いわゆるスマートフォン。</p>\n"
                + "<p id=\"L56\"><br></br></p>\n" + "<p id=\"L57\">「これをか？　まあ可能じゃが…。いくつか制限されるぞ。それでもいいなら…」</p>\n"
                + "<p id=\"L58\">「例えば？」</p>\n"
                + "<p id=\"L59\">「君からの直接干渉はほぼ出来ん。通話やメール、サイトへの書き込み等じゃな。見るだけ読むだけなら問題ない。そうじゃな…ワシに電話くらいはできるようにしとこう」</p>\n"
                + "<p id=\"L60\">「充分ですよ」</p>\n" + "<p id=\"L61\"><br></br></p>\n"
                + "<p id=\"L62\">　元いた世界の情報が引き出せれば、それはかなりの武器になる。何をするにしても役立つには違いない。</p>\n"
                + "<p id=\"L63\"><br></br></p>\n" + "<p id=\"L64\">「バッテリーは君の魔力で充電できるようにしとこうかの。これで電池切れは心配あるまい」</p>\n"
                + "<p id=\"L65\">「魔力？　向こうの世界にはそんな力があるんですか？　じゃあ魔法とかも？」</p>\n"
                + "<p id=\"L66\">「あるよ。なに、君ならすぐに使えるようになる」</p>\n" + "<p id=\"L67\"><br></br></p>\n"
                + "<p id=\"L68\">　魔法が使えるようになるのか。それは面白そうだ。異世界へ行く楽しみができた。</p>\n" + "<p id=\"L69\"><br></br></p>\n"
                + "<p id=\"L70\">「さて、そろそろ蘇ってもらうとするか」</p>\n" + "<p id=\"L71\">「いろいろお世話になりました」</p>\n"
                + "<p id=\"L72\">「いや、元はといえば悪いのはこっちじゃから。おっと最後にひとつ」</p>\n" + "<p id=\"L73\"><br></br></p>\n"
                + "<p id=\"L74\">　神様が軽く手をかざすと暖かな光が僕の周りを包む。</p>\n" + "<p id=\"L75\"><br></br></p>\n"
                + "<p id=\"L76\">「蘇ってまたすぐ死んでしまっては意味ないからのう。基礎能力、身体能力、その他諸々底上げしとこう。これでよほどのことがなければ死ぬことはない。間抜けな神様が雷でも落とさん限りはな」</p>\n"
                + "<p id=\"L77\"><br></br></p>\n" + "<p id=\"L78\">　そう言って神様は自虐的に笑った。つられて僕も笑う。</p>\n"
                + "<p id=\"L79\"><br></br></p>\n" + "<p id=\"L80\">「一度送り出してしまうと、もうワシは干渉できんのでな。最後のプレゼントじゃ」</p>\n"
                + "<p id=\"L81\">「ありがとうございます」</p>\n" + "<p id=\"L82\">「手出しはできんが、相談に乗るぐらいはできる。困ったらいつでもそれで連絡しなさい」</p>\n"
                + "<p id=\"L83\"><br></br></p>\n"
                + "<p id=\"L84\">　神様は僕の手の中にあるスマホを指差しそう言った。気安く神様に電話ってのもなかなかできないと思うけど、本当に困ったら力を借りるとしよう。</p>\n"
                + "<p id=\"L85\"><br></br></p>\n" + "<p id=\"L86\">「では、またな」</p>\n" + "<p id=\"L87\"><br></br></p>\n"
                + "<p id=\"L88\">　神様が微笑んだ次の瞬間、僕の意識はフッと途絶えた。</p>\n" + "</div><div class=\"novel_bn\">\n"
                + "<a href=\"/n1443bp/2/\">次へ&#xa0;&gt;&gt;</a><a href=\"https://ncode.syosetu.com/n1443bp/\">目次</a></div>"
                + System.lineSeparator() + "</body>\r\n" + "</html>" + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateCssPath() {
        String expected = "OEBPS/horizontal.css";
        String actual = epubFormat.generateCssPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateCssContent() {
        String expected = "html{" + System.lineSeparator()
                + "    font-family: 'foobar', \"HiraMinProN-W3\", \"@ＭＳ明朝\", serif, sans-serif;"
                + System.lineSeparator() + "    font-size: 14pt;" + System.lineSeparator() + "    margin: auto 1em;"
                + System.lineSeparator() + "    padding: 1em 0;" + System.lineSeparator() + "    max-width: 28em;}"
                + System.lineSeparator() + "body{" + System.lineSeparator() + "    margin: 0;" + System.lineSeparator()
                + "}" + System.lineSeparator() + "h1{" + System.lineSeparator() + "    font-weight: normal;"
                + System.lineSeparator() + "    line-height: 2;" + System.lineSeparator() + "    font-size: 2em;"
                + System.lineSeparator() + "    margin-top: 2em;" + System.lineSeparator() + "}";
        String actual = epubFormat.generateCssContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateCss() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateCss(zipOutputStream);
        }

        String expected = "OEBPS/horizontal.css" + System.lineSeparator() + "html{" + System.lineSeparator()
                + "    font-family: 'foobar', \"HiraMinProN-W3\", \"@ＭＳ明朝\", serif, sans-serif;"
                + System.lineSeparator() + "    font-size: 14pt;" + System.lineSeparator() + "    margin: auto 1em;"
                + System.lineSeparator() + "    padding: 1em 0;" + System.lineSeparator() + "    max-width: 28em;}"
                + System.lineSeparator() + "body{" + System.lineSeparator() + "    margin: 0;" + System.lineSeparator()
                + "}" + System.lineSeparator() + "h1{" + System.lineSeparator() + "    font-weight: normal;"
                + System.lineSeparator() + "    line-height: 2;" + System.lineSeparator() + "    font-size: 2em;"
                + System.lineSeparator() + "    margin-top: 2em;" + System.lineSeparator() + "}"
                + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateEbook() throws FileNotFoundException, IOException {
        epubFormat.generate(volume);
        String epubPath = System.getProperty("user.dir") + "\\test.epub";
        assertEquals("D:\\Programming\\git\\repository\\epub_scraper_desktop\\test.epub", epubPath);

    }
}
