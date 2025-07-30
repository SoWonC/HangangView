package egovframework.new_hangang.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DamDto {
    private String dmobscd;
    private String addr;
    private String agcnm;
    private String etcaddr;
    private double fldlmtwl;
    private double lon;
    private double lat;
    private String obsnm;
    private double pfh;
}