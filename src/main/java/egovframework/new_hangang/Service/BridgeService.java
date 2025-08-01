package egovframework.new_hangang.Service;

import egovframework.new_hangang.dto.BridgeDto;
import egovframework.new_hangang.mapper.BridgeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BridgeService {

    @Autowired
    ConversionService conversionService;
    @Autowired
    BridgeMapper bridgeMapper;

    public List<BridgeDto> bridgesegy() {
        List<BridgeDto> dbdata = bridgeMapper.selectBridgesSeGy();
        return (dbdata == null) ? new ArrayList<>() : dbdata;
    }
}
