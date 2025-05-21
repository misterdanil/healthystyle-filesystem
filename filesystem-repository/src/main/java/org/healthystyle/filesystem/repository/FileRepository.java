package org.healthystyle.filesystem.repository;

import org.healthystyle.filesystem.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
	@Query("SELECT f FROM File f WHERE f.root = :root AND f.relativePath = :relativePath")
	File findByRootAndRelativePath(String root, String relativePath);
	
	@Query("SELECT EXISTS(SELECT f FROM File f WHERE f.root = :root AND f.relativePath = :relativePath)")
	boolean existsByRootAndRelativePath(String root, String relativePath);
}
