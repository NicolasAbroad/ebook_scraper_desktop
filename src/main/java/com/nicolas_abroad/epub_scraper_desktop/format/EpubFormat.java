package com.nicolas_abroad.epub_scraper_desktop.format;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Chapter;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;

/**
 * Epub format.
 * @author Nicolas
 */
public class EpubFormat implements EbookFormat {

    // ----------------------------------------------------------
    // mimetype
    // ----------------------------------------------------------
    /** mimetype file name */
    private static final String MIMETYPE_TITLE = "mimetype";

    /** mimetype file text content */
    private static final String MIMETYPE_TEXT = "application/epub+zip";

    // ----------------------------------------------------------
    // container.xml
    // ----------------------------------------------------------
    /** container.xml file name */
    private static final String CONTAINER_TITLE = "container";

    /** container.xml file text content */
    private static final String CONTAINER_TEXT = "<?xml version=\"1.0\"?>" + System.lineSeparator()
            + "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">"
            + System.lineSeparator() + "    <rootfiles>" + System.lineSeparator()
            + "        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>"
            + System.lineSeparator() + "    </rootfiles>" + System.lineSeparator() + "</container>";

    // ----------------------------------------------------------
    // content.opf
    // ----------------------------------------------------------
    /** content.opf file name */
    private static final String CONTENT_TITLE = "content";

    /** content.opf file text metadata top */
    private static final String CONTENT_TEXT_METADATA_TOP = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + System.lineSeparator()
            + "<package prefix=\"cc: http://creativecommons.org/ns#\" unique-identifier=\"uid\" version=\"3.0\" xml:lang=\"en\" xmlns=\"http://www.idpf.org/2007/opf\">"
            + System.lineSeparator() + "    <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + System.lineSeparator() + "        <meta property=\"dcterms:modified\">2010-02-17T04:39:13Z</meta>"
            + System.lineSeparator();

    /** content.opf file text metadata title node */
    private static final String CONTENT_TEXT_METADATA_TITLE = "        <dc:title>%s</dc:title>"
            + System.lineSeparator();

    /** content.opf file text metadata author name node */
    private static final String CONTENT_TEXT_METADATA_AUTHOR_NAME = "        <dc:creator id=\"creator\">%s</dc:creator>"
            + System.lineSeparator();

    /** content.opf file text metadata uid node */
    private static final String CONTENT_TEXT_METADATA_UID = "        <dc:identifier id=\"uid\">nicolas%s</dc:identifier>"
            + System.lineSeparator();

    /** content.opf file text metadata bottom */
    private static final String CONTENT_TEXT_METADATA_BOTTOM = "        <dc:language>ja-JP</dc:language>"
            + System.lineSeparator() + "        <dc:rights>Public Domain</dc:rights>" + System.lineSeparator()
            + "        <dc:publisher>Nicolas</dc:publisher>" + System.lineSeparator() + "    </metadata>"
            + System.lineSeparator();

    /** content.opf file text manifest top */
    private static final String CONTENT_TEXT_MANIFEST_TOP = "    <manifest >" + System.lineSeparator()
            + "        <item id=\"ncx\" href=\"toc.ncx\" media-type=\"application/x-dtbncx+xml\"/>"
            + System.lineSeparator()
            + "        <item href=\"nav.xhtml\" id=\"nav\" media-type=\"application/xhtml+xml\" properties=\"nav\"/>"
            + System.lineSeparator() + "        <item id=\"hstyle\" href=\"horizontal.css\" media-type=\"text/css\"/>"
            + System.lineSeparator()
            + "        <item id=\"pagetemplate\" href=\"page-template.xpgt\" media-type=\"application/vnd.adobe-page-template+xml\"/>"
            + System.lineSeparator();

    /** content.opf file text manifest item node */
    private static final String CONTENT_TEXT_MANIFEST_ITEM = "        <item id =\"%s\" href=\"%s.xhtml\" media-type=\"application/xhtml+xml\"/>"
            + System.lineSeparator();

    /** content.opf file text manifest bottom */
    private static final String CONTENT_TEXT_MANIFEST_BOTTOM = "    </manifest>" + System.lineSeparator();

    /** content.opf file text spine top */
    private static final String CONTENT_TEXT_SPINE_TOP = "    <spine toc=\"ncx\" page-progression-direction=\"rtl\">"
            + System.lineSeparator() + "        <itemref idref=\"nav\"/>" + System.lineSeparator();

