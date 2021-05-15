package net.xasquatch.document.mapper.builder;

import org.apache.ibatis.jdbc.SQL;

public class MemberBuilder {

    public static final String selectMemberList(String searchValue) {
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
                WHERE("m.email LIKE '%memberEmail%'");
                OR();
                WHERE("m.email LIKE '%memberName%'");
                OR();
                WHERE("a.name LIKE '%Auth%'");
            }
            ORDER_BY("rownum DESC");
        }}.toString();
    }

    public static void main(String[] args) {
        String query = MemberBuilder.selectMemberList("test");
        System.out.println(query);
    }
}
