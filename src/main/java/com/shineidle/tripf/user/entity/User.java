package com.shineidle.tripf.user.entity;

import com.shineidle.tripf.common.entity.BaseEntity;
import com.shineidle.tripf.oauth2.user.OAuth2UserInfo;
import com.shineidle.tripf.user.type.UserAuth;
import com.shineidle.tripf.user.type.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "`user`")
@DynamicUpdate
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 320)
    private String email;

    private String password;

    @Column(length = 50)
    private String name;

    @Column(nullable = true)
    private String provider; // 소셜 로그인 제공자 (Google, Naver, Kakao)

    @Column(nullable = true, unique = true)
    private String providerId; // 소셜 제공자의 사용자 ID

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private UserAuth auth;

    private String address;

    private LocalDateTime deletedAt;


    public User(String email, String password, String name, UserAuth auth, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = null;
        this.providerId = null;
        this.status = UserStatus.ACTIVATE;
        this.auth = auth;
        this.address = address;
        this.deletedAt = null;
    }

    public User(String email, String name, String provider, String providerId) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.status = UserStatus.ACTIVATE;
        this.auth = UserAuth.NORMAL;
    }

    protected User() {
    }

    public User(Long userId) {
        this.id = userId;
    }


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */


    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */


    /**
     * 소셜 로그인 (기존 유저 업데이트)
     * @param oAuth2UserInfo {@link OAuth2UserInfo}
     * @return User
     */
    public User update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.provider = oAuth2UserInfo.getProvider().getRegistrationId();
        this.providerId = oAuth2UserInfo.getId();
        return this;
    }

    /**
     * 닉네임 수정
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 회원 탈퇴
     */
    public void deactivate() {
        this.status = UserStatus.DEACTIVATE;
        this.deletedAt = LocalDateTime.now();
    }
}
