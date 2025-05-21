package org.healthystyle.filesystem.service.error;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FileNotFoundException extends AbstractException {
	private Long id;

	public FileNotFoundException(BindingResult result, Long id) {
		super(result, "File not found by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
