package net.xasquatch.document.mapper.builder;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("/WEB-INF/setting.properties")
public class StorageBuilder {

    @Value("${files.context.path}")
    private static String filesContextPath;

    public static final String selectStorageList(Object searchValue, Object currentPage, Object pageLimit) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            if (searchValue == null || searchValue.equals(""))
                whereCase = false;

            //QUERY
            SET("@rownum:=0");
            SELECT("(@rownum:= @rownum + 1) AS rownum, " +
                    "m.no AS no, m.email AS email, m.pwd AS pwd, m.nick_name AS nick_name, " +
                    "");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("storage s ON m.no = s.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("s.url LIKE '" + filesContextPath + "%" + searchValue + "%'");
            }
            ORDER_BY("rownum DESC");
            LIMIT(currentPage + ", " + pageLimit);
        }}.toString();
    }

    public static final String selectStorageListCount(Object searchValue) {
        return new SQL() {{
            //SQL 조건 설정
            boolean whereCase = true;
            if (searchValue == null || searchValue.equals(""))
                whereCase = false;
            //QUERY
            SELECT("COUNT(*)");
            FROM("mbr m");
            RIGHT_OUTER_JOIN("storage s ON m.no = s.mbr_no");
            if (whereCase) {
                WHERE("m.email LIKE '%" + searchValue + "%'");
                OR();
                WHERE("m.nick_name LIKE '%" + searchValue + "%'");
                OR();
                WHERE("s.url LIKE '" + filesContextPath + "%" + searchValue + "%'");
            }
        }}.toString();
    }


}