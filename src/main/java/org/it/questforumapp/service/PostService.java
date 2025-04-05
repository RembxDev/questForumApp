package org.it.questforumapp.service;

import lombok.RequiredArgsConstructor;
import org.it.questforumapp.model.Post;
import org.it.questforumapp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Value("${app.posts.perPage}")
    private int postsPerPage;

    @Transactional
    public void addPost(Post post) {
        postRepository.save(post);
    }

    public Optional<Post> getPostById(Integer postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getAllPostsByDateCreated(int page) {
        return fetchPostsByDateCreated(page).getContent();
    }

    public List<Post> getAllPostsBySearchString(String search, int page) {
        return fetchPostsBySearchString(search, page).getContent();
    }

    public int getTotalPagesBySearchString(String search) {
        return fetchPostsBySearchString(search, 0).getTotalPages();
    }

    public int getTotalPagesByDateCreated() {
        return fetchPostsByDateCreated(0).getTotalPages();
    }

    public List<Post> getAllPostsByAuthorId(Integer authorId) {
        return postRepository.findAllByAuthorId(authorId);
    }

    public Long getTotalPostCount() {
        return postRepository.count();
    }

    public List<Post> getRecentPosts() {
        return postRepository.findTop10ByOrderByDateCreatedDesc();
    }

    private Page<Post> fetchPostsByDateCreated(int page) {
        return postRepository.findAllByOrderByDateCreated(toPageable(page));
    }

    private Page<Post> fetchPostsBySearchString(String search, int page) {
        return postRepository.findByTitleContainingIgnoreCase(search, toPageable(page));
    }

    private Pageable toPageable(int page) {
        return PageRequest.of(page, postsPerPage);
    }
}
