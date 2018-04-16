package com.caijia.ad.fetchdata.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_answer", schema = "main", catalog = "")
public class QuestionAnswer implements Serializable {
    private long answerId;
    private String answerText;
    private int answerOrder;
    private Question question;
    private int ok;

    @Basic
    @Column(name = "answer_ok")
    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id",referencedColumnName = "_id")
    @JsonBackReference
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Id
    @Column(name = "_id")
    @GeneratedValue
    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    @Basic
    @Column(name = "answer_text")
    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Basic
    @Column(name = "answer_order")
    public int getAnswerOrder() {
        return answerOrder;
    }

    public void setAnswerOrder(int answerOrder) {
        this.answerOrder = answerOrder;
    }
}
