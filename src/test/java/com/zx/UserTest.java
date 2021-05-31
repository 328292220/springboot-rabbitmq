package com.zx;

import com.zx.dao.UsersMapper;
import com.zx.model.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class UserTest {
    @Resource
    UsersMapper mapper;
    @Test
    public void insert(){
        Users record = new Users();
        record.setId(2);
        record.setUsername("zhangxin");
        record.setPasswd("123456");
        mapper.insert(record);
    }
}
