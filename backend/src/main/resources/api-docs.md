# Coffee Community 백엔드 API 명세

본 문서는 Coffee Community 백엔드가 제공하는 REST API를 정리합니다. 기본 URL은 `/api`이며, 별도로 표기한 경우가 아니면 모든 요청/응답은 JSON 형식입니다.

## 인증 및 권한
- **인증 방식:** `Authorization: Bearer <JWT>` 헤더 사용.
- **권한:** `ROLE_USER`는 일반 기능 사용, `ROLE_ADMIN`은 관리자 전용 엔드포인트 접근 가능.
- **미인증 시:** `401`과 함께 `{ "success": false, "message": "인증이 필요합니다." }` 형식의 에러가 반환됩니다.
- **권한 부족 시:** `403`과 함께 `{ "success": false, "message": "수정 권한 없음" }` 등 예외 메시지가 반환됩니다.

---

## 인증 (Auth)
| Path | Method | 요청 바디 | 응답 | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/api/auth/signup` | POST | `{ "username": "string", "password": "string" }` | `200 OK` (본문 없음) | `400` 잘못된 입력 또는 중복 사용자 |
| `/api/auth/login` | POST | `{ "username": "string", "password": "string" }` | `{ "accessToken": "string", "role": "ADMIN/USER", "username": "string" }` | `401` 자격 증명 오류 |

### 예시
**로그인 요청**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "barista",
  "password": "secret123"
}
```

**로그인 응답**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "USER",
  "username": "barista"
}
```

---

## 게시글 (Posts)
- 기본 경로: `/api/posts`
- `BoardType`: `GENERAL` 또는 `QNA`. 생성/수정 시 미지정하면 기본값 `GENERAL`.

| Path | Method | 요청 파라미터 | 응답 | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/api/posts` | POST | Body: `{ "title": "string", "content": "string", "boardType": "GENERAL/QNA" }` (인증 필요) | `200 OK` (본문 없음) | `401` 미인증, `400` 게시글 생성 중 오류 |
| `/api/posts` | GET | Query: `boardType=GENERAL/QNA` | `200 OK` List of `PostResponse` | `400` 잘못된 게시판 타입 |
| `/api/posts/{postId}` | GET | Path: `postId` | 단일 `PostResponse` | `400` 게시글 없음 |
| `/api/posts/{postId}` | PUT | Body: `{ "title": "string", "content": "string", "boardType": "GENERAL/QNA" }` (작성자 인증 필요) | `200 OK` (본문 없음) | `401` 미인증, `403` 작성자 아님, `400` 게시글 없음 |
| `/api/posts/{postId}` | DELETE | Path: `postId` (작성자 또는 관리자) | `200 OK` (본문 없음) | `401` 미인증, `403` 권한 없음, `400` 게시글 없음 |

`PostResponse` 필드: `{ id, title, content, authorUsername, boardType, createdAt }`

### 예시
**게시글 생성**
```http
POST /api/posts
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "title": "하리오 V60 추출 노하우",
  "content": "30초 블루밍 후 2회 분할 주입...",
  "boardType": "GENERAL"
}
```

**게시글 조회 응답 (단일)**
```json
{
  "id": 12,
  "title": "하리오 V60 추출 노하우",
  "content": "30초 블루밍 후 2회 분할 주입...",
  "authorUsername": "barista",
  "boardType": "GENERAL",
  "createdAt": "2026-01-06T08:15:30"
}
```

---

## 댓글 (Comments)
- 기본 경로: `/api/comments`

| Path | Method | 요청 파라미터 | 응답 | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/api/comments/post/{postId}` | POST | Body: `{ "content": "string" }` (인증 필요) | `CommentResponse` | `401` 미인증, `400` 게시글 없음 |
| `/api/comments/post/{postId}` | GET | Path: `postId` | `200 OK` List of `CommentResponse` | `400` 게시글 없음 |
| `/api/comments/{commentId}` | DELETE | Path: `commentId` (작성자 또는 관리자) | `200 OK` (본문 없음) | `401` 미인증, `403` 권한 없음, `400` 댓글 없음 |

`CommentResponse` 필드: `{ id, content, authorUsername }`

### 예시
**댓글 작성 응답**
```json
{
  "id": 5,
  "content": "에어로프레스도 비슷한 비율로 해보세요!",
  "authorUsername": "latte_lover"
}
```

---

## Q&A
- 기본 경로: `/api/qna`
- 모든 응답은 `ApiResponse` 래퍼를 사용: `{ "success": true/false, "data": ..., "message": "string|null" }`

| Path | Method | 요청 파라미터 | 응답(data) | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/api/qna` | POST | Body: `{ "title": "string", "content": "string" }` (인증 필요) | `QuestionResponse` | `401` 미인증 |
| `/api/qna` | GET | - | `List<QuestionResponse>` | - |
| `/api/qna/{id}` | GET | Path: `id` | `QuestionResponse` | `400` 질문 없음 |
| `/api/qna/{questionId}/answers` | POST | Body: `{ "content": "string" }` (인증 필요) | `AnswerResponse` | `401` 미인증, `400` 질문 없음 |
| `/api/qna/{questionId}/answers/{answerId}/accept` | POST | Path: `questionId`, `answerId` (질문 작성자 인증 필요) | `void` | `401` 미인증, `403` 작성자 아님, `400` 이미 해결/답변 없음 |

