package br.jus.stf.plataforma.shared.certification.interfaces.commands;

public class PrepareCommand {

	private String certificateAsHex;

	public String getCertificateAsHex() {
		return certificateAsHex;
	}

	public void setCertificateAsHex(String certificateAsHex) {
		this.certificateAsHex = certificateAsHex;
	}

}
