package com.matchday.matchdayserver.common.s3.Service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.FileStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.common.s3.S3PresignedUrlProvider;
import com.matchday.matchdayserver.common.s3.dto.S3PresignedResponse;
import com.matchday.matchdayserver.common.s3.enums.FileExtension;
import com.matchday.matchdayserver.team.service.TeamService;
import com.matchday.matchdayserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {

  private final S3PresignedUrlProvider s3PresignedUrlProvider;
  private final UserService userService;
  private final TeamService teamService;

  //업로드 Presigned URL 요청
  public S3PresignedResponse generateUploadUrl(String folderName, Long id, String extension) {
    validateIdExists(folderName, id); // id 유효성 검사
    FileExtension fileExtension = FileExtension.from(extension); // 파일 확장자 유효성 검사
    String key = buildKey(folderName, id, extension);
    String uploadUrl = s3PresignedUrlProvider.generateUploadUrl(key, fileExtension.getContentType());
    return new S3PresignedResponse(uploadUrl, key);
  }

  //Read용 Presigned URL 응답
  public S3PresignedResponse generateReadUrl(String folderName, Long id, String key) {
    validateIdExists(folderName, id); // id 유효성 검사
    s3PresignedUrlProvider.validateFileExists(key); // 파일 존재 여부 확인
    String readUrl = s3PresignedUrlProvider.generateReadUrl(key);
    return new S3PresignedResponse(readUrl);
  }

  //id 유효성 검사
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

  // 파일 존재 여부 확인
  public void validateFileExists(String key) {
    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket("your-bucket-name") // 버킷 이름
          .key(key)
          .build();

      s3PresignedUrlProvider.getS3Client().getObject(getObjectRequest); // S3에서 파일을 읽어오는 시도
    } catch (S3Exception e) {
      throw new ApiException(FileStatus.NOTFOUND_FILE); // 파일이 없을 경우 예외 처리
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
