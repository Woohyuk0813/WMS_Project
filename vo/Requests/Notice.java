package vo.Requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class Notice {

    private int noticeID; // 공지 ID
    private String n_title; // 공지 제목
    private String n_content; // 공지 내용
    private LocalDateTime n_createAt; // 공지 작성일
    private LocalDateTime n_updateAt; // 공지 수정일
    private int n_priority; // 중요도
    private int mid; // 관리자 ID
}
