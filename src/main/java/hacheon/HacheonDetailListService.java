package hacheon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class HacheonDetailListService {

	@Autowired
	private HacheonDetailListDao hacheonDetailListDao;

	

	public HacheonDetailList getList(String hacheon_code) {
		
		return hacheonDetailListDao.selectAll(hacheon_code);
	}

}
