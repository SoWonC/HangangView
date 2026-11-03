# 한강뷰 (HangangView)

<p align="center">
  <img src="https://github.com/user-attachments/assets/1f13b81b-b138-4858-9b9f-247bb9355df2" width="45%" />
  <img src="https://github.com/user-attachments/assets/1714a306-0954-4cbd-9ffe-ea461eb2398f" width="45%" />
</p>

한강 수자원 데이터를 지도 위에서 실시간으로 볼 수 있게 만든 모니터링 시스템입니다.

## 개요

2023년 10월~11월, 자바 응용 SW 개발자 취업과정 파이널 팀 프로젝트로 처음 만들었습니다.
이후 공간정보 회사에 들어가 일하면서 배운 걸 바탕으로 혼자 다시 손봤습니다.

다리, 댐 수위 같은 공공 수자원 데이터를 한곳에 모아서
브이월드 지도에 표시하는 게 목표였습니다.

## 맡은 부분

지도 화면과 그 뒤 백엔드 처리는 혼자 만들었습니다.

- 브이월드 API 연동, 지도 마커 표시
- 외부 API 호출 → 데이터 정리 → DB 저장 → 화면 전달까지 전체 흐름
- 지도 관련 Controller, Service, Mapper, SQL
- 메인 화면과 마커 팝업

로그인, 공지사항, 관리자 기능은 다른 팀원이 맡았습니다.

## 기술

- Backend: Java, Spring Framework, MyBatis
- DB: PostgreSQL
- Frontend: JSP, HTML, CSS, JavaScript
- 지도: VWorld API
- 환경: IntelliJ, Tomcat, Maven

## 주요 기능

- 다리별 수위를 지도 마커로 표시
- 마커 클릭 시 상세 수위 정보 팝업
- 다리 / 댐 / 강수량 마커를 체크박스로 골라 보기, 전체보기 버튼
- 지도 배경 변경 (일반, 백지도, 야간, 항공사진)

## 리뉴얼하면서 바꾼 것

처음 버전은 코드가 한 파일에 몰려 있었고,
API 응답을 받아서 바로 화면에 뿌리는 방식이라 느렸습니다.

- 코드를 Controller, Service, Mapper로 나눔
- API 응답을 DB에 먼저 저장하고 조회하는 방식으로 변경해서 로딩 속도 개선
- 수위, 댐, 다리 테이블을 따로 나누고 좌표가 어긋나지 않게 다시 설계
