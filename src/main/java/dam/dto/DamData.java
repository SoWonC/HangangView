package dam.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DamData {
    @JsonProperty("dmobscd")
    private String dmobscd;

    @JsonProperty("ymdhm")
    private Date ymdhm;

    @JsonProperty("swl")
    private double swl;

    @JsonProperty("inf")
    private double inf;

    @JsonProperty("sfw")
    private double sfw;

    @JsonProperty("ecpc")
    private double ecpc;

    @JsonProperty("tototf")
    private double tototf;


}

