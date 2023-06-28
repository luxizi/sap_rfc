package com.weimeizi.rfc.pojo;

import com.weimeizi.rfc.annotaition.DateFormat;
import com.weimeizi.rfc.annotaition.OdpsEntity;
import com.weimeizi.rfc.annotaition.Partition;
import com.weimeizi.rfc.annotaition.SAPEntity;
import lombok.Data;

/**
 * @program: sap_rfc
 * @description: 公司调拨查询(STO销售与采购)怕pojo
 * @author: licihui
 * @create: 2023-06-07 12:23
 **/
@SAPEntity
@Data
@OdpsEntity("stg_zwmz_fm_204")
public class ZWmzFm204 {
    private String VBELN_VF;
    private String FKART;
    private String VKORG;
    private String VKGRP;
    private String VKBUR;
    private String FKDAT;
    private String KONDA;
    private String BZIRK;
    private String RFBSK;
    private String ERNAM;
    private String KUNAG;
    private String SFAKN;
    private String XBLNR;
    private String FKSTO;
    private String POSNR_VF;
    private String FKIMG;
    private String UMVKZ;
    private String UMVKN;
    private String FKLMG;
    private String NETWR;
    private String PSTYV;
    private String WAVWR;
    private String MWSBP;
    private String WADAT_IST;
    private String VBELN_VL;
    private String POSNR_VL;
    private String MATNR;
    private String WERKS;
    private String LGORT;
    private String AUART;
    private String AUGRU;
    private String BSTDK;
    private String VBELN;
    private String POSNR;
    private String BSTKD;
    private String KONDA_TXT;
    private String BZIRK_TXT;
    private String NAME1;
    private String PSTYV_TXT;
    private String VKGRP_TXT;
    private String VKBUR_TXT;
    private String ARKTX;
    private String WERKS_TXT;
    private String LGORT_TXT;
    private String AUGRU_TXT;
    private String ZAMOUNT4;
    private String ZPRICE4;
    private String ZSJCB;
    private String ZYEAR;
    private String ZMONTH;
    private String ZAMOUNT2;
    private String ZPRICE3;
    private String ZGROES;
    private String ZEAN11;
    private String ZAMOUNT;
    private String ZPRICE1;
    private String ZPRICE2;
    private String ZUNAME;
    private String ZZDOC;
    @Partition("pt")
    @DateFormat(value = "yyyyMMdd",plusDays = -1,isNow = true)
    private String PT;
}