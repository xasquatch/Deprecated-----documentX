package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("/WEB-INF/setting.properties")
public class MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    @Value("${password.encryption.salt}")
    private String salt;




}
