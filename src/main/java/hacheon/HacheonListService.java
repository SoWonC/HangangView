package hacheon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HacheonListService {

	@Autowired
	private HacheonListDao hacheonListDao;
	public List<HacheonList> getListAll() {
		
		
//		List<HacheonList> hacheonList = new ArrayList<>();
//
//        // 샘플 데이터 추가
//        hacheonList.add(new HacheonList("항목 1"));
//        hacheonList.add(new HacheonList("항목 2"));
//        hacheonList.add(new HacheonList("항목 3"));
		return hacheonListDao.selectAll();
	}

}
