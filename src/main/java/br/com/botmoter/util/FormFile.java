package br.com.botmoter.util;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class FormFile {
	private final String fileName;
	private final byte[] bytes;

	public FormFile(String fileName, byte[] bytes) {
		this.fileName = fileName;
		this.bytes = bytes;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getBytes() {
		return bytes;
	}
}
