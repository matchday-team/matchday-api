package com.matchday.matchdayserver.s3.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.s3.S3PresignedUrlProvider;
import com.matchday.matchdayserver.s3.dto.S3PresignedResponse;
import com.matchday.matchdayserver.s3.enums.FileExtension;
import com.matchday.matchdayserver.s3.enums.FolderType;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.team.service.TeamService;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import com.matchday.matchdayserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {

  private final S3PresignedUrlProvider s3PresignedUrlProvider;
  private final UserService userService;
  private final TeamService teamService;
  private final UserRepository userRepository;
  private final TeamRepository teamRepository;

    // 업로드 Presigned URL 요청
    public S3PresignedResponse generateUploadUrl(String folderName, Long id, String extension) {
        FolderType folderType = FolderType.from(folderName);

        // 파일 확장자 유효성 검사
        FileExtension fileExtension = FileExtension.from(extension);

        // 파일 이름 생성
        String key = buildKey(folderName, id, extension);

        String uploadUrl = s3PresignedUrlProvider.generateUploadUrl(key, fileExtension.getContentType());

        switch (folderType) {
            case USERS -> {
                User user = userRepository.findById(id)
                    .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));
                user.updateProfileImg(key);
                userRepository.save(user);
            }
            case TEAMS -> {
                Team team = teamRepository.findById(id)
                    .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));
                team.updateTeamImg(key);
                teamRepository.save(team);
            }
        }
        return new S3PresignedResponse(uploadUrl, key);
    }

    //Read용 Presigned URL 응답
  public S3PresignedResponse generateReadUrl(String folderName, Long id, String key) {
    // user or team id 유효성 검사
    validateIdExists(folderName, id);

    // S3 저장소 내 파일 존재 여부 확인
    s3PresignedUrlProvider.validateFileExists(key);

    String readUrl = s3PresignedUrlProvider.generateReadUrl(key);
    return new S3PresignedResponse(readUrl);
  }

  //id 유효성 검사
  private void validateIdExists(String folderName, Long id) {
      FolderType folderType = FolderType.from(folderName);
      boolean exists = switch (folderType) {
          case USERS -> userRepository.existsById(id);
          case TEAMS -> teamRepository.existsById(id);
      };
      if (!exists) {
          throw switch (folderType) {
              case USERS -> new ApiException(UserStatus.NOTFOUND_USER);
              case TEAMS -> new ApiException(TeamStatus.NOTFOUND_TEAM);
          };
      }
  }


    //UUID 생성
  private String createUniqueFileName() {
    return UUID.randomUUID().toString();
  }

  //고유한 fileName 생성
  private String buildKey(String folderName, Long id, String extension) {
    String uniqueFileName = createUniqueFileName();
    return String.format("%s/%d/%s.%s", folderName, id, uniqueFileName, extension);
  }

}
