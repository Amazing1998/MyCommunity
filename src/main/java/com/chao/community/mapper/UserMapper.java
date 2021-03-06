package com.chao.community.mapper;

import com.chao.community.POJO.GitHubUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where account_id =#{accountId}")
    public GitHubUser queryById(String accountId);

    @Select("select * from user where token =#{token}")
    public GitHubUser queryByToken(String token);

    @Insert("insert into user(id,account_id,name,token,gmt_create,gmt_modified,head_url) values (#{id},#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insertUser(GitHubUser gitHubUser);
}
