package com.matchday.matchdayserver.testcontroller;

import com.matchday.matchdayserver.common.annotation.UserId;
import com.matchday.matchdayserver.common.annotation.UserSession;
import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.user.model.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test-aop")
public class AOPTestController {

    @GetMapping
    public ApiResponse<?> hello(@UserId Long requestUserId){
        System.out.println("UserId 어노테이션 작동 테스트 메소드 진행");
        return ApiResponse.ok(requestUserId);
    }

    @GetMapping("/userSession")
    public ApiResponse<?> hello2(@UserSession User user){
        System.out.println("userSession 어노테이션 작동 테스트 메소드 진행");
        System.out.println("user.getName():"+user.getName());
        System.out.println("user.getEmail():"+user.getEmail());
        return ApiResponse.ok(user.getId());
    }
}
