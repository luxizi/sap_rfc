package com.weimeizi.rfc.pojo;

import com.weimeizi.rfc.annotaition.DateFormat;
import com.weimeizi.rfc.annotaition.OdpsEntity;
import com.weimeizi.rfc.annotaition.Partition;
import com.weimeizi.rfc.annotaition.SAPEntity;
import lombok.Data;

/**
 * @program: sap_rfc
 * @description: BDP产品库存数据导出接口pojo
 * @author: licihui
 * @create: 2023-06-01 14:37
 **/
@SAPEntity
@Data
@OdpsEntity("stg_zwmz_fm_dt_05")
public class ZWMZFMDT05 {
    private String MANDT;
    private String MATNR;
    private String MAKTX;
    private String WERKS;
    private String NAME1;
    private String LGORT;
    private String LGOBE;
    private String UMREZ;
    private String EAN11;
    private String GROES;
    private String MEINS;
    private String CHARG;
    private String CLABS;
    private String HSDAT;
    private String O_CLABS;
    private String VMENG;
    private String EMENG;
    private String CINSM;
    private String CSPEM;
    private String ZEIAR;
    private String LBTXT;
    private String WGBEZ;
    private String KASPE;
    @Partition("pt")
    @DateFormat(value = "yyyyMMdd",plusDays = -1,isNow = true)
    private String PT;
}