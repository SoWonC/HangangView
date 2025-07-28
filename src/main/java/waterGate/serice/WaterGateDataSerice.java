package waterGate.serice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import waterGate.dto.WaterGateData;
import waterGate.dto.WaterGateDatas;
import waterGate.dto.WaterGateDto;

@Service
public class WaterGateDataSerice {

    public List<WaterGateData> WaterDatain(String wlobscdin, int sd, String startDate, String endDate ){
            String pattern = "\\D";
            startDate = startDate.replaceAll(pattern, "");
            endDate = endDate.replaceAll(pattern, "");
            startDate = startDate.substring(0, startDate.length() - 1) + "0";
            endDate = endDate.substring(0, endDate.length() - 1) + "0";
            String date = "";
             if (sd == 1){
                date = "10m";
             } else if (sd == 2) {
                 date = "1H";
             } else if (sd == 3) {
                date = "1D";
             }

            if(startDate.equals("0")||endDate.equals("0")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDateTime currentDateTime = LocalDateTime.now();
            int currentMinute = currentDateTime.getMinute();
            int adjustedMinute = currentMinute - (currentMinute % 10) - 10;
            if (adjustedMinute < 0) {
                adjustedMinute = 0;
            }
            currentDateTime = currentDateTime.withMinute(adjustedMinute);

            endDate = currentDateTime.format(formatter);
            date = "10m";
            LocalDateTime oneDayAgo = currentDateTime.minusDays(1);
            startDate = oneDayAgo.format(formatter);
        }

        String jsonData = "";
        List<WaterGateData> list = new ArrayList<>();

        try {
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/waterlevel/list/"+date+"/"+wlobscdin+"/"+startDate+"/"+endDate+".json");
            System.out.println(url);
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
                String wlobscd = contentObject.getString("wlobscd");
                Date ymdhm = null;
                SimpleDateFormat sdf = null;

                String dateString= contentObject.getString("ymdhm");
                if(sd ==1) {
                    sdf = new SimpleDateFormat("yyyyMMddHHmm");
                }else if(sd ==2) {
                    sdf = new SimpleDateFormat("yyyyMMddHH");
                }else if(sd ==3) {
                    sdf = new SimpleDateFormat("yyyyMMdd");
                }

                try {
                    ymdhm = sdf.parse(dateString); // 문자열을 Date 객체로 변환
                } catch (ParseException e) {
                    e.printStackTrace();
                }




                String rewl = contentObject.getString("wl");
                String refw = contentObject.getString("fw");


                try {
                    double wl = Double.parseDouble(rewl);
                    double fw = Double.parseDouble(refw);


                    WaterGateData waterGateData = new WaterGateData(wlobscd, ymdhm, wl, fw);
                    list.add(waterGateData);
                }catch (NumberFormatException e){
                    double wl = 0;
                    double fw = 0;
                    WaterGateData waterGateData = new WaterGateData(wlobscd, ymdhm, wl, fw);
                    list.add(waterGateData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<WaterGateDatas> dataGet(List<WaterGateData> list2, WaterGateDto dto) {
        List<WaterGateDatas> list = new ArrayList<>();
        for(WaterGateData data : list2) {
            WaterGateDatas water = new WaterGateDatas(dto.getWlobscd(), data.getYmdhm(), data.getWl(), data.getFw(), dto.getAttwl(), dto.getWrnwl()
                    , dto.getAlmwl(), dto.getSrswl(), dto.getPfh(), dto.getFstnyn());
            System.out.println(water);
        list.add(water);
        }

        return list;
    }



}