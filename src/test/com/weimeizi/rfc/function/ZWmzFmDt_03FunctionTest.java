package com.weimeizi.rfc.function;

import com.weimeizi.rfc.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname ZWMZ_FM_DT_03$ZWMZ_FM_DT_03FunctionTest
 * @Description TODO
 * @Date 2023-05-31 10:42
 * @Author by 03126
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ZWmzFmDt_03FunctionTest {



    @Test
    public void testRun(){
        ZWmzFmDt03Function fc = new ZWmzFmDt03Function();
        fc.run();
    }
}
