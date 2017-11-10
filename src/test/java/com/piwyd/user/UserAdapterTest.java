package com.piwyd.user;

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
                .id("1")
                .email("userName")
                .password("azerty")
                .build();

        UserEntity userEntity = userAdapter.userToDao(userDto);

        assertThat(userEntity.getId()).isEqualTo(userDto.getId());
        assertThat(userEntity.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(userEntity.getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    public void should_return_an_user_dto() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("userName")
                .password("azerty")
                .build();

        UserDto userDto = userAdapter.userToDto(userEntity);

        assertThat(userDto.getId()).isEqualTo(userEntity.getId());
        assertThat(userDto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(userDto.getPassword()).isEqualTo(userEntity.getPassword());
    }
}
