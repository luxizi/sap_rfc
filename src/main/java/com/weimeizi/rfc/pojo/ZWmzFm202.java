package com.weimeizi.rfc.pojo;

import com.weimeizi.rfc.annotaition.DateFormat;
import com.weimeizi.rfc.annotaition.OdpsEntity;
import com.weimeizi.rfc.annotaition.Partition;
import com.weimeizi.rfc.annotaition.SAPEntity;
import lombok.Data;

/**
 * @program: sap_rfc
 * @description: ZWmzFm202函数实体
 * @author: licihui
 * @create: 2023-06-01 11:11
 **/
@SAPEntity
@Data
@OdpsEntity("stg_zwmz_fm_202")
public class ZWmzFm202 {

    private String MATNR;
    private String MAKTX;
    private String WERKS;
    private String NAME1;
    private String LGORT;
    private String BWART;
    private String BTEXT;
    private String SOBKZ;
    private String MBLNR;
    private String ZEILE;
    private String BUDAT;
    private String ERFMG;
    private String ERFME;
    private String ANLN1;
    private String ANLN2;
    private String APLZL;
    private String AUFNR;
    private String AUFPL;
    private String BKTXT;
    private String BLDAT;
    private String BPMNG;
    private String BPRME;
    private String BSTME;
    private String BSTMG;
    private String BUKRS;
    private String BWTAR;
    private String CHARG;
    private String CPUDT;
    private String CPUTM;
    private String DMBTR;
    private String EBELN;
    private String LONGNUM;
    private String EBELP;
    private String EXBWR;
    private String EXVKW;
    private String GRUND;
    private String KDAUF;
    private String KDEIN;
    private String KDPOS;
    private String KOSTL;
    private String KUNNR;
    private String KZBEW;
    private String KZVBR;
    private String KZZUG;
    private String LIFNR;
    private String MAT_KDAUF;
    private String MAT_KDPOS;
    private String MEINS;
    private String MENGE;
    private String MJAHR;
    private String NPLNR;
    private String VORNR;
    private String PSPID;
    private String RSNUM;
    private String RSPOS;
    private String SGTXT;
    private String SHKZG;
    private String USNAM;
    private String VGART;
    private String VKWRT;
    private String WAERS;
    private String WBXTCZR;
    private String WBXTDJH;
    private String WBXTHH;
    private String WBXTRQ;
    private String XABLN;
    private String XAUTO;
    private String XBLNR;
    private String MAA_URZEI;
    private String XMACC;
    @Partition("pt")
    @DateFormat(value = "yyyyMMdd",plusDays = -1,isNow = true)
    private String PT;

}
