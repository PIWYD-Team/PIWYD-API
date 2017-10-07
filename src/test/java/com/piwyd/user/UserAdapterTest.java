package com.piwyd.user;

import com.piwyd.user.UserAdapter;
import com.piwyd.user.UserDao;
import com.piwyd.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserAdapterTest {

    @InjectMocks
    UserAdapter userAdapter;

    @Test
    public void should_return_an_user_dao() {
        UserDto userDto  = UserDto.builder()
                .id(1L)
                .email("userName")
                .password("azerty")
                .build();

        UserDao userDao = userAdapter.userToDao(userDto);

        assertThat(userDao.getId()).isEqualTo(userDto.getId());
        assertThat(userDao.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(userDao.getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    public void should_return_an_user_dto() {
        UserDao userDao  = UserDao.builder()
                .id(1L)
                .email("userName")
                .password("azerty")
                .build();

        UserDto userDto = userAdapter.userToDto(userDao);

        assertThat(userDto.getId()).isEqualTo(userDao.getId());
        assertThat(userDto.getEmail()).isEqualTo(userDao.getEmail());
        assertThat(userDto.getPassword()).isEqualTo(userDao.getPassword());
    }
}
