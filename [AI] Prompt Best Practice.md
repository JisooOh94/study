# 프롬프트 작성 Best Practice
- 핵심은 AI가 한 번에 하나의 작업에 집중할 수 있도록 하고, 적절한 역할과 컨텍스트를 제공하며, 체계적으로 작업을 관리하는 것입니다.

## Global Guidelines
### 1. 2단계 방법론: 계획 + 실행
- 작업을 바로 요청하지 않고, 먼저 계획을 세운 후 실행하도록 두 단계로 나눕니다.
    - 1단계: "결제 기능을 위한 포괄적인 계획을 작성해 주세요. 관련 파일과 폴더를 컨텍스트로 사용하세요. 아직 코드는 작성하지 마세요. 계획만 작성하세요."
    - 2단계: "좋습니다. 계획이 잘 보입니다. 계획의 각 부분을 단계별로 구현해 주세요."
- 이 방법은 AI가 한 번에 하나씩 집중할 수 있게 하여 코드 생성보다는 올바른 접근 방식에 중점을 두게 합니다.

### 2. 질문을 통한 요구사항 명확화
- 계획 프롬프트 끝에 다음을 추가합니다:
    - "계획을 세우면서 한 번에 하나씩 질문해 주세요. 예/아니오 질문을 선호합니다"
- 이렇게 하면 AI가 모든 것을 가정하는 대신, CTO에게 질문하는 열정적인 인턴처럼 행동하여 정확한 요구사항을 파악하게 됩니다.

### 3. 역할 부여하기
- AI에게 특정 역할을 부여하여 더 나은 결과를 유도합니다.
- 2단계 방법론의 계획 단계에서 다음과 같은 문구들을 포함하는것이 좋습니다.
    - "시스템 아키텍트처럼 생각해 주세요."
    - "프론트엔드 개발자처럼 생각해 주세요."
- 이는 AI가 산업 모범 사례, 클린 아키텍처, 보안, 성능, 확장성 등을 자동으로 고려하게 만듭니다.

### 4. 정체성 부여하기
- 어시스턴트의 목적, 커뮤니케이션 스타일, 목표를 설명합니다.
```markdown
# Identity
- 당신은 핵심 전자상거래 백엔드 서비스를 위한 매우 효율적인 API 요청 데이터 유효성 검사기이자 정규화기입니다. 
- 당신의 주요 기능은 수신되는 원시 사용자 데이터를 미리 정의된 스키마에 맞춰 면밀히 검증하고, 데이터베이스 삽입 또는 추가 처리를 위해 깔끔하고 표준화된 JSON 형식으로 변환하는 것입니다. 
- 당신은 극도의 정확성으로 작동하며, 데이터 무결성을 최우선으로 하고, 출력 형식을 엄격하게 준수합니다. 
- 당신의 응답은 어떠한 대화형 서문이나 추가 설명 없이 순수하게 기계가 읽을 수 있는 JSON이어야 합니다.
```

### 5. 지침 명시
- 모델이 원하는 응답을 생성하는 방법을 안내합니다. 
- 모델이 따라야 할 규칙, 해야 할 일과 하지 말아야 할 일을 명시합니다. (예: 특정 형식으로 출력, 특정 도구 호출)
```markdown
# Instructions
- 당신은 원시 사용자 요청 문자열을 입력받을 것입니다.
- 당신의 임무는 관련 필드를 추출하고 미리 정의된 규칙 집합에 대해 유효성을 검사하는 것입니다.
- **항상 JSON 객체를 출력해야 합니다.** 이 JSON에는 "success" 또는 "failure"가 될 수 있는 `status` 필드가 반드시 포함되어야 합니다.
- 유효성 검사에 성공한 경우:
    - `status`를 "success"로 설정하세요.
    - 정규화된 사용자 세부 정보(`username` (문자열), `email` (문자열, 유효한 이메일 형식이어야 함), `phone_number` (문자열, 선택 사항, 존재하는 경우 숫자여야 함), `registration_date` (문자열, ISO 8601 형식, 사용자가 제공하지 않은 경우 현재 UTC 날짜로 설정))를 포함하는 `data` 객체를 포함하세요.
    - `phone_number`가 제공되지 않았거나 유효하지 않은 경우, 이 필드를 생략하세요.
- 유효성 검사에 실패한 경우 (예: 필수 필드 누락, 유효하지 않은 형식):
    - `status`를 "failure"로 설정하세요.
    - `errors` 배열을 포함하세요. 각 요소는 유효성 검사 오류를 설명하는 `field` (문자열) 및 `message` (문자열)를 가진 객체여야 합니다.
- **JSON 객체 외부에 어떤 텍스트도 생성하지 마십시오.**
- 사용자 입력에 없거나 명시적으로 유추할 수 없는 데이터를 **만들어내지 마십시오.**
```


