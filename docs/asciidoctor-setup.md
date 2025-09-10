# Asciidoctor 설명과 Markdown으로 설정하는 방법

마지막 수정: 2025-09-10 06:12

## 1) Asciidoctor란?
- Asciidoctor는 AsciiDoc 문서를 HTML/PDF 등으로 변환하는 도구입니다.
- Spring REST Docs는 테스트로 만든 조각 문서(snippets, *.adoc)를 Asciidoctor로 모아 최종 문서를 만듭니다.
- 장점: 가벼움, 버전 관리에 친화적, 포함(include)/속성(attributes) 등 구성 유연, 테마/하이라이트 지원.

이 저장소에서는 Gradle 플러그인(org.asciidoctor.jvm.convert)을 사용해 index.adoc → HTML을 생성합니다.

핵심 흐름
1. 테스트 실행 → build/generated-snippets 아래에 *.adoc 스니펫 생성
2. src/docs/asciidoc/index.adoc에서 include::{snippets}/... 로 스니펫 포함
3. gradlew.bat asciidoctor → build/docs/asciidoc/index.html 생성

### 용어 정리: AsciiDoc vs Asciidoctor
- AsciiDoc: 마크업 언어(.adoc 파일 형식)
- Asciidoctor: AsciiDoc을 HTML/PDF 등으로 변환하는 도구/엔진(Gradle 플러그인 포함)

## 2) Gradle 기본 설정(Asciidoctor)
이미 저장소에 설정되어 있습니다(요약).
```gradle
plugins {
    id 'org.asciidoctor.jvm.convert' version '4.0.2'
}

tasks.named('test') {
    outputs.dir file('build/generated-snippets')
}

asciidoctor {
    inputs.dir file('build/generated-snippets')
    baseDirFollowsSourceFile()
    sources { include 'index.adoc' }
    attributes 'snippets': file('build/generated-snippets')
}
```

## 3) Markdown으로 설정하는 방법 (현실적인 3가지 옵션)
Asciidoctor는 기본적으로 AsciiDoc을 처리합니다. Markdown(.md)을 그대로 변환하려면 별도 변환 단계나 다른 도구를 쓰는 방식이 안전합니다. 아래 3가지 중 팀 상황에 맞는 방법을 고르세요.

### 옵션 A. AsciiDoc 메인(index.adoc)은 유지 + 일부 Markdown을 그대로 포함(원문 유지)
- 방법: AsciiDoc 문서에서 `.md` 파일을 include로 불러오되, Markdown을 AsciiDoc으로 렌더링하지 않고 "원문 코드 블록"처럼 보여줍니다.
- 장점: 추가 도구 설치 없음, 빠르게 기존 .md 참고문서를 함께 배포 가능
- 단점: Markdown이 실제로 렌더링되지 않음(그대로 텍스트/코드 블록)
- 예시(index.adoc):
```adoc
== 부록: 팀 가이드(Markdown 원문 포함)
[listing]
----
include::../guides/team-guide.md[]
----
```

### 옵션 B. 빌드 전에 Markdown → AsciiDoc으로 변환(권장)
- 개념: kramdown-asciidoc 또는 Pandoc으로 .md → .adoc 변환한 뒤, Asciidoctor로 HTML을 만듭니다.
- 장점: 최종 결과가 온전히 Asciidoctor 렌더링을 타므로 스타일/테마/목차 일관성 유지
- 단점: 변환 도구 설치 필요(개발 PC 또는 CI)

실행 방법 예시 1: Pandoc 사용(CI에서 권장)
- 사전: CI 환경에 pandoc 설치
- CI 단계:
```yaml
- name: Convert Markdown to AsciiDoc
  run: |
    pandoc -f gfm -t asciidoc -o src/docs/asciidoc/md-guide.adoc docs/md-guide.md
- name: Generate Docs
  run: ./gradlew asciidoctor
```
- index.adoc에서 변환된 파일 포함:
```adoc
include::md-guide.adoc[]
```

실행 방법 예시 2: kramdown-asciidoc(Ruby Gem)
- 사전: Ruby + `gem install kramdown-asciidoc`
- 로컬/CI 명령:
```powershell
kramdoc --format=GFM --output=src/docs/asciidoc/md-guide.adoc docs/md-guide.md
./gradlew.bat asciidoctor
```

### 옵션 C. Markdown 전용 정적 사이트(MkDocs 등)로 빌드하고, REST Docs 스니펫만 포함
- 개념: 문서는 MkDocs(또는 Docusaurus)로, API 예시/스니펫 파일(.adoc/.adoc 변환된 md)은 링크/삽입으로 연결
- 장점: 팀이 Markdown에 익숙하면 작성이 빠름, 풍부한 테마/검색 기능
- 단점: REST Docs의 .adoc 스니펫을 그대로 Markdown에 렌더링하기 어렵고, 통합 스타일을 맞추려면 추가 작업 필요
- 실무 팁: 스니펫의 HTTP 예시는 Markdown fenced code block으로 복사하여 사용하고, 표는 Markdown 표로 재구성

## 4) 팀 추천 가이드
- 완성도/유지보수 기준: 옵션 B(변환) → 옵션 C → 옵션 A 순으로 추천
- 빠른 시작: 지금처럼 AsciiDoc을 기본으로 사용하고, Markdown 자료는 점진적으로 변환/정리

## 5) 예시: Markdown 파일을 추가하고 변환하여 포함하기
1) Markdown 작성: docs/md-guide.md 생성(팀 가이드, 규칙 등)
2) 변환: CI에서 `pandoc` 또는 `kramdown-asciidoc`으로 `src/docs/asciidoc/md-guide.adoc` 생성
3) 포함: index.adoc에 `include::md-guide.adoc[]` 한 줄 추가

## 6) FAQ
- Q. Asciidoctor가 .md를 바로 변환하나요?
  - A. 기본은 AsciiDoc 전용입니다. .md는 별도 변환 단계를 두는 것이 안전합니다.
- Q. 꼭 변환해야 하나요?
  - A. Markdown을 그대로 쓰려면 MkDocs 같은 Markdown 전용 툴로 사이트를 만들고, REST Docs 스니펫/예시를 적절히 옮겨 적는 방식(옵션 C)을 고려하세요.
- Q. REST Docs 스니펫은 항상 .adoc인가요?
  - A. 네, 테스트에서 생성되는 스니펫은 AsciiDoc 형식입니다. 문서 빌드 체인이 Asciidoctor를 사용할 때 가장 자연스럽습니다.


## 7) 추가 자료
- AsciiDoc 문법 빠른 가이드: docs/asciidoc-syntax-guide.md
