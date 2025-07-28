package dam.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DamDto {


    @JsonProperty("dmobscd")
    private String dmobscd;

    @JsonProperty("addr")
    private String addr;

    @JsonProperty("agcnm")
    private String agcnm;

    @JsonProperty("etcaddr")
    private String etcaddr;

    @JsonProperty("fldlmtwl")
    private double fldlmtwl;

    @JsonProperty("lon")
    private double lon;

    @JsonProperty("lat")
    private double lat;


    @JsonProperty("obsnm")
    private String obsnm;

    @JsonProperty("pfh")
    private double pfh;


}

