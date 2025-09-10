# Spring REST Docs 공식 문서 링크와 사용 가이드

마지막 수정: 2025-09-10 06:57

이 문서는 Spring REST Docs 공식 문서를 빠르게 찾고, 실제 업무에 바로 적용할 수 있도록 “어디를 읽고, 무엇을 하면 되는지”를 정리했습니다.

## 1) 공식 문서 링크
- HTML (한 페이지): https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/
- HTML (여러 페이지): https://docs.spring.io/spring-restdocs/docs/current/reference/html/
- PDF: https://docs.spring.io/spring-restdocs/docs/current/reference/pdf/spring-restdocs-reference.pdf
- 샘플 코드: https://github.com/spring-projects/spring-restdocs/tree/main/samples

Tip
- Ctrl+F로 검색하세요. 예: “snippets”, “MockMvc”, “RestAssured”, “path parameters”, “payload”.
- 버전 고정이 필요하면 URL의 current를 원하는 버전(예: 3.0.2)으로 바꾸세요.

## 2) 문서 구조와 길 찾기
공식 문서는 크게 다음을 다룹니다(키워드로 검색해 바로 이동하세요).
- 시작하기/설치: Gradle/Maven 설정, 테스트 통합 방법
- 테스트 통합 방식: MockMvc, WebTestClient, RestAssured
- 문서화 대상: 경로/쿼리 파라미터, 요청/응답 필드, 헤더, 경로 변수 등
- 스니펫(snippets): 생성되는 조각 문서 종류와 내용
- 커스터마이즈: 스니펫 템플릿, 어설션/전처리(preprocess) 등
- 제약사항 문서화: Bean Validation 연계(ConstraintDescriptions)

읽는 요령
- 먼저 자신이 쓰는 테스트 도구(예: MockMvc) 섹션을 보고, 다음으로 “문서화 항목”을 찾아 각 항목별 예시 코드를 확인하세요.
- include로 스니펫을 끼우는 부분은 Asciidoctor(이 저장소의 docs/asciidoctor-setup.md)와 함께 보시면 빠르게 이해됩니다.

## 3) 바로 쓰는 핵심 주제 요약(치트시트)
- MockMvc 통합: andDo(document("...", ...))
  - 키워드: “MockMvc”, “MockMvcRestDocumentation”
- RestAssured 통합: filter(document("..."))
  - 키워드: “RestAssured”, “RestDocumentationFilter”
- 경로 변수/요청 파라미터: pathParameters(...), requestParameters(...)
  - 키워드: “Path parameters”, “Request parameters”
- 요청/응답 바디 필드: requestFields(...), responseFields(...)
  - 키워드: “Payload fields”, “fieldWithPath”
- 헤더: requestHeaders(...), responseHeaders(...)
  - 키워드: “Headers”
- 전처리(preprocess): preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())
  - 키워드: “preprocess”, “OperationPreprocessors”
- 제약사항(Bean Validation): ConstraintDescriptions
  - 키워드: “ConstraintDescriptions”

## 4) 자주 쓰는 작업 흐름(워크플로우)
1. 테스트에 문서화 코드 추가
   - 예: andDo(document("user-get", pathParameters(...), responseFields(...)))
2. 테스트 실행 → build/generated-snippets에 조각 문서 생성
3. Asciidoctor로 HTML 생성 → build/docs/asciidoc/index.html
4. 필요 시 스니펫/스타일을 조정하고 다시 빌드

스니펫 include 예시(이 저장소 템플릿)
- index.adoc에서:
  - include::{snippets}/user-get/http-request.adoc[]
  - include::{snippets}/user-get/http-response.adoc[]
  - include::{snippets}/user-get/response-fields.adoc[]

## 5) 버전 맞추기(중요)
- REST Docs 라이브러리 버전과 공식 문서 버전을 일치시키면 혼란이 줄어듭니다.
  - 예: build.gradle의 testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc:3.0.2'
  - 공식 문서 URL: docs/.../3.0.2/reference/htmlsingle/
- Spring Boot BOM을 쓰면 버전이 관리될 수 있으니, 실제 적용 버전을 확인하세요.

## 6) 흔한 문제와 해결 팁
- 스니펫이 생성되지 않음: 테스트에서 andDo(document(...)) 호출이 누락된 경우가 많습니다.
- HTML에 스니펫이 비어있음: asciidoctor 속성 'snippets' 경로가 올바른지 확인하세요.
- 한글 깨짐(PDF): Asciidoctor PDF 폰트 설정이 필요합니다(HTML 사용 권장).
- 멀티 모듈: 모듈별 generated-snippets를 한 곳에 모아 상위 문서에서 include하세요.

## 7) 빠른 작업 가이드(무엇을 찾을지 키워드)
- 경로 변수 문서화: “Path parameters”, parameterWithName
- 요청 파라미터 문서화: “Request parameters”, parameterWithName
- 요청/응답 바디 필드: “Payload fields”, fieldWithPath, subsectionWithPath
- 공통 헤더: “Headers”, headerWithName
- 전처리(보기 좋게): “preprocess”, prettyPrint
- 에러 응답 문서화: “Response fields”, “constraints”, “custom snippets”

## 8) 우리 저장소와 연결해서 보기
- Asciidoctor 설정 가이드: docs/asciidoctor-setup.md
- AsciiDoc 문법 빠른 가이드: docs/asciidoc-syntax-guide.md
- 발표자료(슬라이드): docs/restdocs-presentation.md
- 발표 대본: docs/restdocs-presentation-script.md

이 페이지를 시작점으로 공식 문서를 열고, 위 키워드로 검색하면서 필요한 부분을 바로 적용해 보세요. 팀 위키에 본 페이지와 htmlsingle 링크를 함께 고정해두면 온보딩이 쉬워집니다.
