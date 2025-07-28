package precipitation.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrecipitationDto {

    @JsonProperty("rfobscd")
    private String rfobscd;

    @JsonProperty("agcnm")
    private String agcnm;

    @JsonProperty("obsnm")
    private String obsnm;

    @JsonProperty("addr")
    private String addr;

    @JsonProperty("etcaddr")
    private String etcaddr;

    @JsonProperty("lon")
    private double lon;

    @JsonProperty("lat")
    private double lat;



}

