package br.jus.stf.plataforma.shared.certification.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;

import br.jus.stf.plataforma.shared.certification.signature.SignatureContext;
import br.jus.stf.plataforma.shared.certification.signature.SignatureContextId;
import br.jus.stf.plataforma.shared.certification.signature.SignedDocument;

public class SHA256DetachedAssinadorPorPartesTest extends AbstractAssinaturaDemoTest {

	private AssinadorPorPartes app;
	private X509Certificate[] cadeia;
	private byte[] pdf;
	private String reason;
	private SignatureContext ca;
	private PrivateKey key;
	private CRL[] crls;

	@Before
	public void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		cadeia = recuperarCadeia();
		pdf = getPdf();
		reason = "RAZAO";
		ca = new SignatureContext(new SignatureContextId("12345"));
		key = AssinaturaTestUtil.getPrivateKeyPessoa001();
		crls = AssinaturaTestUtil.getCrls();

	}

	private byte[] getPdf() throws IOException {
		InputStream is = AssinaturaTestUtil.getInputStreamFromClasspath(PDF_DE_TESTE_001);
		byte[] pdf = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		return pdf;
	}

	private HashSignature assinar(byte[] dataToSign) throws GeneralSecurityException {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(key);
		signature.update(dataToSign);

		byte[] signed = signature.sign();

		return new HashSignature(signed);
	}

	@Test
	public void testGerarPdfAssinado() throws Exception {
		app = new SHA256DetachedAssinadorPorPartes(false);
		ca.registerCertificateChain(cadeia);
		ca.registerCrls(crls);
		byte[] dataToSign = app.preAssinar(ca);
		HashSignature assinatura = assinar(dataToSign);
		app.posAssinar(ca, assinatura);
		
		FileInputStream is = null;
		try {
			is = new FileInputStream(new File(ca.signedFilePath()));
			byte[] pdfAssinado = IOUtils.toByteArray(is);
			validarPdf(pdfAssinado);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao recuperar documento assinado.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		
	}

	private void validarPdf(byte[] pdfAssinado) throws IOException, NoSuchAlgorithmException {
		PdfReader reader = new PdfReader(pdfAssinado);

		// Recupera os campos das assinaturas do pdf.
		AcroFields acroFields = reader.getAcroFields();

		for (String nomeCampo : acroFields.getSignatureNames()) {
			PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(nomeCampo, "BC");
			try {
				Assert.assertTrue("Assinatura deveria ser válida.", pdfPKCS7.verify());
			} catch (SignatureException e) {
				Assert.fail("Erro ao verificar a assinatura.");
			} catch (GeneralSecurityException e) {
				Assert.fail("Erro ao verificar a assinatura.");
			}
		}

	}

}
