# WebSocket 스펙 문서

## 목차

1. [WebSocket 연결 및 엔드포인트](#1-websocket-연결-및-엔드포인트)
2. [메시지 송신 (이벤트 기록)](#2-메시지-송신-이벤트-기록)
3. [메시지 수신 (이벤트 구독)](#3-메시지-수신-이벤트-구독)
4. [이벤트 전체 삭제 알림](#4-이벤트-전체-삭제-알림)
5. [이벤트 저장 및 검증 로직](#5-이벤트-저장-및-검증-로직)
6. [Validation 상세](#6-validation-상세)
7. [전체 플로우 요약](#7-전체-플로우-요약)
8. [기타 참고사항](#8-기타-참고사항)

## 1. WebSocket 연결 및 엔드포인트

```javascript
const socket = new SockJS("http://localhost:8080/ws");
const client = new Client({
    webSocketFactory: () => socket,
    onConnect: () => {
        setConnected(true);
        setError(null);
        console.log("Connected to WebSocket");

        client.subscribe(`/topic/match/${matchId}`, (message: IMessage) => {
            const event = JSON.parse(message.body);
            setEvents((prev) => [event, ...prev]);
        });

        client.subscribe("/user/queue/errors", (message: IMessage) => {
            const errorResponse: ApiResponse<any> = JSON.parse(message.body);
            setError(errorResponse.message);
            console.error("WebSocket Error:", errorResponse);
        });
    },
    onStompError: (frame: IFrame) => {
        console.error("Error connecting to WebSocket:", frame);
        setConnected(false);
        setError("WebSocket 연결 오류가 발생했습니다.");
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});
```

-   연결: `http://{BASE_URL}:8080/ws` → upgrade to STOMP
-   STOMP 메시지 송신 경로: `/app/match/{matchId}`
-   STOMP 구독 경로: `/topic/match/{matchId}`

## 2. 메시지 송신 (이벤트 기록)

### 요청 메시지 구조

```json
{
    "token": "string", // 현재는 Archives로 등록된 user의 ID
    "data": {
        "userId": 1, // 이벤트를 기록하는 선수 ID
        "eventType": "GOAL", // 이벤트 타입
        "description": "좋은 골이었습니다!" // 이벤트 설명(선택)
    }
}
```

-   **token**: 사용자 인증을 위한 토큰(숫자 ID 또는 JWT 등)
-   **data**: 이벤트 데이터
    -   **userId**: 이벤트를 기록하는 선수의 ID
    -   **eventType**: 이벤트 타입
    -   **description**: 이벤트 설명(선택)

### 이벤트 타입 (MatchEventType)

-   GOAL (골)
-   ASSIST (어시스트)
-   SHOT (슛)
-   VALID_SHOT (유효슛)
-   FOUL (파울)
-   OFFSIDE (오프사이드)
-   SUB_IN (교체입장)
-   SUB_OUT (교체퇴장)
-   YELLOW_CARD (옐로카드)
-   RED_CARD (레드카드/퇴장)
-   OWN_GOAL (자책골)

### 선수 교체 요청

#### 메시지 구조

```json
{
    "token": "string", // 현재는 Archives로 등록된 user의 ID
    "data": {
        "fromMatchUserId": 1, // 교체할 선수의 매치 유저 아이디
        "toUserId": 2, // 교체할 선수의 유저 아이디
        "message": "부상으로 인한 교체" // 교체 사유 등
    }
}
```

-   **token**: 사용자 인증을 위한 토큰(숫자 ID 또는 JWT 등)
-   **data**: 교체 데이터
    -   **fromMatchUserId**: 교체될 선수의 매치 유저 아이디
    -   **toUserId**: 교체될 선수의 유저 아이디
    -   **message**: 교체 사유 등 (선택)

#### 선수 교체 요청 엔드포인트

-   STOMP 메시지 송신 경로: `/app/match/{matchId}/exchange`
-   STOMP 구독 경로: `/topic/match/{matchId}`

교체 요청 시 자동으로 생성되는 이벤트:

-   SUB_IN: 교체 입장 선수에 대한 이벤트
-   SUB_OUT: 교체 퇴장 선수에 대한 이벤트

## 3. 메시지 수신 (이벤트 구독)

### 구독 경로

`/topic/match/{matchId}`

### 수신 메시지 구조

```json
{
    "id": 1, // 이벤트 ID
    "elapsedMinutes": 23, // 경기 시작 후 경과 시간(분)
    "teamId": 1, // 팀 ID
    "teamName": "FC 서울", // 팀 이름
    "userId": 1, // 선수 ID
    "userName": "손흥민", // 선수 이름
    "eventLog": "골" // 이벤트 로그(한글 설명)
}
```

## 4. 이벤트 전체 삭제 알림

경기 ID에 해당하는 모든 이벤트가 삭제되었을 경우, 백엔드에서는 해당 내용을 WebSocket 메시지로 알립니다.

### 발송 채널

`/topic/match-delete-events`

### 메시지 형식 (MatchEventDeleteResponse)

```json
{
    "id": 1 // 삭제된 이벤트들의 matchId
}
```

**설명**: DELETE `/api/v1/match-event/{matchId}` API 호출 시, 해당 matchId에 연결된 모든 이벤트가 삭제되며, 그 결과로 위 채널로 삭제 완료 메시지가 전송됩니다. 프론트에서는 이 메시지를 수신하여 관련 UI를 갱신하거나 알림을 띄울 수 있습니다.

## 5. 이벤트 저장 및 검증 로직

### 인증 및 권한 검증

-   token 필드로 인증된 사용자인지 확인
-   해당 matchId에 실제로 참가 중인 선수(userId)인지 검증
-   인증 실패/권한 없음 시 예외 발생

### 예외 코드

| 예외명                               | 설명                         | httpStatusCode | customStatusCode |
| ------------------------------------ | ---------------------------- | -------------- | ---------------- |
| UserStatus.NOTFOUND_USER             | 해당 유저가 존재하지 않음    | 400            | 4002             |
| MatchStatus.NOT_PARTICIPATING_PLAYER | 경기에 참여 중이지 않은 선수 | 400            | 6008             |
| TeamStatus.NOTFOUND_TEAM             | 해당 팀이 존재하지 않음      | 400            | 5002             |

### 이벤트 저장 및 파생 이벤트 생성

이벤트 타입에 따라 다음과 같이 여러 이벤트가 자동 생성될 수 있음:

-   GOAL: 골, 슛, 유효슛 이벤트 동시 생성
-   OFFSIDE: 오프사이드, 파울 이벤트 동시 생성
-   VALID_SHOT: 유효슛, 슛 이벤트 동시 생성
-   기타: 단일 이벤트 생성

각 이벤트는 DB에 저장되고, 구독자에게 각각 개별적으로 전송됨

## 6. Validation 상세

-   token이 유효한지, MatchUserRole 등록할 때 기록관(Archieves)로 등록된 유저인지 검증
-   userId가 해당 matchId에 실제로 참가 중인지 검증
-   eventType이 허용된 값인지(enum)
-   description은 400자 이내(엔티티 제약)

위 조건 중 하나라도 위반 시 예외 발생 및 저장/전송 불가

## 7. 전체 플로우 요약

1. 클라이언트가 `/app/match/{matchId}`로 이벤트 메시지 전송
2. 서버에서 인증/권한/유효성 검증
3. 이벤트 저장 및 파생 이벤트 생성
4. 각 이벤트별로 `/topic/match/{matchId}`로 구독자에게 전송
5. 클라이언트는 실시간으로 이벤트 수신 및 UI 반영

## 8. 기타 참고사항

-   현재 token에는 userId가 들어가고 있지만 추후 Security 구현에 따라서 token이 사라질 수도 아니면 JWT Token이 들어가야 할 수도 있습니다.
