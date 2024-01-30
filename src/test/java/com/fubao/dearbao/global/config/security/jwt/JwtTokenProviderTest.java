package com.fubao.dearbao.global.config.security.jwt;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.global.common.vo.AuthToken;
import com.fubao.dearbao.global.util.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class JwtTokenProviderTest extends IntegrationTestSupport {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;

    @DisplayName("엑세스 토큰과 리프레시 토큰을 생성한다.")
    @Test
    void createToken() {
        //given
        String id = "id";
        Duration expectedDuration = Duration.ofDays(14); // refreshTokenValidityInDay는 해당 값에 맞춰 설정

        //when
        AuthToken authToken = jwtTokenProvider.createToken(id);

        //then
        assertThat(authToken).isNotNull();
        assertThat(authToken.getAccessToken()).isNotNull();
        assertThat(authToken.getRefreshToken()).isNotNull();
        then(redisUtil).should(times(1))
            .setStringData(
                eq(authToken.getRefreshToken()),
                eq(authToken.getAccessToken()),
                eq(expectedDuration)
            );
    }
}