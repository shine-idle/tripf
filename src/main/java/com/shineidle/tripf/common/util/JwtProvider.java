package com.shineidle.tripf.common.util;

import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import com.shineidle.tripf.user.type.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    /**
     * JWT Secret Key
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 엑세스토큰 만료시간(milli seconds)
     */
    @Getter
    @Value("${jwt.expiry.access}")
    private Long accessExpiryMillis;

    /**
     * 리프레시토큰 만료시간(milli seconds)
     */
    @Getter
    @Value("${jwt.expiry.refresh}")
    private Long refreshExpiryMillis;

    private final UserRepository userRepository;

    /**
     * 토큰 생성 후 리턴 </br>
     * 입력받은 {@link Authentication}에서 추출한 {@code username}으로 토큰 생성
     *
     * @param authentication 인증 완료된 후 세부 정보
     * @return 토큰
     * @throws EntityNotFoundException 입력받은 이메일에 해당하는 사용자를 찾지 못했을 경우
     */
    public String generateToken(Authentication authentication, boolean isSocialLogin, TokenType tokenType) throws EntityNotFoundException {
        String username = authentication.getName();
        String token;

        if (isSocialLogin) {
            log.info(authentication.getAuthorities().toString());
            token = generateTokenForSocialLogin(username, authentication.getAuthorities(), tokenType);
        } else {
            token = generateTokenForNormalLogin(username, tokenType);
        }

        return token;
    }

    /**
     * 입력받은 토큰에서 {@link Authentication}의 {@code username}을 리턴
     *
     * @param token 토큰
     * @return username
     */
    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    /**
     * 토큰이 유효한지 체크
     *
     * @param token 토큰
     * @return 유효 여부 </br> true 유효, false 유효하지 않음
     */
    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT Token : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token : {}", e.getMessage());
        }

        return false;
    }

    /**
     * (일반 로그인) 이메일을 이용해 토큰을 생성 (HS256 알고리즘 이용)
     *
     * @param email 이메일
     * @return 생성된 토큰
     * @throws EntityNotFoundException 입력받은 이메일에 해당하는 유저를 찾지 못한 경우 예외 발생
     */
    private String generateTokenForNormalLogin(String email, TokenType tokenType) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("유저를 찾을 수 없습니다."));

        return createToken(email, user.getAuth().toString(), tokenType);
    }

    /**
     * (소셜 로그인) 이메일을 이용해 토큰을 생성 (HS256 알고리즘 이용)
     *
     * @param email 이메일
     * @return 생성된 토큰
     */
    private String generateTokenForSocialLogin(String email, Collection<? extends GrantedAuthority> authorities, TokenType tokenType) {
        String auth = authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("AUTH_guest");

        log.info("JwtProvider auth: {}", auth);
        return createToken(email, auth, tokenType);
    }

    private String createToken(String email, String auth, TokenType tokenType) {
        Date currentDate = new Date();
        Date expireDate = new Date(tokenType.equals(TokenType.ACCESS) ?
                currentDate.getTime() + this.accessExpiryMillis : currentDate.getTime() + this.refreshExpiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("auth", auth)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * JWT claim 부분 가져오기
     *
     * @param token 토큰
     * @return {@link Claims}
     * @see <a href="https://ko.wikipedia.org/wiki/JSON_%EC%9B%B9_%ED%86%A0%ED%81%B0">JSON 웹 토큰</a>
     */
    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("Token is empty");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰의 만료 여부 확인
     *
     * @param token 토큰
     * @return 만료 true, 만료되지 않음 false
     */
    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 토큰의 만료일을 리턴
     *
     * @param token 토큰
     * @return 만료일
     */
    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    /**
     * 토큰에 입력 받은 로직을 적용하고 그 결과를 리턴
     *
     * @param token          토큰
     * @param claimsResolver 토큰에 적용할 로직
     * @param <T>            {@code claimsResolver}의 리턴 타입
     * @return {@code T}
     */
    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }

    public String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.replace("Bearer ", "");
        } else {
            return null;
        }
    }

    /**
     * JWT 토큰을 기반으로 사용자 인증 정보를 생성
     *
     * @param token JWT 토큰
     * @return Authentication 객체 (사용자 이름과 권한 포함)
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        if (claims.getSubject() == null) {
            throw new MalformedJwtException("Invalid token: no subject found");
        }

        String username = claims.getSubject();
        String auth = claims.get("auth", String.class);

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(auth));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
