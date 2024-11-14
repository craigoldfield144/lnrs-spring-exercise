package com.oldfield.lnrs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LnrsSpringExerciseApplicationTests {

    @Autowired
    private CompanySearchController controller;

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
