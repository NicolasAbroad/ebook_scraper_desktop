package com.nicolas_abroad.epub_scraper_desktop.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.adobe.epubcheck.api.EpubCheck;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Chapter;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.SyosetsuScraper;

/**
 * Unit test class for the EpubFormat class.
 * @author Nicolas
 */
public class EpubFormatTest {

    private EpubFormat epubFormat = new EpubFormat();
    private static String volumeTitle = "test";
    private static Path epubPath = Paths.get(System.getProperty("user.dir") + "\\" + volumeTitle + ".epub");

    /** Volume used for happy path testing */
    private static Volume volume;
    /** Volume used for unhappy path testing. Title contains "&". */
    private static Volume edgeCaseVolume;
    private static String chapterContent;

    /** Temporary folder used to ensure no files are left after tests */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Setup volume used for tests
     * @throws IOException
     */
    @BeforeClass
    public static void setUp() throws IOException {
        List<String> chapterUrls = new ArrayList<String>();
        chapterUrls.add("https://ncode.syosetu.com/n5464di/1/");
        chapterUrls.add("https://ncode.syosetu.com/n5464di/2/");
        chapterUrls.add("https://ncode.syosetu.com/n5464di/3/");
        chapterUrls.add("https://ncode.syosetu.com/n5464di/4/");
        chapterUrls.add("https://ncode.syosetu.com/n5464di/5/");
        chapterUrls.add("https://ncode.syosetu.com/n5464di/6/");
        chapterUrls.add("https://ncode.syosetu.com/n5464di/7/");
        volume = new Volume(new SyosetsuScraper(), chapterUrls);
        volume.setTitle(volumeTitle);
        volume.setAuthor("nicolas");
        volume.generate();

        chapterUrls = new ArrayList<String>();
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/151/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/152/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/153/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/154/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/155/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/156/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/157/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/158/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/159/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/160/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/161/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/162/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/163/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/164/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/165/");
        chapterUrls.add("https://ncode.syosetu.com/n6681fa/166/");
        edgeCaseVolume = new Volume(new SyosetsuScraper(), chapterUrls);
        edgeCaseVolume.setTitle("第12話：Hooked  Feeling");
        edgeCaseVolume.setVolumeNumber("13");
        edgeCaseVolume.setAuthor("大虎龍真");
        edgeCaseVolume.generate();
    }

