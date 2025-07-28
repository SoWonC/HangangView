package waterGate.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WaterGateData {
    @JsonProperty("wlobscd")
    private String wlobscd;

    @JsonProperty("ymdhm")
    private Date ymdhm;

    @JsonProperty("wl")
    private double wl;

    @JsonProperty("fw")
    private double fw;


}

