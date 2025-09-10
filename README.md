# REST Docs 발표자료

이 저장소에는 Spring REST Docs 발표를 위한 자료가 포함되어 있습니다.

- 발표자료(슬라이드형 Markdown): docs/restdocs-presentation.md
- 발표 스크립트(대본): docs/restdocs-presentation-script.md
- Asciidoctor 설명과 Markdown 설정 가이드: docs/asciidoctor-setup.md
- AsciiDoc 문법 빠른 가이드: docs/asciidoc-syntax-guide.md
- Spring REST Docs 공식 레퍼런스 링크 모음: docs/spring-restdocs-reference.md
- 데모 코드: Spring Boot(UserController) + REST Docs(MockMvc) 테스트 포함
- 최종 수정: 2025-09-10 06:54

발표자료는 다음을 다룹니다:
- REST Docs 개요와 동작 원리
- Gradle/Asciidoctor 설정 방법
- 테스트 기반 문서화(MockMvc/RestAssured) 예제
- 스니펫 구성과 커스터마이징
- 장단점 및 대안 비교(Swagger/OpenAPI 등)
- 실전 데모 가이드 및 발표자 노트

## 데모 실행 방법(Windows PowerShell)
사전 요구사항
- JDK 17+ 설치 및 JAVA_HOME 환경변수 설정

명령
1) 테스트 실행(스니펫 생성)
- `./gradlew.bat test`
- 기대: `build\\generated-snippets\\user-get\\http-response.adoc` 등 생성

2) HTML 문서 생성(Asciidoctor)
- `./gradlew.bat asciidoctor`
- 기대: `build\\docs\\asciidoc\\index.html` 생성 → 브라우저로 열기

3) 애플리케이션 실행 + 정적 문서 확인(자동 복사 적용)
- `./gradlew.bat bootRun`
- 정적 문서: http://localhost:8080/docs/index.html
- API 예시: GET http://localhost:8080/users/1 → 예시 사용자 JSON 응답

참고
- Gradle이 test → asciidoctor → copyRestDocs 순서로 문서를 생성/복사하고, Spring Boot가 classpath:/static/docs를 통해 정적으로 제공합니다.

필요에 따라 팀 상황에 맞게 문서와 코드를 복사/편집하여 사용해 주세요.