# Spring REST Docs 발표 스크립트

- 관련 슬라이드: docs/restdocs-presentation.md

---

## 0. 시작 (0:00–0:02)
- [슬라이드 1]
- “안녕하세요. 오늘은 Spring REST Docs로 ‘테스트로 믿을 수 있는 API 문서’를 만드는 방법을 소개합니다. 저는 (이름)입니다. 40분 동안 개념, 설정, 데모, 실전 팁을 빠르게 살펴보겠습니다.”
- “질문은 메모해 두셨다가 Q&A 시간에 편하게 주세요.”

---

## 1. 오늘 순서 (0:02–0:04)
- [슬라이드 2]
- “REST Docs가 무엇인지, 왜 쓰는지, 어떻게 작동하는지, 설정 방법, 테스트 기반 문서화 데모 순으로 보고 마지막에 문서 꾸미기 팁과 Q&A로 마무리하겠습니다.”

---

## 2. REST Docs 소개 (0:04–0:06)
- [슬라이드 3]
- “REST Docs는 테스트를 돌린 결과로 생기는 ‘조각 문서(snippet)’를 Asciidoctor로 모아 최종 문서를 만드는 도구입니다.”
- “핵심은 ‘테스트가 통과해야 문서가 최신’이라는 점입니다. 문서가 코드와 함께 움직입니다.”

---

## 3. 핵심 개념 정리 (0:06–0:08)
- [슬라이드 4]
- “핵심은 세 가지입니다. 1) 조각 문서, 2) Asciidoctor로 모으기, 3) 테스트. 테스트가 실제 요청/응답을 잡아 조각 문서를 만들고, Asciidoctor가 이것을 포함해서 HTML/PDF 문서를 만듭니다.”

---

## 4. 장점 (0:08–0:10)
- [슬라이드 5]
- “테스트가 문서를 보장하니 믿을 수 있고 정확합니다. 문서는 리뷰/PR 과정에 자연스럽게 섞이고, Asciidoctor로 모양과 구성을 쉽게 바꿀 수 있습니다.”

---

## 5. 단점과 주의 (0:10–0:12)
- [슬라이드 6]
- “처음 설정이 조금 번거롭고, 문서용 테스트를 쓰는 시간이 듭니다. 그리고 기본으로 실행 화면(UI)을 주지 않아서 HTML/PDF을 만드는 과정을 따로 넣어야 합니다.”

---

## 6. 다른 방법과 같이 쓰기 (0:12–0:14)
- [슬라이드 7]
- “Swagger/OpenAPI는 빨리 화면으로 보여주고 도구가 많습니다. REST Docs는 실제 응답을 캡처해 문서 품질이 좋습니다. 실무에서는 둘을 같이 쓰면 좋습니다. ‘REST Docs로 정리된 문서 + OpenAPI로 스키마/도구 연계’.”

---

## 7. Gradle 설정 핵심 (0:14–0:16)
- [슬라이드 8]
- “test 작업이 조각 문서를 만들고, asciidoctor 작업이 이를 입력으로 받아 index.adoc을 HTML로 바꿉니다. 원하면 BootJar에 넣어 /docs 경로로 정적 제공도 가능합니다.”

---

## 8. 폴더 구조 (0:16–0:17)
- [슬라이드 9]
- “테스트를 실행하면 build/generated-snippets 아래에 조각 문서가 생깁니다. src/docs/asciidoc/index.adoc에서 이것을 포함합니다.”

---

## 9. index.adoc 구조 (0:17–0:18)
- [슬라이드 10]
- “index.adoc은 목차, 스타일 설정, 포함 구문으로 구성됩니다. snippets 속성으로 조각 문서 경로를 넘기는 패턴을 씁니다.”

---

## 10. 테스트 예시 개념 (0:18–0:20)
- [슬라이드 11–12]
- “MockMvc나 RestAssured 테스트에서 andDo(document("…"))로 문서화 지점을 적어 둡니다. 경로 값, 요청/응답 필드를 설명하는 방식입니다.”

