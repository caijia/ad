package com.caijia.ad.fetchdata.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_type", schema = "main", catalog = "")
public class QuestionType implements Serializable {
    private long typeId;
    private int questionType;
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id",referencedColumnName = "_id")
    @JsonBackReference
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Basic
    @Column(name = "question_type")
    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    @Id
    @Column(name = "_id")
    @GeneratedValue
    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

}
