# Chapter11-project
Chapter11조 팀 프로젝트 입니다.

Coffee Community는 원두 정보, 추출 방식, 개인의 커피 취향을 기록하고 사용자 간 지식을 공유하는 커뮤니티 웹 서비스입니다. 
스프링 시큐리티를 이용한 보안 강화와 질문·답변의 상태 관리 시스템을 통해 신뢰성 있는 커뮤니티 환경을 구축했습니다.

프로젝트 개요
목적: 커피 애호가들을 위한 정보 기록 및 Q&A 기반의 지식 공유 플랫폼 구축

핵심 기능:
JWT 기반의 보안 로그인 및 회원가입
커피 추출 레시피 및 원두 리뷰 게시판 (CRUD)
질문 상태(대기/완료) 관리가 가능한 Q&A 게시판
사용자별 활동 내역 확인(MyPage)

기술 스택 (Tech Stack)
Frontend
Core: HTML5, CSS3, JavaScript (ES6+)
Library: jQuery 1.12.3 (AJAX 기반 비동기 통신)
Style: Responsive Sidebar Layout, Image Slider

Backend
Framework: 
Security: 
Database: 
Build Tool: 

Infrastructure & Deployment
Server: 
CI/CD: 
Web Server:


본 프로젝트는 main / develop / feature 브랜치 전략을 사용합니다.
기능 단위로 feature 브랜치를 생성하여 develop 브랜치로 Pull Request를 통해 병합합니다.
main 브랜치는 최종 배포용으로만 사용합니다.

## Docker & 배포 실행 방법

### 1) Docker 이미지 빌드 및 컨테이너 실행
루트 경로가 아닌 `backend` 디렉터리에서 아래 명령어를 실행합니다.

```bash
# 최초 혹은 변경 후 빌드
docker compose build

# 백엔드(Spring Boot) + MySQL 컨테이너 기동
docker compose up -d
```

### 2) 서비스 구성
- **MySQL**: 8.0 이미지, 초기 스키마/데이터는 `docker/mysql/init.sql` 자동 실행
- **백엔드**: Java 21, Gradle로 빌드된 Spring Boot JAR 실행
- 포트 매핑: 백엔드 `8080`, DB `3306`

### 3) 환경 변수
`compose.yaml`에서 기본값이 설정되어 있으며 필요 시 아래와 같이 덮어쓸 수 있습니다.

```bash
DB_HOST=db
DB_PORT=3306
DB_NAME=dbdb
DB_USER=dbuser
DB_PASSWORD=db1234!
```

### 4) 로컬 확인
- API: `http://localhost:8080` 에서 백엔드 동작 확인
- DB: MySQL 클라이언트로 `localhost:3306` 접속 (`dbuser` / `db1234!`)

### 5) 컨테이너 종료
```bash
docker compose down
```

