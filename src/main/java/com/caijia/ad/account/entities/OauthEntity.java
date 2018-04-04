package com.caijia.ad.account.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "oauth", schema = "ad", catalog = "")
public class OauthEntity {
    private int id;
    private Integer userId;
    private String oauthName;
    private String oauthId;
    private String oauthAccessToken;
    private String oauthExpires;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "oauth_name")
    public String getOauthName() {
        return oauthName;
    }

    public void setOauthName(String oauthName) {
        this.oauthName = oauthName;
    }

    @Basic
    @Column(name = "oauth_id")
    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    @Basic
    @Column(name = "oauth_access_token")
    public String getOauthAccessToken() {
        return oauthAccessToken;
    }

    public void setOauthAccessToken(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
    }

    @Basic
    @Column(name = "oauth_expires")
    public String getOauthExpires() {
        return oauthExpires;
    }

    public void setOauthExpires(String oauthExpires) {
        this.oauthExpires = oauthExpires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OauthEntity that = (OauthEntity) o;
        return id == that.id &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(oauthName, that.oauthName) &&
                Objects.equals(oauthId, that.oauthId) &&
                Objects.equals(oauthAccessToken, that.oauthAccessToken) &&
                Objects.equals(oauthExpires, that.oauthExpires);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, oauthName, oauthId, oauthAccessToken, oauthExpires);
    }
}
