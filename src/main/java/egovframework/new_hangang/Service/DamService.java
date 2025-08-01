package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.DamDto;
import egovframework.new_hangang.mapper.DamMapper;
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

    @Autowired
    DamMapper damMapper;

    public List<DamDto> damsegy() {
        return damMapper.selectDamSeGy();
    }

}
