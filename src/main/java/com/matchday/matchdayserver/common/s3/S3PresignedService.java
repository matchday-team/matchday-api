package com.matchday.matchdayserver.common.s3;

import com.matchday.matchdayserver.common.s3.dto.S3PresignedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {

  private final S3PresignedUrlProvider s3Service;

  //업로드 Presigned URL 요청
  public S3PresignedResponse generateUploadUrl(String folderName, Long id, String extension) {
    String key = buildKey(folderName, id, extension);
    String uploadUrl = s3Service.generateUploadUrl(key);
    return new S3PresignedResponse(uploadUrl, key);
  }

  //Read용 Presigned URL 응답
  public S3PresignedResponse generateReadUrl(Long id, String key) {
    String readUrl = s3Service.generateReadUrl(key);
    return new S3PresignedResponse(readUrl);
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
