package com.lt.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gaijf
 * @description
 * @date 2020/4/1
 */
@SpringBootTest
class RealFundDetailAllTest {

    @Autowired
    RealFundDetailAll realFundDetailAll;

    @Test
    void execute() {
        realFundDetailAll.execute();
    }
}