package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.DamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


@Service
public class DamService {

    @Autowired
    ConversionService conversionService; // 위도/경도 변환을 위한 서비스 주입

    // 전체 댐 정보를 가져오는 메서드
    public List<DamDto> DamAll() {
        String jsonData = ""; // API로부터 수신한 JSON 데이터를 담을 변수
        List<DamDto> list = new ArrayList<>(); // 결과로 반환할 댐 정보 리스트

        try {
            // 외부 API 호출 (댐 정보 JSON)
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/dam/info.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 응답을 버퍼로 읽음
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // 응답 내용 조합
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jsonData = response.toString(); // 최종 JSON 문자열
            connection.disconnect(); // 연결 종료
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 콘솔 출력
        }

        // JSON 원본 출력 (디버깅용)
//        System.out.println("=============================" + jsonData);

        try {
            // JSON 파싱
            JSONObject jsonObject = new JSONObject(jsonData) {
            };
            JSONArray contentArray = jsonObject.getJSONArray("content"); // "content" 배열 추출

            for (int i = 0; i < contentArray.length(); i++) {
                if (!contentArray.isNull(i)) {
                    JSONObject contentObject = contentArray.getJSONObject(i);

                    // 각 필드 추출
                    String dmobscd = contentObject.getString("dmobscd"); // 댐 코드
                    String addr = contentObject.getString("addr");       // 주소
                    String agcnm = contentObject.optString("agcnm", "");// 관리 기관명
                    String etcaddr = contentObject.getString("etcaddr"); // 기타 주소
                    String relon = contentObject.getString("lon");       // 경도(문자열)
                    String relat = contentObject.getString("lat");       // 위도(문자열)

                    // 위도, 경도 변환 (ex: 도분초 → 십진수)
                    double lon = conversionService.conversion(relon);
                    double lat = conversionService.conversion(relat);

                    String obsnm = contentObject.getString("obsnm");     // 관측소명
                    double pfh = 0;          // 최고 수위
                    double fldlmtwl = 0;     // 홍수 제한 수위

                    // 수치형 필드 예외처리 포함한 파싱
                    try {
                        pfh = Double.parseDouble(contentObject.getString("pfh"));
                        fldlmtwl = Double.parseDouble(contentObject.getString("fldlmtwl"));
                    } catch (NumberFormatException e) {
                        // 잘못된 숫자 형식이 들어올 경우 무시
                    }

                    // 서울/경기 지역 필터링
                    if (addr.contains("서울") || addr.contains("경기")) {
                        // DTO 객체 생성 및 리스트에 추가
                        DamDto damDto = new DamDto(dmobscd, addr, agcnm, etcaddr, fldlmtwl, lon, lat, obsnm, pfh);
                        list.add(damDto);
                    }

                } else {
                    // null 객체가 포함된 경우 인덱스 출력
                    System.out.println("null =" + i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // JSON 파싱 중 예외 발생 시 출력
        }

        return list; // 최종 결과 리스트 반환
    }

}
