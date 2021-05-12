package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.AuthorizationMapper;
import net.xasquatch.document.model.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorizationDao {

    @Autowired
    private AuthorizationMapper authorizationMapper;

    public List<Authorization> selectByEmail(String email) {
        return authorizationMapper.selectByEmail(email);

    }

    public boolean insertAuthorization(String authority, Long memberNo) {
        return authorizationMapper.insertAuthorization(authority, memberNo) >= 1;
    }
}
