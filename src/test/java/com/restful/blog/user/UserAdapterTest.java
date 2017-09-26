package com.restful.blog.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by rfruitet on 11/08/2017.
 */
@RunWith(SpringRunner.class)
public class UserAdapterTest {

    @InjectMocks
    UserAdapter userAdapter;

    @Test
    public void should_return_an_user_dao() {
        UserDto userDto  = UserDto.builder()
                .idUser(1)
                .nameUser("userName")
                .build();

        UserDao userDao = userAdapter.userToDao(userDto);

        assertThat(userDao.getIdUser()).isEqualTo(userDto.getIdUser());
        assertThat(userDao.getNameUser()).isEqualTo(userDto.getNameUser());
    }

    @Test
    public void should_return_an_user_dto() {
        UserDao userDao  = UserDao.builder()
                .idUser(1)
                .nameUser("userName")
                .build();

        UserDto userDto = userAdapter.userToDto(userDao);

        assertThat(userDto.getIdUser()).isEqualTo(userDao.getIdUser());
        assertThat(userDto.getNameUser()).isEqualTo(userDao.getNameUser());
    }
}
