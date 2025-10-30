package model.request_service;

import util.DBUtil;
import vo.Requests.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public int createRequest(Request request) {
        String sql = "INSERT INTO Request(uid, r_title, r_content, r_type, r_status, r_createdAt, r_updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 파라미터 바인딩
            pstmt.setInt(1, request.getUid());
            pstmt.setString(2, request.getR_title());
            pstmt.setString(3, request.getR_content());
            pstmt.setString(4, request.getR_type().name());
            pstmt.setString(5, request.getR_status());

            // 실행 후 영향받은 행 수 반환
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                // INSERT 성공 시 생성된 PK를 받아온다.
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    // ResultSet의 첫 컬럼에 생성된 키가 들어있음
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int updateRequest(int uid, int requestID, String title, String content) {
        // uid와 requestID가 모두 일치하는 레코드만 수정
        String sql = "UPDATE Request SET r_title=?, r_content=?, r_updatedAt=NOW() WHERE requestID=? AND uid=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           //DB에 실행할 SQL 문을 실행할 때 값을 바인딩해서 쓰기 위해 함
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, requestID);
            pstmt.setInt(4, uid);

            // executeUpdate = 몇 개의 행이 영향을 받았는지
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteRequest(int uid, int requestID) {
        String sql;
        if (uid == 0) {
            sql = "DELETE FROM Request WHERE requestID=?";
        } else {
            sql = "DELETE FROM Request WHERE requestID=? AND uid=?";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestID);
            if (uid != 0) {
                pstmt.setInt(2, uid);
            }

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 실패 시 0 반환
        return 0;
    }

    public int updateResponse(int requestID, String response) {
        // r_status를 '답변완료'로 변경
        String sql = "UPDATE Request SET r_status='답변완료', r_response=?, r_updatedAt=NOW() WHERE requestID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, response);
            pstmt.setInt(2, requestID);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Request> selectAllRequests(Request.RequestType type) {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT * FROM Request WHERE r_type=? ORDER BY r_createdAt DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // enum을 문자열로 변환해서 바인딩
            pstmt.setString(1, type.name());
            try (ResultSet rs = pstmt.executeQuery()) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 결과가 없으면 빈 리스트 반환
        return list;
    }

    // --- 사용자 본인 문의 조회 (타입별 필터 적용) ---
    public List<Request> selectMyRequest(int uid, Request.RequestType type) {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT * FROM Request WHERE uid=? AND r_type=? ORDER BY r_createdAt DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, uid);
            pstmt.setString(2, type.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- ResultSet → Request 매핑 ---
    private Request mapResultSetToRequest(ResultSet rs) throws SQLException {
        Request req = new Request();

        // 정수형 PK: 기본값으로 0을 가질 수 있음 (DB에 값이 있으면 그 값)
        req.setRequestID(rs.getInt("requestID"));

        // 글 작성자 ID
        req.setUid(rs.getInt("uid"));
        req.setR_title(rs.getString("r_title"));
        req.setR_content(rs.getString("r_content"));
        req.setR_response(rs.getString("r_response"));
        req.setR_status(rs.getString("r_status"));
        String type = rs.getString("r_type"); //DB 테이블의 r_type 컬럼을 가져옴
        if (type != null) req.setR_type(Request.RequestType.valueOf(type)); //valueOf = 문자열을 enum 상수로 변환할 때 쓰는 메소드
        else req.setR_type(Request.RequestType.board);
        Timestamp created = rs.getTimestamp("r_createdAt"); //DB 전용 타입이라 애플리케이션 코드에서 쓰기 불편, 날짜/시간 연산 지원이 거의 없음
        if (created != null) req.setR_createAt(created.toLocalDateTime());
        Timestamp updated = rs.getTimestamp("r_updatedAt");
        if (updated != null) req.setR_updateAt(updated.toLocalDateTime());

        // 매핑 완료된 Request 객체 반환
        return req;
    }
}
