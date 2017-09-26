package com.restful.blog.user;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:user-init.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:user-cleanup.sql")
public @interface UserData {
}
