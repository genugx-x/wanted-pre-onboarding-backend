# wanted-pre-onboarding-backend

원티드 프리온보딩 백엔드 인턴십 - 선발 과제

### 지원자: 강성범

### 프로젝트 개요 (구현 방법 및 이유에 대한 간략한 설명)
API 요구사항과 예외 발생 상황을 고려하여 각각의 엔드포인트 접근 상황에 맞게 
기능별 단위 테스트를 철저히 진행하며 개발을 진행했습니다. 
이 과정은 초기에는 요구사항을 충족하는 코드를 작성하는 시간이 늘어나게 됐지만, 
그 결과로 문제 발생 상황을 미리 파악하고 대응할 수 있어 전체적인 개발 진행에는 효율적이었습니다.

### 애플리케이션의 실행 방법

1. **실행 환경**
    - Java 17 이상
    - Gradle 8.2.1 이상
    - Docker 20.10.23 이상
    - Docker-Compose 2.15.1 이상
2. **소스 코드 다운로드**: 이 저장소를 클론하거나 ZIP 파일로 다운로드 받아주세요.
   ```bash
   https://github.com/genugx-x/wanted-pre-onboarding-backend.git
   ```
3. **실행 방법**
    1. 프로젝트 루트 경로로 이동합니다.
       ```bash
       $ cd wanted-pre-onboarding-backend
    2. 우선 `Mysql`을 `docker-compose`로 실행하기 위해 `docker` 디렉토리 하위에 `.env` 파일을 생성하고 다음과 같이 작성합니다.
       ```bash
       #.env 파일
       MYSQL_ROOT_PASSWORD=[YOUR_ROOT_PASSWORD]
       MYSQL_USER=wpob
       MYSQL_PASSWORD=[YOUR_USER_PASSWORD]
       ```
    3. 다음으로 `src/main/resources/application-dev.yml`을 수정합니다.
       ```yaml
       spring:
          datasource:
          ...
             username: wpob
             password: [YOUR_USER_PASSWORD]
          ...
       ```
    4. `docker` 디렉토리로 다시 이동하여 `docker-compose.yml` 파일이 있는 위치에서 다음 명령어를 사용하면 mysql이 동작합니다.
       ```bash
       $ docker-compose up -d
       [+] Running 2/2
       ⠿ Network docker_default  Created                                                                                                                        0.0s
       ⠿ Container mysql         Started
       ```
       정상 동작 확인을 위해서는 다음과 같이 입력하세요.
       ```bash
       $ docker ps
       CONTAINER ID   IMAGE       COMMAND                  CREATED              STATUS              PORTS                               NAMES
       e8586f13f42a   mysql:8.0   "docker-entrypoint.s…"   About a minute ago   Up About a minute   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql      
       ``` 
    5. Gradle build를 합니다.
       ```bash      
       $ gradle clean build
    6. 프로젝트 루트 경로에서 `build/libs` 로 이동한 후 다음 명령어를 입력하면 애플리케이션이 실행됩니다.
       ```bash
       $ java -jar -Dspring.profiles.active=dev wpob-0.0.1-SNAPSHOT.jar

4. **엔드 포인트**
    - GET /: 애플리케이션 정상 동작을 확인합니다.
    - POST /auth/signup: 새로운 사용자를 생성합니다.
    - POST /auth/login: 사용자가 입력한 로그인 입력을 검증하고 성공시 JWT를 발급합니다.
    - [/] [/auth/**]: 모든 사용자 접근이 허용되며, 이외에는 JWT 인증이 필요합니다.
    - POSTS /posts: 새로운 게시글을 생성합니다.
    - GET /posts: 게시글 목록을 조회합니다. 페이징 정보가 없는 경우 1페이지, 10개의 글을 조회합니다. 조회된 정보는 게시글 ID와 제목입니다.
    - GET /posts?size=10&page=4: 게시글 목록을 조회합니다. 4페이지의 10개 글을 조회합니다. 조회된 정보는 게시글 ID와 제목입니다.
    - GET /posts/{postsId}: 특정 게시글 1개를 조회합니다. 반환된 정보는 게시글 ID, 제목, 내용 입니다.
    - PATCH /posts/{postsId}: 특정 게시글 1개를 수정합니다. 게시글의 작성자만이 수정할 수 있습니다.
    - DELETE /posts/{postsId}: 특정 게시글을 삭제합니다. 게시글의 작성자만이 삭제할 수 있습니다.

### 데이터베이스 테이블 구조

![db_wpob.png](images%2Fdb_wpob.png)

### API 데모 영상 - [링크](https://drive.google.com/file/d/1m2fB_pxrL70CYYGIbiLBxDtQHRx8jbyO/view?usp=drive_linkhttps://drive.google.com/file/d/1m2fB_pxrL70CYYGIbiLBxDtQHRx8jbyO/view?usp=drive_link)

### API 명세(request/response 포함)

1. **사용자 회원가입**
    - URL: POST /auth/signup
    - 헤더
      - 'Content-Type': application/json
    - 요청
      ```http request
      POST http://localhost:8080/auth/signup
      Content-Type: application/json
      
      {
        "email": "user@email.com", 
        "password": "abcd1234"
      }
    - 응답
        - 성공
            - 상태 코드: 200 OK
        - 실패
            - 상태 코드: 400 Bad Request
            - JSON
                - 예시
                  ```json
                  {
                    "code": "400",
                    "message": "잘못된 요청입니다.",
                    "validation": {
                      "password": "비밀번호 형식이 올바르지 않습니다.",
                      "email": "이메일 형식이 올바르지 않습니다."
                    }
                  }
2. **사용자 로그인**
    - URL: POST /auth/login
    - 헤더
      - 'Content-Type': application/json
    - 요청
      ```http request
      POST http://localhost:8080/auth/login
      Content-Type: application/json
 
      {
        "email": "user@email.com",
        "password": "1z2x3c4v"
      }
    - 응답:
        - 성공
            - 상태 코드: 200
              ```http request
              {
                "token": {JWT Token}
              }
        - 실패
            - 상태 코드: 404 Not Found
              ```json
              {
                "code": "404",
                "message": "존재하지 않는 사용자입니다.",
                "validation": {}
              }