## 10-1. document(...) 디테일 (0:20–0:21)
- [슬라이드 10-1]
- 핵심 멘트 순서 제안:
  1) “document 식별자(user-get)는 스니펫 폴더 이름입니다. build/generated-snippets/user-get 아래에 생성돼요.”
  2) “pathParameters, requestParameters, requestParts, requestFields/responseFields로 각각 경로/쿼리/멀티파트/JSON 필드를 설명합니다.”
  3) “필드는 optional(), ignored(), type(JsonFieldType.*), attributes(key("format").value(...))로 더 풍부하게 문서화할 수 있습니다.”
  4) “배열/중첩은 orders[].id, profile.address.city 처럼 경로를 쓰고, 큰 덩어리는 subsectionWithPath("profile")로 한 줄 요약이 가능합니다.”
  5) “preprocessRequest/Response로 prettyPrint, 헤더 제거(removeHeaders)를 적용해 예시를 보기 좋게 만듭니다.”
  6) “relaxedResponseFields는 빠진 필드가 있어도 테스트를 실패시키지 않습니다. 빠른 도입에 좋지만, 놓치는 문서가 생길 수 있어요.”
  7) “Bean Validation 제약은 ConstraintDescriptions로 끌어와 description에 합치면 유지보수가 쉬워집니다.”
- 짧은 예시 멘트:
  - “이 API는 email 형식이고 예시는 attributes로 넣었습니다. roles[]는 배열, profile은 subsection으로 요약했어요.”
- 팁: enum 값은 가능한 값 목록을 설명에 명시하거나 별도 표로 분리, 실패 응답(에러 포맷)은 공통 섹션으로 재사용.

---

## 11. RestAssured 테스트 예시 (0:20)
- [슬라이드 11]
- "RestAssured는 실제 HTTP 경로로 테스트를 수행합니다. RANDOM_PORT로 기동 후 document 필터를 적용해 스니펫을 생성할 수 있습니다."
- 짧은 멘트:
  - "given().filter(document(\"user-get\", pathParameters(...))).when().get(...).then().statusCode(200);"
- 포인트: 실제 네트워크 경로 검증, 필터/보안/헤더까지 현실성 높음.

---

## 12. 조각 문서와 결과 (0:20–0:21)
- [슬라이드 13]
- “테스트 후 http-request.adoc, http-response.adoc, response-fields.adoc 등이 생기고, 그대로 포함하면 표/예시가 문서에 들어갑니다.”

---

## 13. 문서 만들기와 확인 (0:21–0:22)
- [슬라이드 14]
- “CI/CD에서는 test → asciidoctor 순서로 돌리고, build/docs/asciidoc/index.html 결과물을 올리면 리뷰가 쉽습니다.”

---

## 13-1. 테스트 → 스니펫 생성 (단일 테스트도 가능) (0:22)
- [슬라이드 13-1]
- “전체 테스트로 스니펫을 만듭니다: ‘./gradlew.bat test’.”
- “한 클래스만 실행할 수도 있습니다: ‘./gradlew.bat test --tests "org.example.user.UserGetApiDocsTest"’.”
- “한 메서드만 실행도 됩니다: ‘./gradlew.bat test --tests "org.example.user.UserGetApiDocsTest.사용자_조회_문서화"’.”
- “스니펫은 build\\generated-snippets 아래에 생기고, 식별자(예: user-get) 폴더로 정리됩니다.”

다이어그램(말 설명)
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

## 13-2. Asciidoctor → REST Docs HTML (0:23)
- [슬라이드 13-2]
- “index.adoc에서 스니펫을 include 한 뒤 Asciidoctor로 HTML을 만듭니다: ‘./gradlew.bat asciidoctor’.”
- “출력 위치는 build\\docs\\asciidoc\\index.html 입니다.”

다이어그램(말 설명)
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

## 13-3. 배치(배포) 과정 요약 (0:24)
- [슬라이드 13-3]
- “Gradle의 copyRestDocs가 HTML을 정적 리소스 경로로 복사합니다.”
- “bootRun/bootJar가 copyRestDocs에 의존해서, 앱 실행/패키징 전에 문서가 준비됩니다.”
- “실행 후 http://localhost:8080/docs/index.html 에서 바로 볼 수 있습니다.”

다이어그램(말 설명)
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

## 14. 문서 품질/맞춤 팁 (0:22–0:24)
- [슬라이드 15]
- 핵심 멘트:
  - “Bean Validation 제약은 ConstraintDescriptions로 자동 설명을 끌어와 description에 합치면 DTO 변경에 강해집니다.”
  - “Authorization, Correlation-Id 같은 공통 헤더, 공통 에러 포맷은 조각 문서로 분리해 include로 재사용합니다.”
  - “preprocessRequest/Response로 prettyPrint와 헤더 제거를 적용해 예시를 보기 좋게 만듭니다.”
  - “Asciidoctor 테마/하이라이트로 문서 톤과 스타일을 통일합니다.”
