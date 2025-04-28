package com.matchday.matchdayserver.common.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlProvider {

  private final S3Presigner presigner;

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

  private String guessContentType(String fileName) {
    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) { // 확장자 추가 필요시 추가할 예정
      return "image/jpeg";
    } else if (fileName.endsWith(".png")) {
      return "image/png";
    } else {
      return "application/octet-stream";
    }
  }
}