    /** content.opf file text spine itemref node */
    private static final String CONTENT_TEXT_SPINE_ITEMREF = "        <itemref idref =\"%s\"/>"
            + System.lineSeparator();

    /** content.opf file text spine bottom */
    private static final String CONTENT_TEXT_SPINE_BOTTOM = "    </spine>" + System.lineSeparator() + "</package>";

    // ----------------------------------------------------------
    // tox.ncx
    // ----------------------------------------------------------
    /** tox.ncx file name */
    private static final String TOC_TITLE = "toc";

    /** tox.ncx file text head top */
    private static final String TOC_TEXT_HEAD_TOP = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + System.lineSeparator() + "    <ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">"
            + System.lineSeparator() + "        <head>" + System.lineSeparator()
            + "            <meta name=\"dtb:depth\" content=\"1\"/>" + System.lineSeparator()
            + "            <meta name=\"dtb:totalPageCount\" content=\"0\"/>" + System.lineSeparator()
            + "            <meta name=\"dtb:maxPageNumber\" content=\"0\"/>" + System.lineSeparator();

    /** tox.ncx file text head uid node */
    private static final String TOC_TEXT_HEAD_UID = "            <meta name=\"dtb:uid\" content=\"nicolas%s\"/>"
            + System.lineSeparator();

    /** tox.ncx file text head bottom */
    private static final String TOC_TEXT_HEAD_BOTTOM = "        </head>" + System.lineSeparator();

    /** tox.ncx file text doctitle */
    private static final String TOC_TEXT_DOCTITLE = "        <docTitle>" + System.lineSeparator()
            + "        <text>%s</text>" + System.lineSeparator() + "        </docTitle>" + System.lineSeparator();

    /** tox.ncx file text navmap top */
    private static final String TOC_TEXT_NAVMAP_TOP = "        <navMap>" + System.lineSeparator();

    /** tox.ncx file text navmap navmap navpoint */
    private static final String TOC_TEXT_NAVMAP_NAVPOINT = "        <navPoint id=\"%s\" playOrder =\"%s\">"
            + System.lineSeparator() + "            <navLabel>" + System.lineSeparator()
            + "                <text>\"%s\"</text>" + System.lineSeparator() + "            </navLabel>"
            + System.lineSeparator() + "        <content src=\"%s.xhtml\"/>" + System.lineSeparator()
            + "        </navPoint>" + System.lineSeparator();

    /** tox.ncx file text navmap top */
    private static final String TOC_TEXT_NAVMAP_BOTTOM = "</navMap>" + System.lineSeparator() + "</ncx>";

    // ----------------------------------------------------------
    // chapter.xhtml
    // ----------------------------------------------------------
    /** chapter.xhtml file text head top */
    private static final String CHAPTER_TEXT_HEAD_TOP = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>"
            + System.lineSeparator() + "<!DOCTYPE html>" + System.lineSeparator()
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">" + System.lineSeparator() + "<head>"
            + System.lineSeparator() + "<meta charset=\"utf-8\" />" + System.lineSeparator()
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"horizontal.css\" class=\"horizontal\" title=\"horizontal\" />"
            + System.lineSeparator();

    /** chapter.xhtml file text head title node */
    private static final String CHAPTER_TEXT_HEAD_TITLE = "<title>%s</title>" + System.lineSeparator();

    /** chapter.xhtml file text head bottom */
    private static final String CHAPTER_TEXT_HEAD_BOTTOM = "</head>" + System.lineSeparator();

    /** chapter.xhtml file text body top */
    private static final String CHAPTER_TEXT_BODY_TOP = "<body>" + System.lineSeparator();

    /** chapter.xhtml file text body bottom */
    private static final String CHAPTER_TEXT_BODY_BOTTOM = "</body>" + System.lineSeparator() + "</html>";

    // ----------------------------------------------------------
    // horizontal.css
    // ----------------------------------------------------------
    private static final String CSS_TITLE = "horizontal";

