package view.notice_view;

import util.input.AdaptersAndHandler.InputHandler;
import util.input.AdaptersAndHandler.BufferedReaderAdapter;
import vo.Requests.Notice;

import java.io.BufferedReader;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NoticeUserView {

    private final InputHandler input; // 사용자 입력 처리 도우미 클래스
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 생성자
    public NoticeUserView() {
        this.input = new InputHandler(
                new BufferedReaderAdapter(
                        new BufferedReader(new java.io.InputStreamReader(System.in))
                )
        );
    }

    public int noticeUserMenu() {
        return input.readInt(
                "\n====================== 공지사항 메뉴 ======================\n" +
                        "1. 전체 공지 조회\n2. 공지 상세 보기\n3. 뒤로가기\n[메뉴 선택]: ",
                1, 3
        );
    }

    // 전체 공지 목록
    public void selectAll(List<Notice> notices) {
        if (notices == null || notices.isEmpty()) {
            System.out.println("공지사항이 없습니다.");
            return;
        }

        // 표 형태로 출력 (ID, 제목, 작성일, 우선순위)
        System.out.printf("%-5s %-30s %-20s %-10s\n", "ID", "제목", "작성일", "우선순위");

        for (Notice n : notices) {
            String createAt = n.getN_createAt() != null ? n.getN_createAt().format(formatter) : "없음";
            System.out.printf("%-5d %-30s %-20s %-10d\n",
                    n.getNoticeID(), n.getN_title(), createAt, n.getN_priority());
        }
    }

    // 단일 상세 공지
    public void selectNotice(Notice n) {
        if (n == null) {
            System.out.println("공지사항이 존재하지 않습니다.");
            return;
        }

        // 상세 정보 출력
        System.out.println("\n=================== 공지 상세 ===================");
        System.out.println("ID: " + n.getNoticeID());
        System.out.println("제목: " + n.getN_title());
        System.out.println("내용: " + n.getN_content());
        System.out.println("작성일: " + (n.getN_createAt() != null ? n.getN_createAt().format(formatter) : "-"));
        System.out.println("수정일: " + (n.getN_updateAt() != null ? n.getN_updateAt().format(formatter) : "-"));
        System.out.println("우선순위: " + n.getN_priority());
    }
    // 공지 ID 입력받기 (삭제/수정/조회 시 사용)
    public int inputNoticeID(String action) {
        return input.readInt(action + "할 공지 ID를 입력해주세요: ", 1, null);

    }
}