    /**
     * Setup chapter content
     * @throws IOException
     */
    @BeforeClass
    public static void setUpChapterContent() throws IOException {
        chapterContent = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\r\n" + "<!DOCTYPE html>\r\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "<head>\r\n" + "<meta charset=\"utf-8\" />\r\n"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"horizontal.css\" class=\"horizontal\" title=\"horizontal\" />\r\n"
                + "<title>共通⑤ 彼女は幼馴染み？</title>\r\n" + "</head>\r\n" + "<body>\r\n" + "<div class=\"novel_bn\">\n"
                + "<a>&lt;&lt;&#xa0;前へ</a><a>次へ&#xa0;&gt;&gt;</a></div><div id=\"novel_no\">5/7</div><p class=\"novel_subtitle\">共通⑤ 彼女は幼馴染み？</p><div id=\"novel_honbun\" class=\"novel_view\">\n"
                + "<p id=\"L1\">「マグナダリアさん!!」</p>\n" + "<p id=\"L2\">ウラミルヂィが小部屋のドアを蹴破る。</p>\n"
                + "<p id=\"L3\"><br></br></p>\n" + "<p id=\"L4\">室内には筋トレ器具のようなものが無数にある。</p>\n"
                + "<p id=\"L5\">その中に一人の赤髪の女がいた。</p>\n" + "<p id=\"L6\"><br></br></p>\n"
                + "<p id=\"L7\">結われていない長い髪、鎧、背中に剣を背負っている。</p>\n" + "<p id=\"L8\"><br></br></p>\n"
                + "<p id=\"L9\">（なんかこいつどっかで見たことあるな）</p>\n" + "<p id=\"L10\">新斗は彼女の顔を見て、何かを思い出した。</p>\n"
                + "<p id=\"L11\"><br></br></p>\n" + "<p id=\"L12\">「あんた、俺の幼馴染みじゃないよな？」</p>\n"
                + "<p id=\"L13\">彼女の顔が幼馴染みに似ており、念のため確認した新斗。</p>\n" + "<p id=\"L14\"><br></br></p>\n"
                + "<p id=\"L15\">「はぁ!?」</p>\n" + "<p id=\"L16\"><br></br></p>\n" + "<p id=\"L17\">（違ったか</p>\n"
                + "<p id=\"L18\">…今頃あいつは大企業のOLやってんだろうな）</p>\n" + "<p id=\"L19\"><br></br></p>\n"
                + "<p id=\"L20\">「ちょっとウラミルヂィ！誰なのよこいつら!」</p>\n" + "<p id=\"L21\">マグナダリアは新斗達に剣を向けた。</p>\n"
                + "<p id=\"L22\"><br></br></p>\n" + "<p id=\"L23\">「剣士マグナダリア殿</p>\n"
                + "<p id=\"L24\">私の顔に免じて、許してくれ」</p>\n" + "<p id=\"L25\">ディレスタントが前に出た。</p>\n"
                + "<p id=\"L26\"><br></br></p>\n" + "<p id=\"L27\">「ディレスタント女史……」</p>\n"
                + "<p id=\"L28\">（よかった…落ち着いてくれた）</p>\n" + "<p id=\"L29\"><br></br></p>\n"
                + "<p id=\"L30\">「…って尚更ダメよ!!」</p>\n" + "<p id=\"L31\">マグナダリアは剣を振りかざした。</p>\n"
                + "<p id=\"L32\"><br></br></p>\n" + "<p id=\"L33\">「あぶねっ」</p>\n"
                + "<p id=\"L34\">「姫様を盾にするなんて、とんだ悪党ね！」</p>\n" + "<p id=\"L35\"><br></br></p>\n"
                + "<p id=\"L36\">「あらあら～どうしたんでしょう？」</p>\n" + "<p id=\"L37\">「姫様、危ないので我々は離れていましょう」</p>\n"
                + "<p id=\"L38\"><a></a></p>\n" + "<p id=\"L39\"><br></br></p>\n"
                + "<p id=\"L40\">ディレスタントはエカドリーユを連れて、外へ避難した。</p>\n" + "<p id=\"L41\"><br></br></p>\n"
                + "<p id=\"L42\">「ハアアアアアア」</p>\n" + "<p id=\"L43\">あえて避けられていたマグナダリアの剣は確実に新斗を狙う。</p>\n"
                + "<p id=\"L44\"><br></br></p>\n" + "<p id=\"L45\">「やめなさい!」</p>\n" + "<p id=\"L46\"><a></a></p>\n"
                + "<p id=\"L47\"><br></br></p>\n" + "<p id=\"L48\">ウラミルヂィが新斗の前に出て、足で剣の中心を蹴り、無数のナイフを投げる。</p>\n"
                + "<p id=\"L49\">マグナダリアの腕にはあたらずに、無数のナイフは剣を打撃した。</p>\n" + "<p id=\"L50\">マグナダリアの手から剣が落ちる。</p>\n"
                + "<p id=\"L51\"><br></br></p>\n" + "<p id=\"L52\">マグナダリアはようやく話を聞く体制になった。</p>\n"
                + "</div><div class=\"novel_bn\">\n"
                + "<a>&lt;&lt;&#xa0;前へ</a><a>次へ&#xa0;&gt;&gt;</a><a>目次</a></div>\r\n" + "</body>\r\n" + "</html>";
    }

