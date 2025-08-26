package egovframework.new_hangang.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DamDto {
    private String addr;
    private String agcnm;
    private String dmobscd;
    private String etcaddr;
    private String fldlmtwl;
    private String lat;    // DMS
    private String lon;    // DMS
    private String obsnm;
    private String pfh;
    private Double lat_dd;  // 십진수
    private Double lon_dd;  // 십진수
}
