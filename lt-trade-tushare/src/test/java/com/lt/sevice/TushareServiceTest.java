package com.lt.sevice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gaijf
 * @description
 * @date 2020/2/24
 */
@SpringBootTest
class TushareServiceTest {

    @Autowired
    TushareService tushareService;

    @Test
    void stockBasic() {
        tushareService.stockBasic();
    }
}