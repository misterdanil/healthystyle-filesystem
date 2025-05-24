import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.healthystyle.filesystem.app.Main;
import org.healthystyle.filesystem.model.File;
import org.healthystyle.filesystem.repository.FileRepository;
import org.healthystyle.filesystem.service.FileService;
import org.healthystyle.filesystem.service.dto.FileSaveRequest;
import org.healthystyle.filesystem.service.error.FileExistException;
import org.healthystyle.filesystem.service.error.FileNotFoundException;
import org.healthystyle.util.error.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = Main.class)
public class FileTest {
	@Autowired
	private FileService fileService;
	@MockitoBean
	private FileRepository fileRepository;

	private File file;
	private FileSaveRequest fileSaveRequest;

	@BeforeEach
	public void setup() {
		file = new File("article", "/text/image.png", "image/png");
		fileSaveRequest = new FileSaveRequest();
		fileSaveRequest.setRoot("article");
		fileSaveRequest.setRelativePath(file.getRelativePath());
		fileSaveRequest.setBytes("test".getBytes());
	}

	@Test
	public void createFileTest() throws ValidationException, FileExistException {
		when(fileRepository.save(file)).thenReturn(file);
		when(fileRepository.existsByRootAndRelativePath(file.getRoot(), file.getRelativePath())).thenReturn(false);

		fileService.save(fileSaveRequest);
		verify(fileRepository, times(1)).save(any(File.class));
	}

	@Test
	public void createFileExistTest() throws ValidationException, FileExistException {
		when(fileRepository.save(file)).thenReturn(file);
		when(fileRepository.existsByRootAndRelativePath(file.getRoot(), file.getRelativePath())).thenReturn(true);

		assertThrows(FileExistException.class, () -> {
			fileService.save(fileSaveRequest);
		});
		verify(fileRepository, times(0)).save(any(File.class));
	}

	@Test
	public void createFileValidationTest() throws ValidationException, FileExistException {
		fileSaveRequest.setRelativePath(null);

		when(fileRepository.save(file)).thenReturn(file);

		assertThrows(ValidationException.class, () -> {
			fileService.save(fileSaveRequest);
		});
		verify(fileRepository, times(0)).save(any(File.class));
	}

	@Test
	public void fetchFileInputStreamTest() throws ValidationException, FileNotFoundException {
		when(fileRepository.findById(1L)).thenReturn(Optional.of(file));

		fileService.getFileInputStreamById(1L);
	}

	@Test
	public void fetchFileInputStreamNotFoundTest() throws ValidationException, FileNotFoundException {
		when(fileRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(FileNotFoundException.class, () -> {
			fileService.getFileInputStreamById(2L);
		});
	}
}
