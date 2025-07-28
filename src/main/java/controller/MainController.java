package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cctv.Cctvdto;
import dam.dto.DamDto;
import dam.serice.DamDataSerice;
import dam.serice.DamSerice;
import dao.MemberDao;
import hacheon.HacheonDetailList;
import hacheon.HacheonDetailListService;
import hacheon.HacheonList;
import hacheon.HacheonListService;
import precipitation.Dto.PrecipitationDto;
import precipitation.service.PrecipitationDataSerice;
import precipitation.service.PrecipitationSerice;
import spring.AuthInfo;
import waterGate.dto.WaterGateDto;
import waterGate.serice.WaterGateDataSerice;
import waterGate.serice.WaterGateSerice;

@Controller
public class MainController {
	
	@Autowired
	private MemberDao memberDao;
	
	
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
    
    @Autowired
	HacheonListService hacheonListService;
	@Autowired
	HacheonDetailListService hacheonDetailListService;
	
	
	@GetMapping("main")
	public String main(Model model) {
		List<Cctvdto>cctvs = waterGateSerice.cctvAll();
		List<String> list = memberDao.selectCctvwlobscd();
		List<WaterGateDto> waterso = waterGateSerice.waterGateAll();
        List<DamDto> damso = damSerice.DamAll();
        List<PrecipitationDto> weatherso = precipitationSerice.precipitationDataAll();
        model.addAttribute("waterso", waterso);
        model.addAttribute("damso", damso);
        model.addAttribute("weatherso", weatherso);
		model.addAttribute("list", list);
		model.addAttribute("cctvs", cctvs);
		return "main";
	}
	
	// 선우 mainController
	
	@GetMapping({ "/hacheon" })
	public String hacheon(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
		List<HacheonList> hacheonList = hacheonListService.getListAll();
		model.addAttribute("hacheonList", hacheonList);

		return "hacheon";
	}
	
	@GetMapping({ "/hacheonDetail" })

	public String test2(HttpServletRequest request, Model model,String hacheon_code) {
		HttpSession session = request.getSession();
		AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
		HacheonDetailList hacheonDetailList = hacheonDetailListService.getList(hacheon_code);
		model.addAttribute("hacheonDetailList", hacheonDetailList);
		return "hacheonDetail";
	}
	
	
	
	// 소원님 + 축복님 mainController
	
//	@GetMapping("/allso")
//    public String allso(Model model) {
//        List<WaterGateDto> waterso = waterGateSerice.waterGateAll();
//        List<DamDto> damso = damSerice.DamAll();
//        List<PrecipitationDto> weatherso = precipitationSerice.precipitationDataAll();
//        model.addAttribute("waterso", waterso);
//        model.addAttribute("damso", damso);
//        model.addAttribute("weatherso", weatherso);
//        return "allso";
//    }
	
//	@Autowired
//  WaterGateDataSerice waterGateDataSerice;
//
//  @Autowired
//  WaterGateSerice waterGateSerice;
//
//  @Autowired
//  PrecipitationDataSerice precipitationDataSerice;
//
//  @Autowired
//  PrecipitationSerice precipitationSerice;
//
//  @Autowired
//  DamSerice damSerice;
//  @Autowired
//  DamDataSerice damDataSerice;
//  @Autowired
//  BoSerice boSerice;
//  @Autowired
//  BoDataSerice boDataSerice;
//
//  @GetMapping("/main")
//  public void main(Model model) {
//      List<WaterGateDto> list = waterGateSerice.waterGateAll();
//
//      model.addAttribute("list",list);
//  }
//
//  @GetMapping("/waterdata")
//  public void waterdata(@RequestParam(name = "wlobscd",defaultValue = "")String wlobscd,
//                        @RequestParam(name = "sd",defaultValue = "1")String sd,
//          Model model) {
//      int nm = Integer.parseInt(sd);
//      List<WaterGateData> list = waterGateDataSerice.WaterDatain(wlobscd,nm);
//      model.addAttribute("list",list);
//      model.addAttribute("wlobscd",wlobscd);
//  }
//
//  @GetMapping("/precipitation")
//  public void wa(Model model) {
//
//      List<PrecipitationDto>list = precipitationSerice.precipitationDataAll();
//      model.addAttribute("list",list);
//
//  }
//  @GetMapping("/precipitation2")
//  public void test2(@RequestParam(name = "rfobscd",defaultValue = "")String rfobscd,
//                        @RequestParam(name = "sd",defaultValue = "1")String sd,
//                        Model model) throws IOException {
//      int nm = Integer.parseInt(sd);
//      List<PrecipitationData> list = precipitationDataSerice.WaterGateData(rfobscd,nm);
//      model.addAttribute("list",list);
//      model.addAttribute("rfobscd",rfobscd);
//  }
//  @GetMapping("/dam")
//  public void dam(Model model) {
//      System.out.println("=========================================");
//      List<DamDto>list = damSerice.DamAll();
//      System.out.println(list);
//     model.addAttribute("list",list);
//
//  }
//  @GetMapping("/dam2")
//  public void dam2(@RequestParam(name = "dmobscd",defaultValue = "")String dmobscd,
//                    @RequestParam(name = "sd",defaultValue = "1")String sd,
//                    Model model) throws IOException {
//      int nm = Integer.parseInt(sd);
//      List<DamData> list = damDataSerice.DamDatain(dmobscd,nm);
//      model.addAttribute("list",list);
//      model.addAttribute("dmobscd",dmobscd);
//  }
//  @GetMapping("/bo")
//  public void bo(Model model) {
//      System.out.println("=========================================");
//      List<BoDto>list = boSerice.boAll();
//      System.out.println(list);
//      model.addAttribute("list",list);
//
//  }
//  @GetMapping("/bo2")
//  public void bo2(@RequestParam(name = "boobscd",defaultValue = "")String boobscd,
//                   @RequestParam(name = "sd",defaultValue = "1")String sd,
//                   Model model) throws IOException {
//      int nm = Integer.parseInt(sd);
//      List<BoData> list = boDataSerice.boDatain(boobscd,nm);
//      model.addAttribute("list",list);
//      model.addAttribute("dmobscd",boobscd);
//  }
	
	
}
