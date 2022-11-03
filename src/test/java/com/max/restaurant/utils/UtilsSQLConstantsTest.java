package com.max.restaurant.utils;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.management.relation.Role;
import java.util.stream.Stream;

import static com.max.restaurant.utils.UtilsEntityFields.*;

class UtilsSQLConstantsTest {

    public static Stream<Arguments> findByParamCases() {
        return Stream.of(
                Arguments.of(String.format("SELECT * FROM %s WHERE %s=?", USER_TABLE, USER_ID), User.class.getSimpleName(), USER_ID),
                Arguments.of(String.format("SELECT * FROM %s WHERE %s=?", ROLE_TABLE, ROLE_ID), Role.class.getSimpleName(), ROLE_ID),
                Arguments.of(String.format("SELECT * FROM %s WHERE %s=?", CUSTOM_HAS_DISH_TABLE, CUSTOM_COST), CustomHasDish.class.getSimpleName(), CUSTOM_COST)
        );
    }

    public static Stream<Arguments> findAllSomeCases() {
        return Stream.of(
                Arguments.of("SELECT * FROM " + USER_TABLE, User.class.getSimpleName()),
                Arguments.of("SELECT * FROM " + ROLE_TABLE, Role.class.getSimpleName()),
                Arguments.of("SELECT * FROM " + CUSTOM_HAS_DISH_TABLE, CustomHasDish.class.getSimpleName())
        );
    }

    public static Stream<Arguments> insertCases() {
        return Stream.of(
                Arguments.of(String.format("INSERT INTO %s VALUES (%s)", USER_TABLE, "DEFAULT, ?, ?, ?, ?, ?, ?"), User.class.getSimpleName()),
                Arguments.of(String.format("INSERT INTO %s VALUES (%s)",ROLE_TABLE, "DEFAULT, ?"), Role.class.getSimpleName()),
                Arguments.of(String.format("INSERT INTO %s VALUES (%s)", CUSTOM_HAS_DISH_TABLE, "?, ?, ?, ?"), CustomHasDish.class.getSimpleName())
        );
    }

    public static Stream<Arguments> deleteCases() {
        return Stream.of(
                Arguments.of(String.format("DELETE FROM %s WHERE %s=?", USER_TABLE, USER_ID), User.class.getSimpleName(), USER_ID),
                Arguments.of(String.format("DELETE FROM %s WHERE %s=?", USER_TABLE, USER_EMAIL), User.class.getSimpleName(), USER_EMAIL),
                Arguments.of(String.format("DELETE FROM %s WHERE %s=?", CUSTOM_HAS_DISH_TABLE, CUSTOMHASDISH_COUNT), CustomHasDish.class.getSimpleName(), CUSTOMHASDISH_COUNT),
                Arguments.of(String.format("DELETE FROM %s WHERE %s=?", CUSTOM_HAS_DISH_TABLE, CUSTOMHASDISH_C_ID), CustomHasDish.class.getSimpleName(), CUSTOMHASDISH_C_ID),
                Arguments.of(String.format("DELETE FROM %s WHERE %s=?", CUSTOM_HAS_DISH_TABLE, CUSTOMHASDISH_PRICE), CustomHasDish.class.getSimpleName(), CUSTOMHASDISH_PRICE),
                Arguments.of(String.format("DELETE FROM %s WHERE %s=?", ROLE_TABLE, ROLE_ID), Role.class.getSimpleName(), ROLE_ID)
        );
    }

    public static Stream<Arguments> updateCases() {
        return Stream.of(
                Arguments.of(String.format(
                        "UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", USER_TABLE,
                        USER_EMAIL, USER_NAME, USER_LASTNAME, USER_PASSWORD, USER_DETAILS, USER_ROLE_ID, USER_ID),
                        User.class.getSimpleName()),
                Arguments.of(String.format(
                        "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?", CUSTOM_TABLE,
                        CUSTOM_COST, CUSTOM_TIME, CUSTOM_USER_ID, CUSTOM_STATUS_ID, CUSTOM_ID), Custom.class.getSimpleName()),
                Arguments.of(String.format(
                        "UPDATE %s SET %s=?, %s=? WHERE %s=? AND %s=?", CUSTOM_HAS_DISH_TABLE,
                        CUSTOMHASDISH_COUNT, CUSTOMHASDISH_PRICE, CUSTOMHASDISH_C_ID, CUSTOMHASDISH_D_ID), CustomHasDish.class.getSimpleName()),
                Arguments.of(String.format(
                        "UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", DISH_TABLE,
                        DISH_NAME, DISH_PRICE, DISH_DETAILS, DISH_CATEGORY_ID, DISH_IMAGE_PATH, DISH_ID), Dish.class.getSimpleName()),
                Arguments.of(String.format(
                        "UPDATE %s SET %s=? WHERE %s=?", ROLE_TABLE, ROLE_NAME, ROLE_ID), Role.class.getSimpleName()),
                Arguments.of(String.format(
                        "UPDATE %s SET %s=?, %s=? WHERE %s=?", STATUS_TABLE,
                        STATUS_NAME, STATUS_DETAILS, STATUS_ID), Status.class.getSimpleName())
        );
    }

    @ParameterizedTest
    @MethodSource("findAllSomeCases")
    void getSQLFindAll(String expected, String simpleClassName) {
        try {
            String actual = UtilsSQLConstants.getSQLFindAll(simpleClassName);
            Assertions.assertDoesNotThrow(() -> new Exception());
            Assertions.assertEquals(expected, actual);
        } catch (DAOException e) {
            System.out.println(e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("findByParamCases")
    void getSQLFindByParamTest(String expected, String simpleClassName, String inputParamName) {

        try {
            String actual = UtilsSQLConstants.getSQLFindByParam(simpleClassName, inputParamName);
            Assertions.assertEquals(expected, actual);
            Assertions.assertDoesNotThrow(() -> new Exception());
        } catch (DAOException e) {
            System.out.println(e);
        }
    }

    @ParameterizedTest
    @MethodSource("insertCases")
    void getSqlInsertEntity(String expected, String simpleClassName) {

        try {
            String actual = UtilsSQLConstants.getSqlInsertEntity(simpleClassName);
            Assertions.assertEquals(expected, actual);
            Assertions.assertDoesNotThrow(() -> new Exception());
        } catch (DAOException e) {
            System.out.println(e);
        }
    }

    @ParameterizedTest
    @MethodSource("deleteCases")
    void getSQLDeleteByParam(String expected, String simpleClassName, String inputParamName) {
        try {
            String actual = UtilsSQLConstants.getSQLDeleteByParam(simpleClassName, inputParamName);
            Assertions.assertEquals(expected, actual);
            Assertions.assertDoesNotThrow(() -> new Exception());
        } catch (DAOException e) {
            System.out.println(e);
        }
    }

    @ParameterizedTest
    @MethodSource("updateCases")
    void getSQLUpdateEntityById(String expected, String simpleClassName) {
        try {
            String actual = UtilsSQLConstants.getSQLUpdateEntityById(simpleClassName);
            Assertions.assertEquals(expected, actual);
            Assertions.assertDoesNotThrow(() -> new Exception());
        } catch (DAOException e) {
            System.out.println(e);
        }
    }
}