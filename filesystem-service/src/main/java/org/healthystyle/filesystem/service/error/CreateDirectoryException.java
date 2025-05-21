package org.healthystyle.filesystem.service.error;

import java.nio.file.Path;

public class CreateDirectoryException extends RuntimeException {
	private Path path;

	public CreateDirectoryException(String message, Throwable cause, Path path) {
		super(message, cause);
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

}
