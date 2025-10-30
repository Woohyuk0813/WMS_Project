package controller.request_controller;

import controller.notice_controller.NoticeControllerImpl;
import model.request_service.RequestDAO;
import view.request_view.RequestAdminView;
import view.request_view.RequestUserView;
import vo.Requests.Request;
import vo.Requests.Request.RequestType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Request_Controller {

    private final RequestUserView userView; // 사용자 화면 (View 객체)
    private final RequestAdminView adminView; // 관리자 화면 (View 객체)
    private final RequestDAO dao; // DB 연동 DAO
    private final int userId; // 현재 사용자의 ID
    private final boolean isAdmin; // 관리자 여부
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // 생성자
    public Request_Controller(int userId, boolean isAdmin) {
        userView = new RequestUserView();
        adminView = new RequestAdminView();
        dao = new RequestDAO();

        // 전달받은 사용자 ID와 관리자 여부를 필드에 저장
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public void run() throws IOException {
        if (isAdmin) adminMenuLoop();
        else userMenuLoop();
    }

    // --- 관리자 메인 메뉴 루프 ---
    private void adminMenuLoop() throws IOException {
        while (true) {
            int mainChoice = adminView.mainMenu(); // 1=문의관리, 2=공지사항 관리, 3=뒤로가기
            switch (mainChoice) {
                case 1 -> requestMenuLoop();
                case 2 -> {
                    NoticeControllerImpl noticeController = new NoticeControllerImpl(true); // 관리자 권한으로 관리자모드 활성화
                    noticeController.run();
                }
                case 3 -> {
                    System.out.println("뒤로갑니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 사용자 메인 메뉴 루프 ---
    private void userMenuLoop() throws IOException {
        while (true) {
            int mainChoice = userView.mainMenu(); // 예: 1=문의하기, 2=공지사항, 3=뒤로가기
            switch (mainChoice) {
                case 1 -> userRequestLoop();
                case 2 -> {
                    NoticeControllerImpl noticeController = new NoticeControllerImpl(false); // 사용자 모드
                    noticeController.run();
                }
                case 3 -> {
                    System.out.println("뒤로갑니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 관리자 문의 관리 메뉴
    private void requestMenuLoop() throws IOException {
        while (true) {
            //1:1문의 관리 / 문의게시판 관리 / 뒤로가기
            int choice = adminView.requestMenu();
            switch (choice) {
                case 1 -> oneToOneAdminLoop();
                case 2 -> boardAdminLoop();
                case 3 -> { return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 관리자 1:1 문의 관리 루프 ---
    private void oneToOneAdminLoop() throws IOException {
        while (true) {
            // 조회 / 답변 등록/수정 / 뒤로가기
            int choice = adminView.oneToOneMenu();
            switch (choice) {
                case 1 -> {
                    List<Request> list = dao.selectAllRequests(RequestType.onetoone); // 모든 게시판 글(onetoone 타입) 조회 및 출력
                    adminView.selectAllRequests(list); // 관리자 뷰에서 행단위로 형태로 출력
                }
                case 2 -> {
                    Request req = adminView.updateResponse();
                    if (req != null) {
                        if (dao.updateResponse(req.getRequestID(), req.getR_response()) > 0)
                            System.out.println("답변 등록/수정 완료");
                        else System.out.println("답변 등록/수정 실패");
                    }
                }
                case 3 -> { return; }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 관리자 게시판 관리    ---
    private void boardAdminLoop() throws IOException {
        while (true) {
            // 모든 문의 조회 / 답변 등록 / 특정 문의 삭제 / 뒤로가기
            int choice = adminView.requestBoardMenu();
            switch (choice) {
                case 1 -> {
                    // 모든 게시판 글(board 타입) 출력
                    List<Request> list = dao.selectAllRequests(RequestType.board);
                    adminView.selectAllRequests(list);
                }
                case 2 -> {
                    Request req = adminView.updateResponse();
                    if (req != null) {
                        if (dao.updateResponse(req.getRequestID(), req.getR_response()) > 0) // 영향받은 행 수 반환
                            System.out.println("답변 등록/수정 완료");
                        else System.out.println("답변 등록/수정 실패");
                    }
                }
                case 3 -> {
                    int id = adminView.deleteRequest();
                    if (id > 0) {
                        if (dao.deleteRequest(0, id) > 0)
                            System.out.println("삭제 완료");
                        else System.out.println("삭제 실패");
                    }
                }
                case 4 -> { return; } // 뒤로가기
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 사용자 문의 메뉴 ---
    private void userRequestLoop() throws IOException {
        while (true) {
            int choice = userView.requestMenu();
            switch (choice) {
                case 1 -> requestBoardLoop();
                case 2 -> oneToOneLoop();
                case 3 -> { return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // --- 사용자 게시판 ---
    private void requestBoardLoop() throws IOException {
        while (true) {
            int choice = userView.requestBoardMenu();
            switch (choice) {
                case 1 -> {
                    Request req = userView.createRequest();
                    req.setUid(userId); // 현재 로그인한 사용자의 ID로 설정
                    req.setR_type(RequestType.board);
                    int id = dao.createRequest(req);
                    System.out.println(id > 0 ? "등록 완료, ID: " + id : "등록 실패");
                }
                case 2 -> {
                    int id = userView.updateRequest();
                    if (id > 0) {
                        System.out.print("제목: ");
                        String title = reader.readLine();
                        System.out.print("내용: ");
                        String content = reader.readLine();
                        if (dao.updateRequest(userId, id, title, content) > 0)
                            System.out.println("수정 완료");
                        else System.out.println("수정 실패");
                    }
                }
                case 3 -> userView.selectAllRequest(dao.selectAllRequests(RequestType.board));
                // 전체 글 조회 : DAO에서 리스트를 가져와 뷰에 출력

                case 4 -> userView.selectMyRequest(dao.selectMyRequest(userId, RequestType.board));
                // 내 글 조회 : 뷰에 출력

                case 5 -> { //뷰에서 삭제할 ID를 받고, 본인 id로 삭제 요청
                    int id = userView.deleteRequest();
                    if (id > 0) {
                        if (dao.deleteRequest(userId, id) > 0)
                            System.out.println("삭제 완료");
                        else System.out.println("삭제 실패");
                    }
                }
                default -> { return; } // 메뉴 범위를 벗어나는 경우(예: 6=뒤로가기 처리) — 이 구현에서는 default가 뒤로가기로 쓰임
            }
        }
    }

    // --- 사용자 1:1 문의 루프 ---
    private void oneToOneLoop() throws IOException {
        while (true) {
            int choice = userView.oneToOneMenu();
            switch (choice) {
                case 1 -> {
                    Request req = userView.createRequest();
                    req.setUid(userId);
                    req.setR_type(RequestType.onetoone);
                    int id = dao.createRequest(req);
                    System.out.println(id > 0 ? "등록 완료, ID: " + id : "등록 실패");
                }
                case 2 -> {
                    int id = userView.updateRequest();
                    if (id > 0) {
                        System.out.print("제목: ");
                        String title = reader.readLine();
                        System.out.print("내용: ");
                        String content = reader.readLine();
                        if (dao.updateRequest(userId, id, title, content) > 0)
                            System.out.println("수정 완료");
                        else System.out.println("수정 실패");
                    }
                }
                case 3 -> userView.selectMyRequest(dao.selectMyRequest(userId, RequestType.onetoone)); // 내 1:1 문의 조회

                case 4 -> {
                    int id = userView.deleteRequest();
                    if (id > 0) {
                        if (dao.deleteRequest(userId, id) > 0)
                            System.out.println("삭제 완료");
                        else System.out.println("삭제 실패");
                    }
                }
                default -> { return;
                }
            }
        }
    }
}
