package com.caijia.ad.fetchdata.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Question implements Serializable {
    private long questionId;
    private String questionText;
    private String questionUrl;
    private int questionSubject;

    private Set<QuestionAnswer> answers = new HashSet<>();
    private Set<QuestionAnalysis> analyses = new HashSet<>();
    private Set<QuestionType> types = new HashSet<>();

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL
    )
    public Set<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<QuestionAnswer> answers) {
        this.answers = answers;
    }

    public void addAnswer(QuestionAnswer answer) {
        answer.setQuestion(this);
        answers.add(answer);
    }

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL
    )
    public Set<QuestionAnalysis> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(Set<QuestionAnalysis> analyses) {
        this.analyses = analyses;
    }

    public void addAnalysis(QuestionAnalysis analysis) {
        analysis.setQuestion(this);
        analyses.add(analysis);
    }

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL
    )
    public Set<QuestionType> getTypes() {
        return types;
    }

    public void setTypes(Set<QuestionType> types) {
        this.types = types;
    }

    public void addType(QuestionType type) {
        type.setQuestion(this);
        types.add(type);
    }

    @Id
    @Column(name = "_id")
    @GeneratedValue
    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
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
    @Column(name = "question_url")
    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

    @Basic
    @Column(name = "question_subject")
    public int getQuestionSubject() {
        return questionSubject;
    }

    public void setQuestionSubject(int questionSubject) {
        this.questionSubject = questionSubject;
    }
}
