package com.matchday.matchdayserver.common.Aspect;

import com.matchday.matchdayserver.common.auth.CustomUserDetails;
import com.matchday.matchdayserver.common.auth.JwtTokenFilter;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AnnotationStatus;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CheckTeamAspect {

    private final UserTeamRepository userTeamRepository;

    @Before("@annotation(com.matchday.matchdayserver.common.annotation.CheckTeam)")
    public void checkTeam(JoinPoint joinPoint) {
        // 1. userId 가져오기
        Optional<CustomUserDetails> customUserDetails = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .map(attr -> (CustomUserDetails) attr.getAttribute(JwtTokenFilter.USERDETAILS_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST));

        Long userId = customUserDetails.map(CustomUserDetails::getUserId)
            .orElseThrow(() -> new ApiException(AuthStatus.UNAUTHORIZED));

        // 2. teamId 추출
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Long teamId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if ("teamId".equals(paramNames[i]) && args[i] instanceof Long) {
                teamId = (Long) args[i];
                break;
            }
        }

        if (teamId == null) {
            throw new ApiException(AnnotationStatus.BAD_REQUEST_ANNOTATION, "teamId가 필요합니다.");
        }

        // 3. match_user 검증
        log.warn("match_user 검증 userId: {}, teamId: {}", userId, teamId);
        boolean exists = userTeamRepository.existsByUserIdAndTeamId(userId, teamId);
        if (!exists) {
            throw new ApiException(AuthStatus.FORBIDDEN, "해당 팀 소속 경기 참가 기록이 없습니다.");
        }

        log.warn("[CheckTeam AOP] userId: {}, teamId: {} => 통과", userId, teamId);
    }
}