- 짧은 코드 멘트:
  - “var cons = new ConstraintDescriptions(UserDto.class); nameDesc = String.join(", ", cons.descriptionsForProperty("name"));”
  - “document(..., preprocessResponse(prettyPrint(), removeHeaders(\"Set-Cookie\")))”
- 체크리스트: enum 값 표, 날짜/통화 format 속성, 실패 응답 공통 스키마, 샘플 데이터 비식별화

---

## 15. CI/CD 연동하기 (0:24)
- [슬라이드 15]
- “CI에서 test → asciidoctor 순서로 돌리고, build/docs/asciidoc/index.html을 아티팩트/페이지로 배포합니다.”
- “GitHub Actions 예시: Test → Generate Docs → Upload artifact (path: build/docs/asciidoc)”
- “BootJar 전에 asciidoctor를 수행해 /docs/index.html로 정적 제공할 수도 있습니다.”

---

## 16. 데모 진행 대본 (0:24–0:30)
- [슬라이드 16]
- “이제 데모 흐름을 정리하겠습니다. 실제 프로젝트에도 그대로 적용할 수 있습니다.”
- [화면 전환: IDE와 터미널]
- “1) 테스트 실행: PowerShell에서 ‘gradlew.bat test’. 실행되면 build\generated-snippets 경로에 조각 문서가 생깁니다.”
- “2) 문서 생성: ‘gradlew.bat asciidoctor’. 결과는 build\docs\asciidoc\index.html 입니다.”
- “3) index.html을 열어 포함된 조각 문서를 확인합니다. 필드를 하나 더 추가하고 다시 실행하면 문서가 자동으로 갱신됩니다.”
- [문제 대비 멘트]
- “조각 문서가 없으면 테스트에 document 호출이 빠졌을 수 있습니다. 경로나 속성은 asciidoctor 작업 inputs/attributes를 확인하세요.”

---

## 15. 좋은 방법 (0:30–0:32)
- [슬라이드 17]
- “성공 응답만이 아니라 4xx/5xx 실패 응답도 문서화해야 운영에 도움이 됩니다. 에러 포맷을 정하고 문서에 고정 섹션으로 넣어 두세요.”

---

## 16. 문제 해결 (0:32–0:33)
- [슬라이드 18]
- “조각 문서 비어 있음, 경로 불일치, 한글 폰트 깨짐, 여러 모듈에서 모으기 등 흔한 문제는 부록 B/C/D를 참고하면 대부분 해결됩니다.”

---

## 17. 참고 링크 (0:33–0:34)
- [슬라이드 19]
- “공식 문서와 샘플 저장소 링크를 드립니다. spring-restdocs-examples가 특히 도움이 됩니다.”

---

## 18. Q&A (0:34–0:38)
- [슬라이드 20]
- “질문 받겠습니다.”
- 예시 답변:
  - “OpenAPI와 차이는? — REST Docs는 테스트 기반 정적 문서, OpenAPI는 실행 중 스키마/도구 중심입니다. 둘을 함께 쓰는 전략이 좋습니다.”
  - “문서 최신 유지? — CI에서 test→asciidoctor를 필수로 돌리고 HTML 결과를 리뷰합니다.”
  - “도입 비용? — 공통 조각 문서/테마를 만들어 재사용하고, 중요한 API부터 단계적으로 적용합니다.”

---

## 19. 마무리 (0:38–0:40)
- “오늘 핵심은 ‘테스트가 문서를 보증한다’ 입니다. 팀의 품질 기준에 문서 생성을 포함하면 문서 빚을 크게 줄일 수 있습니다.”
- “자료와 템플릿은 저장소에 있으니 바로 써 보세요. 감사합니다.”

---

# 부록: 시간 단축/확장 버전 멘트

## A. 30분 단축 버전
- 시작/순서: 2분
- 개념/작동/장단점: 8분 (슬라이드 3–6 묶어 압축)
- 설정/구조/index.adoc: 6분 (슬라이드 7–10)
- 테스트/조각 문서/생성: 6분 (슬라이드 11–14)
- 팁/문제 해결: 4분 (슬라이드 15,18 요약)
- Q&A/마무리: 4분

