package com.shineidle.tripf.notification.service;

import com.shineidle.tripf.global.security.auth.UserDetailsImpl;
import com.shineidle.tripf.domain.notification.dto.NotificationResponseDto;
import com.shineidle.tripf.domain.notification.entity.Notification;
import com.shineidle.tripf.domain.notification.repository.NotificationRepository;
import com.shineidle.tripf.domain.notification.service.NotificationServiceImpl;
import com.shineidle.tripf.domain.notification.type.NotifyType;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.service.UserService;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private User actor;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = new User("testUser@gmail.com",
                "Develop!1234",
                "testUser",
                UserAuth.ADMIN,
                "Seoul"
        );
        actor = new User(
                "actorUser@gmail.com",
                "Develop!1234",
                "actorUser",
                UserAuth.NORMAL,
                "Busan"
        );

        notification = Notification.create(user, actor, NotifyType.NEW_COMMENT, "새 댓글이 있습니다.", 100L);

        // SecurityContext 설정
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findNotificationTest() {
        // Given - 사용자에게 알림이 저장되어 있음
        when(notificationRepository.findByUserId(user.getId())).thenReturn(List.of(notification));

        // When - 사용자의 알림 목록을 조회
        List<NotificationResponseDto> notifications = notificationService.findNotification(user.getId());

        // Then - 알림이 정상적으로 조회되는지 검증
        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getNotifyContext()).isEqualTo("새 댓글이 있습니다.");
        verify(notificationRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void createNotificationTest() {
        // Given - 특정 사용자(user)가 다른 사용자(actor)로부터 알림을 받음

        // When - 알림 생성 요청
        notificationService.createNotification(user, actor, NotifyType.NEW_COMMENT, "새 댓글이 있습니다.", 100L);

        // Then - 알림이 정상적으로 저장되었는지 검증
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}
