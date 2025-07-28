package dam.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DamDatas {

    private String dmobscd;

    //측정일v
    private Date ymdhm;

    //수위v
    private double swl;

    //유입량v
    private double inf;

    //저수량v
    private double sfw;

    //유량v
    private double ecpc;

    //총 방류 유량v
    private double tototf;

    //홍수 우려 수위*
    private double fldlmtwl;

    //수위 제한선*
    private double pfh;
}

