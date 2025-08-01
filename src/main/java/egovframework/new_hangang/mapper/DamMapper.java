package egovframework.new_hangang.mapper;

import egovframework.new_hangang.dto.BridgeDto;
import egovframework.new_hangang.dto.DamDto;

import java.util.List;

public interface DamMapper {
    List<DamDto> selectAllDam();
    List<DamDto> selectDamSeGy();
}