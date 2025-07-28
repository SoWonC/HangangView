package hacheon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HacheonDetailList {
    // 하천명
    private String hacheonName;

    // 하천코드
    private String hacheonCode;

    // 하천등급
    private String hacheonGrade;

    // 기점 위치 시도
    private String startLocationProvince;

    // 기점 위치 시군구
    private String startLocationDistrict;

    // 기점 위치 읍면동
    private String startLocationTown;

    // 기점 위치 경계
    private String startLocationBoundary;

    // 기점 계획 빈도
    private String startPlanFrequency;

    // 기점 계획 홍수량
    private String startPlanFloodVolume;

    // 기점 계획 홍수위
    private String startPlanFloodLevel;

    // 기점 계획 하폭
    private String startPlanWidth;

    // 종점 위치 시도
    private String endLocationProvince;

    // 종점 위치 시군구
    private String endLocationDistrict;

    // 종점 위치 읍면동
    private String endLocationTown;

    // 종점 위치 경계
    private String endLocationBoundary;

    // 종점 계획 빈도
    private String endPlanFrequency;

    // 종점 계획 홍수량
    private String endPlanFloodVolume;

    // 종점 계획 홍수위
    private String endPlanFloodLevel;

    // 종점 계획 하폭
    private String endPlanWidth;

    // 하천연장 계
    private String riverLength;

    // 하천연장 하천기본계획 수립구간 고시일
    private String riverPlanDeclaredDate;

    // 하천연장 하천기본계획 수립구간 연장
    private String riverPlanExtension;

    // 하천연장 하천기본계획 미수립구간
    private String riverPlanUnestablished;

    // 유로연장
    private String euroLength;

    // 유역면적
    private String drainageArea;

    // 하천정비현황 합계
    private String riverRemedyTotal;

    // 하천정비현황 제방정비완료구간
    private String riverRemedyCompletionZone;

    // 하천정비현황 제방보강필요구간
    private String riverRemedyReinforcementZone;

    // 하천정비현황 제방신설필요구간
    private String riverRemedyCreationZone;

    // 하천지정근거및일자
    private String riverDesignationBasisDate;

   
}
