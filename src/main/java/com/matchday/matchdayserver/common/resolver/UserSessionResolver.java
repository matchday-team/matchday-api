package com.matchday.matchdayserver.common.resolver;

import com.matchday.matchdayserver.common.annotation.UserSession;
import com.matchday.matchdayserver.common.auth.CustomUserDetails;
import com.matchday.matchdayserver.common.auth.JwtTokenFilter;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    //파라미터에 UserSession 어노테이션이 있고 User 타입 받을때
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserSession.class) && parameter.getParameterType()
            .equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Optional<CustomUserDetails> customUserDetails = Optional.ofNullable(
                RequestContextHolder.getRequestAttributes())
            .map(attributes -> (CustomUserDetails) attributes.getAttribute(JwtTokenFilter.USERINFO_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST));

        Long userId = customUserDetails
            .map(CustomUserDetails::getUserId)
            .orElse(null);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));

        return user;
    }
}
