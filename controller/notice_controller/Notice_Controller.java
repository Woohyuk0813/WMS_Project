package controller.notice_controller;

public interface Notice_Controller {
    int createNotice();       // 공지 작성
    int updateNotice();       // 공지 수정
    int deleteNotice();       // 공지 삭제
    void selectAll();         // 전체 공지 조회
    void selectNotice();      // 공지 상세 보기
}

// 실수 방지 – 메소드 이름, 매개변수, 반환 타입이 틀리면 컴파일 에러 발생
// 가독성 향상 – 다른 개발자가 이 메소드가 오버라이드된 것임을 쉽게 알 수 있음