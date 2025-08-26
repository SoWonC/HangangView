package egovframework.new_hangang.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BridgeDto {
    private String addr;
    private String agcnm;
    private String almwl;
    private String attwl;
    private String etcaddr;
    private String fstnyn;
    private String gdt;
    private String lat;    // DMS
    private String lon;    // DMS
    private String obsnm;
    private String pfh;
    private String srswl;
    private String wlobscd;
    private String wrnwl;
    private Double lat_dd;  // 십진수
    private Double lon_dd;  // 십진수

}

