package com.caijia.ad.fetchdata.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_analysis", schema = "main", catalog = "")
public class QuestionAnalysis implements Serializable {
    private long analysisId;
    private String analysisText;
    private String analysisUserId;
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

    @Id
    @Column(name = "_id")
    @GeneratedValue
    public long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(long analysisId) {
        this.analysisId = analysisId;
    }

    @Basic
    @Column(name = "analysis_text")
    public String getAnalysisText() {
        return analysisText;
    }

    public void setAnalysisText(String analysisText) {
        this.analysisText = analysisText;
    }

    @Basic
    @Column(name = "analysis_user_id")
    public String getAnalysisUserId() {
        return analysisUserId;
    }

    public void setAnalysisUserId(String analysisUserId) {
        this.analysisUserId = analysisUserId;
    }
}
