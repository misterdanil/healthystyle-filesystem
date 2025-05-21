package org.healthystyle.filesystem.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class FileSaveRequest {
	@NotEmpty(message = "Укажите относительный путь")
	private String relativePath;
	@NotEmpty(message = "Укажите корневой путь")
	private String root;
	@NotNull(message = "Укажите файл")
	private byte[] bytes;

	public FileSaveRequest() {
		super();
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] file) {
		this.bytes = file;
	}

}
