package org.healthystyle.filesystem.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.filesystem.model.File;
import org.healthystyle.filesystem.repository.FileRepository;
import org.healthystyle.filesystem.service.FileService;
import org.healthystyle.filesystem.service.dto.FileSaveRequest;
import org.healthystyle.filesystem.service.error.CreateDirectoryException;
import org.healthystyle.filesystem.service.error.FileExistException;
import org.healthystyle.filesystem.service.error.FileNotFoundException;
import org.healthystyle.filesystem.service.error.FileSaveException;
import org.healthystyle.filesystem.service.util.MimeTypeRecognizer;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import jakarta.annotation.PostConstruct;

@Service
@PropertySource("classpath:path.properties")
public class FileServiceImpl implements FileService {
	@Autowired
	private FileRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private Environment env;
	private String absolutePath;
	@Autowired
	private MimeTypeRecognizer recognizer;

	private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

	@PostConstruct
	public void init() {
		absolutePath = env.getProperty("app.absolute_path");
	}

	@Override
	public File save(FileSaveRequest saveRequest) throws ValidationException, FileExistException {
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "file");

		LOG.debug("Validating file: {}", saveRequest);
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("Exception occurred while saving file: %s", result, saveRequest);
		}

		String root = saveRequest.getRoot();
		String relativePath = saveRequest.getRelativePath();
		LOG.debug("Checking file for existing by root '{}' and relative path '{}'", root, relativePath);
		if (repository.existsByRootAndRelativePath(root, relativePath)) {
			result.reject("file.save.exists", "Файл с таким именем уже существует");
			throw new FileExistException(result, root, relativePath);
		}

		LOG.debug("The file is valid: {}", saveRequest);

		byte[] bytes = saveRequest.getBytes();

		String mimeType = recognizer.recognize(bytes);

		File file = new File(root, relativePath, mimeType);
		LOG.debug("Saving file: {}", file);
		file = repository.save(file);
		LOG.info("File was saved successfully: {}", file);

		Path path = Paths.get(absolutePath, root, relativePath);

		Path directory = path.getParent();
		LOG.debug("Creating directories: {}", directory);
		try {

			Files.createDirectories(directory);
		} catch (IOException e) {
			throw new CreateDirectoryException(
					"Could not create directory '" + directory + "' to save file '" + path.getFileName() + "'", e,
					directory);
		}
		LOG.debug("Directories was created successfully: {}", directory);

		LOG.debug("Saving file: {}", path);
		try {
			Files.write(path, bytes);
		} catch (IOException e) {
			throw new FileSaveException(e, path);
		}
		LOG.info("File was written successfully: {}", path);

		return file;
	}

	@Override
	public File findById(Long id) throws ValidationException, FileNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "file");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("file.find.id.not_null", "Укажите идентификатор для поиска");
		}
		if (result.hasErrors()) {
			throw new ValidationException("Exception occurred while fetching file by id. Id is null", result);
		}

		Optional<File> file = repository.findById(id);
		if (file.isEmpty()) {
			throw new FileNotFoundException(result, id);
		} else {
			LOG.info("Got file successfully by id '{}'", id);
			return file.get();
		}
	}

	@Override
	public InputStream getFileInputStreamById(Long id) throws ValidationException, FileNotFoundException {
		File file = findById(id);

		LOG.debug("Getting input stream by file: {}", file);
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(Paths.get(absolutePath, file.getRoot(), file.getRelativePath()).toFile());
		} catch (java.io.FileNotFoundException e) {
			// has already checked
			throw new RuntimeException("File not found, but was checked that has already existed", e);
		}
		LOG.info("Got input stream successfuly by file id: {}", id);

		return inputStream;
	}

}
