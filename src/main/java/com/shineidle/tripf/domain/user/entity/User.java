package com.shineidle.tripf.domain.user.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.global.security.oauth2.user.OAuth2UserInfo;
import com.shineidle.tripf.domain.user.type.UserAuth;
import com.shineidle.tripf.domain.user.type.UserStatus;
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

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
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

    public User() {
    }

    public User(Long userId) {
        this.id = userId;
    }

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