    private static final String CSS_TEXT = "html{" + System.lineSeparator()
            + "    font-family: 'foobar', \"HiraMinProN-W3\", \"@ＭＳ明朝\", serif, sans-serif;" + System.lineSeparator()
            + "    font-size: 14pt;" + System.lineSeparator() + "    margin: auto 1em;" + System.lineSeparator()
            + "    padding: 1em 0;" + System.lineSeparator() + "    max-width: 28em;}" + System.lineSeparator()
            + "body{" + System.lineSeparator() + "    margin: 0;" + System.lineSeparator() + "}"
            + System.lineSeparator() + "h1{" + System.lineSeparator() + "    font-weight: normal;"
            + System.lineSeparator() + "    line-height: 2;" + System.lineSeparator() + "    font-size: 2em;"
            + System.lineSeparator() + "    margin-top: 2em;" + System.lineSeparator() + "}";

    // ----------------------------------------------------------
    // page-template.xpgt
    // ----------------------------------------------------------
    private static final String PAGE_TEMPLATE_TITLE = "page-template";

    private static final String PAGE_TEMPLATE_TEXT = "<ade:template xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ade=\"http://ns.adobe.com/2006/ade\""
            + System.lineSeparator() + "         xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">"
            + System.lineSeparator() + System.lineSeparator() + "  <fo:layout-master-set>" + System.lineSeparator()
            + "   <fo:simple-page-master master-name=\"single_column\">" + System.lineSeparator()
            + "        <fo:region-body margin-bottom=\"3pt\" margin-top=\"0.5em\" margin-left=\"3pt\" margin-right=\"3pt\"/>"
            + System.lineSeparator() + "    </fo:simple-page-master>" + System.lineSeparator() + "  "
            + System.lineSeparator() + "    <fo:simple-page-master master-name=\"single_column_head\">"
            + System.lineSeparator() + "        <fo:region-before extent=\"8.3em\"/>" + System.lineSeparator()
            + "        <fo:region-body margin-bottom=\"3pt\" margin-top=\"6em\" margin-left=\"3pt\" margin-right=\"3pt\"/>"
            + System.lineSeparator() + "    </fo:simple-page-master>" + System.lineSeparator() + System.lineSeparator()
            + "    <fo:simple-page-master master-name=\"two_column\"    margin-bottom=\"0.5em\" margin-top=\"0.5em\" margin-left=\"0.5em\" margin-right=\"0.5em\">"
            + System.lineSeparator() + "        <fo:region-body column-count=\"2\" column-gap=\"10pt\"/>"
            + System.lineSeparator() + "    </fo:simple-page-master>" + System.lineSeparator() + System.lineSeparator()
            + "    <fo:simple-page-master master-name=\"two_column_head\" margin-bottom=\"0.5em\" margin-left=\"0.5em\" margin-right=\"0.5em\">"
            + System.lineSeparator() + "        <fo:region-before extent=\"8.3em\"/>" + System.lineSeparator()
            + "        <fo:region-body column-count=\"2\" margin-top=\"6em\" column-gap=\"10pt\"/>"
            + System.lineSeparator() + "    </fo:simple-page-master>" + System.lineSeparator() + System.lineSeparator()
            + "    <fo:simple-page-master master-name=\"three_column\" margin-bottom=\"0.5em\" margin-top=\"0.5em\" margin-left=\"0.5em\" margin-right=\"0.5em\">"
            + System.lineSeparator() + "        <fo:region-body column-count=\"3\" column-gap=\"10pt\"/>"
            + System.lineSeparator() + "    </fo:simple-page-master>" + System.lineSeparator() + System.lineSeparator()
            + "    <fo:simple-page-master master-name=\"three_column_head\" margin-bottom=\"0.5em\" margin-top=\"0.5em\" margin-left=\"0.5em\" margin-right=\"0.5em\">"
            + System.lineSeparator() + "        <fo:region-before extent=\"8.3em\"/>" + System.lineSeparator()
            + "        <fo:region-body column-count=\"3\" margin-top=\"6em\" column-gap=\"10pt\"/>"
            + System.lineSeparator() + "    </fo:simple-page-master>" + System.lineSeparator() + System.lineSeparator()
            + "    <fo:page-sequence-master>" + System.lineSeparator()
            + "        <fo:repeatable-page-master-alternatives>" + System.lineSeparator()
            + "            <fo:conditional-page-master-reference master-reference=\"three_column_head\" page-position=\"first\" ade:min-page-width=\"80em\"/>"
            + System.lineSeparator()
            + "            <fo:conditional-page-master-reference master-reference=\"three_column\" ade:min-page-width=\"80em\"/>"
            + System.lineSeparator()
            + "            <fo:conditional-page-master-reference master-reference=\"two_column_head\" page-position=\"first\" ade:min-page-width=\"50em\"/>"
            + System.lineSeparator()
            + "            <fo:conditional-page-master-reference master-reference=\"two_column\" ade:min-page-width=\"50em\"/>"
            + System.lineSeparator()
            + "            <fo:conditional-page-master-reference master-reference=\"single_column_head\" page-position=\"first\" />"
            + System.lineSeparator()
            + "            <fo:conditional-page-master-reference master-reference=\"single_column\"/>"
            + System.lineSeparator() + "        </fo:repeatable-page-master-alternatives>" + System.lineSeparator()
            + "    </fo:page-sequence-master>" + System.lineSeparator() + System.lineSeparator()
            + "  </fo:layout-master-set>" + System.lineSeparator() + System.lineSeparator() + "  <ade:style>"
            + System.lineSeparator()
            + "    <ade:styling-rule selector=\".title_box\" display=\"adobe-other-region\" adobe-region=\"xsl-region-before\"/>"
            + System.lineSeparator() + "  </ade:style>" + System.lineSeparator() + System.lineSeparator()
            + "</ade:template>";

