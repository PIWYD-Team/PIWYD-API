package com.piwyd.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    FileEntity findOneById(Long id);

    List<FileEntity> findAllByIdOwner(Long id);
}
