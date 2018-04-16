package com.caijia.ad.fetchdata.repository;

import com.caijia.ad.fetchdata.entities.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepo extends JpaRepository<QuestionAnswer,Integer> {


}
