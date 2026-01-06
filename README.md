# ☕ Chapter11-project
Chapter11조 팀 프로젝트

**Coffee Community**는 원두 정보, 추출 방식, 개인의 커피 취향을 기록하고  
사용자 간 Q&A 기반의 지식 공유를 지원하는 커뮤니티 웹 서비스입니다.

Spring Security 기반 **JWT 인증**, **질문·답변 상태 관리**,  
**활동 알림** 및 **접근 로그 수집**을 통해 신뢰성 있는 커뮤니티 환경을 제공합니다.

---

## 프로젝트 개요

- **목적**
    - 커피 애호가들을 위한 정보 기록 및 Q&A 기반 지식 공유 플랫폼 구축

- **핵심 기능**
    - JWT 기반 회원가입/로그인 및 역할(ADMIN / USER)별 권한 제어
    - 게시판(자유 / 질문) CRUD 및 댓글 작성·삭제
    - 질문 상태(대기 / 해결) 전환 및 답변 채택
    - 답변·채택 시 활동 알림 제공
    - 마이페이지에서 내 게시글·질문 관리
    - 관리자용 게시글·댓글 관리 및 접근 로그 모니터링

---

## 주요 기능 상세

### JWT 인증·인가
- `/api/auth/signup`, `/api/auth/login`을 통해 JWT 발급
- `Authorization: Bearer <token>` 헤더 기반 인증
- 필터 체인을 통한 **Stateless 인증 구조**

### 게시판 관리
- `BoardType (GENERAL / QNA)`로 게시글 분리
- 작성자 또는 관리자만 수정·삭제 가능
- 게시판별 목록 조회 지원

### 댓글 기능
- 게시글 단위 댓글 작성·조회·삭제
- 작성자 또는 관리자만 삭제 가능

### Q&A 흐름 관리
- 질문 작성 / 조회
- 답변 등록
- 질문 작성자에 의한 **답변 채택**
- 채택 시 질문 상태 `SOLVED`로 변경

### 알림 시스템
- 답변 등록 및 채택 시 알림 생성
- `/notifications`에서 최신 20개 조회
- 읽음 처리 지원
- 본인 알림 및 수신자 없는 경우 알림 생성 방지

### 마이페이지
- 내가 작성한 게시글(페이지네이션 / 전체 조회)
- 내가 작성한 질문 목록 제공
- 내 게시글 삭제 기능

### 관리자 도구
- 전체 게시글 조회 및 삭제
- 게시글의 댓글 일괄 삭제
- 사용자 접근 로그 조회(페이지네이션)

### 접근 로그 기록
- 인터셉터 기반 로그 수집
- 사용자명 / IP / 요청 URI 저장
- 관리자 전용 페이지에서 조회 가능

---

## API 엔드포인트

> JWT가 필요한 API는  
> `Authorization: Bearer <token>` 헤더가 필요합니다.  
> 관리자 API는 **ADMIN 권한**만 접근 가능합니다.

### 인증
- `POST /api/auth/signup` — 회원가입
- `POST /api/auth/login` — 로그인 및 JWT 발급

### 게시글
- `POST /api/posts` — 게시글 작성
- `GET /api/posts?boardType=GENERAL|QNA` — 게시판별 목록 조회
- `GET /api/posts/{postId}` — 게시글 상세 조회
- `PUT /api/posts/{postId}` — 게시글 수정 (작성자)
- `DELETE /api/posts/{postId}` — 게시글 삭제 (작성자 / 관리자)

### 댓글
- `POST /api/comments/post/{postId}` — 댓글 작성
- `GET /api/comments/post/{postId}` — 댓글 목록 조회
- `DELETE /api/comments/{commentId}` — 댓글 삭제

### Q&A
- `POST /api/qna` — 질문 작성
- `GET /api/qna` — 질문 목록 조회
- `GET /api/qna/{id}` — 질문 상세 조회
- `POST /api/qna/{questionId}/answers` — 답변 작성
- `POST /api/qna/{questionId}/answers/{answerId}/accept` — 답변 채택

### 마이페이지
- `GET /api/mypage/posts` — 내 게시글(페이지네이션)
- `GET /api/mypage/posts/all` — 내 게시글 전체 조회
- `DELETE /api/mypage/posts/{postId}` — 내 게시글 삭제
- `GET /api/mypage/questions` — 내 질문 목록

### 알림
- `GET /notifications` — 알림 목록 조회
- `POST /notifications/read/{id}` — 알림 읽음 처리

### 관리자
- `GET /api/admin/access-logs` — 접근 로그 조회
- `GET /api/admin/mypage/posts` — 전체 게시글 조회
- `DELETE /api/admin/mypage/posts/{postId}` — 게시글 삭제
- `DELETE /api/admin/posts/{postId}/comments` — 댓글 일괄 삭제

---

## 설계 포인트

- **Stateless 보안 구조**
    - CSRF 비활성화
    - `JwtAuthenticationFilter`로 세션 없는 인증 흐름 구성

- **역할 기반 권한 분리**
    - 컨트롤러 + 서비스 레이어 이중 검증
    - 작성자 / 관리자 권한 명확화

- **Q&A 상태 모델링**
    - `QuestionStatus (WAITING / SOLVED)`
    - 답변 채택 시 상태 전환 + 알림 발송

- **알림 최소화 로직**
    - 수신자 없거나 본인일 경우 알림 미생성

- **DTO 변환 계층**
    - 엔티티 직접 노출 방지
    - Response DTO를 통한 응답 일관성 유지

---

## 🛠 기술 스택

### Frontend
- HTML5, CSS3, JavaScript (ES6+)
- jQuery 1.12.3 (AJAX)
- Responsive Sidebar Layout

### Backend
- Spring Boot 3.2 (Java 21)
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Gradle

### Infrastructure
- Docker Compose
- 내장 Tomcat

---

## 브랜치 전략

- `main` : 최종 배포 브랜치
- `develop` : 통합 개발 브랜치
- `feature/*` : 기능 단위 작업 브랜치  
  → Pull Request 기반 병합

---

## 🐳Docker & 실행 방법

### Docker 빌드 및 실행
```bash
cd backend
docker compose build
docker compose up -d
