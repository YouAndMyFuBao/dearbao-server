package com.fubao.dearbao.global.util;

import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SlackWebhookUtil {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final Slack slackClient;

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    public void slackNotificationThread(Exception e, HttpServletRequest request) {
        HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(
            request);
        threadPoolTaskExecutor.execute(
            () -> sendSlackAlertErrorLog(e.getMessage(), httpServletRequestWrapper));
    }

    public void slackNotificationServerError(ResponseCode responseCode) {
        slackNotification(responseCode);
    }

    private void slackNotification(ResponseCode responseCode) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                .text("서버 에러 발생! 백엔드 측의 빠른 확인 요망")
                .attachments(
                    List.of(generateSlackAttachmentMessage(responseCode))
                )
            ));
        } catch (IOException slackError) {
            log.error("Slack 통신과의 예외 발생");
        }
    }

    private Attachment generateSlackAttachmentMessage(ResponseCode responseCode) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .format(LocalDateTime.now());
        return Attachment.builder()
            .color("ff0000")  // 붉은 색으로 보이도록
            .title(requestTime + " 발생 에러 로그")
            .fields(List.of(
                    generateSlackField("Error Code", responseCode.getCode()),
                    generateSlackField("Error Status", String.valueOf(responseCode.getStatus())),
                    generateSlackField("Error Message", responseCode.getMessage())
                )
            )
            .build();
    }

    private void sendSlackAlertErrorLog(String message, HttpServletRequest request) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                .text("서버 에러 발생! 백엔드 측의 빠른 확인 요망")
                .attachments(
                    List.of(generateSlackAttachment(message, request))
                )
            ));
        } catch (IOException slackError) {
            log.error("Slack 통신과의 예외 발생");
        }
    }

    private Attachment generateSlackAttachment(String message, HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .format(LocalDateTime.now());
        String xffHeader = request.getHeader("X-FORWARDED-FOR");  // 프록시 서버일 경우 client IP는 여기에 담긴다.
        return Attachment.builder()
            .color("ff0000")  // 붉은 색으로 보이도록
            .title(requestTime + " 발생 에러 로그")
            .fields(List.of(
                    generateSlackField("Request IP",
                        xffHeader == null ? request.getRemoteAddr() : xffHeader),
                    generateSlackField("Request URL",
                        request.getRequestURL() + " " + request.getMethod()),
                    generateSlackField("Error Message", message)
                )
            )
            .build();
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
            .title(title)
            .value(value)
            .valueShortEnough(false)
            .build();
    }
}