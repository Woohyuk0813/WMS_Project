    package vo.Requests;

    import lombok.Data;
    import java.time.LocalDateTime;

    @Data
    public class    Request {
        private int requestID; // 문의글 ID
        private int uid;       // 사용자 ID
        private String r_title; // 문의 제목
        private String r_content; // 문의 내용
        private String r_response; // 관리자 답변
        private LocalDateTime r_createAt; // 작성일
        private LocalDateTime r_updateAt; // 수정일
        private String r_status; // 답변 상태
        private RequestType r_type; // 문의 유형

        public enum RequestType {
            board, onetoone
        }
    }