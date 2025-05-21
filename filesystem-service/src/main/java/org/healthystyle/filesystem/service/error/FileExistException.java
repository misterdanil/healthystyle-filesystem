package org.healthystyle.filesystem.service.error;

import java.util.Arrays;

import org.healthystyle.util.error.AbstractException;
import org.healthystyle.util.error.Field;
import org.springframework.validation.BindingResult;

public class FileExistException extends AbstractException {
	private String root;
	private String relativePath;

	public FileExistException(BindingResult result, String root, String relativePath) {
		super(result, "File has already existed by root '%s' and relative path '%s'", root, relativePath);
		this.root = root;
		this.relativePath = relativePath;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getCode() {
		return "EXIST-1001";
	}

}
