package com.matchday.matchdayserver.common.s3;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.FileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlProvider {

  private final S3Presigner presigner;
  private final S3Client s3Client;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  //Upload Presigned URL 발급
  public String generateUploadUrl(String key, String contentType) {
    //S3에 업로드할 객체 설정
    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(contentType)
        .build();

    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5)) //유효 시간 5분
        .putObjectRequest(objectRequest)
        .build();

    return presigner.presignPutObject(presignRequest).url().toString();
  }

  //Read용 Presigned URL 발급
  public String generateReadUrl(String key) {
    validateFileExists(key); // 파일 존재 여부 확인
    GetObjectRequest objectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))  //유효 시간 5분
        .getObjectRequest(objectRequest)
        .build();

    return presigner.presignGetObject(presignRequest).url().toString();
  }

  // 파일 존재 여부 확인
  public void validateFileExists(String key) {
    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      // S3에서 파일을 읽어오는 시도
      s3Client.getObject(getObjectRequest);
    } catch (S3Exception e) {
      throw new ApiException(FileStatus.NOTFOUND_FILE);
    }
  }
}
