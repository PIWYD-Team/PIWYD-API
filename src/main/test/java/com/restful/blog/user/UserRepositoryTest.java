package com.restful.blog.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@UserData
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void should_get_all_users() {
        List<UserDao> all = userRepository.findAll();

        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    public void should_get_user_by_id_user() {
        int idUser = 1;
        UserDao userbyIdUser = userRepository.findByIdUser(idUser);

        assertThat(userbyIdUser.getIdUser()).isEqualTo(idUser);
        assertThat(userbyIdUser.getNameUser()).isEqualTo("first");
    }
}
