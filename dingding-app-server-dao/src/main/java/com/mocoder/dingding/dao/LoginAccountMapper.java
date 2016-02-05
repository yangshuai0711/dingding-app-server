package com.mocoder.dingding.dao;

import com.mocoder.dingding.model.LoginAccount;
import com.mocoder.dingding.model.LoginAccountCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoginAccountMapper {
    int countByExample(LoginAccountCriteria example);

    int deleteByExample(LoginAccountCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoginAccount record);

    int insertSelective(LoginAccount record);

    List<LoginAccount> selectByExample(LoginAccountCriteria example);

    LoginAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LoginAccount record, @Param("example") LoginAccountCriteria example);

    int updateByExample(@Param("record") LoginAccount record, @Param("example") LoginAccountCriteria example);

    int updateByPrimaryKeySelective(LoginAccount record);

    int updateByPrimaryKey(LoginAccount record);
}