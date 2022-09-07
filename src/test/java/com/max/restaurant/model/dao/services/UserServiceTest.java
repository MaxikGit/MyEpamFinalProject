package com.max.restaurant.model.dao.services;

import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.management.relation.Role;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.CUSTOM_COST;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    public static Stream<Arguments> emailCases() {
        return Stream.of(
                Arguments.of("user@domain.com", true),
                Arguments.of("user@domain.co.in", true),
                Arguments.of("user.name@domain.com", true),
                Arguments.of("user_name@domain.com", true),
                Arguments.of("username@yahoo.corporate.in", true),
                Arguments.of(".username@yahoo.com", false),
                Arguments.of("username@yahoo.com.", false),
                Arguments.of("username@yahoo..com", false),
                Arguments.of("username@yahoo.c", false),
                Arguments.of("username@yahoo.corporate", false)
        );
    }

    private Method getEmailIsValidTestMethod() throws NoSuchMethodException {
        Method method = UserService.class.getDeclaredMethod("emailIsValid", boolean.class);
        method.setAccessible(true);
        return method;
    }
    @ParameterizedTest
    @MethodSource("emailCases")
    void emailIsValidTest(String input, boolean expected ){
        try {
            Assertions.assertEquals( expected, getEmailIsValidTestMethod().invoke(new UserService(), input) );
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    void findUserByEmail() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void findAllUsers() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }
}