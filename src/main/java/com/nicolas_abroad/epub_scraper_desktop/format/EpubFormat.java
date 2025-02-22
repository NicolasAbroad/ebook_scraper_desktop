package com.nicolas_abroad.epub_scraper_desktop.format;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Chapter;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.utils.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Epub format.
 *
 * @author Nicolas
 */
public class EpubFormat implements EbookFormat {

	private final TemplateEngine xmlTemplateEngine = initTemplateEngine(TemplateMode.XML);
	private final TemplateEngine textTemplateEngine = initTemplateEngine(TemplateMode.TEXT);

	private static final class ExtensionType {
		private static final String EPUB = ".epub";
	}

	// ----------------------------------------------------------
	// file generation
	// ----------------------------------------------------------
	@Override
	public void generate(Volume volume) throws IOException {
		String fileName = volume.getTitle() + ExtensionType.EPUB;
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

	/** Write file to zip stream */
	public void writeFileToZip(ZipOutputStream zipOutputStream, File file) throws IOException {
		writeFileToZip(zipOutputStream, file, "");
	}

	/** Write file to zip stream */
	public void writeFileToZip(ZipOutputStream zipOutputStream, File file, String path) throws IOException {
		String directory = StringUtils.isEmpty(path) ? "" : path + "/";
		ZipEntry entry = new ZipEntry(directory + file.getName());
		entry.setMethod(ZipEntry.STORED);
		byte[] entryBytes = Files.readAllBytes(file.toPath());
		entry.setSize(entryBytes.length);
		entry.setCrc(IOUtils.calculateCrc(entryBytes));
		zipOutputStream.putNextEntry(entry);
		zipOutputStream.write(entryBytes);
		zipOutputStream.closeEntry();
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

	private TemplateEngine initTemplateEngine(TemplateMode templateMode) {
		TemplateEngine engine = new TemplateEngine();

		FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setTemplateMode(templateMode);
		resolver.setCharacterEncoding("UTF-8");
		engine.addTemplateResolver(resolver);

		return engine;
	}

	/** Generate mimetype file within zip file */
	public void generateMimetype(ZipOutputStream zipOutputStream) throws IOException {
		File file = IOUtils.getResource("epub_templates/mimetype");
		writeFileToZip(zipOutputStream, file);
	}

	/** Generate container file */
	public void generateContainer(ZipOutputStream zipOutputStream) throws IOException {
		File file = IOUtils.getResource("epub_templates/META-INF/container.xml");
		writeFileToZip(zipOutputStream, file, "META-INF");
	}

	/** Generate content file */
	public void generateContent(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
		final String filePath = "OEBPS/content.opf";
		File file = IOUtils.getResource("epub_templates/" + filePath);
		Context context = new Context();
		context.setVariable("volume", volume);

		String fileContent = xmlTemplateEngine.process(file.getPath(), context);
		writeFileToZip(zipOutputStream, filePath, fileContent);
	}

	/** Generate toc file */
	public void generateToc(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
		final String filePath = "OEBPS/toc.ncx";
		File file = IOUtils.getResource("epub_templates/" + filePath);
		Context context = new Context();
		context.setVariable("volume", volume);

		String fileContent = textTemplateEngine.process(file.getPath(), context);
		writeFileToZip(zipOutputStream, filePath, fileContent);
	}

	/** Generate a single chapter file */
	public void generateChapter(ZipOutputStream zipOutputStream, Chapter chapter) throws IOException {
		File file = IOUtils.getResource("epub_templates/OEBPS/chapter.xhtml");
		Context context = new Context();
		context.setVariable("chapter", chapter);

		String chapterFileName = "OEBPS/c" + chapter.getChapterNumber() + ".xhtml";
		String fileContent = textTemplateEngine.process(file.getPath(), context);
		writeFileToZip(zipOutputStream, chapterFileName, fileContent);
	}

	/** Generate css file */
	public void generateCss(ZipOutputStream zipOutputStream) throws IOException {
		File file = IOUtils.getResource("epub_templates/OEBPS/horizontal.css");
		writeFileToZip(zipOutputStream, file, "OEBPS");
	}

	/** Generate page template file */
	public void generatePageTemplate(ZipOutputStream zipOutputStream) throws IOException {
		File file = IOUtils.getResource("epub_templates/OEBPS/page-template.xpgt");
		writeFileToZip(zipOutputStream, file, "OEBPS");
	}

	/** Generate nav file */
	public void generateNav(ZipOutputStream zipOutputStream, Volume volume) throws IOException {
		final String filePath = "OEBPS/nav.xhtml";
		File file = IOUtils.getResource("epub_templates/" + filePath);
		Context context = new Context();
		context.setVariable("volume", volume);

		String fileContent = xmlTemplateEngine.process(file.getPath(), context);
		writeFileToZip(zipOutputStream, filePath, fileContent);
	}

}
