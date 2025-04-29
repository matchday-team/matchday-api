package com.matchday.matchdayserver.s3.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3PresignedResponse {
  private final String url;
  private final String fileName;

  //Read용 url 반환
  public S3PresignedResponse(String url){
    this.url = url;
    this.fileName = null;
  }
}
