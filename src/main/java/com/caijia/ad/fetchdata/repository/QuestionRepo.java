package com.caijia.ad.fetchdata.repository;

import com.caijia.ad.fetchdata.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface QuestionRepo extends JpaRepository<Question, Integer> {

    @Query(value = "update question set question_chapter = ?1 where question_id = ?2 and question_subject = ?3",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateQuestionChapter(int chapter, String questionId, int subject);
}
