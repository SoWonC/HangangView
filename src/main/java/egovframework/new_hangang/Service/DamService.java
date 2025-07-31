package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.DamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class DamService {

    @Autowired
    ConversionService conversionService;

    public List<DamDto> DamAll() {
        List<DamDto> list = new ArrayList<>();
        String jsonData = fetchJsonFromApi();

        if (jsonData == null || jsonData.isEmpty()) {
            return list;
        }

        try {
            JSONArray contentArray = new JSONObject(jsonData).getJSONArray("content");

            for (int i = 0; i < contentArray.length(); i++) {
                if (contentArray.isNull(i)) {
//                    System.out.println("null = " + i);
                    continue;
                }

                JSONObject obj = contentArray.getJSONObject(i);
                String addr = obj.optString("addr", "");

                if (!(addr.contains("서울") || addr.contains("경기"))) continue;

                DamDto dto = parseDamDto(obj);
                if (dto != null) list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private String fetchJsonFromApi() {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/dam/info.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null) response.append(line);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private DamDto parseDamDto(JSONObject obj) {
        try {
            String dmobscd = obj.optString("dmobscd", "");
            String addr = obj.optString("addr", "");
            String agcnm = obj.optString("agcnm", "");
            String etcaddr = obj.optString("etcaddr", "");
            String relon = obj.optString("lon", "0");
            String relat = obj.optString("lat", "0");
            String obsnm = obj.optString("obsnm", "");

            double lon = conversionService.conversion(relon);
            double lat = conversionService.conversion(relat);

            double pfh = parseDoubleSafe(obj.optString("pfh", "0"));
            double fldlmtwl = parseDoubleSafe(obj.optString("fldlmtwl", "0"));

            return new DamDto(dmobscd, addr, agcnm, etcaddr, fldlmtwl, lon, lat, obsnm, pfh);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
