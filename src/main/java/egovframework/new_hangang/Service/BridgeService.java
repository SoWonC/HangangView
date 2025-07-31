package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.BridgeDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class BridgeService {

    @Autowired
    ConversionService conversionService;

    public List<BridgeDto> BridgeAll() {
        List<BridgeDto> list = new ArrayList<>();
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

                BridgeDto dto = parseBridgeDto(obj);
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
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/waterlevel/info.json");
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

    private BridgeDto parseBridgeDto(JSONObject obj) {
        try {
            String wlobscd = obj.optString("wlobscd", "");
//            String dmobscd = obj.optString("dmobscd", "");
            String addr = obj.optString("addr", "");
            String agcnm = obj.optString("agcnm", "");
            String etcaddr = obj.optString("etcaddr", "");
            String relon = obj.optString("lon", "0");
            String relat = obj.optString("lat", "0");
            String obsnm = obj.optString("obsnm", "");
            String fstnyn = obj.optString("fstnyn", "");
            double gdt   = Double.parseDouble(obj.optString("gdt", "0"));
            double attwl = Double.parseDouble(obj.optString("attwl", "0"));
            double wrnwl = Double.parseDouble(obj.optString("wrnwl", "0"));
            double almwl = Double.parseDouble(obj.optString("almwl", "0"));
            double srswl = Double.parseDouble(obj.optString("srswl", "0"));
            double pfh   = Double.parseDouble(obj.optString("pfh", "0"));
            double lon = conversionService.conversion(relon);
            double lat = conversionService.conversion(relat);

            return new BridgeDto(wlobscd,agcnm,obsnm,addr,etcaddr,lon,lat,gdt,attwl,wrnwl,almwl,srswl,pfh,fstnyn);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
