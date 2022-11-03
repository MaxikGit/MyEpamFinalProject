package com.max.restaurant.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UtilsPasswordEncryptionTest {

    @ParameterizedTest
    @ValueSource(strings = {"klimenko", "Klimenko", "dfcz123", "gfhf_фііф12"})
    void testAllPositive(String input) {
        assertDoesNotThrow(() -> {
                    String password = UtilsPasswordEncryption.getNewEncryptedPass(input.toCharArray());
                    for (int i = 0; i < 100; i++) {
                        assertTrue(UtilsPasswordEncryption.authenticate(input.toCharArray(), password));
                    }
                }
        );
    }
    @ParameterizedTest
    @ValueSource(strings = {"klimenko", "Klimenko", "dfcz123", "gfhf_фііф12"})
    void testAllNegative(String input) {
        String wrongInput = input.replaceFirst(Character.toString(input.charAt(0)), Character.toString(input.charAt(0) + 1));
        System.out.println("wrong input - " + wrongInput + " & right input - " + input);
        assertDoesNotThrow(() -> {
                    String passwordInDataBase = UtilsPasswordEncryption.getNewEncryptedPass(input.toCharArray());
                    for (int i = 0; i < 100; i++) {
                        assertFalse(UtilsPasswordEncryption.authenticate(wrongInput.toCharArray(), passwordInDataBase));
                    }
                }
        );
    }
}