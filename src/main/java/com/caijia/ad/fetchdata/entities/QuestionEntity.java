package com.caijia.ad.fetchdata.entities;

import javax.persistence.*;
import java.awt.print.Book;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "question", schema = "main", catalog = "")
public class QuestionEntity implements Serializable {
    private Short id;
    private String questionText;
    private String questionImg;
    private Short questionSubject;
    private Short questionParentType;
    private Short questionChildType;
    private Short questionChapterType;
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
    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
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

    @Basic
    @Column(name = "question_subject")
    public Short getQuestionSubject() {
        return questionSubject;
    }

    public void setQuestionSubject(Short questionSubject) {
        this.questionSubject = questionSubject;
    }

    @Basic
    @Column(name = "question_parent_type")
    public Short getQuestionParentType() {
        return questionParentType;
    }

    public void setQuestionParentType(Short questionParentType) {
        this.questionParentType = questionParentType;
    }

    @Basic
    @Column(name = "question_child_type")
    public Short getQuestionChildType() {
        return questionChildType;
    }

    public void setQuestionChildType(Short questionChildType) {
        this.questionChildType = questionChildType;
    }

    @Basic
    @Column(name = "question_chapter_type")
    public Short getQuestionChapterType() {
        return questionChapterType;
    }

    public void setQuestionChapterType(Short questionChapterType) {
        this.questionChapterType = questionChapterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionEntity that = (QuestionEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(questionText, that.questionText) &&
                Objects.equals(questionImg, that.questionImg) &&
                Objects.equals(questionSubject, that.questionSubject) &&
                Objects.equals(questionParentType, that.questionParentType) &&
                Objects.equals(questionChildType, that.questionChildType) &&
                Objects.equals(questionChapterType, that.questionChapterType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, questionText, questionImg, questionSubject, questionParentType, questionChildType, questionChapterType);
    }
}
