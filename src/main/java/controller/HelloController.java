package controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dam.dto.DamData;
import dam.dto.DamDatas;
import dam.dto.DamDto;
import dam.serice.DamDataSerice;
import dam.serice.DamSerice;
import net.sf.json.JSONArray;
import precipitation.Dto.PrecipitationData;
import precipitation.Dto.PrecipitationDto;
import precipitation.service.PrecipitationDataSerice;
import precipitation.service.PrecipitationSerice;
import waterGate.dto.WaterGateData;
import waterGate.dto.WaterGateDatas;
import waterGate.dto.WaterGateDto;
import waterGate.serice.WaterGateDataSerice;
import waterGate.serice.WaterGateSerice;

@Controller
public class HelloController {

    @Autowired
    WaterGateDataSerice waterGateDataSerice;

    @Autowired
    WaterGateSerice waterGateSerice;

    @Autowired
    PrecipitationDataSerice precipitationDataSerice;

    @Autowired
    PrecipitationSerice precipitationSerice;

    @Autowired
    DamSerice damSerice;
    @Autowired
    DamDataSerice damDataSerice;

    @GetMapping("/waterdata")
    public String waterdata(@RequestParam(name = "wlobscd",defaultValue = "")String wlobscd,
                          @RequestParam(name = "startDate",defaultValue = "0")String startDate,
                          @RequestParam(name = "endDate",defaultValue = "0")String endDate,
                          @RequestParam(name = "st",defaultValue = "1")String st,
                          Model model) {
        int nm = Integer.parseInt(st);
        List<WaterGateData> list2 = waterGateDataSerice.WaterDatain(wlobscd,nm,startDate,endDate);
        List<WaterGateDto> waterGates = waterGateSerice.waterGateAll();
        WaterGateDto waterGate = null;
        for (WaterGateDto wg : waterGates){
           if(wg.getWlobscd().equals(wlobscd)){
               waterGate = wg;
           }
        }
        List<WaterGateDatas>list =  waterGateDataSerice.dataGet(list2,waterGate);

        System.out.println("-----------------------"+list);
        model.addAttribute("wgs",waterGates);
        model.addAttribute("wg",waterGate);
        model.addAttribute("list", JSONArray.fromObject(list));
        model.addAttribute("list2",list);
        model.addAttribute("wlobscd",wlobscd);
        
        return "waterdata";
    }
    @GetMapping("/precipitation2")
    public String test2(@RequestParam(name = "rfobscd",defaultValue = "")String rfobscd,
                      @RequestParam(name = "startDate",defaultValue = "0")String startDate,
                      @RequestParam(name = "endDate",defaultValue = "0")String endDate,
                      @RequestParam(name = "sd",defaultValue = "1")String sd,
                          Model model){
        int nm = Integer.parseInt(sd);
        List<PrecipitationData> list = precipitationDataSerice.PrecipitationData(rfobscd,nm,startDate,endDate);
        List<PrecipitationDto> precipitations = precipitationSerice.precipitationDataAll();
        PrecipitationDto precipitation = null;
        for (PrecipitationDto pt : precipitations){
            if(pt.getRfobscd().equals(rfobscd)){
                precipitation = pt;
            }
        }

        model.addAttribute("pt",precipitation);
        model.addAttribute("pts",precipitations);
        model.addAttribute("list", JSONArray.fromObject(list));
        model.addAttribute("list2",list);
        model.addAttribute("rfobscd",rfobscd);
        
        return "precipitation2";
    }
    
    @GetMapping("/dam2")
    public String dam2(@RequestParam(name = "dmobscd",defaultValue = "")String dmobscd,
                     @RequestParam(name = "startDate",defaultValue = "0")String startDate,
                     @RequestParam(name = "endDate",defaultValue = "0")String endDate,
                     @RequestParam(name = "sd",defaultValue = "1")String sd,
                      Model model) throws IOException {
        int nm = Integer.parseInt(sd);
        List<DamData> list2 = damDataSerice.DamDatain(dmobscd,nm,startDate,endDate);
        List<DamDto> damDtos = damSerice.DamAll();
        DamDto dam = null;
        for (DamDto dm : damDtos) {
            if (dm.getDmobscd().equals(dmobscd)) {
                dam = dm;
            }
        }
        List<DamDatas>list =  damDataSerice.dataGet(list2,dam);
        System.out.println("-----------------"+list);
        model.addAttribute("dam",dam);
        model.addAttribute("dams",damDtos);
        model.addAttribute("list", JSONArray.fromObject(list));
        model.addAttribute("list2",list);
        model.addAttribute("dmobscd",dmobscd);
        
        return "dam2";
    }

    @GetMapping("/damso")
    public String damso(Model model) {
        List<DamDto> damso = damSerice.DamAll();
        model.addAttribute("damso",damso);
        return "damso";
    }
    @GetMapping("/weatherso")
    public String weatherso(Model model) {
        List<PrecipitationDto> weatherso = precipitationSerice.precipitationDataAll();
        model.addAttribute("weatherso",weatherso);
        return "weatherso";
    }
    @GetMapping("/waterso")
    public String sowon(Model model) {
        List<WaterGateDto> waterso = waterGateSerice.waterGateAll();
        model.addAttribute("waterso",waterso);
        return "waterso";
    }
    
    @GetMapping("/allso")
    public String allso(Model model) {
        List<WaterGateDto> waterso = waterGateSerice.waterGateAll();
        List<DamDto> damso = damSerice.DamAll();
        List<PrecipitationDto> weatherso = precipitationSerice.precipitationDataAll();
        model.addAttribute("waterso",waterso);
        model.addAttribute("damso",damso);
        model.addAttribute("weatherso",weatherso);
        return "allso";
    }

}