말하기 팁: 예시 코드는 화면만 보여주고 핵심 문장만 말합니다. 데모는 미리 캡처한 화면으로 대체 가능.

## B. 45분 확장 버전
- 장단점 비교에 조직 맥락/운영 시나리오 추가(5분)
- 데모에서 실패 응답 문서화까지 시연(5분)
- CI/CD 설정(YAML) 설명 확대(3분)

---

# 부록: 데모 진행 멘트 (Windows)

1) “테스트부터 실행하겠습니다. PowerShell에서 ‘./gradlew.bat test’.”
2) “build\\generated-snippets 폴더가 생겼는지 확인. user-get 아래 http-response.adoc 등 확인.”
3) “이제 HTML을 만들겠습니다. ‘./gradlew.bat asciidoctor’.”
4) “build\\docs\\asciidoc\\index.html 더블클릭으로 브라우저 열기.”
5) “필드 하나 추가 후 문서 변화 보기. 테스트의 response-fields에 ‘age’ 추가 → test → asciidoctor 재실행.”
6) 오류 대처: “조각 문서가 안 보이면 document 호출 누락일 수 있습니다. asciidoctor inputs.dir과 attributes snippets 경로 확인.”

---

# 부록: 원격 발표 팁
- 화면 공유 시 터미널 글꼴 18pt 이상, IDE는 프레젠테이션 모드 권장.
- 데모 중단 대비로 스크린샷과 미리 만든 index.html 준비.
- Q&A는 채팅 수합 → 말로 답변 → 링크는 채팅에 바로 공유.


---

## 추가 안내: Asciidoctor/Markdown 설정 (부록 F/G 안내)
- [슬라이드 부록 F] "Asciidoctor 한눈에 보기": 테스트 → 스니펫(.adoc) → index.adoc include → asciidoctor로 HTML 생성 흐름을 15초 정도로 요약 언급합니다.
- [슬라이드 부록 G] "Markdown으로 설정하는 방법 요약":
  - 옵션 A: AsciiDoc 유지 + Markdown 원문 포함(빠르지만 렌더링 X)
  - 옵션 B(권장): Markdown을 CI에서 AsciiDoc으로 변환 후 포함(Pandoc/kramdown-asciidoc)
  - 옵션 C: Markdown 사이트(MkDocs 등) 사용 + REST Docs 예시를 복사/링크
- 자세한 절차는 docs/asciidoctor-setup.md 문서를 발표 후 공유합니다.


---

## 추가 스크립트: 정적 리소스 접근 실패(net::ERR_EMPTY_RESPONSE) 대응
- [문제 상황 멘트]
  - "가끔 Font Awesome이나 Google Fonts 같은 외부 CSS/폰트가 로드되지 않아 net::ERR_EMPTY_RESPONSE가 뜰 수 있습니다."
- [원인 요약]
  - "CDN 차단/불안정, 프록시/방화벽, 잘못된 경로(/docs 기준 상대 경로), CSP/SRI, 리버스 프록시 타임아웃 등이 원인입니다."
- [현장 즉시 대처 순서]
  1) "경로 확인: /docs/index.html 기준이면 링크를 './assets/...'(상대) 또는 '/docs/assets/...'(절대)로 맞춥니다."
  2) "CDN 대신 로컬 자산으로 전환(권장): font-awesome.min.css와 webfonts를 static/docs/assets 아래 복사하고 링크 변경."
  3) "대안: WebJars 사용 → 의존성 'org.webjars:font-awesome:4.7.0' 추가 후 '/webjars/font-awesome/4.7.0/css/font-awesome.min.css'로 링크."
  4) "CDN을 계속 쓰면 integrity/crossorigin 추가, 사내 프록시/방화벽에 도메인 허용, 리버스 프록시 타임아웃 점검."
- [예시 멘트]
  - "지금은 링크를 CDN 대신 './assets/font-awesome/css/font-awesome.min.css'로 바꾸고, assets 폴더를 정적 경로에 함께 배포하겠습니다."
- [체크리스트]
  - "링크 경로 OK? CDN 허용? 가능하면 로컬/웹자르로? 프록시 타임아웃? CSP/SRI 충돌 없는지?"