`QuestionResponse` 필드: `{ id, title, content, authorUsername, status, createdAt, answers: [AnswerResponse...] }`

`AnswerResponse` 필드: `{ id, content, authorUsername, accepted, createdAt }`

### 예시
**질문 생성 응답**
```json
{
  "success": true,
  "data": {
    "id": 21,
    "title": "콜드브루 추출 시간 질문",
    "content": "12시간과 18시간 중 맛 차이가 있나요?",
    "authorUsername": "dripper",
    "status": "PENDING",
    "createdAt": "2026-01-06T07:58:10",
    "answers": []
  },
  "message": null
}
```

**답변 채택 에러 예시**
```json
{
  "success": false,
  "data": null,
  "message": "답변 채택 권한이 없습니다."
}
```

---

## 마이페이지 (MyPage)
- 기본 경로: `/api/mypage`
- 모든 엔드포인트는 인증 필요. 페이징 컬렉션은 Spring 표준 파라미터 `page`, `size`, `sort` 사용 (기본: `createdAt,DESC`).

| Path | Method | 요청 파라미터 | 응답 | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/api/mypage/posts` | GET | Query: `page`, `size`, `sort` | `Page<PostResponse>` | `401` 미인증 |
| `/api/mypage/posts/all` | GET | - | `List<PostResponse>` | `401` 미인증 |
| `/api/mypage/posts/{postId}` | DELETE | Path: `postId` (작성자) | `200 OK` (본문 없음) | `401` 미인증, `403` 권한 없음, `400` 게시글 없음 |
| `/api/mypage/questions` | GET | - | `List<QuestionResponse>` (내 질문 최신순) | `401` 미인증 |

---

## 관리자 (Admin)
- 기본 경로: `/api/admin`
- 모든 엔드포인트는 `ROLE_ADMIN` JWT 필요.

| Path | Method | 요청 파라미터 | 응답 | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/api/admin/access-logs` | GET | Query: `page`, `size`, `sort` | `Page<AccessLogResponse>` | `401` 미인증, `403` 권한 없음 |
| `/api/admin/mypage/posts` | GET | Query: `page`, `size`, `sort` | `Page<PostResponse>` | `401` 미인증, `403` 권한 없음 |
| `/api/admin/mypage/posts/{postId}` | DELETE | Path: `postId` | `200 OK` (본문 없음) | `401` 미인증, `403` 권한 없음, `400` 게시글 없음 |
| `/api/admin/posts/{postId}/comments` | DELETE | Path: `postId` | `200 OK` (본문 없음) | `401` 미인증, `403` 권한 없음 |

`AccessLogResponse` 필드: `{ id, username, action(uri), ip, timestamp }`

---

## 알림 (Notifications)
- 기본 경로: `/notifications` (인증 필요)

| Path | Method | 요청 파라미터 | 응답 | 주요 에러 |
| --- | --- | --- | --- | --- |
| `/notifications` | GET | - | `ApiResponse<List<NotificationResponse>>` (최신 20개) | `401` 미인증 |
| `/notifications/read/{id}` | POST | Path: `id` | `ApiResponse<Void>` | `401` 미인증, `400` 알림 없음 |

`NotificationResponse` 필드: `{ id, message, link, read, createdAt }`

### 예시
**알림 목록 응답**
```json
{
  "success": true,
  "data": [
    {
      "id": 3,
      "message": "내 질문에 새로운 답변이 등록되었습니다.",
      "link": "/qna.html?questionId=21",
      "read": false,
      "createdAt": "2026-01-06T08:10:00"
    }
  ],
  "message": null
}
```

---

## 에러 처리 공통 규칙
- `AccessDeniedException` → `403` + `{ "success": false, "message": "..." }`
- 인증되지 않은 요청 → `401` + `{ "success": false, "message": "인증이 필요합니다." }`
- 존재하지 않는 리소스/기타 예외 → `400` + `{ "success": false, "message": "게시글 없음" }` 등의 메시지

---

## 변경 이력
| 버전 | 날짜 | 작성자 | 변경 내용 |
| --- | --- | --- | --- |
| 1.0.0 | 2026-01-06 | ChatGPT | 최초 API 명세 작성 |