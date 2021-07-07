package com.jpt21.socialmedia.repository;

import com.jpt21.socialmedia.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status,Long> {
    List<Status> findByIsPublic(Boolean isPublic);
    Status save(Status status);
}