3. **게시글 생성**
    - URL: POST /posts
    - 헤더: 로그인 후 발급 받은 토큰(JWT)
        - 'Content-Type': application/json
        - 'Authorization': Bearer {JWT Token}
    - 요청
      ```http request
      POST http://localhost:8080/posts
      Content-Type: application/json
      Authentication: Bearer {JWT Token}
 
      {
        "title": "제목을 입력하세요.",
        "content": "내용을 입력하세요."
      }

    - 응답:
        - 성공
            - 상태 코드: 200
        - 실패
            - 상태 코드: 403 Forbidden

4. **게시글 목록 조회**
    - URL: GET /posts?size=10&page=1
    - 헤더: 로그인 후 발급 받은 토큰(JWT)
        - 'Authorization': Bearer {JWT Token}
    - 파라미터
        - size : 한 페이지에 노출되는 글의 수
        - page : 조회 하려는 페이지 위치
    - 요청
      ```http request
      GET http://localhost:8080/posts?size=10&page=1
      Authentication: Bearer {JWT Token}
    - 응답:
        - 성공
            - 상태 코드: 200
              ```json
              {
                 "totalPostCount": 3,
                 "totalPageCount": 1,
                 "posts": [
                   {
                     "id": 3,
                     "title": "제목입니다.",
                     "content": null
                   },
                   {
                     "id": 2,
                     "title": "제목입니다.",
                     "content": null
                   },
                   {
                     "id": 1,
                     "title": "제목입니다.",
                     "content": null
                   },
                   ...
                 ]
              }
5. **특정 게시글 조회**
    - URL: GET /posts/{postId}
    - 헤더: 로그인 후 발급 받은 토큰(JWT)
        - 'Authorization': Bearer {JWT Token}
    - 파라미터
      - postId: 게시글 ID 
    - 요청
      ```http request
      GET http://localhost:8080/posts/1
      Authentication: Bearer {JWT Token}
    - 응답:
        - 성공
          - 상태 코드: 200
            ```json
            {
              "id": 1,
              "title": "새 게시글을 생성합니다.",
              "content": "내용도 작성해 줍니다."
            }
      - 실패
          - 상태 코드: 404 Not Found (존재하지 않는 게시글 ID로 조회한 경우)
            ```json
            {
              "code": "404",
              "message": "존재하지 않는 게시글입니다.",
              "validation": {}
            }
6. **특정 게시글 수정**
    - URL: PATCH /posts/{postId}
    - 헤더: 로그인 후 발급 받은 토큰(JWT)
        - 'Content-Type': application/json 
        - 'Authorization': Bearer {JWT Token}
    - 파라미터
        - postId: 게시글 ID
    - 요청
      ```http request
      PATCH http://localhost:8080/posts/1
      Content-Type: application/json
      Authentication: Bearer {JWT Token}
    - 응답:
        - 성공
            - 상태 코드: 200
              ```json
              {
                "title": "수정 제목을 입력해주세요.",
                "content": "수정 내용을 입력해주세요."
              }
        - 실패
            - 상태 코드(1): 404 Not Found (존재하지 않는 게시글 ID로 조회한 경우)
              ```json
              {
                "code": "404",
                "message": "존재하지 않는 게시글입니다.",
                "validation": {}
              }
            - 상태 코드(2): 403 Forbidden (게시글 작성자와 작업 요청자가 다른 경우)
              ```json
              {
                "code": "403",
                "message": "요청하신 작업을 수행할 권한이 없습니다.",
                "validation": {}
              }
7. **특정 게시글 삭제**
    - URL: DELETE /posts/{postId}
    - 헤더: 로그인 후 발급 받은 토큰(JWT)
        - 'Authorization': Bearer {JWT Token}
    - 파라미터
        - postId: 게시글 ID
    - 요청
      ```http request
      DELETE http://localhost:8080/posts/1
      Authentication: Bearer {JWT Token}
    - 응답:
        - 성공
            - 상태 코드: 200
        - 실패
            - 상태 코드(1): 404 Not Found (존재하지 않는 게시글 ID로 조회한 경우) 
              ```json
              {
                "code": "404",
                "message": "존재하지 않는 게시글입니다.",
                "validation": {}
              }
            - 상태 코드(2): 403 Forbidden (게시글 작성자와 작업 요청자가 다른 경우)
              ```json
              {
                "code": "403",
                "message": "요청하신 작업을 수행할 권한이 없습니다.",
                "validation": {}
              }
            
### AWS 서버 링크 및 구조
- http://wpob.ap-northeast-2.elasticbeanstalk.com
- 서버 구조
  ![server-architecture.png](images%2Fserver-architecture.png)
