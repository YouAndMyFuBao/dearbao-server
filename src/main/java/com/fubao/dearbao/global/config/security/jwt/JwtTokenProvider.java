package com.fubao.dearbao.global.config.security.jwt;

import static io.jsonwebtoken.Jwts.builder;

import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.vo.AuthToken;
import com.fubao.dearbao.global.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private final SecretKey secretKey;

    private final long accessTokenValidityInMinute;
    private final long refreshTokenValidityInDay;

    private final UserDetailsService userDetailsService;

    private final JwtParser jwtParser;
    private final RedisUtil redisUtil;

    public JwtTokenProvider(
        @Value("${security.access-token-minute}") long accessTokenMinute,
        @Value("${security.refresh-token-day}") long refreshTokenDay,
        @Value("${security.code}") String secret,
        UserDetailsService userDetailsService, RedisUtil redisUtil) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenValidityInMinute = accessTokenMinute;
        this.refreshTokenValidityInDay = refreshTokenDay;
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public AuthToken createToken(String id) {
        LocalDateTime now = LocalDateTime.now();
        AuthToken authTokens = AuthToken.of(createAccessToken(now, id),
            createRefreshToken(now, id));
        redisUtil.setStringData(authTokens.getRefreshToken(), authTokens.getAccessToken(),
            Duration.ofDays(refreshTokenValidityInDay));
        return authTokens;
    }

    private String createAccessToken(LocalDateTime time, String username) {
        return builder()
            .setSubject(username)
            .claim(TOKEN_TYPE, ACCESS_TOKEN)
            .setIssuedAt(Timestamp.valueOf(time))
            .setExpiration(Timestamp.valueOf(time.plusMinutes(accessTokenValidityInMinute)))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    private String createRefreshToken(LocalDateTime time, String username) {
        return builder()
            .setSubject(username)
            .claim(TOKEN_TYPE, REFRESH_TOKEN)
            .setIssuedAt(Timestamp.valueOf(time))
            .setExpiration(Timestamp.valueOf(time.plusDays(refreshTokenValidityInDay)))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰 유효성 및 만료기간 검사
    //todo test code 작성
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: ", e);
            request.setAttribute("exception", ResponseCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: ", e);
            request.setAttribute("exception", ResponseCode.UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException | SecurityException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: ", e);
            request.setAttribute("exception", ResponseCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: ", e);
            request.setAttribute("exception", ResponseCode.INVALID_TOKEN);
        }
        return false;
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        String usernameFromToken = jwtParser.parseClaimsJws(accessToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}