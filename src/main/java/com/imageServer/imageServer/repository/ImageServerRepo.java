package com.imageServer.imageServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.imageServer.imageServer.model.Images;

public interface ImageServerRepo extends JpaRepository<Images, Long> {

}
