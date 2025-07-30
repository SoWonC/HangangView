package egovframework.new_hangang.controller;

import egovframework.new_hangang.Service.DamService;
import egovframework.new_hangang.dto.DamDto;
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


    @GetMapping(value = "/dam", produces = "application/json; charset=UTF-8")
    public List<DamDto> dam() {
        return damService.DamAll(); // JSON으로 응답
    }
}
