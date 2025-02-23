package com.nicolas_abroad.epub_scraper_desktop.format;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Chapter;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.utils.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Epub format.
 *
 * @author Nicolas
 */
public class EpubFormat implements EbookFormat {

	private final TemplateEngine textTemplateEngine = initTemplateEngine();

	private static final class ExtensionType {
		private static final String EPUB = ".epub";
	}

	private static final String TEMPLATE_FOLDER = "epub_templates/";

	// ----------------------------------------------------------
	// Utils
	// ----------------------------------------------------------

	private TemplateEngine initTemplateEngine() {
		TemplateEngine engine = new TemplateEngine();

		StringTemplateResolver resolver = new StringTemplateResolver();
		resolver.setTemplateMode(TemplateMode.TEXT);
		engine.addTemplateResolver(resolver);

		return engine;
	}

	/** Write file to zip stream */
	public void writeFileToZip(ZipOutputStream zipOutputStream, String filePath, String fileContent) throws IOException {
		ZipEntry entry = new ZipEntry(filePath);
		entry.setMethod(ZipEntry.STORED);
		byte[] entryBytes = fileContent.getBytes(StandardCharsets.UTF_8);
		entry.setSize(entryBytes.length);
		entry.setCrc(IOUtils.calculateCrc(entryBytes));
		zipOutputStream.putNextEntry(entry);
		zipOutputStream.write(entryBytes);
		zipOutputStream.closeEntry();
	}

	private Context generateContext(Volume volume) {
		Context context = new Context();
		context.setVariable("volume", volume);
		return context;
	}

	private Context generateContext(Chapter chapter) {
		Context context = new Context();
		context.setVariable("chapter", chapter);
		return context;
	}

	private void generateFile(ZipOutputStream zipOutputStream, String filePath) throws IOException {
		String fileContent = IOUtils.getFileContent(TEMPLATE_FOLDER + filePath);
		writeFileToZip(zipOutputStream, filePath, fileContent);
	}

	private void generateFile(ZipOutputStream zipOutputStream, String filePath, Context context) throws IOException {
		String templateFileContent = IOUtils.getFileContent(TEMPLATE_FOLDER + filePath);
		String fileContent = textTemplateEngine.process(templateFileContent, context);
		writeFileToZip(zipOutputStream, filePath, fileContent);
	}

	// ----------------------------------------------------------
	// file generation
	// ----------------------------------------------------------

	@Override
	public void generate(Volume volume) throws IOException {
		String fileName = volume.getTitle() + ExtensionType.EPUB;
		File epubFile = new File(fileName);
		try (FileOutputStream fileOutputStream = new FileOutputStream(epubFile);
			 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			 ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream)) {

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

	/** Generate mimetype file within zip file */
	public void generateMimetype(ZipOutputStream zipOutputStream) throws IOException {
		generateFile(zipOutputStream, "mimetype");
	}

	/** Generate container file */
	public void generateContainer(ZipOutputStream zipOutputStream) throws IOException {
		generateFile(zipOutputStream, "META-INF/container.xml");
	}

	/** Generate content file */
	public void generateContent(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
		Context context = generateContext(volume);
		final String filePath = "OEBPS/content.opf";
		generateFile(zipOutputStream, filePath, context);
	}

	/** Generate toc file */
	public void generateToc(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
		Context context = generateContext(volume);
		final String filePath = "OEBPS/toc.ncx";
		generateFile(zipOutputStream, filePath, context);
	}

	/** Generate a single chapter file */
	public void generateChapter(ZipOutputStream zipOutputStream, Chapter chapter) throws IOException {
		Context context = generateContext(chapter);
		final String filePath = "OEBPS/chapter.xhtml";
		String templateFileContent = IOUtils.getFileContent(TEMPLATE_FOLDER + filePath);
		String chapterFileName = "OEBPS/c" + chapter.getChapterNumber() + ".xhtml";
		String fileContent = textTemplateEngine.process(templateFileContent, context);
		writeFileToZip(zipOutputStream, chapterFileName, fileContent);
	}

	/** Generate css file */
	public void generateCss(ZipOutputStream zipOutputStream) throws IOException {
		generateFile(zipOutputStream, "OEBPS/horizontal.css");
	}

	/** Generate page template file */
	public void generatePageTemplate(ZipOutputStream zipOutputStream) throws IOException {
		generateFile(zipOutputStream, "OEBPS/page-template.xpgt");
	}

	/** Generate nav file */
	public void generateNav(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
		Context context = generateContext(volume);
		final String filePath = "OEBPS/nav.xhtml";
		generateFile(zipOutputStream, filePath, context);
	}

}
