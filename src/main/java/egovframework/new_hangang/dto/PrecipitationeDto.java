package egovframework.new_hangang.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrecipitationeDto {
    private String addr;
    private String agcnm;
    private String etcaddr;
    private String lat;    // DMS 문자열
    private String lon;    // DMS 문자열
    private String obsnm;
    private String rfobscd;
    private Double lat_dd;  // 십진수
    private Double lon_dd;  // 십진수
}
