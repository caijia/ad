package com.caijia.ad.fetchdata.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_answer", schema = "main", catalog = "")
public class QuestionAnswer implements Serializable {
    private String answerId;
    private String answerText;
    private Short id;
    private Short answerOrder;
    private Short answerOk;

    private Question question;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id",referencedColumnName = "question_id")
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Basic
    @Column(name = "answer_id")
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
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

    @Id
    @Column(name = "_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    @Basic
    @Column(name = "answer_order")
    public Short getAnswerOrder() {
        return answerOrder;
    }

    public void setAnswerOrder(Short answerOrder) {
        this.answerOrder = answerOrder;
    }

    @Basic
    @Column(name = "answer_ok")
    public Short getAnswerOk() {
        return answerOk;
    }

    public void setAnswerOk(Short answerOk) {
        this.answerOk = answerOk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAnswer that = (QuestionAnswer) o;
        return Objects.equals(answerId, that.answerId) &&
                Objects.equals(answerText, that.answerText) &&
                Objects.equals(id, that.id) &&
                Objects.equals(answerOrder, that.answerOrder) &&
                Objects.equals(answerOk, that.answerOk);
    }

    @Override
    public int hashCode() {

        return Objects.hash(answerId, answerText, id, answerOrder, answerOk);
    }
}
