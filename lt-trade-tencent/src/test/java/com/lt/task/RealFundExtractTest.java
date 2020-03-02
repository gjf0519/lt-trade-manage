package com.lt.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@SpringBootTest
class RealFundExtractTest {

    @Autowired
    RealFundExtract realFundExtract;

    @Test
    void execute() throws ParseException {
        realFundExtract.execute();
    }
}