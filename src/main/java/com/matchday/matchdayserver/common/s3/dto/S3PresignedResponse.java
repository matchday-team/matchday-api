package com.matchday.matchdayserver.common.s3.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3PresignedResponse {
  private final String url;
  private String fileName;

  public S3PresignedResponse(String url, String fileName) {
    this.url = url;
    this.fileName = fileName;
  }
}
