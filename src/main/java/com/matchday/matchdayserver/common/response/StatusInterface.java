package com.matchday.matchdayserver.common.response;

public interface StatusInterface {
    int getHttpStatusCode();

    int getCustomStatusCode();

    String getDescription();
}
