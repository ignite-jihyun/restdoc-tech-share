# Spring REST Docs 발표자료

일시: 2025-09-09

---

## 1. 오늘 진행 순서
- REST Docs가 무엇인지
- 왜 쓰는지: 장점/단점과 다른 방법 비교
- 작동 방식 (테스트로 문서 만들기)
- Gradle/Asciidoctor 설정
- 예시: MockMvc / RestAssured 테스트로 조각 문서 만들기
- 문서 꾸미기(맞춤)와 자동화 팁
- 데모 흐름
- 질문 시간(Q&A)

---

## 2. REST Docs 소개
- 테스트를 돌려서 생기는 조각 문서(snippets)를 Asciidoctor로 모아 API 문서를 만듭니다.
- 테스트가 통과해야 문서가 새로 갱신되므로, 문서와 실제 API 차이가 줄어듭니다.
- Swagger UI와 달리, 실행 중에 보이는 화면이 아니라 빌드/테스트 할 때 문서를 만듭니다.

---

## 3. 핵심 개념
- 조각 문서(snippets): 요청/응답, 필드 설명, 경로/쿼리 값 등 작은 문서 조각
- Asciidoctor: 조각 문서를 포함(포함하기)해서 최종 adoc → HTML/PDF로 만듦
- 테스트: MockMvc/RestAssured/JUnit으로 실제 요청/응답을 잡아 조각 문서를 만듦

---

## 4. 장점
- 믿을 수 있음: 테스트가 통과하면 문서도 최신
- 정확함: 실제 요청/응답을 기반으로 만듦
- 유연함: Asciidoctor로 레이아웃과 구성 바꾸기 쉬움
- 리뷰 편함: 문서가 코드리뷰/PR 과정과 자연스럽게 함께 감

---

## 5. 단점과 주의할 점
- 초기 설정이 조금 복잡함(Gradle/Asciidoctor, 폴더 구조)
- 문서용 테스트를 쓰는 데 시간이 듦(필드 설명 등)
- 실행 화면(UI)은 기본 제공이 아님(HTML/PDF을 만드는 과정이 따로 필요)

---

## 6. 다른 방법과 비교
- Swagger/OpenAPI (springdoc-openapi 등)
  - 장점: 실행 중 보는 화면(UI), 빠른 시각화, 클라이언트 SDK 만들기 쉬움
  - 단점: 실제 응답 예시를 계속 맞추는 추가 작업 필요, 주석으로 스키마 관리 필요
- REST Docs
  - 장점: 테스트 기반이라 믿을 수 있고, 실제 응답을 캡처해서 품질이 좋음
  - 단점: 초기에 설정/작성 비용이 있음
- 같이 쓰기: REST Docs(정리된 문서) + OpenAPI(스키마/도구 연동)


### 클라이언트 SDK 만들기 쉬움
OpenAPI Generator (권장), Swagger Codegen
수십 종 언어/런타임을 지원: TypeScript(axios/fetch), Kotlin/Java(OkHttp/Retrofit/WebClient), Swift,

https://www.postman.com/


TypeScript(axios) SDK 생성
```bash
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-axios \
  -o ./sdk/typescript
```

Kotlin(OkHttp/Retrofit) SDK 생성
```bash
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g kotlin \
  -o ./sdk/kotlin \
  -p library=jvm-okhttp4
```

---

## 7. Gradle 설정 예시 (Spring Boot)
```gradle
plugins {
    id 'org.springframework.boot' version '3.3.1'
    id 'io.spring.dependency-management' version '1.1.5'
    id 'java'
    id 'org.asciidoctor.jvm.convert' version '4.0.2'
}

dependencies {
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    // 또는 RestAssured 사용 시
    // testImplementation 'org.springframework.restdocs:spring-restdocs-restassured'
}

tasks.named('test') {
    useJUnitPlatform()
    outputs.dir file("build/generated-snippets")
}

asciidoctor {
    // asciidoctor 작업의 **입력(input)**으로 build/generated-snippets 폴더를 지정합니다.
    inputs.dir file("build/generated-snippets") 
    
    // Asciidoctor의 **baseDir(기준 경로)**를 “현재 소스 파일(index.adoc)이 있는 위치”로 맞추라는 뜻입니다.
    baseDirFollowsSourceFile() 
    
    // : 어떤 .adoc 파일을 변환할지 지정합니다. 여기서는 index.adoc만 변환 대상으로 삼아요.
    // src/docs/asciidoc 밑에 여러 .adoc이 있어도 메인 진입점(index.adoc)만 지정해서 그 안에서 다른 파일을 include하도록 하는 구조를 많이 씁니다.
    sources { include 'index.adoc' } 
    
    // Asciidoctor가 변환할 때, src/docs/asciidoc/images/** 경로의 이미지를 결과물(build/docs/asciidoc)에 함께 복사해 줍니다.
    resources { from('src/docs/asciidoc') { include 'images/**' } } 
}

bootJar {
    dependsOn asciidoctor
    from("build/docs/asciidoc") { into 'static/docs' }
}
```

