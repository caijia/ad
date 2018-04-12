package com.caijia.ad.fetchdata.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_type", schema = "main")
public class QuestionTypeEntity implements Serializable {
    private long id;
    private long questionType;
    private long questionId;

    @Basic
    @Column(name = "question_id")
    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Id
    @Column(name = "_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "question_type")
    public long getQuestionType() {
        return questionType;
    }

    public void setQuestionType(long questionType) {
        this.questionType = questionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionTypeEntity that = (QuestionTypeEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(questionType, that.questionType) &&
                Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, questionType, questionId);
    }
}
