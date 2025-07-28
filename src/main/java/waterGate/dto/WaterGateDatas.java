package waterGate.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WaterGateDatas {

    private String wlobscd;


    private Date ymdhm;

    //수위
    private double wl;

    //유량
    private double fw;

    //적정 수위
    private double attwl;

    //경계수위
    private double wrnwl;

    //경보 수위
    private double almwl;

    //가뭄 최저 수위
    private double srswl;

    //홍수 우려 수위
    private double pfh;

    //홍수 여부
    private String fstnyn;

}

