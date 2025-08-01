package egovframework.new_hangang.mapper;

import egovframework.new_hangang.dto.PrecipitationeDto;

import java.util.List;

public interface PrecipitationeMapper {
    List<PrecipitationeDto> selectAllPrecipitatione();
    List<PrecipitationeDto> selectPrecipitationeSeGy();
}