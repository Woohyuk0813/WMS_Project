# 📦 WMS (Warehouse Management System) - Furniture  

## 📖 프로젝트 개요  
WMS(Warehouse Management System)는 **가구(Furniture) 중심의 창고 관리 시스템**으로, 입고·출고, 재고 관리, 창고 스펙 관리, 사용자 권한 분리 등을 지원합니다.  
Java와 MySQL을 기반으로 개발되며, 관리자(Admin), 창고 관리자(Storage Manager), 사업자(Business), 운송자(Transporter), 고객센터(Customer Service) 등 다양한 역할(Role)을 고려한 **확장성 있는 구조**를 목표로 합니다.  

---

## 🎯 프로젝트 목적
- 가구 재고의 **입고/출고 프로세스 자동화**
- 창고별 **용량, 섹션 단위 관리**
- 사용자 권한 기반 접근 제어
- 효율적인 DB 설계를 통한 **SCM(Supply Chain Management) 연계**
- 협업 중심의 Git 브랜치 전략 운영  

---

## ⚙️ 기술 스택
- **Backend**: Java (JDK 17), JDBC  
- **Database**: MySQL 8.0  
- **Version Control**: Git, GitHub (Branch Protection & PR Rule 적용)  
- **Tools**: Maven, ERD(DBML), GitHub Projects  

---

## 🗂️ 데이터베이스 주요 테이블
- **Users**: 사용자 정보 및 권한(Role)  
- **Admin**: 관리자 계정  
- **Warehouse**: 창고 정보 (소/중/대 규모, 섹션 구조 포함)  
- **Item(Furniture)**: 가구 정보 (카테고리, 소재, 브랜드, 생산일자, 평점 등)  
- **Inbound / InboundLine**: 입고 처리 및 상세 내역  
- **Outbound / OutboundLine**: 출고 처리 및 상세 내역  
- **Inventory**: 재고 현황  
- **CustomerCenter**: 고객센터 문의/응답  

---

## 📑 주요 기능
- 회원가입 / 로그인 / 수정 / 탈퇴  
- 관리자(Admin) 로그인 및 회원 관리  
- 창고 등록 / 수정 / 조회  
- 가구(Furniture) 등록 및 상세 정보 관리  
- 입고(Receiving) / 출고(Shipping) 처리  
- 재고 현황 및 통계 조회  
- 고객센터 문의 처리  

---

## 🔗 Git 브랜치 전략
- `main`: 운영 브랜치 (배포용)  
- `develop`: 통합 브랜치 (테스트/머지)  
- `feature/{기능명}`: 기능 단위 개발 브랜치  
- `dev/{이니셜}`: 개인 개발 브랜치 (예: `dev/LKM`)  

### 협업 규칙
1. 각자 **개인 브랜치(dev/이니셜)** 에서 작업  
2. 기능 개발 후 **feature 브랜치**에 merge  
3. **develop 브랜치**에 PR 생성 → 코드 리뷰 후 merge  
4. 최종적으로 `main`에 반영  

---

