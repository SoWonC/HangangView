package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.PrecipitationeDto;
import egovframework.new_hangang.mapper.PrecipitationeMapper;
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
public class PrecipitationeService {

    @Autowired
    ConversionService conversionService;

    @Autowired
    PrecipitationeMapper  precipitationeMapper;

    public List<PrecipitationeDto> precipitationsegy() {
        return  precipitationeMapper.selectPrecipitationeSeGy();
    }

}


