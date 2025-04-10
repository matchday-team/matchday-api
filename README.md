## 📝 규칙

### 💻 컨벤션

- Commit Message :
  - feat(새 기능), fix(버그난거 수정), refactor(기능은 안바뀌는데 코드 변경), modify/update(기존 있던 기능 수정), chore(기타 잡다한 빌드 로그), config(dependency)
    prefix(범위): 커밋 내용 간단하게
- PR Rule
  - [JIRA ISSUE 번호] 제목
  - PR Template

```markdown
## Overview

어떤 내용 변경 했는지 요약

## Review Request

어떤 부분 중점으로 봐줬으면 좋겠는지

## Reference

어떤거 참조해서 했는지
```

- PR 머지시 최소 1명 Approval 필요

### 🗺️ 아키텍처

- Layered
  - Interface가 필요한 부분에만 Interface를 쓰고 테스트코드 Interface 기준으로 작성
    - Cache, In Memory -> Redis
- 폴더 구조
  - 도메인 (user, league)
    - controller
      - request
      - response
    - service
    - repository
      - projection
    - entity

### 🏗️ 인프라

- 데브 서버 NCP -> Server + DB + AWS S3
  - 크레딧 사용해서 부하테스트나 테스트 돌리고 테스트 결과 바탕으로 예상 트래픽 산정해서 운영서버 스펙 결정
- 운영 서버 -> AWS EC2 + RDS + S3
- CI/CD -> github action
- Spring Boot, MySQL
