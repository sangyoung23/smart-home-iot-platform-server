# 📘 Smart Home Backend

## 1. 프로젝트 개요
본 프로젝트는 **스마트홈 IoT 데이터 수집 및 통계 처리를 위한 백엔드 서버**입니다.  
센서 데이터 수집, 통계 집계, 배치 처리 기능을 중심으로  
**Spring Boot 기반의 RESTful API 서버**로 구현되었습니다.

---

## 2. 프로젝트 목적

본 백엔드는 실제 서비스 배포를 목표로 한 프로젝트라기보다는,  
**Docker 기반 환경 구성과 Kafka를 활용한 실시간 메시지 처리 흐름을 학습하기 위한 연습용 프로젝트**입니다.

주요 목표는 다음과 같습니다.

- `docker-compose`를 활용하여 하나의 네트워크 환경에서  
  **백엔드(Spring Boot), 데이터베이스, Kafka, 시뮬레이터, 프론트엔드**를 연결하는 구조를 직접 구성
- Kafka를 이용한 **실시간 데이터 수집 및 메시지 처리 흐름** 이해
- 실시간 처리와 배치 처리를 분리하여 **역할에 따른 데이터 처리 구조 설계 경험**
- 실제 IoT / 데이터 수집 시스템에서 활용 가능한 **아키텍처 패턴을 연습하고 검증**

본 프로젝트에서는 실제 IoT 장비 대신 **Java 기반 시뮬레이터**를 통해 센서 데이터를 생성하고,  
해당 데이터를 **Kafka로 전송하여 백엔드에서 이를 수신·처리하는 구조**로 설계하였습니다.

실시간 데이터는 화면 요청 시 즉시 조회되는 용도로 사용되며,  
통계 데이터는 배치 작업을 통해 일정 주기로 집계되도록 구성되어 있습니다.

> 프로젝트 진행 과정에서는 Jira를 활용한 작업 단위 관리와  
> Notion을 통한 설계 및 정리 문서화를 병행했습니다.
<img width="1071" height="788" alt="스크린샷 2026-01-14 오후 4 55 31" src="https://github.com/user-attachments/assets/a9e5d48a-e05d-4ad1-b79d-26ef5cae3e6d" />

---

## 3. 기술 스택
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Batch
- MySQL
- Redis
- MongoDB
- Docker
- Kafka

---

## 4. 주요 기능

### 4.1 Kafka 기반 센서 데이터 수집
- 시뮬레이터에서 Kafka로 전송된 센서 데이터를 **토픽별로 구독**
- 센서 타입(온도, 습도, 배터리 등)에 따라 메시지 분리 처리
- Kafka Consumer를 통한 **비동기 데이터 수신 구조** 구현

---

### 4.2 원본 데이터 저장 (MongoDB)
- Kafka로 수신한 센서 데이터를 **가공 없이 원본 형태로 MongoDB에 저장**
- 대용량 데이터 적재를 고려한 NoSQL 구조 설계
- 추후 재처리 및 통계 집계를 위한 기준 데이터로 활용

---

### 4.3 실시간 데이터 조회 처리 (Redis)
- 화면에서 **실시간 센서 데이터 조회 요청 시 사용**
- Kafka로 수신된 최신 센서 데이터를 Redis에 저장
- 대시보드 및 실시간 조회 API의 **응답 속도 개선**
- 잦은 조회가 발생하는 데이터에 대해 캐시 역할 수행

---

### 4.4 스케줄러 기반 통계 배치 처리
- Spring Scheduler를 통해 **정해진 시간 주기로 배치 작업 실행**
- 스케줄러는 배치 Job을 트리거하는 역할만 수행
- 실제 데이터 집계 로직은 **Spring Batch로 분리**
- MongoDB 원본 데이터를 기준으로 통계 데이터 집계
- 일별 평균 / 최소 / 최대 값 계산 후 MySQL에 저장

---

### 4.5 통계 데이터 조회
- 집계된 통계 데이터를 REST API로 제공
- 기간 / 장치 기준 통계 조회 기능

---

### 4.6 시스템 흐름 요약

```text
[Simulator]
    ↓
[Kafka Topic]
    ↓
[Backend Consumer]
    ↓
[MongoDB (원본 데이터)]
    ↓
[Redis (실시간 데이터)]
    ↓
[Spring Batch]
    ↓
[MySQL (통계 데이터)]
```

### 5. 프로젝트 구조
```text
backend
 ├─ aop            // 공통 관심사 처리 (로깅, 트랜잭션 등)
 ├─ batch          // Spring Batch Job / Step 구성
 ├─ config         // 애플리케이션 및 공통 설정
 ├─ controller     // REST API 엔드포인트
 ├─ document       // Swagger / API 문서 관련 구성
 ├─ domain         // JPA Entity (도메인 모델)
 ├─ dto            // 요청 / 응답 DTO
 ├─ helper         // 공통 유틸리티 및 헬퍼 클래스
 ├─ repository     // JPA Repository
 ├─ security       // 인증 / 인가 관련 설정
 └─ service        // 비즈니스 로직
```

### 6. 정리

본 프로젝트는
Docker 환경 구성, Kafka 기반 실시간 메시지 처리, 배치 기반 통계 처리 구조를 직접 설계하고 구현해보기 위한 학습용 프로젝트입니다.
