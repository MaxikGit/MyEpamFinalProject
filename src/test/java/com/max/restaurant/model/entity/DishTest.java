package com.max.restaurant.model.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DishTest {

    public static Stream<Arguments> someCasesOfImagePath() {
        return Stream.of(
                Arguments.of("/views/images/bbq_british_ribs.webp", "bbq_british_ribs.webp"),
                Arguments.of("/views/images/bbq_british_ribs.webp", "/views/images/bbq_british_ribs.webp"),
                Arguments.of("\\views\\images\\bbq_british_ribs.webp", "\\views\\images\\bbq_british_ribs.webp")
        );
    }

    @ParameterizedTest
    @MethodSource("someCasesOfImagePath")
    void setImagePath(String expected, String input) {
        Dish dish = new Dish();
        dish.setImagePath(input);
        String actual = dish.getImagePath();
        Assertions.assertEquals(expected, actual);
    }
}