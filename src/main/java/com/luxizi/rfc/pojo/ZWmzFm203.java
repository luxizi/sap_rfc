package com.luxizi.rfc.pojo;

import com.luxizi.rfc.annotaition.DateFormat;
import com.luxizi.rfc.annotaition.OdpsEntity;
import com.luxizi.rfc.annotaition.Partition;
import com.luxizi.rfc.annotaition.SAPEntity;

import lombok.Data;

/**
 * @program: sap_rfc
 * @description: 公司调拨查询(STO销售与采购)怕pojo
 * @author: licihui
 * @create: 2023-06-07 12:23
 **/
@SAPEntity
@Data
@OdpsEntity("stg_zwmz_fm_203")
public class ZWmzFm203 {
    private String EBELN;
    private String EBELP;
    private String EBELP_POSNR;
    private String LOEKZ;
    private String MATNR;
    private String BUKRS;
    private String WERKS;
    private String MENGE;
    private String NETWR;
    private String BRTWR;
    private String BSTYP;
    private String BSART;
    private String LIFNR;
    private String BEDAT;
    private String EAN11;
    private String MAKTX;
    private String NAME1;
    private String MBLNR;
    private String MJAHR;
    private String BUDAT;
    private String MENGE_GR;
    private String DMBTR_GR;
    private String BELNR;
    private String GJAHR;
    private String BUDAT_INV;
    private String MENGE_INV;
    private String DMBTR_INV;
    private String MWSBP_INV;
    private String DMBTR_AP;
    private String VBELN_VL;
    private String BLDAT_VL;
    private String MENGE_VL;
    private String VBELN_VF;
    private String BLDAT_VF;
    private String MENGE_VF;
    private String DMBTR_VF;
    private String MWSBP_VF;
    private String DMBTR_AR;
    private String DMBTR_APARDIF;
    private String DMBTR_GRIRDIF;
    private String MENGE_GRDIF;
    private String MENGE_SPDIF;
    private String ZPOST_FLAG;
    @Partition("pt")
    @DateFormat(value = "yyyyMMdd",plusDays = -1,isNow = true)
    private String PT;
}