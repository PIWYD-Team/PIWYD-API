package com.restful.blog.user;

import com.piwyd.user.UserDao;
import com.piwyd.user.UserRepository;
import com.piwyd.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    private UserDao user1;
    private UserDao user2;
    private UserDao user3;
    private UserDao user4;

    private Date date;

    public void initUser() {
        date = new Date();

        user1 = UserDao.builder()
                .id(1L)
                .idUser(10)
                .emailUser("firstUser")
                .build();

        user2 = UserDao.builder()
                .id(2L)
                .idUser(20)
                .emailUser("secondUser")
                .build();

        user3 = UserDao.builder()
                .id(3L)
                .idUser(30)
                .emailUser("thirdUser")
                .build();

        user4 = UserDao.builder()
                .id(4L)
                .idUser(40)
                .emailUser("fourthUser")
                .build();
    }


    @Before
    public void configureMock() {
        initUser();

        List<UserDao> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        when(userRepository.findAll()).thenReturn(users);
        when(userRepository.save(any(UserDao.class))).thenReturn(user1);
        when(userRepository.findByIdUser(10)).thenReturn(user1);
    }

    @Test
    public void should_get_all_users() {
        
    }
}
