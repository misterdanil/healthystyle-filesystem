package org.healthystyle.filesystem.service;

import java.io.InputStream;

import org.healthystyle.filesystem.model.File;
import org.healthystyle.filesystem.service.dto.FileSaveRequest;
import org.healthystyle.filesystem.service.error.FileExistException;
import org.healthystyle.filesystem.service.error.FileNotFoundException;
import org.healthystyle.util.error.ValidationException;

public interface FileService {
	File save(FileSaveRequest saveRequest) throws ValidationException, FileExistException;

	File findById(Long id) throws ValidationException, FileNotFoundException;

	InputStream getFileInputStreamById(Long id) throws ValidationException, FileNotFoundException;
}
