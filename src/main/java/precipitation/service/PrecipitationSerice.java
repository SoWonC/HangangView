package precipitation.service;

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

import precipitation.Dto.PrecipitationDto;
import service.ConversionSerice;

@Service
public class PrecipitationSerice {

    @Autowired
    ConversionSerice conversionSerice;
    public List<PrecipitationDto> precipitationDataAll(){

        String jsonData = "";
        List<PrecipitationDto> list = new ArrayList<>();
        try {
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/rainfall/info.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray contentArray = jsonObject.getJSONArray("content");
            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject contentObject = contentArray.getJSONObject(i);
                String rfobscd = contentObject.getString("rfobscd");
                String agcnm = contentObject.getString("agcnm");
                String obsnm = contentObject.getString("obsnm");
                String addr = contentObject.getString("addr");
                String etcaddr = contentObject.getString("etcaddr");

                String relon = contentObject.getString("lon");
                double lon = conversionSerice.conversion(relon);

                String relat = contentObject.getString("lat");
                double lat = conversionSerice.conversion(relat);


                if (addr.contains("서울") || addr.contains("경기")) {
                    PrecipitationDto precipitationDto = new PrecipitationDto(rfobscd,agcnm,obsnm,addr,etcaddr,lon,lat);
                    list.add(precipitationDto);
                } else {
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }




}
