package com.matchday.matchdayserver.common.exception;


import com.matchday.matchdayserver.common.response.StatusInterface;

public interface ApiExceptionInterface {
    StatusInterface getStatus();
}
