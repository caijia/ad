package com.caijia.ad.fetchdata.repository;

import com.caijia.ad.fetchdata.entities.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepo extends JpaRepository<QuestionEntity, Integer> {

    @Query(value = "insert into question(_id,question_text,question_img,question_subject)" +
            " values (?1,?2,?3,?4)", nativeQuery = true)
    @Modifying
    void insertQuestion(int _id, String question_text, String question_img, int question_subject);
}
