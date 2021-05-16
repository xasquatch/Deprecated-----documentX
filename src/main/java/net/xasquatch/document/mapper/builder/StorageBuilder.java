package net.xasquatch.document.mapper.builder;

import net.xasquatch.document.model.Member;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("/WEB-INF/setting.properties")
public class StorageBuilder {

    //TODO: 임시방편으로 값 넣어줌 수정 필요
    @Value("${files.context.path}")
    private static final String filesContextPath = "/storage";

    public static final String selectStorageList(Member member, Object searchValue, Object currentPage, Object pageLimit) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            String userDir = "";
            if (searchValue == null || searchValue.equals("")) whereCase = false;
            userDir = "/" + member.getNo();

            //QUERY
            SET("@rownum:=0");
            SELECT("(@rownum:= @rownum + 1) AS rownum, " +
                    "s.no AS no, s.mbr_no AS mbr_no, " +
                    "m.nick_name AS mbr_nick_name, s.type AS dataType, " +
                    "s.url AS url, s.date AS date");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("storage s ON m.no = s.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
            }
            WHERE("s.url LIKE '" + filesContextPath + userDir + "/%" + searchValue + "%'");
            ORDER_BY("rownum DESC");
            LIMIT(currentPage + ", " + pageLimit);
        }}.toString();
    }

    public static final String selectStorageListCount(Member member, Object searchValue) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            String userDir = "";

            if (searchValue == null || searchValue.equals("")) whereCase = false;
            userDir = "/" + member.getNo();

            //QUERY
            SELECT("COUNT(*)");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("storage s ON m.no = s.mbr_no");

            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
            }
            WHERE("s.url LIKE '" + filesContextPath + userDir + "/%" + searchValue + "%'");
        }}.toString();
    }

    public static final String selectStorageListForManageMent(Member member, Object searchValue, Object currentPage, Object pageLimit) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            String userDir = "";
            if (searchValue == null || searchValue.equals("")) whereCase = false;
            userDir = "/" + member.getNo();

            //QUERY
            SET("@rownum:=0");
            SELECT("(@rownum:= @rownum + 1) AS rownum, " +
                    "s.no AS no, s.mbr_no AS mbr_no, " +
                    "m.nick_name AS mbr_nick_name, s.type AS dataType, " +
                    "s.url AS url, s.date AS date");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("storage s ON m.no = s.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("s.url LIKE '" + filesContextPath + "/%" + searchValue + "%'");

            }
            ORDER_BY("rownum DESC");
            LIMIT(currentPage + ", " + pageLimit);
        }}.toString();
    }

    public static final String selectStorageListForManageMentCount(Member member, Object searchValue) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            String userDir = "";

            //QUERY
            SELECT("COUNT(*)");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("storage s ON m.no = s.mbr_no");

            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("s.url LIKE '" + filesContextPath + "/%" + searchValue + "%'");
            }
        }}.toString();
    }
}