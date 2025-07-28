package dam.serice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dam.dto.DamDto;
import service.ConversionSerice;

@Service
public class DamSerice {

    @Autowired
    ConversionSerice conversionSerice;
    public List<DamDto> DamAll() {
        String jsonData = "";
        List<DamDto> list = new ArrayList<>();
        try {
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/dam/info.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jsonData = response.toString();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("============================="+jsonData);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray contentArray = jsonObject.getJSONArray("content");
                for (int i = 0; i < contentArray.length(); i++) {
                    if(!contentArray.isNull(i)) {
                    JSONObject contentObject = contentArray.getJSONObject(i);
                        String dmobscd = contentObject.getString("dmobscd");
                        String addr = contentObject.getString("addr");
                        String agcnm = contentObject.getString("agcnm");
                        String etcaddr = contentObject.getString("etcaddr");

                        String relon = contentObject.getString("lon");
                        double lon = conversionSerice.conversion(relon);
                        String relat = contentObject.getString("lat");
                        double lat = conversionSerice.conversion(relat);
                        String obsnm = contentObject.getString("obsnm");
                        double pfh = 0;
                        double fldlmtwl = 0;

                        try {
                        pfh = Double.parseDouble(contentObject.getString("pfh"));
                        fldlmtwl = Double.parseDouble(contentObject.getString("fldlmtwl"));
                        }catch (NumberFormatException e) {

                        }


                        if (addr.contains("서울") || addr.contains("경기")) {
                            DamDto damDto = new DamDto(dmobscd, addr, agcnm, etcaddr, fldlmtwl, lon, lat, obsnm, pfh);
                            list.add(damDto);
                        } else {
                        }
                    }else {
                        System.out.println("null =" + i);
                    }

                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



}
