package com.matchday.matchdayserver.common.s3;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.common.s3.dto.S3PresignedResponse;
import com.matchday.matchdayserver.team.service.TeamService;
import com.matchday.matchdayserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {

  private final S3PresignedUrlProvider s3Service;
  private final UserService userService;
  private final TeamService teamService;

  //업로드 Presigned URL 요청
  public S3PresignedResponse generateUploadUrl(String folderName, Long id, String extension) {
    validateIdExists(folderName, id);
    String key = buildKey(folderName, id, extension);
    String uploadUrl = s3Service.generateUploadUrl(key);
    return new S3PresignedResponse(uploadUrl, key);
  }

  //Read용 Presigned URL 응답
  public S3PresignedResponse generateReadUrl(Long id, String key) {
    String readUrl = s3Service.generateReadUrl(key);
    return new S3PresignedResponse(readUrl);
  }

  private void validateIdExists(String folderName, Long id) {
    if (folderName.equals("users")) {
      if (!userService.existsById(id)) {
        throw new ApiException(UserStatus.NOTFOUND_USER);
      }
    } else if (folderName.equals("teams")) {
      if (!teamService.existsById(id)) {
        throw new ApiException(TeamStatus.NOTFOUND_TEAM);
      }
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
