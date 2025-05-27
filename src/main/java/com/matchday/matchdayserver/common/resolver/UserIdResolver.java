package com.matchday.matchdayserver.common.resolver;

import com.matchday.matchdayserver.common.annotation.UserId;
import com.matchday.matchdayserver.common.auth.CustomUserDetails;
import com.matchday.matchdayserver.common.auth.JwtTokenFilter;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import java.util.Optional;

//HandlerMethodArgumentResolver를 상속받아 controller 의 Parameter를 가공함
@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {

    //파라미터에 UserId 어노테이션이 있고 Long 자료형 받을때
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(
            parameter.getParameterType());
    }

    //해당 메소드 리턴 값으로 치환
    @Override
    @Nullable
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Optional<CustomUserDetails> customUserDetails = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .map(attributes -> (CustomUserDetails) attributes.getAttribute(JwtTokenFilter.USERINFO_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST));

        Long userId = customUserDetails
            .map(CustomUserDetails::getUserId)
            .orElse(null);

        return userId;
    }
}
