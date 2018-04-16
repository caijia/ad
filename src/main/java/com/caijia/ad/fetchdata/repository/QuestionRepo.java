package com.caijia.ad.fetchdata.repository;

import com.caijia.ad.fetchdata.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Integer> {


}
