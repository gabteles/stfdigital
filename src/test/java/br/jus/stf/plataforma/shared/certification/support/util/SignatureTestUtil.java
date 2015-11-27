package br.jus.stf.plataforma.shared.certification.support.util;

import java.io.InputStream;

public class SignatureTestUtil {

	public static InputStream getDocumentToSign(String fileName) {
		return getInputStreamFromClasspath(fileName);
	}

	private static InputStream getInputStreamFromClasspath(String fileName) {
		return SignatureTestUtil.class.getClassLoader().getResourceAsStream("assinador/" + fileName);
	}

}
