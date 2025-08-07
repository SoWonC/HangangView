package egovframework.new_hangang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamApiDto {
    private String dmobscd;   // 댐 코드
    private String ymdhm;     // 관측 일시 (예: 20250806) → LocalDate 또는 String 처리 가능
    private double swl;       // 수위
    private double inf;       // 유입량
    private double sfw;       // 저수량
    private double ecpc;      // 유량 (공백 대비 처리 필요)
    private double tototf;    // 총 방류량
}
