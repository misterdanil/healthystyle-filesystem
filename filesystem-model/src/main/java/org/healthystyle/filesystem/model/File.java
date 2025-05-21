package org.healthystyle.filesystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "file_root_idx", columnList = "root"))
public class File {
	@Id
	@SequenceGenerator(name = "file_id_generator", sequenceName = "file_sequence")
	@GeneratedValue(generator = "file_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private String root;
	@Column(name = "relative_path", nullable = false, unique = true)
	private String relativePath;
	@Column(nullable = false)
	private String mimeType;

	public File() {
		super();
	}

	public File(String root, String relativePath, String mimeType) {
		super();
		this.root = root;
		this.relativePath = relativePath;
		this.mimeType = mimeType;
	}

	public Long getId() {
		return id;
	}

	public String getRoot() {
		return root;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public String getMimeType() {
		return mimeType;
	}
}