---

## 8. 폴더 구조 예시
```
src
├─ main
│  └─ resources
│
├─ test
│  └─ java ... (REST Docs 테스트)
└─ docs
   └─ asciidoc
      ├─ index.adoc (메인 문서)
      └─ snippets (생성물 포함 폴더 아님, 포함 경로는 build/generated-snippets 참조)

build
└─ generated-snippets (테스트 실행 시 생성)
```

---

## 9. index.adoc 예시
```adoc
= API 문서
:toc: left
:toclevels: 3
:doctype: book

== 개요
이 문서는 테스트로 만든 조각 문서를 포함합니다.

== 사용자 API
include::{{snippets}}/user-get/http-request.adoc[]
include::{{snippets}}/user-get/http-response.adoc[]
include::{{snippets}}/user-get/response-fields.adoc[]
```
- 빌드할 때 속성(attributes)으로 snippets 경로를 넘기거나, asciidoctor 작업에서 변수로 지정할 수 있습니다.

TOC 속성: AsciiDoc User Guide – Table of Contents
- https://docs.asciidoctor.org/asciidoc/latest/toc/
Doctype 속성: AsciiDoc User Guide – Document Types
- https://docs.asciidoctor.org/asciidoc/latest/document/doctypes/
Attributes 전체 목록: AsciiDoc User Guide – Attributes
- https://docs.asciidoctor.org/asciidoc/latest/attributes/document-attributes-ref/

https://docs.asciidoctor.org/asciidoc/latest/attributes/document-attributes/

---

## 10. MockMvc 테스트 예시
```java
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class UserApiDocsTest {

    @Autowired MockMvc mockMvc;

    @Test
    void 사용자_조회_문서화() throws Exception {
        mockMvc.perform(get("/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("user-get",
                pathParameters(
                    parameterWithName("id").description("사용자 ID")
                ),
                responseFields(
                    fieldWithPath("id").description("사용자 ID"),
                    fieldWithPath("name").description("이름"),
                    fieldWithPath("email").description("이메일")
                )
            ));
    }
}
```

---

## 11. RestAssured 테스트 예시
```java
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiRestAssuredDocsTest {

    @LocalServerPort int port;

    @Test
    void 사용자_조회_문서화_RA() {
        RestAssured.port = port;

        RestAssured.given()
            .filter(document("user-get",
                pathParameters(parameterWithName("id").description("사용자 ID"))
            ))
            .accept(ContentType.JSON)
        .when()
            .get("/users/{id}", 1)
        .then()
            .statusCode(200);
    }
}
```

---

## 12. 만들어지는 조각 문서 예시
- http-request.adoc: 요청 라인/헤더/바디
- http-response.adoc: 상태/헤더/바디
- path-parameters.adoc, request-parameters.adoc, request-fields.adoc, response-fields.adoc 등

예시(response-fields.adoc):
```adoc
|===
|Path|Type|Description
|id|Number|사용자 ID
|name|String|이름
|email|String|이메일
|===
```

---

## 13. Asciidoctor로 문서 만들기
- 명령: `./gradlew asciidoctor` (Windows: `gradlew.bat asciidoctor`)
- 출력 파일: `build/docs/asciidoc/index.html`
- Spring Boot와 함께 쓸 때는 `bootJar`에 넣어 `/docs/index.html`로 제공할 수 있습니다.

---

