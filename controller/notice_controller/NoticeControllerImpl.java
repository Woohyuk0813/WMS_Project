package controller.notice_controller;

import model.request_service.NoticeDAO;
import view.notice_view.NoticeAdminView;
import view.notice_view.NoticeUserView;
import vo.Requests.Notice;
import java.util.List;

public class NoticeControllerImpl implements Notice_Controller {

    private final NoticeAdminView adminView;
    private final NoticeUserView userView;
    private final NoticeDAO dao; // DB 접근 객체 (DAO)
    private final boolean isAdmin; // 현재 모드(관리자 / 사용자) 구분

    // 생성자
    public NoticeControllerImpl(boolean isAdmin) {
        this.adminView = new NoticeAdminView();
        this.userView = new NoticeUserView();
        this.dao = new NoticeDAO();
        this.isAdmin = isAdmin;
    }

    public void run() {
        if (isAdmin) adminMenuLoop();
        else userMenuLoop();
    }

    private void adminMenuLoop() {
        while (true) {
            int choice = adminView.noticeAdminMenu();
            switch (choice) {
                case 1 -> createNotice();
                case 2 -> updateNotice();
                case 3 -> deleteNotice();
                case 4 -> selectAll();
                case 5 -> selectNotice();
                case 6 -> {
                    System.out.println("공지사항 메뉴에서 뒤로갑니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void userMenuLoop() {
        while (true) {
            int choice = userView.noticeUserMenu();
            switch (choice) {
                case 1 -> selectAll();       // 전체 공지 조회
                case 2 -> selectNotice();    // 공지 상세 조회
                case 3 -> {                  // 뒤로가기
                    System.out.println("공지사항 메뉴에서 뒤로갑니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 공지 작성 ---
    @Override
    public int createNotice() {
        Notice notice = adminView.createNotice();   // 뷰에서 입력받아 Notice 객체 생성
        int result = dao.createNotice(notice);      // DAO 통해 DB에 삽입
        System.out.println(result > 0 ? "공지 작성 완료" : "공지 작성 실패");
        return result;
    }

    // --- 공지 수정 ---
    @Override
    public int updateNotice() {
        int noticeID = adminView.inputNoticeID("수정"); // 수정할 공지 ID 입력
        Notice existing = dao.selectNotice(noticeID);   // DB에서 해당 공지 조회
        if (existing == null) {
            System.out.println("공지 존재하지 않음");
            return 0;
        }

        Notice updated = adminView.createNotice();      // 새 공지 입력 받음
        updated.setNoticeID(noticeID);                  // 기존 ID 유지
        int result = dao.updateNotice(updated);         // DB 업데이트 실행
        System.out.println(result > 0 ? "공지 수정 완료" : "공지 수정 실패");
        return result;
    }

    // --- 공지 삭제 ---
    @Override
    public int deleteNotice() {
        int noticeID = adminView.inputNoticeID("삭제"); // 삭제할 공지 ID 입력
        int result = dao.deleteNotice(noticeID);        // DB에서 삭제
        System.out.println(result > 0 ? "공지 삭제 완료" : "공지 삭제 실패");
        return result;
    }

    // --- 전체 공지 조회 ---
    @Override
    public void selectAll() {
        List<Notice> notices = dao.selectAll();         // DB에서 전체 공지 가져오기
        if (isAdmin) adminView.selectAll(notices);      // 관리자 뷰 출력
        else userView.selectAll(notices);              // 사용자 뷰 출력
    }

    // --- 공지 상세 조회 ---
    @Override
    public void selectNotice() {
        int noticeID;
        if (isAdmin) {
            noticeID = adminView.inputNoticeID("조회");
        } else {
            noticeID = dao.selectAll().isEmpty() ? 0 : userView.inputNoticeID("조회");
        }
        Notice notice = dao.selectNotice(noticeID);     // DB에서 단일 공지 조회
        if (isAdmin) adminView.selectNotice(notice);    // 관리자 뷰 출력
        else userView.selectNotice(notice);            // 사용자 뷰 출력
    }
}
