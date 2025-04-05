package org.it.questforumapp.service;

import lombok.RequiredArgsConstructor;

import org.it.questforumapp.model.User;
import org.it.questforumapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik o ID " + id + " nie istnieje"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o nazwie: " + username));
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void changeUserRole(Integer userId, String newRole) {
        User user = findById(userId);
        user.setRole(newRole);
        save(user);
    }

    @Transactional
    public void blockUser(Integer userId) {
        setUserVisibility(userId, false);
    }

    @Transactional
    public void unblockUser(Integer userId) {
        setUserVisibility(userId, true);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    protected void setUserVisibility(Integer userId, boolean visible) {
        User user = findById(userId);
        user.setVisible(visible);
        save(user);
    }
}
