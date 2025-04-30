package com.matchday.matchdayserver.common.response;

public enum UserTeamStatus implements StatusInterface{
  NOTFOUND_TEAMUSER(400, 8001, "팀 유저간 소속관계가 없음");

  private final int httpStatusCode;
  private final int customStatusCode;
  private final String description;

  UserTeamStatus(int httpStatusCode, int customStatusCode, String description) {
    this.httpStatusCode = httpStatusCode;
    this.customStatusCode = customStatusCode;
    this.description = description;
  }

  @Override
  public int getHttpStatusCode() {
    return this.httpStatusCode;
  }

  @Override
  public int getCustomStatusCode() {
    return this.customStatusCode;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
