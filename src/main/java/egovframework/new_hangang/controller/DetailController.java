package egovframework.new_hangang.controller;

import egovframework.new_hangang.Service.DamDataService;
import egovframework.new_hangang.Service.DamService;
import egovframework.new_hangang.dto.DamData;
import egovframework.new_hangang.dto.DamDatas;
import egovframework.new_hangang.dto.DamDto;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")


public class DetailController {

    @Autowired
    private DamService damService;
    @Autowired
    private DamDataService damDataService;

    @GetMapping("/openpopupdam")
    public String openpopupdam(@RequestParam(name = "dmobscd", defaultValue = "") String dmobscd,         // 댐 코드
                               @RequestParam(name = "startDate", defaultValue = "0") String startDate,   // 시작 날짜 (yyyy-MM-ddTHH:mm)
                               @RequestParam(name = "endDate", defaultValue = "0") String endDate,       // 종료 날짜
                               @RequestParam(name = "interval", defaultValue = "1") String interval,                 // 간격 (1:10분, 2:1시간, 3:하루)
                               Model model) throws IOException {

        // 간격 문자열을 정수로 변환 (예: 1, 2, 3)
        int nm = Integer.parseInt(interval);

        // 선택된 댐 코드와 기간, 간격을 기준으로 수위 원시 데이터 가져오기
        List<DamData> list2 = damDataService.DamDatain(dmobscd, nm, startDate, endDate);

        // 전체 댐 목록 가져오기 (드롭다운 등에 사용)
        List<DamDto> damDtos = damService.damsegy();

        // 선택된 댐 코드에 해당하는 단일 댐 객체 추출
        DamDto dam = null;
        for (DamDto dm : damDtos) {
            if (dm.getDmobscd().equals(dmobscd)) {
                dam = dm;
            }
        }

        // 원시 수위 데이터 + 기준 수위값(DamDto)을 기반으로 가공된 차트용 데이터 생성
        List<DamDatas> list = damDataService.dataGet(list2, dam);

        // 디버깅용 출력
        System.out.println("-----------------" + list);

        // View(JSP)에서 사용할 데이터들을 모델에 담아 전달
        model.addAttribute("dam", dam);                            // 현재 선택된 댐
        model.addAttribute("dams", damDtos);                       // 전체 댐 목록
        model.addAttribute("list", JSONArray.fromObject(list));    // 가공된 차트 데이터 (JSON 문자열)
        model.addAttribute("list2", list);                         // 테이블용 원시 수위 데이터
        model.addAttribute("dmobscd", dmobscd);                    // 현재 선택된 댐 코드

        // dam2.jsp (또는 dam2.html)로 렌더링 요청
        return "detail/dam";
    }


//            (@RequestParam("dmobscd") String dmobscd,
//                               @RequestParam(name = "startDate", defaultValue = "0") String startDate,
//                               @RequestParam(name = "endDate", defaultValue = "0") String endDate, Model model) {
//        model.addAttribute("onedamlist", damService.findbydam(dmobscd));
//        model.addAttribute("apidam", damService.damapi(dmobscd));
//        return "detail/dam";
//    }

}