## 13-1. 테스트 → 스니펫 생성 (단일 테스트도 가능)
- 전체 테스트로 생성: `./gradlew.bat test`
- 단일 클래스만 실행: `./gradlew.bat test --tests "org.example.user.UserGetApiDocsTest"`
- 단일 메서드만 실행: `./gradlew.bat test --tests "org.example.user.UserGetApiDocsTest.사용자_조회_문서화"`
- 생성 위치: `build\\generated-snippets\\<식별자>` (예: `user-get`)

흐름(ASCII 다이어그램)
```
[JUnit/MockMvc 테스트]
         |
         v
[andDo(document("..."))]
         |
         v
[build/generated-snippets]
```

---

## 13-2. Asciidoctor → REST Docs HTML
- 스니펫을 index.adoc에서 include → HTML 생성
- 명령: `./gradlew.bat asciidoctor`
- 출력: `build\\docs\\asciidoc\\index.html`

흐름(ASCII 다이어그램)
```
[build/generated-snippets] --(include)--> [src/docs/asciidoc/index.adoc]
                                       |
                                       v
                                 [Asciidoctor]
                                       |
                                       v
                      [build/docs/asciidoc/index.html]
```

---

## 13-3. 배치(배포) 과정 요약
- HTML을 정적 경로로 복사: Gradle `copyRestDocs` 태스크가 수행
- 애플리케이션 실행/패키징 전에 문서 복사: `bootRun`/`bootJar`가 `copyRestDocs`에 의존
- 실행 후 접속: http://localhost:8080/docs/index.html

흐름(ASCII 다이어그램)
```
[index.html] --(copyRestDocs)--> [build/resources/main/static/docs]
                                         |
                                         v
                               [Spring Boot 실행]
                                         |
                                         v
                           GET /docs/index.html 로 제공
```

---

## 14. 문서 품질/맞춤 팁
- ConstraintDescriptions로 Bean Validation 제약을 자동으로 설명할 수 있음
```java
var constraints = new ConstraintDescriptions(UserDto.class);
String nameDesc = String.join(", ", constraints.descriptionsForProperty("name"));
```
- 공통 헤더/에러 응답 조각 문서를 재사용 가능한 설명 정보로 빼서 관리
- 예제 바디는 ObjectMapper pretty print로 보기 좋게 출력
- 문서 스타일은 Asciidoctor 테마/하이라이트로 통일감 유지

---

## 15. CI/CD 연동하기
- 테스트에서 조각 문서 생성 → asciidoctor 실행 → 결과물 업로드
- GitHub Actions 예:
```yaml
- name: Test
  run: ./gradlew test
- name: Generate Docs
  run: ./gradlew asciidoctor
- name: Upload artifact
  uses: actions/upload-artifact@v4
  with:
    name: api-docs
    path: build/docs/asciidoc
```

---

## 16. 데모 진행 흐름(현장)
1) `git checkout demo-start` (미리 준비한 브랜치) [이 저장소는 개념 데모로 설명]
2) 간단한 UserController, UserService, DTO 만들기
3) MockMvc 테스트 작성 + document("user-get") 추가
4) `gradlew.bat test` 실행 → build/generated-snippets 확인
5) `src/docs/asciidoc/index.adoc`에서 include 작성
6) `gradlew.bat asciidoctor` 실행 → index.html 열기
7) 필드를 하나 더 추가 → 테스트/문서 자동 갱신 확인

---

## 17. 좋은 방법(베스트 프랙티스)
- 실패 응답(400, 401, 403, 404, 409, 422, 500…)도 문서화하면 운영에 도움
- 공통 에러 포맷(에러코드, 메시지, traceId) 표준화
- API 버전 전략과 문서 버전 표시
- 샘플 값은 현실적으로: 개인정보 보호, 경계값 포함

---

## 18. 문제 해결
- 조각 문서가 비어 있음: 테스트에서 andDo(document()) 호출 여부 확인
- asciidoctor가 조각 문서를 못 찾음: 경로/속성(snippets 폴더) 확인
- 한글 깨짐: Asciidoctor PDF 폰트 설정 필요
- 여러 모듈: 모듈별 조각 문서를 모아 상위 문서에서 함께 포함

