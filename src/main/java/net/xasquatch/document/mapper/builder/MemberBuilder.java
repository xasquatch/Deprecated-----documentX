package net.xasquatch.document.mapper.builder;

import net.xasquatch.document.model.enumulation.AccessRight;
import org.apache.ibatis.jdbc.SQL;

public class MemberBuilder {

    public static final String selectMemberList(AccessRight accessRight, Object searchValue, Object currentPage, Object pageLimit) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            if (searchValue == null || searchValue.equals(""))
                whereCase = false;

            //QUERY
            SET("@rownum:=0");
            SELECT("(@rownum:= @rownum + 1) AS rownum, " +
                    "m.no AS no, m.email AS email, m.pwd AS pwd, m.nick_name AS nick_name, " +
                    "a.name AS authorization");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("authorization a ON m.no = a.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("a.name LIKE '%" + searchValue + "%'");
            }
            ORDER_BY("rownum DESC");
            LIMIT(currentPage + ", " + pageLimit);
        }}.toString();
    }

    public static final String selectMemberListCount(AccessRight accessRight, Object searchValue) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            if (searchValue == null || searchValue.equals(""))
                whereCase = false;
            //QUERY
            SELECT("COUNT(*)");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("authorization s ON m.no = s.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("a.name LIKE '%" + searchValue + "%'");
            }
        }}.toString();
    }

}
