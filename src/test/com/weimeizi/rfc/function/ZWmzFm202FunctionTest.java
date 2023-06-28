package com.weimeizi.rfc.function;

import com.weimeizi.rfc.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: sap_rfc
 * @description: 测试获取ZWmzFm202函数并插入ods_zwmz_fm_202表中
 * @author: licihui
 * @create: 2023-06-01 11:42
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ZWmzFm202FunctionTest {

    @Test
    public void testRun(){
        ZWmzFm202Function fc = new ZWmzFm202Function();
        fc.run();
    }

}