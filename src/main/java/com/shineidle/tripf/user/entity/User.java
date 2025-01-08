package com.shineidle.tripf.user.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.user.UserAuth;
import com.shineidle.tripf.user.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 320)
    private String email;

    private String password;

    @Column(length = 50)
    private String name;

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
        this.status = UserStatus.ACTIVATE;
        this.auth = auth;
        this.address = address;
        this.deletedAt = null;
    }

    protected User() {
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
    // 닉네임 수정
    // 비밀번호 수정
    // 회원탈퇴
}
