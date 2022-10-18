package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.DishDAO;
import com.max.restaurant.model.dao.daoimpl.StatusDAO;
import com.max.restaurant.model.dao.daoimpl.UserDAO;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Status;
import com.max.restaurant.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.max.restaurant.utils.UtilsEntityFields.DISH_CATEGORY_ID;
import static com.max.restaurant.utils.UtilsEntityFields.USER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static List<User> entities;
    private static UserDAO mockDAO;
    private static UserService service;

    @BeforeAll
    static void init() throws DAOException, NoSuchFieldException, IllegalAccessException {
        mockDAO = Mockito.mock(UserDAO.class);
        service = new UserService();
        Field daoField = UserService.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(service, mockDAO);
    }

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
        Method method = UserService.class.getDeclaredMethod("emailIsValid", String.class);
        method.setAccessible(true);
        return method;
    }

    @ParameterizedTest
    @MethodSource("emailCases")
    void emailIsValidTest(String input, boolean expected) {
        Assertions.assertDoesNotThrow(() -> Assertions
                .assertEquals(expected, getEmailIsValidTestMethod().invoke(service, input))
        );
    }

    @Test
    void findUserByEmailTest() {
        Connection mockConn = Mockito.mock(Connection.class);
        List<User> users = initlist(1);
        String email = users.get(0).getEmail();
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.getConnection())
                    .thenReturn(mockConn);
            Mockito.when(mockDAO.findObjByParam(Mockito.eq(USER_EMAIL), Mockito.eq(email), Mockito.eq(mockConn)))
                    .thenReturn(users);
            assertEquals(users.get(0), service.findUserByEmail(email));
            assertNull(service.findUserByEmail(email + email));
            assertNull(service.findUserByEmail("wrong" + email));
        });
    }

    @Test
    void findUserByIdTest() {
        User user = initlist(1).get(0);
        int id = user.getId();
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.findObjById(id))
                    .thenReturn(user);
            assertEquals(user, service.findUserById(id));
        });
    }

    @ParameterizedTest
    @ValueSource (ints = {0, -55})
    void findUserByIdFailedTest(int id) {
        assertThrows(DAOServiceException.class, () -> service.findUserById(id));
    }

    @Test
    void insertUserTest() {
        User user = initlist(1).get(0);
        Connection mockConn = Mockito.mock(Connection.class);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.getConnection()).thenReturn(mockConn);
            Mockito.when(mockDAO.insertObj(Mockito.eq(user), Mockito.eq(mockConn)) ).thenReturn(Boolean.TRUE);
            service.insertUser(user);
        });
    }

    @Test
    void insertUserNotInsertedFailedTest() {
        User user = initlist(1).get(0);
        Connection mockConn = Mockito.mock(Connection.class);
        assertDoesNotThrow(() -> {
            Mockito.when(mockDAO.getConnection()).thenReturn(mockConn);
            Mockito.when(mockDAO.insertObj(Mockito.eq(user), Mockito.eq(mockConn)) )
                    .thenReturn(false);
        });
        assertThrows(DAOServiceException.class, () -> service.insertUser(user));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3, -1})
    void insertUserWrongUserRoleIdFailedTest(int roleId) {
        User user = initlist(1).get(0);
        user.setRoleId(roleId);
        assertThrows(DAOServiceException.class, () -> service.insertUser(user));
    }

    @ParameterizedTest
    @ValueSource(strings ={"", "L"})
    @NullSource
    void insertUserWrongUserNameFailedTest(String name) {
        User user = initlist(1).get(0);
        user.setName(name);
        assertThrows(DAOServiceException.class, () -> service.insertUser(user));
    }

    @Test
    void insertUserWrongUserLastNameFailedTest() {
        User user = initlist(1).get(0);
        user.setLastName(null);
        assertThrows(DAOServiceException.class, () -> service.insertUser(user));
        user.setLastName("");
        assertThrows(DAOServiceException.class, () -> service.insertUser(user));
    }

    @ParameterizedTest
    @ValueSource(strings ={"", "1", "L", "hf", "11", "12d", "111"})
    @NullSource
    void insertUserWrongPasswordFailedTest(String pass) {
        User user = initlist(1).get(0);
        user.setPassword(pass);
        assertThrows(DAOServiceException.class, () -> service.insertUser(user));
    }

    private static List<User> initlist(int size) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            User user = new User();
            user.setId(i);
            user.setName("User#" + i);
            user.setLastName("O`User#" + i);
            user.setEmail(String.format("user%d@domain.com", i));
            user.setPassword(user.getName());
            user.setRoleId((int) (Math.random() * size + 1.2));
            users.add(user);
        }
        return users;
    }
}