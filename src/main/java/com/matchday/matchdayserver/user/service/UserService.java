package com.matchday.matchdayserver.user.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import com.matchday.matchdayserver.user.model.Entity.User;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.repository.UserRepository;
import lombok.NoArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(UserCreateRequest request){

        if (userRepository.existsByName(request.getName())) {
            throw new ApiException(DefaultStatus.BAD_REQUEST, "이미 존재하는 유저 이름");
        }

        User user = new User(request.getName());
        userRepository.save(user);
    }
}
