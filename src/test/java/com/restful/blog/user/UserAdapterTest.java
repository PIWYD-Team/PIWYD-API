package com.restful.blog.user;

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
                .id(1)
                .email("userName")
                .password("azerty")
                .build();

        UserDao userDao = userAdapter.userToDao(userDto);

        assertThat(userDao.getIdUser()).isEqualTo(userDto.getId());
        assertThat(userDao.getEmailUser()).isEqualTo(userDto.getEmail());
        assertThat(userDao.getPasswordUser()).isEqualTo(userDto.getPassword());
    }

    @Test
    public void should_return_an_user_dto() {
        UserDao userDao  = UserDao.builder()
                .idUser(1)
                .emailUser("userName")
                .passwordUser("azerty")
                .build();

        UserDto userDto = userAdapter.userToDto(userDao);

        assertThat(userDto.getId()).isEqualTo(userDao.getIdUser());
        assertThat(userDto.getEmail()).isEqualTo(userDao.getEmailUser());
        assertThat(userDto.getPassword()).isEqualTo(userDao.getPasswordUser());
    }
}
