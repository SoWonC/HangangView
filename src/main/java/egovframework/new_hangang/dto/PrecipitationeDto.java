package egovframework.new_hangang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrecipitationeDto {
    private String rfobscd;
    private String agcnm;
    private String obsnm;
    private String addr;
    private String etcaddr;
    private double lon;
    private double lat;
}