---

## 19. 참고 링크
- Spring REST Docs: https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/
- Asciidoctor: https://asciidoctor.org/
- spring-restdocs-examples: https://github.com/spring-projects/spring-restdocs/tree/main/samples

---

## 20. Q&A
- 예상 질문과 답변 가이드
  - OpenAPI와 무엇이 다른가요?
  - 문서 자동화 수준은?
  - 이전 문서를 어떻게 옮기나요(마이그레이션)?

---

## 발표자 노트(내부용)
- 데모는 로컬에서 포트 충돌 주의
- 테스트 속도 개선을 위해 WebMvcTest 같은 일부만 테스트 사용하는 방법 고려
- 팀 표준 조각 문서/스타일 템플릿을 별도 라이브러리로 만들어 재사용 제안

---

## 부록 A. 발표 진행 시간표 (권장 40분)
- 0–2분: 인사, 주제/목표 소개
- 2–6분: REST Docs 개념/작동 방식 요약 (슬라이드 2–3)
- 6–12분: 장단점/다른 방법 비교 (슬라이드 4–6)
- 12–18분: Gradle/폴더 구조 설명 (슬라이드 7–9)
- 18–28분: 데모 진행 (슬라이드 10–16)
  - 테스트 실행 → 조각 문서 생성 확인 → asciidoctor → index.html 보기
- 28–33분: 좋은 방법/문제 해결 (슬라이드 17–18)
- 33–38분: Q&A (슬라이드 20)
- 38–40분: 정리 및 다음 행동 안내

---

## 부록 B. 데모 상세 스크립트 (Windows)
사전 준비
- JDK 17+ 설치, 환경변수 JAVA_HOME 설정
- Gradle Wrapper 포함(이 저장소 포함), IDE에서 인덱싱 완료
- 포트 충돌 점검: (Spring Boot 데모 시) 8080 포트 중복 확인

데모 진행
1) 저장소 열기: IDE에서 프로젝트 열기
2) (선택) 브랜치 전환: git checkout demo-start
3) 테스트 실행: PowerShell에서
   - 명령: `./gradlew.bat test`
   - 기대: `build\generated-snippets` 폴더 생성 확인
4) 조각 문서 확인:
   - 예: `build\generated-snippets\user-get\http-response.adoc` 존재
5) Asciidoctor로 HTML 만들기:
   - 명령: `./gradlew.bat asciidoctor`
   - 기대: `build\docs\asciidoc\index.html` 생성
6) 결과 확인:
   - IDE 또는 파일 탐색기에서 index.html 더블클릭 → 브라우저로 열기
7) 변화 확인:
   - 테스트에서 response-fields에 필드 하나 추가 → 다시 `gradlew.bat test` → HTML 재생성 → 변경 반영 확인

문제 상황과 즉시 대응
- 조각 문서 없음: 테스트에 `andDo(document("..."))` 호출 누락 → 코드 보완 후 재실행
- asciidoctor 실패: `inputs.dir` 경로와 `sources.include` 확인
- 한글 깨짐(PDF): 폰트 설정 추가 필요(발표는 HTML 위주 권장)
- 포트 충돌: `application.yml`에서 server.port 변경 또는 RANDOM_PORT 사용

되돌리기
- Git에서 변경 취소로 데모 전 상태 복원
- `build` 폴더 삭제 후 다시 빌드로 깨끗한 상태 만들기

---

## 부록 C. 자주 묻는 질문(FAQ) 예시 답변
Q1. OpenAPI(Swagger)와 어떤 점이 다른가요?
- 짧은 답: REST Docs는 테스트 기반 정적 문서, OpenAPI는 실행 중 스키마/화면 중심.
- 자세한 답: REST Docs는 실제 요청/응답을 테스트로 잡아서 믿을 수 있습니다. OpenAPI는 생태계·도구(스키마, SDK, UI)에 강합니다. 두 방법을 함께 쓰면 좋습니다.

Q2. 문서를 항상 최신으로 유지하려면?
- 테스트가 통과할 때만 조각 문서가 새로 생기므로, CI에서 test → asciidoctor를 꼭 돌리고, 변경 리뷰 때 HTML 결과물을 함께 봅니다.

