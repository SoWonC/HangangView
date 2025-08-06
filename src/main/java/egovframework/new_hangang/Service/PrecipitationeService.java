package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.PrecipitationeDto;
import egovframework.new_hangang.mapper.PrecipitationeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public List<PrecipitationeDto> findbybridge(String rfobscd) {
        return precipitationeMapper.findPrecipitatione(rfobscd);
    }
}


