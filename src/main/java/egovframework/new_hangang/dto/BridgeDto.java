package egovframework.new_hangang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BridgeDto {
    private String wlobscd;
    private String agcnm;
    private String obsnm;
    private String addr;
    private String etcaddr;
    private double lon;
    private double lat;
    private double gdt;
    private double attwl;
    private double wrnwl;
    private double almwl;
    private double srswl;
    private double pfh;
    private String fstnyn;
}