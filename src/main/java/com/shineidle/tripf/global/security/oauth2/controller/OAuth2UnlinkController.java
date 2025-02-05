package com.shineidle.tripf.global.security.oauth2.controller;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.UserErrorCode;
import com.shineidle.tripf.global.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.global.security.oauth2.user.OAuth2Provider;
import com.shineidle.tripf.global.security.oauth2.user.OAuth2UserUnlinkManager;
import com.shineidle.tripf.global.security.oauth2.util.CookieUtils;
import com.shineidle.tripf.domain.user.entity.RefreshToken;
import com.shineidle.tripf.domain.user.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth2/unlink")
@RequiredArgsConstructor
public class OAuth2UnlinkController {
    private final OAuth2UserUnlinkManager unlinkManager;
    private final RefreshTokenService refreshTokenService;

    /**
     * 소셜 연동해제 (탈퇴)
     *
     * @param provider            {@link OAuth2Provider}
     * @param authorizationHeader Authorization 헤더
     * @param request             {@link HttpServletRequest}
     * @param response            {@link HttpServletResponse}
     */
    @Operation(summary = "소셜 연동해제 (탈퇴)")
    @PostMapping("/{provider}")
    public ResponseEntity<Void> unlink(
            @PathVariable("provider") OAuth2Provider provider,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        unlinkManager.unlink(provider, accessToken);

        RefreshToken refreshToken = refreshTokenService.findByUserId(UserAuthorizationUtil.getLoginUserId()).orElseThrow(() ->
                new GlobalException(UserErrorCode.TOKEN_NOT_FOUND));
        refreshTokenService.deleteTokenAndUser(refreshToken);

        CookieUtils.deleteCookie(request, response, "refresh_token");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
