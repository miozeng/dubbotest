package com.dubbo.user.core.dao;

import org.springframework.stereotype.Repository;

import com.dubbo.common.dao.BaseDaoImpl;
import com.dubbo.user.api.entity.User;


@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {


}
