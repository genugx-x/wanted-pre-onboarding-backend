# wanted-pre-onboarding-backend
원티드 프리온보딩 백엔드 인턴십 - 선발 과제
 
### 지원자: 강성범  
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
      $cd wanted-pre-onboarding-backend
   2. 우선 Mysql을 docker-compose로 실행하기 위해 docker 폴더 하위에 .env 파일을 생성하고 다음과 같이 작성합니다.
      ```bash
      # .env 파일
      MYSQL_ROOT_PASSWORD=[YOUR_ROOT_PASSWORD]
      MYSQL_USER=wpob
      MYSQL_PASSWORD=[YOUR_USER_PASSWORD]
      ```
   3. 다음으로 src/main/resources/application-dev.yml을 수정합니다.
      ```yaml
      spring:
         datasource:
         ...
            username: wpob
            password: [YOUR_USER_PASSWORD]
         ...
      ```
   4. docker 디렉토리로 다시 이동하여 docker-compose.yml 파일이 있는 위치에서 다음 명령어를 사용하면 mysql이 동작합니다.
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
   6. 프로젝트 루트 경로에서 `build/libs` 로 이동한 후 다음 명령어를 입력하면 애플리케이션 실행이 완료됩니다.
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

### 구현한 API의 동작을 촬영한 데모 영상 링크

### 구현 방법 및 이유에 대한 간략한 설명  
### API 명세(request/response 포함)
1. **사용자 회원가입**
   - URL: POST /auth/signup
   - 파라미터: 없음
   - 요청: JSON  
     {"email": "user@email.com", "password": "abcd1234"}
   - 응답:
     - 상태 코드: 200 OK
     - 
2. **사용자 로그인**
3. **게시글 생성**
4. **게시글 목록 조회**
5. **특정 게시글 조회**
6. **특정 게시글 수정**
7. **특정 게시글 삭제**