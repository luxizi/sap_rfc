package com.weimeizi.rfc.function;

import com.weimeizi.rfc.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**abaaba
 * @program: sap_rfc
 * @description: 公司调拨查询(STO销售与采购)函数测试
 * @author: licihui
 * @create: 2023-06-07 14:46
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ZWmzFm203FunctionTest {
    
    @Test
    public void testfc(){
        ZWmzFm203Function fc = new ZWmzFm203Function();
        fc.run();
    }
}