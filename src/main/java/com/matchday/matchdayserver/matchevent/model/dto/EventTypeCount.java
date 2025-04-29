package com.matchday.matchdayserver.matchevent.model.dto;

public interface EventTypeCount {//쿼리 결과를 맵핑시키기 위해 사용 (인터페이스 DTO)
  String getEventType();
  Long getCount();
}
