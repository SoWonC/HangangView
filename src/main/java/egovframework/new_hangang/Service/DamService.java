package egovframework.new_hangang.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.new_hangang.dto.DamApiDto;
import egovframework.new_hangang.dto.DamDto;
import egovframework.new_hangang.mapper.DamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DamService {

    @Autowired
    ConversionService conversionService;

    @Autowired
    DamMapper damMapper;

    @Autowired
    private RestTemplate restTemplate;

    public List<DamDto> damsegy() {

        return damMapper.selectDamSeGy();
    }

    public List<DamDto> findbydam(String dmobscd) {

        return damMapper.selectfindDam(dmobscd);
    }


    public List<DamApiDto> damapi(String dmobscd) {
        String url = "https://api.hrfco.go.kr/52832662-D130-4239-9C5F-730AD3BE6BC6/dam/list/1D/" + dmobscd + ".json";

        try {
            // 외부 API 요청 → 응답을 문자열(String)로 받음
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Jackson의 ObjectMapper로 JSON 파서 준비
            ObjectMapper mapper = new ObjectMapper();

            // 응답 본문(String)을 JSON 트리 구조(JsonNode)로 파싱
            JsonNode root = mapper.readTree(response.getBody());

            // 결과 저장할 리스트 준비
            List<DamApiDto> result = new ArrayList<>();

            // JSON 최상단에서 "data" 키에 해당하는 배열 노드 추출
            JsonNode data = root.path("content");

            // "content" 배열 내부의 각 객체(JSON 오브젝트)를 순회
            for (JsonNode node : data) {
                // 각 JSON 객체를 DamDto 객체로 변환
                DamApiDto dto = mapper.treeToValue(node, DamApiDto.class);
                // 변환된 객체를 결과 리스트에 추가
                result.add(dto);
            }

            // 최종 변환 결과 반환
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}