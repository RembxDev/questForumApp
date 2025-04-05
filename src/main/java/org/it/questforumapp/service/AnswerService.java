package org.it.questforumapp.service;


import lombok.RequiredArgsConstructor;
import org.it.questforumapp.model.Answer;
import org.it.questforumapp.model.Post;
import org.it.questforumapp.model.User;
import org.it.questforumapp.repository.AnswerRepository;
import org.it.questforumapp.repository.PostRepository;
import org.it.questforumapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addAnswer(Integer postId, String content, Principal principal) {
        Post post = findPostById(postId);
        User user = findUserByUsername(principal.getName());

        Answer answer = buildAnswer(post, user, content);
        answerRepository.save(answer);

        updatePostAnswerCount(post);
    }

    public long getTotalAnswerCount() {
        return answerRepository.count();
    }

    public List<Answer> getAllAnswersByUserId(Integer userId) {
        return answerRepository.findAllByAuthorId(userId);
    }

    private Post findPostById(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Nie odnaleziono postu!"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Nie odnaleziono użytkownika!"));
    }

    private Answer buildAnswer(Post post, User user, String content) {
        return Answer.builder()
                .author(user)
                .content(content)
                .dateAdded(LocalDateTime.now())
                .post(post)
                .build();
    }

    private void updatePostAnswerCount(Post post) {
        post.setAnswerCount(post.getAnswerCount() + 1);
        postRepository.save(post);
    }
}
