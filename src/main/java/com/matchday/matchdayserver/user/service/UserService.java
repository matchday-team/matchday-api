package com.matchday.matchdayserver.user.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.user.model.Entity.User;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(UserCreateRequest request){
        validateDuplicateUser(request.getName());
        User user = new User(request.getName());
        userRepository.save(user);
    }

    //유저 이름 중복 체크
    private void validateDuplicateUser(String name) {
        if (userRepository.existsByName(name)) {
            throw new ApiException(UserStatus.DUPLICATE_NAME);
        }
    }
}
