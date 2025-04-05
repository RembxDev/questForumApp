package org.it.questforumapp.repository;


import org.it.questforumapp.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByTitleContainingIgnoreCase(String search, Pageable pageable);
    Page<Post> findAllByOrderByDateCreated(Pageable pageable);
    List<Post> findTop10ByOrderByDateCreatedDesc();
    List<Post> findAllByAuthorId(Integer authorId);
    List<Post> findFirstByAuthorId(Integer authorId);

}
