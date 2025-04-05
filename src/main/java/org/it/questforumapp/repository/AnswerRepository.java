package org.it.questforumapp.repository;


import org.it.questforumapp.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findAllByAuthorId(int userId);

}
