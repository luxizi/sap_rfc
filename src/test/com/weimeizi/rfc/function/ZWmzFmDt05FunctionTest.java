package com.weimeizi.rfc.function;

import com.weimeizi.rfc.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: sap_rfc
 * @description: 测试基于批次库存的账龄报表Function
 * @author: licihui
 * @create: 2023-06-01 14:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ZWmzFmDt05FunctionTest {


    @Test
    public void testRun(){
        ZWmzFmDt05Function wmzFmDt05Function = new ZWmzFmDt05Function();
        wmzFmDt05Function.run();
    }
}