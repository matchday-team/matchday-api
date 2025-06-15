package com.matchday.matchdayserver.common;

public class Constants {

  public static final String FRONTEND_LOCAL_URL = "http://localhost:5173";
  public static final String FRONTEND_LOCAL_HTTPS_URL = "https://localhost:5173";
  public static final String FRONTEND_PRODUCTION_URL = "https://matchday-planner.com/";
  public static final String FRONTEND_DEV_URL = "https://*.matchday-planner.com/";
  public static final String FRONTEND_BRANCH_DEPLOY_URL= "https://*.match-day.workers.dev/";
  public static final String BACKEND_LOCAL_URL = "http://localhost:8080"; //swagger
  public static final String BACKEND_PRODUCTION_URL = "https://dev-api.matchday-planner.com";
  public static final String FRONTEND_LOCAL_VUE_URL = "http://localhost:3000";

  // WebSocket
    public static final String WEBSOCKET_ERROR_PATH = "/queue/errors";

  //삭제예정
    public static final String FRONTEND_LOCAL_URL_OLD = "http://localhost:5173";
    public static final String FRONTEND_PRODUCTION_URL_OLD = "https://match-day.netlify.app";


}
