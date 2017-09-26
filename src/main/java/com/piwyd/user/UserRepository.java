package com.piwyd.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {
    UserDao save(UserDao userDao);

    UserDao findByIdUser(int id);
}