    // ----------------------------------------------------------
    // nav.html
    // ----------------------------------------------------------
    private static final String NAV_TITLE = "nav";

    private static final String NAV_TEXT_TOP = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:epub=\"http://www.idpf.org/2007/ops\" xml:lang=\"en-US\" lang=\"en-US\">"
            + System.lineSeparator() + "    <head>" + System.lineSeparator()
            + "        <title>EPUB 3 Navigation Document</title>" + System.lineSeparator()
            + "        <meta charset=\"utf-8\"/>" + System.lineSeparator()
            + "        <link rel=\"horizontal\" type=\"text/css\" href=\"horizontal.css\"/>" + System.lineSeparator()
            + "    </head>" + System.lineSeparator() + "    <body>" + System.lineSeparator()
            + "        <nav epub:type=\"toc\" id=\"toc\"><ol>" + System.lineSeparator();

    private static final String NAV_TEXT_BOTTOM = "</ol></nav>" + System.lineSeparator() + "    </body>"
            + System.lineSeparator() + "</html>";

    // ----------------------------------------------------------
    // folder paths
    // ----------------------------------------------------------
    private static final String META_INF_FOLDER = "META-INF/";

    private static final String OEBPS_FOLDER = "OEBPS/";

    // ----------------------------------------------------------
    // extension types
    // ----------------------------------------------------------
    private static final class EXTENSION_TYPE {
        private static final String XML = ".xml";
        private static final String XHTML = ".xhtml";
        private static final String OPF = ".opf";
        private static final String NCX = ".ncx";
        private static final String CSS = ".css";
        private static final String XPGT = ".xpgt";
        private static final String EPUB = ".epub";
    }

