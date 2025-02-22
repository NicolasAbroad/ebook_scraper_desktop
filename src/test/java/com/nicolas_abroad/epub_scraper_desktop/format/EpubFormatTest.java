package com.nicolas_abroad.epub_scraper_desktop.format;

import com.adobe.epubcheck.api.EpubCheck;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Chapter;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.utils.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test class for the EpubFormat class.
 *
 * @author Nicolas
 */
public class EpubFormatTest {

	private interface FunctionalException {
		@FunctionalInterface
		interface ConsumerWithExceptions<T, E extends Exception> {
			void accept(T t) throws E;
		}

		@FunctionalInterface
		interface BiConsumerWithExceptions<T, U, E extends Exception> {
			void accept(T t, U u) throws E;
		}
	}

	/** Tested class */
	private final EpubFormat epubFormat = new EpubFormat();

	/** Volume used for happy path testing */
	private static Volume volume;

	/** Volume used for unhappy path testing. Title contains "&". */
	private static Volume edgeCaseVolume;

	/** Temporary folder used to ensure no files are left after tests */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/** Setup volume used for tests */
	@BeforeClass
	public static void setUp() {
		Chapter c1 = generateChapter(1);
		Chapter c2 = generateChapter(2);
		List<Chapter> v1Chapters = Arrays.asList(c1, c2);
		volume = new Volume();
		volume.setTitle("喜びの道");
		volume.setAuthor("作者");
		volume.setVolumeNumber("01");
		volume.setChapters(v1Chapters);
		volume.setChapterUrls(v1Chapters.stream().map(Chapter::getUrl).toList());

		Chapter c151 = generateEdgeCaseChapter(151);
		Chapter c152 = generateEdgeCaseChapter(152);
		Chapter c153 = generateEdgeCaseChapter(153);
		Chapter c154 = generateEdgeCaseChapter(154);
		Chapter c155 = generateEdgeCaseChapter(155);
		List<Chapter> v2Chapters = Arrays.asList(c151, c152, c153, c154, c155);
		edgeCaseVolume = new Volume();
		edgeCaseVolume.setTitle("悲しみの道");
		edgeCaseVolume.setVolumeNumber("13");
		edgeCaseVolume.setAuthor("大虎龍真");
		edgeCaseVolume.setChapters(v2Chapters);
		edgeCaseVolume.setChapterUrls(v2Chapters.stream().map(Chapter::getUrl).toList());
	}

	private static Chapter generateChapter(int i) {
		Chapter c = new Chapter();
		c.setUrl("/" + i + "/");
		c.setChapterNumber(i);
		c.setTitle("タイトル " + i);
		c.setText("<p>テキスト " + i + "</p>\r\n<p>あいうえお</p>");
		return c;
	}

	private static Chapter generateEdgeCaseChapter(int i) {
		Chapter c = new Chapter();
		c.setUrl("/" + i + "/");
		c.setChapterNumber(i);
		c.setTitle("&gt;");
		c.setText("<p>テキスト " + i + "</p>\r\n<p>あいうえお</p>");
		return c;
	}

//	@AfterClass
//	public static void cleanUp() throws IOException {
//		Path volumePath = generateVolumePath(volume);
//		Path edgeCaseVolumePath = generateVolumePath(edgeCaseVolume);
//		Files.deleteIfExists(volumePath);
//		Files.deleteIfExists(edgeCaseVolumePath);
//	}

	private static Path generateVolumePath(Volume volume) {
		return Paths.get(System.getProperty("user.dir") + "\\" + volume.getTitle() + ".epub");
	}

