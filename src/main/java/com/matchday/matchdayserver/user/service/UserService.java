package com.matchday.matchdayserver.user.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.team.service.TeamService;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.model.dto.response.UserInfoResponse;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.mapper.UserMapper;
import com.matchday.matchdayserver.user.repository.UserRepository;
import com.matchday.matchdayserver.userteam.model.dto.JoinUserTeamResponse;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import com.matchday.matchdayserver.userteam.model.mapper.UserTeamMapper;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final MatchUserRepository matchUserRepository;

    public Long createUser(UserCreateRequest request){
        User user = User.builder()
            .name(request.getName())
            .profileImg(request.getProfileImg())
            .build();
        userRepository.save(user);
        return user.getId();
    }

    public JoinUserTeamResponse joinTeam(Long userId, UserJoinTeamRequest request){
        //유저객체 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));
        //팀객체 가져오기
        Team team = teamRepository.findById(request.getTeamId()).
                orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));
        //이미 팀에 가입되어 있는지 검증
        if(teamService.validateUserInTeam(userId,request.getTeamId())){
           throw new ApiException(TeamStatus.ALREADY_JOINED_USER);
        }
        //userTeam 객체 생성.
        UserTeam userTeam = UserTeam.builder()
                .user(user)
                .team(team)
                .number(request.getNumber())
                .defaultPosition(request.getDefaultPosition())
                .isActive(true)
                .build();
        //객체 저장
        userTeamRepository.save(userTeam);
        return UserTeamMapper.toJoinUserTeamResponse(userTeam);
    }

    public boolean existsById(Long userId) {
      return userRepository.existsById(userId);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId){
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));

      List<Long> teamIds = userTeamRepository.findActiveTeamIdsByUserId(userId);
      List<Long> matchIds = matchUserRepository.findMatchIdsByUserId(userId);
      return UserMapper.userInfoResponse(user, teamIds, matchIds);
    }
}


