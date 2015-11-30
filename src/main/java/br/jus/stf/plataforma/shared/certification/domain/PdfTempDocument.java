package br.jus.stf.plataforma.shared.certification.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;

import br.jus.stf.plataforma.shared.certification.domain.model.SigningDocument;

public class PdfTempDocument implements SigningDocument {

	private static final String FILE_NAME_PREFFIX = "_DocToSignTemp_";
	private static final String FILE_NAME_EXTENSION = ".pdf";

	private File file;

	public PdfTempDocument(InputStream stream) {
		Validate.notNull(stream);

		file = createTempFile(stream);
	}

	private File createTempFile(InputStream stream) {
		File tempFile = null;
		try (InputStream in = stream) {
			tempFile = File.createTempFile(FILE_NAME_PREFFIX, FILE_NAME_EXTENSION);
			FileUtils.copyInputStreamToFile(in, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return tempFile;
	}

	public String tempId() {
		return file.getName();
	}

	public String tempPath() {
		return file.getAbsolutePath();
	}

	@Override
	public InputStream stream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Erro ao abrir pdf temporário para assinatura.", e);
		}
	}

}