Q3. 처음 도입 비용을 줄이려면?
- 표준 조각 문서/스타일 템플릿을 만들어 재사용하고, 중요한 API부터 단계적으로 적용하세요. WebMvcTest 등 일부만 테스트로 속도를 높일 수 있습니다.

Q4. 큰 조직/여러 모듈에서는?
- 모듈별로 만든 조각 문서를 모아 상위 문서에서 함께 포함하고, 공통 규칙(에러 포맷/용어)을 공유 라이브러리로 배포합니다.

Q5. 예제 응답의 개인정보/보안은?
- 샘플 데이터 정책을 정하고, 민감정보는 가리고(마스킹) 비식별화 규칙을 문서에 적습니다.

---

## 부록 D. 발표/데모 체크리스트
장비/환경
- [ ] 노트북 전원/어댑터, 인터넷 연결 확인
- [ ] JDK/Gradle Wrapper 동작 확인, 저장소 클론 완료
- [ ] 브라우저 기본 앱 설정(팝업 차단 해제 권장)
저장소 준비
- [ ] `build` 폴더 정리, 캐시 문제 제거
- [ ] 데모용 테스트 코드/리소스 미리 점검
- [ ] `build\generated-snippets` 예시 조각 문서 준비(시간 절약)
진행
- [ ] 시간 타이머 준비
- [ ] 실패 시 대체 시나리오 준비(스크린샷/미리 빌드한 HTML)
마무리
- [ ] 슬라이드/HTML 문서와 소스 링크 공유
- [ ] 피드백 폼 안내

---

## 부록 E. 맞춤(커스텀) 가이드
브랜딩/테마
- 슬라이드 상단에 회사 로고 추가(마크다운 이미지): `![Logo](docs/images/logo.png)`
- Asciidoctor 테마: `src\docs\asciidoc\theme`에 사용자 테마 yml 추가 후 `-a pdf-theme=...` 사용
용어/도메인 바꾸기
- 예제 User → 팀의 핵심 도메인(예: Order, Product)으로 변경
- 공통 에러 포맷, 응답 래퍼(예: { code, message, data }) 표준화 및 문서화
자동화/품질
- CI에서 HTML 결과물을 업로드하고, 내부 위키/Pages에 자동 게시
- 실패 응답 문서화를 빌드 품질 기준에 포함(테스트/문서 커버리지)


---

## 부록 F. Asciidoctor 한눈에 보기
- 역할: AsciiDoc(.adoc)을 HTML/PDF로 바꿔 주는 엔진입니다.
- 우리 흐름: 테스트 → 스니펫(.adoc) 생성 → index.adoc에서 include → asciidoctor로 HTML 생성.
- 장점: 버전관리 친화, include/attributes 유연, 테마와 하이라이트 지원.
- Gradle 사용 포인트: org.asciidoctor.jvm.convert 플러그인, inputs.dir로 스니펫 경로 연결, attributes로 {snippets} 전달.
- 산출물: build\docs\asciidoc\index.html

---

## 부록 G. Markdown으로 설정하는 방법 요약
Markdown을 그대로 쓰고 싶을 때 선택지 3가지입니다. 팀 상황에 맞게 고르세요.

1) 옵션 A: AsciiDoc은 유지, Markdown은 원문 그대로 포함(렌더링 없이 코드블록 형태)
- 추가 도구가 필요 없고 빠릅니다.
- 단, .md가 예쁘게 렌더링되지는 않습니다.

2) 옵션 B(권장): 빌드 전에 Markdown → AsciiDoc 변환 후 포함
- 도구: Pandoc 또는 kramdown-asciidoc
- 장점: 최종 문서가 Asciidoctor 파이프라인으로 일관되게 렌더링됨(스타일/목차/테마 OK)
- 예: CI에서 .md → md-guide.adoc 변환 후 index.adoc에 include

3) 옵션 C: Markdown 전용 사이트(MkDocs 등)로 구축 + REST Docs 내용은 복사/링크로 포함
- 장점: 팀이 Markdown에 익숙하면 속도가 빠름
- 단점: REST Docs 스니펫(.adoc)과의 스타일 통합은 추가 작업이 필요

자세한 내용: docs/asciidoctor-setup.md 참조