package waterGate.serice;

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

import cctv.Cctvdto;
import dao.MemberDao;
import service.ConversionSerice;
import waterGate.dto.WaterGateDto;

@Service
public class WaterGateSerice {

	@Autowired
	MemberDao memberDao;
    @Autowired
    ConversionSerice conversionSerice;

    public List<WaterGateDto> waterGateAll() {
        String jsonData = "";
        List<WaterGateDto> list = new ArrayList<>();
        try {
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/waterlevel/info.json");
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
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray contentArray = jsonObject.getJSONArray("content");
            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject contentObject = contentArray.getJSONObject(i);
                String wlobscd = contentObject.getString("wlobscd");
                String agcnm = contentObject.getString("agcnm");
                String obsnm = contentObject.getString("obsnm");
                String addr = contentObject.getString("addr");
                String etcaddr = contentObject.getString("etcaddr");
                String relon = contentObject.getString("lon");
                double lon = conversionSerice.conversion(relon);
                String relat = contentObject.getString("lat");
                double lat = conversionSerice.conversion(relat);


                String fstnyn = contentObject.getString("fstnyn");
                double gdt = 0;
                double attwl = 0;
                double wrnwl = 0;
                double almwl = 0;
                double srswl = 0;
                double pfh = 0;


                try {
                gdt = Double.parseDouble(contentObject.getString("gdt"));
                attwl = Double.parseDouble(contentObject.getString("attwl"));
                wrnwl = Double.parseDouble(contentObject.getString("wrnwl"));
                almwl = Double.parseDouble(contentObject.getString("almwl"));
                srswl = Double.parseDouble(contentObject.getString("srswl"));
                pfh = Double.parseDouble(contentObject.getString("pfh"));

                }catch (NumberFormatException e) {

                }


                if (addr.contains("서울") || addr.contains("경기")) {
                    if(attwl!=0&&wrnwl!=0&&almwl!=0&&srswl!=0&&pfh!=0) {

                        WaterGateDto waterGateDto = new WaterGateDto(wlobscd, agcnm, obsnm, addr, etcaddr, lon, lat, gdt, attwl, wrnwl, almwl, srswl, pfh, fstnyn);
                        list.add(waterGateDto);
                    }
                } else {
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Cctvdto> cctvAll() {
          List<String>cctvs = memberDao.selectCctvwlobscd();
           String jsonData = "";
           List<Cctvdto> list = new ArrayList<>();
           try {
               URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/waterlevel/info.json");
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
           try {
               JSONObject jsonObject = new JSONObject(jsonData);
               JSONArray contentArray = jsonObject.getJSONArray("content");
               for (int i = 0; i < contentArray.length(); i++) {
                   JSONObject contentObject = contentArray.getJSONObject(i);
                   String wlobscd = contentObject.getString("wlobscd");
                   String addr = contentObject.getString("addr");
                   String relon = contentObject.getString("lon");
                   double lon = conversionSerice.conversion(relon);
                   String relat = contentObject.getString("lat");
                   double lat = conversionSerice.conversion(relat);


                   Cctvdto dto = new Cctvdto(wlobscd, addr, addr, lon, lat);
                 
                 
                 for(String a: cctvs) {
                    String b = a.replaceAll("\\s", "");
                    if(dto.getWlobscd().equals(b)) {
                           list.add(dto);
                        }
              }
                 
     
                   
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
           System.out.println(list);
           return list;
       }
  

}
