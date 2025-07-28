package precipitation.service;

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

import precipitation.Dto.PrecipitationData;

@Service
public class PrecipitationDataSerice {

    public List<PrecipitationData> PrecipitationData(String rfobscdin, int sd,String startDate, String endDate){
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
        List<PrecipitationData> list = new ArrayList<>();
        try {
            URL url = new URL("http://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/rainfall/list/"+date+"/"+rfobscdin+"/"+startDate+"/"+endDate+".json");
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
                String rfobscd = contentObject.getString("rfobscd");
                Double rf = contentObject.getDouble("rf");

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
                    ymdhm = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                PrecipitationData precipitationData = new PrecipitationData(rfobscd,ymdhm,rf);
                list.add(precipitationData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}
