package precipitation.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrecipitationData {

    @JsonProperty("rfobscd")
    private String rfobscd;

    @JsonProperty("ymdhm")
    private Date ymdhm;

    @JsonProperty("rf")
    private Double rf;


}

