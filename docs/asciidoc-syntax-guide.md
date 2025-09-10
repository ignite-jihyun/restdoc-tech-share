# AsciiDoc 문법 빠른 가이드(쉬운 말 버전)

마지막 수정: 2025-09-10 06:12

이 문서는 AsciiDoc(.adoc) 문서를 빠르게 쓸 수 있도록 자주 쓰는 문법을 예제로 설명합니다. 팀에서 REST Docs와 함께 문서화할 때 바로 참고하세요.

---

## 1) 제목(Heading)과 목차
```adoc
= 문서 제목(레벨 0)
:toc: left
:toclevels: 3

== 1장 제목(레벨 1)
=== 1.1 절 제목(레벨 2)
==== 1.1.1 소절 제목(레벨 3)
```
- 문서 맨 위 `= 제목`은 한 번만 사용합니다.
- `:toc:` 속성을 켜면 자동 목차가 생성됩니다.

---

## 2) 문단/개행/강조
```adoc
일반 문단은 빈 줄로 구분합니다.

줄 바꿈은 스페이스 2개 + 줄바꿈 또는 + 기호를 사용합니다. +
이 줄은 바로 위와 붙습니다.

*굵게*, _기울임_, `코드풍` 강조가 됩니다.
```
- `*굵게*`, `_기울임_`, `` `코드` ``를 지원합니다.

---

## 3) 리스트(목록)
```adoc
- 하이픈으로 순서 없는 목록
* 별표도 가능
** 중첩 목록

. 순서 있는 목록 1
. 순서 있는 목록 2
.. 하위 번호 목록
```

---

## 4) 링크와 이미지
```adoc
URL 링크: https://asciidoctor.org/[]
별칭 링크: https://asciidoctor.org/[Asciidoctor]
상대 경로 링크: docs/asciidoctor-setup.md[설정 가이드]

이미지:
image::images/logo.png[우리 팀 로고, width=200]
```
- 대괄호 안에 링크 텍스트/옵션을 넣습니다.

---

## 5) 코드 블록(소스 하이라이트)
문서 상단에 하이라이터를 켜면 보기 좋아집니다.
```adoc
:source-highlighter: highlightjs
```
언어 지정 예시:
```adoc
[source,java]
----
@RestController
class HelloController {}
----
```
- 언어: `java`, `json`, `http`, `bash`, `yaml` 등

인라인 코드: 백틱(`)으로 감싸기 → `String name`.

---

## 6) 표(Table)
```adoc
[cols="1,2,3", options="header"]
|===
|필드|타입|설명
|id|Number|식별자
|name|String|이름
|email|String|이메일
|===
```
- `options="header"`로 첫 행을 헤더로 표시합니다.

---

## 7) 인용/팁/경고 박스(Admonitions)
```adoc
[NOTE]
====
중요한 참고사항을 적습니다.
====

[TIP]
====
작은 팁을 적습니다.
====

[WARNING]
====
주의해야 할 점을 적습니다.
====
```
- NOTE, TIP, IMPORTANT, WARNING, CAUTION 등을 지원합니다.

---

## 8) 앵커와 내부 링크(교차 참조)
```adoc
[[user-api]]
== 사용자 API

상단으로 돌아가기: <<user-api, 사용자 API 섹션으로>>
```
- `[[id]]`로 앵커를 만들고 `<<id,표시문구>>`로 이동 링크를 만듭니다.

---

## 9) include(다른 문서/스니펫 포함)
```adoc
== 스니펫 포함 예시
:snippets: build/generated-snippets

include::{snippets}/user-get/http-request.adoc[]
include::{snippets}/user-get/http-response.adoc[]
```
- 속성 `{snippets}`를 미리 정의하고 include로 불러옵니다.
- 경로 기준: `baseDirFollowsSourceFile()` 또는 프로젝트 설정에 따라 상대 경로가 달라질 수 있습니다.

---

## 10) 속성(attributes)과 변수 치환
```adoc
:api-version: v1

현재 버전은 {api-version} 입니다.
```
- 문서 상단이나 Gradle `asciidoctor { attributes ... }`에서 정의한 속성은 `{속성이름}`으로 사용합니다.

---

## 11) Callouts(설명 번호 달기)
```adoc
[source,json]
----
{ "name": "Alice" } // <1>
----
<1> 이름 필드 예시
```
- 코드의 `// <1>`과 아래 번호 목록을 연결해 설명할 수 있습니다.

---

## 12) 블록 종류(간단 정리)
```adoc
// 리스팅(포매팅 유지)
[listing]
----
여러 줄 텍스트/명령을 그대로 보여줍니다.
----

// 사이드바
.Sidebar 제목
****
보조 설명을 담습니다.
****

// 예/주의 묶음: 위의 Admonitions 참고
```

---

## 13) REST Docs와 함께 쓰기 팁
- 스니펫 경로 속성: Gradle에서 `attributes 'snippets': file('build/generated-snippets')`로 전달.
- index.adoc에서는 `ifdef::snippets[] ... endif::[]`로 스니펫이 있을 때만 포함하도록 방어 코드를 쓰면 편합니다.
- 표의 컬럼 너비는 `[cols="1,1,3"]`처럼 조정해 설명 컬럼을 넓게 잡으면 가독성이 좋아집니다.
- HTTP 예시를 더 눈에 띄게 하려면 `[source,http]` 블록을 사용하세요.

---

## 14) Markdown과 다른 점(자주 헷갈림)
- 제목 레벨: `=` 개수로 레벨을 표현(문서 첫 줄 `=`는 최상위 제목).
- include/attributes 같은 “조립 기능”이 문법에 내장되어 있어 큰 문서를 관리하기 쉽습니다.
- 표와 경고박스 문법이 풍부합니다.

---

## 15) 더 보기
- 공식 문법 문서: https://docs.asciidoctor.org/asciidoc/latest/
- 변환 엔진(Asciidoctor): https://asciidoctor.org/
- 이 저장소의 설정 가이드: docs/asciidoctor-setup.md
