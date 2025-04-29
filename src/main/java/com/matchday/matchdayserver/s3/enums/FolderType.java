package com.matchday.matchdayserver.s3.enums;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.FileStatus;

public enum FolderType {
  USERS("users"),
  TEAMS("teams");

  private final String folderName;
  FolderType(String folderName) {
    this.folderName = folderName;
  }

  public static FolderType from(String folderName) {
    for (FolderType type : values()) {
      if (type.folderName.equals(folderName)) {
        return type;
      }
    }
    throw new ApiException(FileStatus.INVALID_FOLDER_NAME);
  }
}