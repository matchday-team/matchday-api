package com.matchday.matchdayserver.s3.enums;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.FileStatus;
import lombok.Getter;

@Getter
public enum FileExtension { //더 추가 할 확장자 있으면 추가 예정
  JPG("jpg", "image/jpeg"),
  JPEG("jpeg", "image/jpeg"),
  PNG("png", "image/png"),
  WEBP("webp", "image/webp");

  private final String extension;
  private final String contentType;

  FileExtension(String extension, String contentType) {
    this.extension = extension;
    this.contentType = contentType;
  }

  public static FileExtension from(String extension) {
    for (FileExtension ext : values()) {
      if (ext.extension.equalsIgnoreCase(extension)) {
        return ext;
      }
    }
    throw new ApiException(FileStatus.INVALID_FILE_EXTENSION);
  }
}
