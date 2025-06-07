package com.matchday.matchdayserver.common.Aspect;

import com.matchday.matchdayserver.common.auth.CustomUserDetails;
import com.matchday.matchdayserver.common.auth.JwtTokenFilter;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AnnotationStatus;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
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
public class CheckMatchAspect {

    private final UserTeamRepository userTeamRepository;
    private final MatchRepository matchRepository;

    @Before("@annotation(com.matchday.matchdayserver.common.annotation.CheckTeam)")
    public void checkTeam(JoinPoint joinPoint) {
        // 1. userId 가져오기
        Optional<CustomUserDetails> customUserDetails = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .map(attr -> (CustomUserDetails) attr.getAttribute(JwtTokenFilter.USERDETAILS_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST));

        Long userId = customUserDetails.map(CustomUserDetails::getUserId)
            .orElseThrow(() -> new ApiException(AuthStatus.UNAUTHORIZED));

        // 2. matchId 추출
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Long matchId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if ("matchId".equals(paramNames[i]) && args[i] instanceof Long) {
                matchId = (Long) args[i];
                break;
            }
        }

        if (matchId == null) {
            throw new ApiException(AnnotationStatus.BAD_REQUEST_ANNOTATION, "matchId가 필요합니다.");
        }

        // 3. team_user 검증
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ApiException(
            MatchStatus.NOTFOUND_MATCH));
        Long awayTeamId=match.getAwayTeam().getId();
        Long homeTeamId=match.getHomeTeam().getId();

        log.warn("[CheckTeam AOP] userId: {}, awayteamId: {} => 검증시작", userId, awayTeamId);
        boolean exists = userTeamRepository.existsByUserIdAndTeamId(userId, awayTeamId);
        if (!exists) {
            log.warn("[CheckTeam AOP] userId: {}, hometeamId: {} => 검증시작", userId, homeTeamId);
            exists = userTeamRepository.existsByUserIdAndTeamId(userId, homeTeamId);
        }
        if (!exists) {
            throw new ApiException(AuthStatus.FORBIDDEN, "해당 팀 소속 경기 참가 기록이 없습니다.");
        }
        log.warn("[CheckTeam AOP] userId: {}, teamId: {} => 통과", userId, matchId);
    }
}
