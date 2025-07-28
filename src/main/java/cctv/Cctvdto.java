package cctv;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cctvdto {
	@JsonProperty("wlobscd")
    private String wlobscd;

	@JsonProperty("etcaddr")
	private String etcaddr;
	  
    @JsonProperty("addr")
    private String addr;

    @JsonProperty("lon")
    private double lon;

    @JsonProperty("lat")
    private double lat;

}
