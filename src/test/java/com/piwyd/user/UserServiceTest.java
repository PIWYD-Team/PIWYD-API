package com.piwyd.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;
    private UserEntity user4;

    private Date date;

    public void initUser() {
        date = new Date();

        user1 = UserEntity.builder()
                .id(1L)
                .id(10L)
                .email("firstUser")
                .build();

        user2 = UserEntity.builder()
                .id(2L)
                .id(20L)
                .email("secondUser")
                .build();

        user3 = UserEntity.builder()
                .id(3L)
                .id(30L)
                .email("thirdUser")
                .build();

        user4 = UserEntity.builder()
                .id(4L)
                .id(40L)
                .email("fourthUser")
                .build();
    }


    @Before
    public void configureMock() {
        initUser();

        List<UserEntity> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        when(userRepository.findAll()).thenReturn(users);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user1);
        when(userRepository.findById(10L)).thenReturn(user1);
    }

    @Test
    public void should_get_all_users() {
        
    }
}
