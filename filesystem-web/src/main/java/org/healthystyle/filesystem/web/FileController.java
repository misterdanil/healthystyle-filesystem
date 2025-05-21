package org.healthystyle.filesystem.web;

import java.io.FileInputStream;
import java.nio.file.Paths;

import org.healthystyle.filesystem.model.File;
import org.healthystyle.filesystem.service.FileService;
import org.healthystyle.filesystem.service.config.RabbitConfig;
import org.healthystyle.filesystem.service.dto.FileSaveRequest;
import org.healthystyle.filesystem.service.error.FileExistException;
import org.healthystyle.filesystem.service.error.FileNotFoundException;
import org.healthystyle.util.error.ErrorResponse;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController
@PropertySource("classpath:path.properties")
public class FileController {
	@Autowired
	private FileService service;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private Environment env;
	private String absolutePath;
	private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

	@PostConstruct
	public void init() {
		absolutePath = env.getProperty("app.absolute_path");
	}

	@RabbitListener(queues = RabbitConfig.FILE_UPLOAD_QUEUE)
	public void addFile(FileSaveRequest saveRequest, MessageProperties props) {
		LOG.debug("Got request to add file: {}", saveRequest);

		File file;
		Object o;
		try {
			file = service.save(saveRequest);
			o = file.getId();
		} catch (ValidationException | FileExistException e) {
			o = new ErrorResponse(e.getCode(), e.getGlobalErrors(), e.getFieldErrors());
		}
		rabbitTemplate.convertAndSend(RabbitConfig.FILESYSTEM_DIRECT_EXCHANGE, props.getReplyTo(), o, msg -> {
			msg.getMessageProperties().setCorrelationId(props.getCorrelationId());
			return msg;
		});

	}

	@GetMapping("/files/{id}")
	public Object getFileById(@PathVariable Long id) {
		LOG.debug("Got request to get file by id '{}'", id);

		File file;
		try {
			file = service.findById(id);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getCode(), e.getGlobalErrors(), e.getFieldErrors()));
		} catch (FileNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getCode(), e.getGlobalErrors(), e.getFieldErrors()));
		}

		try {
			return new InputStreamResource(
					new FileInputStream(Paths.get(absolutePath, file.getRoot(), file.getRelativePath()).toFile()));
		} catch (java.io.FileNotFoundException e) {
			throw new RuntimeException("File not found by path that was saved", e);
		}

	}
}
