package com.weimeizi.rfc.pojo;

import com.weimeizi.rfc.annotaition.DateFormat;
import com.weimeizi.rfc.annotaition.OdpsEntity;
import com.weimeizi.rfc.annotaition.Partition;
import com.weimeizi.rfc.annotaition.SAPEntity;
import lombok.Data;

/**
 * @program: sap_rfc
 * @description: 基于批次库存的账龄报表pojo
 * @author: licihui
 * @create: 2023-06-01 13:39
 **/
@SAPEntity
@Data
@OdpsEntity("stg_zwmz_fm_176")
public class ZWMZFM176 {
    private String MATNR;
    private String WERKS;
    private String WNAME1;
    private String CHARG;
    private String LGORT;
    private String LGOBE;
    private String MAKTX;
    private String MTART;
    private String MATKL;
    private String WGBEZ;
    private String MENGE;
    private String MEINS;
    private String DMBTR;
    private String CHGRK;
    private String VFDAT;
    private String LWEDT;
    private String PSDAT;
    private String KLTSD;
    private String PEINH;
    private String F1MG;
    private String F1DB;
    private String F2MG;
    private String F2DB;
    private String F3MG;
    private String F3DB;
    private String F4MG;
    private String F4DB;
    private String F5MG;
    private String F5DB;
    private String F6MG;
    private String F6DB;
    private String F7MG;
    private String F7DB;
    private String VBELN;
    private String POSNR;
    private String LIFNR;
    private String NAME1;
    @Partition("pt")
    @DateFormat(value = "yyyyMMdd",plusDays = -1,isNow = true)
    private String PT;
}