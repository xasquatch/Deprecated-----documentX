package net.xasquatch.document.mapper.builder;

import org.apache.ibatis.jdbc.SQL;

public class MemberBuilder {

    public static final String selectMember(Object memberNo) {
        return new SQL() {{
            SET("@rownum:=0");
            SELECT("(@rownum:= @rownum + 1) AS rownum, " +
                    "m.no AS no, m.email AS email, " +
                    "DATE_FORMAT(m.created_date, '%Y.%m.%d %H:%i:%s') AS created_date, " +
                    "m.nick_name AS nick_name, a.name AS auth");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("authorization a ON m.no = a.mbr_no");
            WHERE("m.no = '" + memberNo + "'");
            ORDER_BY("m.no ASC, a.name ASC");
        }}.toString();
    }

    public static final String selectMemberList(Object searchValue, Object currentPage, Object pageLimit) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            if (searchValue == null || searchValue.equals(""))
                whereCase = false;

            //QUERY
            SET("@rownum:=0");
            SELECT("(@rownum:= @rownum + 1) AS rownum, " +
                    "m.no AS no, m.email AS email, " +
                    "DATE_FORMAT(m.created_date, '%Y.%m.%d %H:%i:%s') AS created_date, " +
                    "m.nick_name AS nick_name, a.name AS auth");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("authorization a ON m.no = a.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("a.name LIKE '%" + searchValue + "%'");
            }
            ORDER_BY("m.no ASC, a.name ASC");
            LIMIT(currentPage + ", " + pageLimit);
        }}.toString();
    }

    public static final String selectMemberListCount(Object searchValue) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            if (searchValue == null || searchValue.equals(""))
                whereCase = false;

            //QUERY
            SELECT("COUNT(*)");
            FROM("mbr m");
            LEFT_OUTER_JOIN("authorization a ON m.no = a.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("a.name LIKE '%" + searchValue + "%'");
            }
            GROUP_BY("m.no");
        }}.toString();
    }

    public static final String selectHistoryList(long memberNo) {
        return new SQL() {{
            SELECT("no, mbr_no, destination, " +
                    "REPLACE(ip_address, RIGHT(ip_address, 4),'.***') AS ip_address, " +
                    "DATE_FORMAT(date, '%Y.%m.%d %H:%i:%s') AS date");
            FROM("history");
            WHERE("mbr_no=" + memberNo);
        }}.toString();
    }

}
