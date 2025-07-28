package waterGate.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WaterGateDto {
    @JsonProperty("wlobscd")
    private String wlobscd;

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

    @JsonProperty("gdt")
    private double gdt;

    @JsonProperty("attwl")
    private double attwl;

    @JsonProperty("wrnwl")
    private double wrnwl;

    @JsonProperty("almwl")
    private double almwl;

    @JsonProperty("srswl")
    private double srswl;

    @JsonProperty("pfh")
    private double pfh;

    @JsonProperty("fstnyn")
    private String fstnyn;

}