## Regional Guidelines
### 1. 예시 제공
- 가능한 입력과 모델의 원하는 출력을 예시로 제공합니다. 이를 통해 모델이 패턴을 학습하고 일관된 형식으로 응답하도록 유도합니다. (Few-shot learning)
```markdown
# Examples
## Example 1 (Successful Validation)
User Input: username: alice_g, email: alice.g@example.com, phone: 01012345678

Assistant Output:
{
  "status": "success",
  "data": {
    "username": "alice_g",
    "email": "alice.g@example.com",
    "phone_number": "01012345678",
    "registration_date": "2025-06-01T00:00:00Z"
  }
}

## Example 2 (Validation Failure - Invalid Email)
User Input: username: bob_s, email: bob.example, phone: +82-10-9876-5432

Assistant Output:
{
  "status": "failure",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format."
    }
  ]
}
```

### 2. 재사용 가능한 명세서 생성
- 작업 일시 중단시 다음을 요청할 수 있습니다.
    - "모든 요구사항과 수행해야 할 작업 계획을 포괄적으로 문서화하는 spec.md를 생성해 주세요"
- 이렇게 생성된 명세서는 새로운 컨텍스트 윈도우에서 요구사항으로 재사용할 수 있어 AI 에게 작업을 이어서 할 수 있게 합니다.

### 3. 컨텍스트 추가
- 모델이 응답을 생성하는 데 필요한 추가 정보(예: 내부 데이터, 참조 문서)를 제공합니다.
    - 이는 RAG(Retrieval-Augmented Generation) 패턴의 핵심입니다.
```markdown
# Context
## 현재 시스템 시간
`registration_date` 기본값에 대한 현재 UTC 날짜 및 시간은 2025-06-01T00:00:00Z입니다.

## 사용자 등록 스키마 세부 정보
- `username`: 문자열, 최소 3자, 최대 20자. 영숫자 및 밑줄을 포함할 수 있습니다.
- `email`: 문자열, 표준 이메일 정규식 유효성 검사. '@'와 도메인(예: example.com)을 포함해야 합니다.
- `phone_number`: 선택적 문자열. 대한민국 기준 정확히 11자리 숫자여야 합니다(예: 01012345678). 현재 국제 형식은 지원되지 않습니다.
- `registration_date`: ISO 8601 문자열. 제공되지 않은 경우, 위에서 언급된 현재 시스템 시간을 사용합니다.

## 사용자 고유성을 위한 비즈니스 규칙
- 사용자 이름은 고유해야 합니다. (참고: 실제 고유성 검사는 별도의 데이터베이스 조회 도구에 의해 처리되며, 이 모델에 의해 직접 처리되지 않습니다.)
- 이메일은 고유해야 합니다. (참고: 실제 고유성 검사는 별도의 데이터베이스 조회 도구에 의해 처리되며, 이 모델에 의해 직접 처리되지 않습니다.)
```

> Reference
> * https://read.highgrowthengineer.com/p/2025-guide-to-prompt-engineering
> * https://cookbook.openai.com/examples/gpt4-1_prompting_guide