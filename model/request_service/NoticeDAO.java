package model.request_service;

import vo.Requests.Notice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;

public class NoticeDAO {
    public int createNotice(Notice notice) {
        String sql = "INSERT INTO Notice(n_title, n_content, n_createAt, n_updateAt, n_priority, mid) " +
                "VALUES (?, ?, NOW(), NOW(), ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 파라미터 바인딩
            pstmt.setString(1, notice.getN_title());
            pstmt.setString(2, notice.getN_content());
            pstmt.setInt(3, notice.getN_priority());
            pstmt.setInt(4, notice.getMid());

            // 실행 후 영향받은 행 수 반환
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) return 0;

            // 생성된 PK(noticeID) 값 가져오기
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 공지 수정
    public int updateNotice(Notice notice) {
        String sql = "UPDATE Notice SET n_title = ?, n_content = ?, n_updateAt = NOW(), n_priority = ? " +
                "WHERE noticeID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 수정할 값 설정
            pstmt.setString(1, notice.getN_title());
            pstmt.setString(2, notice.getN_content());
            pstmt.setInt(3, notice.getN_priority());
            pstmt.setInt(4, notice.getNoticeID());

            // 실행 후 성공한 행 수 반환
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 공지 삭제
    public int deleteNotice(int noticeID) {
        String sql = "DELETE FROM Notice WHERE noticeID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noticeID); // 삭제할 공지 ID 바인딩
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 전체 공지 조회
    public List<Notice> selectAll() {
        List<Notice> list = new ArrayList<>();
        String sql = "SELECT * FROM Notice ORDER BY n_priority DESC, n_createAt DESC";
        // 우선순위 높은 공지 먼저 정렬 → 같은 우선순위면 최신순 정렬

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // ResultSet → Notice 객체 변환 후 리스트에 추가
            while (rs.next()) {
                list.add(mapResultSetToNotice(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list; // 결과 없으면 빈 리스트 반환
    }

    // 특정 공지 상세 조회
    public Notice selectNotice(int noticeID) {
        String sql = "SELECT * FROM Notice WHERE noticeID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noticeID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToNotice(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // DB에서 가져온 데이터 → 자바 객체로 변환
    private Notice mapResultSetToNotice(ResultSet rs) throws SQLException {
        Notice notice = new Notice();

        // ResultSet에서 컬럼 읽어서 Notice 객체에 세팅
        notice.setNoticeID(rs.getInt("noticeID"));
        notice.setN_title(rs.getString("n_title"));
        notice.setN_content(rs.getString("n_content"));

        // Timestamp → LocalDateTime 변환
        //DB 전용 타입이라 애플리케이션 코드에서 쓰기 불편, 날짜/시간 연산 지원이 거의 없음
        Timestamp createTs = rs.getTimestamp("n_createAt");
        if (createTs != null) notice.setN_createAt(createTs.toLocalDateTime());

        Timestamp updateTs = rs.getTimestamp("n_updateAt");
        if (updateTs != null) notice.setN_updateAt(updateTs.toLocalDateTime());

        notice.setN_priority(rs.getInt("n_priority"));
        notice.setMid(rs.getInt("mid")); // 작성자 ID

        return notice;
    }
}
