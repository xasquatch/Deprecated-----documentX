package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Member;

import java.util.List;

public interface ManagementMapper {

    List<Member> selectMemberList(Object searchValue, Object currentPage, Object pageLimit);


}
