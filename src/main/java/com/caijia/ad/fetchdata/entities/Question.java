package com.caijia.ad.fetchdata.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Question implements Serializable {
    private String questionId;
    private String questionText;
    private String questionUrl;
    private Short id;
    private Short questionSubject;

    private Set<QuestionAnswer> answers = new HashSet<>();
    private Set<QuestionAnalysis> analyses = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "question")
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

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "question")
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

    @Basic
    @Column(name = "question_id")
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
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
    @Column(name = "question_subject")
    public Short getQuestionSubject() {
        return questionSubject;
    }

    public void setQuestionSubject(Short questionSubject) {
        this.questionSubject = questionSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(questionId, question.questionId) &&
                Objects.equals(questionText, question.questionText) &&
                Objects.equals(questionUrl, question.questionUrl) &&
                Objects.equals(id, question.id) &&
                Objects.equals(questionSubject, question.questionSubject);
    }

    @Override
    public int hashCode() {

        return Objects.hash(questionId, questionText, questionUrl, id, questionSubject);
    }
}