	/** Read zip file */
	private String readZipFile(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (ZipInputStream zipInStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))) {
			while (true) {
				ZipEntry entry = zipInStream.getNextEntry();
				if (entry == null)
					break;
				if (entry.isDirectory()) {
					sb.append(entry.getName()).append(System.lineSeparator());
				} else {
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					while (true) {
						int input = zipInStream.read();
						if (input < 0)
							break;
						byteArrayOutputStream.write(input);
					}
					byteArrayOutputStream.flush();
					byteArrayOutputStream.close();
					sb.append(byteArrayOutputStream);
					sb.append(System.lineSeparator());
				}
				zipInStream.closeEntry();
			}
		}
		return sb.toString();
	}

	/** Test writing file to zip */
	@Test
	public void testWriteFileToZip() throws IOException {
		File epubFile = folder.newFile(volume.getTitle() + ".epub");
		String filePath = "testTitle";
		String fileContent = "testContent";
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(
				new BufferedOutputStream(new FileOutputStream(epubFile)))) {
			epubFormat.writeFileToZip(zipOutputStream, filePath, fileContent);
		}

		String expected = fileContent + System.lineSeparator();
		String actual = readZipFile(epubFile);
		assertEquals(expected, actual);
	}

	private File executeTarget(FunctionalException.ConsumerWithExceptions<ZipOutputStream, IOException> generator) throws IOException {
		File epubFile = folder.newFile(volume.getTitle() + ".epub");
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(
				new BufferedOutputStream(new FileOutputStream(epubFile)))) {
			generator.accept(zipOutputStream);
		}
		return epubFile;
	}

	private File executeTarget(FunctionalException.BiConsumerWithExceptions<ZipOutputStream, Volume, IOException> generator, Volume volume) throws IOException {
		File epubFile = folder.newFile(volume.getTitle() + ".epub");
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(
				new BufferedOutputStream(new FileOutputStream(epubFile)))) {
			generator.accept(zipOutputStream, volume);
		}
		return epubFile;
	}

	private File executeTarget(FunctionalException.BiConsumerWithExceptions<ZipOutputStream, Chapter, IOException> generator, Chapter chapter) throws IOException {
		File epubFile = folder.newFile(volume.getTitle() + ".epub");
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(
				new BufferedOutputStream(new FileOutputStream(epubFile)))) {
			generator.accept(zipOutputStream, chapter);
		}
		return epubFile;
	}

	/** Test mimetype file generation */
	@Test
	public void testGenerateMimetype() throws IOException {
		File epubFile = executeTarget(epubFormat::generateMimetype);
		String actual = readZipFile(epubFile);
		File file = IOUtils.getResource("epub_templates/mimetype", "test");
		String expected = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	/** Test container file generation */
	@Test
	public void testGenerateContainer() throws IOException {
		File epubFile = executeTarget(epubFormat::generateContainer);
		String actual = readZipFile(epubFile);
		File file = IOUtils.getResource("epub_templates/META-INF/container.xml", "test");
		String expected = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	/** Test content file generation */
	@Test
	public void testGenerateContent() throws IOException {
		File epubFile = executeTarget(epubFormat::generateContent, volume);
		String actual = readZipFile(epubFile);
		File file = IOUtils.getResource("epub_templates/OEBPS/content.opf", "test");
		String expected = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	/** Test toc file generation */
	@Test
	public void testToc() throws IOException {
		File epubFile = executeTarget(epubFormat::generateToc, volume);
		String actual = readZipFile(epubFile);
		File file = IOUtils.getResource("epub_templates/OEBPS/toc.ncx", "test");
		String expected = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	/** Test chapter file generation */
	@Test
	public void testGenerateChapter() throws IOException {
		File epubFile = executeTarget(epubFormat::generateChapter, volume.getChapters().getFirst());
		String actual = readZipFile(epubFile);
		File file = IOUtils.getResource("epub_templates/OEBPS/chapter.xhtml", "test");
		String expected = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	/** Test css file generation */
	@Test
	public void testGenerateCss() throws IOException {
		File epubFile = executeTarget(epubFormat::generateCss);
		String actual = readZipFile(epubFile);
		File file = IOUtils.getResource("epub_templates/OEBPS/horizontal.css", "test");
		String expected = IOUtils.getFileContent(file);
		assertEquals(expected, actual);
	}

	/** Test if epub file conforms to official epub specifications */
	@Test
	public void testGenerateEpub01() throws IOException {
		epubFormat.generate(volume);
		Path epubPath = generateVolumePath(volume);

		boolean isGenerated = Files.exists(epubPath);
		assertTrue(isGenerated);

		File epubFile = moveToTmpFolder(epubPath);
		EpubCheck epubcheck = new EpubCheck(epubFile);
		assertEquals(0, epubcheck.doValidate());
		epubFile.deleteOnExit();
	}

	/** Test if edge case epub file conforms to official epub specifications */
	@Test
	public void testGenerateEpub02() throws IOException {
		epubFormat.generate(edgeCaseVolume);

		Path epubPath = generateVolumePath(edgeCaseVolume);
		File epubFile = moveToTmpFolder(epubPath);

		EpubCheck epubcheck = new EpubCheck(epubFile);
		assertEquals(0, epubcheck.doValidate());
		epubFile.deleteOnExit();
	}

	/** Move file to tmp folder */
	private File moveToTmpFolder(Path originalPath) throws IOException {
		String strPath = folder.getRoot().getPath() + "\\" + originalPath.getFileName().toString();
		Path newPath = Paths.get(strPath);
		Files.move(originalPath, newPath);
		return newPath.toFile();
	}

}
