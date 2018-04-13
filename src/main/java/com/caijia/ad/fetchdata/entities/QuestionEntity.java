package com.caijia.ad.fetchdata.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "question", schema = "main")
public class QuestionEntity implements Serializable {
    private long id;
    private String questionText;
    private String questionImg;
    private long questionSubject;
    private Set<AnalysisEntity> analysisList = new HashSet<>();
    private Set<AnswerEntity> answerEntities = new HashSet<>();

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL
    )
    public Set<AnalysisEntity> getAnalysisList() {
        return analysisList;
    }

    public void setAnalysisList(Set<AnalysisEntity> analysisList) {
        this.analysisList = analysisList;
    }

    public void addAnalysis(AnalysisEntity analysisEntity) {
        analysisEntity.setQuestion(this);
        analysisList.add(analysisEntity);
    }

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL
    )
    public Set<AnswerEntity> getAnswerEntities() {
        return answerEntities;
    }

    public void setAnswerEntities(Set<AnswerEntity> answerEntities) {
        this.answerEntities = answerEntities;
    }

    public void addAnswer(AnswerEntity answerEntity) {
        answerEntity.setQuestion(this);
        answerEntities.add(answerEntity);
    }

    @Id
    @Column(name = "_id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "question_subject")
    public long getQuestionSubject() {
        return questionSubject;
    }

    public void setQuestionSubject(long questionSubject) {
        this.questionSubject = questionSubject;
    }

    @Basic
    @Column(name = "question_text")
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Basic
    @Column(name = "question_img")
    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionEntity that = (QuestionEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(questionText, that.questionText) &&
                Objects.equals(questionImg, that.questionImg)&&
                Objects.equals(questionSubject, that.questionSubject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionText, questionImg,questionSubject);
    }
}