    // ----------------------------------------------------------
    // file generation
    // ----------------------------------------------------------
    @Override
    public void generate(Volume volume) throws IOException {
        String fileName = volume.getTitle() + EXTENSION_TYPE.EPUB;
        File epubFile = new File(fileName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(epubFile)))) {

            // generate necessary files
            generateMimetype(zipOutputStream);
            generateContainer(zipOutputStream);
            generateCss(zipOutputStream);
            generateNav(zipOutputStream, volume);
            generatePageTemplate(zipOutputStream);

            // generate indexing files
            generateContent(zipOutputStream, volume);
            generateToc(zipOutputStream, volume);

            // generate chapters
            for (Chapter chapter : volume.getChapters()) {
                generateChapter(zipOutputStream, chapter);
            }
        }

    }

    /**
     * Write file to zip stream.
     * @param zipOutputStream
     * @param filePath
     * @param fileContent
     * @throws IOException
     */
    public void writeFileToZip(ZipOutputStream zipOutputStream, String filePath, String fileContent)
            throws IOException {
        ZipEntry entry = new ZipEntry(filePath);
        entry.setMethod(ZipEntry.STORED);
        byte[] entryBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        entry.setSize(entryBytes.length);
        entry.setCrc(calculateCrc(entryBytes));
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(entryBytes);
        zipOutputStream.closeEntry();
    }

    /**
     * Calculate CRC-32 value.
     * @param data
     * @return CRC-32 value
     */
    private long calculateCrc(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    /**
     * Generate mimetype file content string.
     * @return mimetype content
     */
    public String generateMimetypeContent() {
        return MIMETYPE_TEXT;
    }

    /**
     * Generate mimetype file path.
     * @return mimetype file path
     */
    public String generateMimetypePath() {
        return MIMETYPE_TITLE;
    }

    /**
     * Generate mimetype file within zip file.
     * @param zipOutputStream
     * @throws IOException
     */
    public void generateMimetype(ZipOutputStream zipOutputStream) throws IOException {
        String filePath = generateMimetypePath();
        String fileContent = generateMimetypeContent();
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate container file content string.
     * @return container content
     */
    public String generateContainerContent() {
        return CONTAINER_TEXT;
    }

    /**
     * Generate container file path.
     * @return container file path
     */
    public String generateContainerPath() {
        return META_INF_FOLDER + CONTAINER_TITLE + EXTENSION_TYPE.XML;
    }

    /**
     * Generate container file
     * @param zipOutputStream
     * @throws IOException
     */
    public void generateContainer(ZipOutputStream zipOutputStream) throws IOException {
        String filePath = generateContainerPath();
        String fileContent = generateContainerContent();
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate content file path.
     * @return content file path
     */
    public String generateContentPath() {
        return OEBPS_FOLDER + CONTENT_TITLE + EXTENSION_TYPE.OPF;
    }

    /**
     * Generate content file content.
     * @param volume
     * @return content file content
     */
    public String generateContentContent(Volume volume) {
        StringBuilder sb = new StringBuilder();

        // append metadata to stringbuilder
        sb.append(CONTENT_TEXT_METADATA_TOP);
        sb.append(String.format(CONTENT_TEXT_METADATA_TITLE, volume.getTitle()));
        sb.append(String.format(CONTENT_TEXT_METADATA_AUTHOR_NAME, volume.getAuthor()));
        sb.append(String.format(CONTENT_TEXT_METADATA_UID, " " + volume.getAuthor() + " " + volume.getTitle()));
        sb.append(CONTENT_TEXT_METADATA_BOTTOM);

        // append manifest to stringbuilder
        sb.append(CONTENT_TEXT_MANIFEST_TOP);
        for (Chapter chapter : volume.getChapters()) {
            sb.append(String.format(CONTENT_TEXT_MANIFEST_ITEM, "c" + chapter.getChapterNumber(),
                    "c" + chapter.getChapterNumber()));
        }
        sb.append(CONTENT_TEXT_MANIFEST_BOTTOM);

        // append spine to stringbuilder
        sb.append(CONTENT_TEXT_SPINE_TOP);
        for (Chapter chapter : volume.getChapters()) {
            sb.append(String.format(CONTENT_TEXT_SPINE_ITEMREF, "c" + chapter.getChapterNumber()));
        }
        sb.append(CONTENT_TEXT_SPINE_BOTTOM);

        return sb.toString();
    }

    /**
     * Generate content file.
     * @param zipOutputStream
     * @param volume
     * @throws IOException
     */
    public void generateContent(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
        String filePath = generateContentPath();
        String fileContent = generateContentContent(volume);
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate toc file path.
     * @return toc file path
     */
    public String generateTocPath() {
        return OEBPS_FOLDER + TOC_TITLE + EXTENSION_TYPE.NCX;
    }

    /**
     * Generate toc file content
     * @param volume
     * @return toc file content
     */
    public String generateTocContent(Volume volume) {
        StringBuilder sb = new StringBuilder();
        // append head to stringbuilder
        sb.append(TOC_TEXT_HEAD_TOP);
        sb.append(String.format(TOC_TEXT_HEAD_UID, " " + volume.getAuthor() + " " + volume.getTitle()));
        sb.append(TOC_TEXT_HEAD_BOTTOM);

        // append doctitle to stringbuilder
        sb.append(String.format(TOC_TEXT_DOCTITLE, volume.getVolumeNumber() + " - " + volume.getTitle()));

        // append spine to stringbuilder
        sb.append(TOC_TEXT_NAVMAP_TOP);
        for (int i = 0; i < volume.getChapters().size(); i++) {
            Chapter chapter = volume.getChapters().get(i);
            sb.append(String.format(TOC_TEXT_NAVMAP_NAVPOINT, "c" + chapter.getChapterNumber(), i + 1,
                    "c" + chapter.getChapterNumber(), "c" + chapter.getChapterNumber()));
        }
        sb.append(TOC_TEXT_NAVMAP_BOTTOM);

        return sb.toString();
    }

    /**
     * Generate toc file.
     * @param zipOutputStream
     * @param volume
     * @throws IOException
     */
    public void generateToc(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
        String filePath = generateTocPath();
        String fileContent = generateTocContent(volume);
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate a single chapter's path.
     * @param chapter
     * @return chapter file path
     */
    public String generateChapterPath(Chapter chapter) {
        return OEBPS_FOLDER + "c" + chapter.getChapterNumber() + EXTENSION_TYPE.XHTML;
    }

    /**
     * Generate a single chapter's file content.
     * @param chapter
     * @return chapter file content
     */
    public String generateChapterContent(Chapter chapter) {
        StringBuilder sb = new StringBuilder();
        // append head to stringbuilder
        sb.append(CHAPTER_TEXT_HEAD_TOP);
        sb.append(String.format(CHAPTER_TEXT_HEAD_TITLE, chapter.getTitle()));
        sb.append(CHAPTER_TEXT_HEAD_BOTTOM);

        // append body to stringbuilder
        sb.append(CHAPTER_TEXT_BODY_TOP);
        sb.append(chapter.getText() + System.lineSeparator());
        sb.append(CHAPTER_TEXT_BODY_BOTTOM);

        return sb.toString();

    }

    /**
     * Generate a single chapter file.
     * @param zipOutputStream
     * @param chapter
     * @throws IOException
     */
    public void generateChapter(ZipOutputStream zipOutputStream, Chapter chapter) throws IOException {
        String filePath = generateChapterPath(chapter);
        String fileContent = generateChapterContent(chapter);
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate css file path.
     * @return css file path
     */
    public String generateCssPath() {
        return OEBPS_FOLDER + CSS_TITLE + EXTENSION_TYPE.CSS;
    }

    /**
     * Generate css file content.
     * @return css file content
     */
    public String generateCssContent() {
        return CSS_TEXT;
    }

    /**
     * Generate css file.
     * @param zipOutputStream
     * @throws IOException
     */
    public void generateCss(ZipOutputStream zipOutputStream) throws IOException {
        String filePath = generateCssPath();
        String fileContent = generateCssContent();
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate page template file path.
     * @return page template file path
     */
    public String generatePageTemplatePath() {
        return OEBPS_FOLDER + PAGE_TEMPLATE_TITLE + EXTENSION_TYPE.XPGT;
    }

    /**
     * Generate page template file content.
     * @return page template file content
     */
    public String generatePageTemplateText() {
        return PAGE_TEMPLATE_TEXT;
    }

    /**
     * Generate page template file.
     * @param zipOutputStream
     * @throws IOException
     */
    public void generatePageTemplate(ZipOutputStream zipOutputStream) throws IOException {
        String filePath = generatePageTemplatePath();
        String fileContent = generatePageTemplateText();
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

    /**
     * Generate nav file path.
     * @return nav file path
     */
    public String generateNavPath() {
        return OEBPS_FOLDER + NAV_TITLE + EXTENSION_TYPE.XHTML;
    }

    /**
     * Generate nav file content.
     * @param volume
     * @return nav file content
     */
    public String generateNavText(Volume volume) {
        StringBuilder sb = new StringBuilder();
        sb.append(NAV_TEXT_TOP);

        String tab = "    ";
        List<Chapter> chapters = volume.getChapters();
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            sb.append(tab + tab + tab + "<li>");
            sb.append(System.lineSeparator());
            sb.append(tab + tab + tab + tab + "<a href=\"c" + chapter.getChapterNumber() + ".xhtml\">");
            sb.append(System.lineSeparator());
            sb.append(tab + tab + tab + tab + tab + chapter.getChapterNumber() + " - " + chapter.getTitle());
            sb.append(System.lineSeparator());
            sb.append(tab + tab + tab + tab + "</a>");
            sb.append(System.lineSeparator());
            sb.append(tab + tab + tab + "</li>");
            sb.append(System.lineSeparator());
        }

        sb.append(NAV_TEXT_BOTTOM);
        return sb.toString();
    }

    /**
     * Generate nav file.
     * @param zipOutputStream
     * @param volume
     * @throws IOException
     */
    public void generateNav(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
        String filePath = generateNavPath();
        String fileContent = generateNavText(volume);
        writeFileToZip(zipOutputStream, filePath, fileContent);
    }

}
