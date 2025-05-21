package org.healthystyle.filesystem.service.error;

import java.nio.file.Path;

public class FileSaveException extends RuntimeException {
	private Path path;

	public FileSaveException(Throwable cause, Path path) {
		super(cause);
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	@Override
	public String getMessage() {
		return String.format("Could not save file by path '%s'", path);
	}

}
