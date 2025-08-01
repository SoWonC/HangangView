package egovframework.new_hangang.controller;

import egovframework.new_hangang.Service.BridgeService;
import egovframework.new_hangang.Service.DamService;
import egovframework.new_hangang.Service.PrecipitationeService;
import egovframework.new_hangang.dto.BridgeDto;
import egovframework.new_hangang.dto.DamDto;
import egovframework.new_hangang.dto.PrecipitationeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    DamService damService;
    @Autowired
    BridgeService bridgeService;
    @Autowired
    PrecipitationeService precipitationeService;

    @GetMapping(value = "/dam", produces = "application/json; charset=UTF-8")
    public List<DamDto> dam() {
        return damService.damsegy();
    }

    @GetMapping(value = "/bridge", produces = "application/json; charset=UTF-8")
    public List<BridgeDto> bridge() {
        return bridgeService.bridgesegy();
    }
    @GetMapping(value = "/precipitatione", produces = "application/json; charset=UTF-8")
    public List<PrecipitationeDto> Precipitation() {
        return precipitationeService.precipitationsegy();
    }
}
