package com.luxizi.rfc.pojo;

import com.luxizi.rfc.annotaition.DateFormat;
import com.luxizi.rfc.annotaition.OdpsEntity;
import com.luxizi.rfc.annotaition.Partition;
import com.luxizi.rfc.annotaition.SAPEntity;

import lombok.Data;

/**
 * @Classname ZWMZ_FM_DT_03$ZWMZ_FM_DT_03
 * @Description TODO ZWMZ_FM_DT_03
 * @Date 2021-09-30 12:27
 * @Author by 03126
 */
@SAPEntity
@Data
@OdpsEntity("ods_sap_customer")
public class ZWmzFmDt03 {

    @DateFormat(value = "yyyy-MM-dd")
    private String ERDAT;
    private String KTOKD;
    private String TXT30;
    private String KUNNR;
    private String NAME1;
    private String NAME2;
    private String SORT1;
    private String NAME_CO;
    private String STREET;
    private String TELF2;
    private String STR_SUPPL1;
    private String TEL_NUMBER;
    private String VKORG;
    private String VTEXT;
    private String VKBUR;
    private String BEZEI;
    private String BZIRK;
    private String BZTXT;
    private String VKGRP;
    private String BEZEI1;
    private String KATR1;
    private String VTEXT1;
    private String KDGRP;
    private String KTEXT;
    private String KUKLA;
    private String VTEXT2;
    private String LOEVM;
    private String AUFSD;
    private String ERNAM;
    private String NAME_TEXTC;
    private String NIELS;
    private String BEZEI2; // 控制范围
    private String KONDA;
    private String VTEXT3;
    private String ZZQYFZRXM;
    private String ZZQYFZRBH;
    private String ZZQYFZRDH;
    private String STCEG;
    private String ZZFRLXR;
    private String ZZFRLXFS;
    private String STCD5;
    private String KATR2;
    private String RTEXT;
    private String LZONE;
    private String VTEXT_ZONE;
    private String NODEL;
    @DateFormat(value = "yyyy-MM-dd HH:mm:ss:SSSS",isNow = true)
    @Partition("")
    private String INPUTDAT;
}