    /**
     * Read zip file.
     * @param file
     * @return zip file content
     * @throws IOException
     */
    private String readZipFile(File file) throws IOException {
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

    /**
     * Test writing file to zip
     * @throws IOException
     */
    @Test
    public void testWriteFileToZip() throws IOException {
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

    /** Test mimetype file content generation */
    @Test
    public void testGenerateMimetypeContent() {
        String expected = "application/epub+zip";
        String actual = epubFormat.generateMimetypeContent();
        assertEquals(expected, actual);
    }

    /** Test mimetype file path generation */
    @Test
    public void testGenerateMimetypePath() {
        String expected = "mimetype";
        String actual = epubFormat.generateMimetypePath();
        assertEquals(expected, actual);
    }

    /**
     * Test mimetype file generation
     * @throws IOException
     */
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

    /** Test container file content generation */
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

    /** Test container file path generation */
    @Test
    public void testGenerateContainerPath() {
        String expected = "META-INF/container.xml";
        String actual = epubFormat.generateContainerPath();
        assertEquals(expected, actual);
    }

    /**
     * Test container file generation
     * @throws IOException
     */
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

    /** Test content file content generation */
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
                + System.lineSeparator()
                + "        <item id =\"c5\" href=\"c5.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c6\" href=\"c6.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c7\" href=\"c7.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator() + "    </manifest>" + System.lineSeparator()
                + "    <spine toc=\"ncx\" page-progression-direction=\"rtl\">" + System.lineSeparator()
                + "        <itemref idref=\"nav\"/>" + System.lineSeparator() + "        <itemref idref =\"c1\"/>"
                + System.lineSeparator() + "        <itemref idref =\"c2\"/>" + System.lineSeparator()
                + "        <itemref idref =\"c3\"/>" + System.lineSeparator() + "        <itemref idref =\"c4\"/>"
                + System.lineSeparator() + "        <itemref idref =\"c5\"/>" + System.lineSeparator()
                + "        <itemref idref =\"c6\"/>" + System.lineSeparator() + "        <itemref idref =\"c7\"/>"
                + System.lineSeparator() + "    </spine>" + System.lineSeparator() + "</package>";
        String actual = epubFormat.generateContentContent(volume);
        assertEquals(expected, actual);
    }

    /** Test content file path generation */
    @Test
    public void testGenerateContentPath() {
        String expected = "OEBPS/content.opf";
        String actual = epubFormat.generateContentPath();
        assertEquals(expected, actual);
    }

    /**
     * Test content file generation
     * @throws IOException
     */
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
                + System.lineSeparator()
                + "        <item id =\"c5\" href=\"c5.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c6\" href=\"c6.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator()
                + "        <item id =\"c7\" href=\"c7.xhtml\" media-type=\"application/xhtml+xml\"/>"
                + System.lineSeparator() + "    </manifest>" + System.lineSeparator()
                + "    <spine toc=\"ncx\" page-progression-direction=\"rtl\">" + System.lineSeparator()
                + "        <itemref idref=\"nav\"/>" + System.lineSeparator() + "        <itemref idref =\"c1\"/>"
                + System.lineSeparator() + "        <itemref idref =\"c2\"/>" + System.lineSeparator()
                + "        <itemref idref =\"c3\"/>" + System.lineSeparator() + "        <itemref idref =\"c4\"/>"
                + System.lineSeparator() + "        <itemref idref =\"c5\"/>" + System.lineSeparator()
                + "        <itemref idref =\"c6\"/>" + System.lineSeparator() + "        <itemref idref =\"c7\"/>"
                + System.lineSeparator() + "    </spine>" + System.lineSeparator() + "</package>"
                + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);

    }

    /** Test toc file path generation */
    @Test
    public void testTocPath() {
        String expected = "OEBPS/toc.ncx";
        String actual = epubFormat.generateTocPath();
        assertEquals(expected, actual);
    }

    /** Test toc file content generation */
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
                + "        <navPoint id=\"c1\" playOrder =\"1\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c1\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c1.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c2\" playOrder =\"2\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c2\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c2.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c3\" playOrder =\"3\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c3\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c3.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c4\" playOrder =\"4\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c4\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c4.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c5\" playOrder =\"5\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c5\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c5.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c6\" playOrder =\"6\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c6\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c6.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c7\" playOrder =\"7\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c7\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c7.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator() + "</navMap>"
                + System.lineSeparator() + "</ncx>";
        String actual = epubFormat.generateTocContent(volume);
        assertEquals(expected, actual);
    }

    /**
     * Test toc file generation
     * @throws IOException
     */
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
                + "        <navPoint id=\"c1\" playOrder =\"1\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c1\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c1.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c2\" playOrder =\"2\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c2\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c2.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c3\" playOrder =\"3\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c3\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c3.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c4\" playOrder =\"4\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c4\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c4.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c5\" playOrder =\"5\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c5\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c5.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c6\" playOrder =\"6\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c6\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c6.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator()
                + "        <navPoint id=\"c7\" playOrder =\"7\">" + System.lineSeparator() + "            <navLabel>"
                + System.lineSeparator() + "                <text>\"c7\"</text>" + System.lineSeparator()
                + "            </navLabel>" + System.lineSeparator() + "        <content src=\"c7.xhtml\"/>"
                + System.lineSeparator() + "        </navPoint>" + System.lineSeparator() + "</navMap>"
                + System.lineSeparator() + "</ncx>" + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    /** Test chapter file path generation */
    @Test
    public void testGenerateChapterPath() {
        Chapter chapter = volume.getChapters().get(0);
        String expected = "OEBPS/c1.xhtml";
        String actual = epubFormat.generateChapterPath(chapter);
        assertEquals(expected, actual);
    }

    /** Test chapter file content generation */
    @Test
    public void testGenerateChapterContent() {
        Chapter chapter = volume.getChapters().get(4);
        String expected = chapterContent;
        String actual = epubFormat.generateChapterContent(chapter);
        assertEquals(expected, actual);
    }

    /**
     * Test chapter file generation
     * @throws IOException
     */
    @Test
    public void testGenerateChapter() throws IOException {
        File epubFile = folder.newFile(volume.getTitle() + ".epub");
        Chapter chapter = volume.getChapters().get(4);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {
            epubFormat.generateChapter(zipOutputStream, chapter);
        }

        String expected = "OEBPS/c5.xhtml" + System.lineSeparator() + chapterContent + System.lineSeparator();
        String actual = readZipFile(epubFile);
        assertEquals(expected, actual);
    }

    /** Test css file path generation */
    @Test
    public void testGenerateCssPath() {
        String expected = "OEBPS/horizontal.css";
        String actual = epubFormat.generateCssPath();
        assertEquals(expected, actual);
    }

    /** Test css file content generation */
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

    /**
     * Test css file generation
     * @throws IOException
     */
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

    /**
     * Test epub file generation
     * @throws IOException
     */
    @Test
    public void testGenerateEpub01() throws IOException {
        epubFormat.generate(volume);
        boolean isGenerated = Files.exists(epubPath);
        assertTrue(isGenerated);
        epubPath.toFile().delete();
    }

    /**
     * Test if epub file conforms to official epub specifications
     * @throws IOException
     */
    @Test
    public void testGenerateEpub02() throws IOException {
        epubFormat.generate(volume);

        File epubFile = epubPath.toFile();
        EpubCheck epubcheck = new EpubCheck(epubFile);
        assertTrue(epubcheck.validate());

        epubFile.delete();
    }

    /**
     * Test if edge case epub file conforms to official epub specifications
     * @throws IOException
     */
    @Test
    public void testGenerateEpub03() throws IOException {
        epubFormat.generate(edgeCaseVolume);

        Path epubPath = Paths.get(System.getProperty("user.dir") + "\\" + edgeCaseVolume.getTitle() + ".epub");
        File epubFile = epubPath.toFile();
        EpubCheck epubcheck = new EpubCheck(epubFile);
        assertTrue(epubcheck.validate());

        epubFile.delete();
    }

}
