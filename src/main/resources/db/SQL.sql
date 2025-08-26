-- ===============================
-- 1. 테이블 생성
-- ===============================
CREATE TABLE public.rainfall_info (
                                      addr        text,
                                      agcnm       text,
                                      etcaddr     text,
                                      lat         text,   -- 원본 위도 (DMS: "37-42-34")
                                      lon         text,   -- 원본 경도 (DMS: "128-35-57")
                                      obsnm       text,
                                      rfobscd     text,
                                      created_at  timestamp DEFAULT now()
);

CREATE TABLE public.waterlevel_info (
                                        addr        text,
                                        agcnm       text,
                                        almwl       text,
                                        attwl       text,
                                        etcaddr     text,
                                        fstnyn      text,
                                        gdt         text,
                                        lat         text,
                                        lon         text,
                                        obsnm       text,
                                        pfh         text,
                                        srswl       text,
                                        wlobscd     text,
                                        wrnwl       text,
                                        created_at  timestamp DEFAULT now()
);

CREATE TABLE public.dam_info (
                                 addr        text,
                                 agcnm       text,
                                 dmobscd     text,
                                 etcaddr     text,
                                 fldlmtwl    text,
                                 lat         text,
                                 lon         text,
                                 obsnm       text,
                                 pfh         text,
                                 created_at  timestamp DEFAULT now()
);

-- ===============================
-- 2. 안전한 캐스팅 함수 생성
-- ===============================
CREATE OR REPLACE FUNCTION safe_cast_double(text)
RETURNS double precision AS $$
BEGIN
  IF $1 IS NULL OR trim($1) = '' THEN
    RETURN NULL;
END IF;
RETURN $1::double precision;
EXCEPTION
  WHEN others THEN
    RETURN NULL;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- ===============================
-- 3. double 좌표 컬럼 추가
-- ===============================
ALTER TABLE public.rainfall_info
    ADD COLUMN IF NOT EXISTS lat_dd double precision,
    ADD COLUMN IF NOT EXISTS lon_dd double precision;

ALTER TABLE public.waterlevel_info
    ADD COLUMN IF NOT EXISTS lat_dd double precision,
    ADD COLUMN IF NOT EXISTS lon_dd double precision;

ALTER TABLE public.dam_info
    ADD COLUMN IF NOT EXISTS lat_dd double precision,
    ADD COLUMN IF NOT EXISTS lon_dd double precision;

-- ===============================
-- 4. 좌표 변환 및 값 채우기
-- ===============================
-- Rainfall
UPDATE public.rainfall_info
SET lat_dd = safe_cast_double(split_part(lat,'-',1))
    + safe_cast_double(split_part(lat,'-',2))/60
    + safe_cast_double(split_part(lat,'-',3))/3600,
    lon_dd = safe_cast_double(split_part(lon,'-',1))
        + safe_cast_double(split_part(lon,'-',2))/60
        + safe_cast_double(split_part(lon,'-',3))/3600;

-- Waterlevel
UPDATE public.waterlevel_info
SET lat_dd = safe_cast_double(split_part(lat,'-',1))
    + safe_cast_double(split_part(lat,'-',2))/60
    + safe_cast_double(split_part(lat,'-',3))/3600,
    lon_dd = safe_cast_double(split_part(lon,'-',1))
        + safe_cast_double(split_part(lon,'-',2))/60
        + safe_cast_double(split_part(lon,'-',3))/3600;

-- Dam
UPDATE public.dam_info
SET lat_dd = safe_cast_double(split_part(lat,'-',1))
    + safe_cast_double(split_part(lat,'-',2))/60
    + safe_cast_double(split_part(lat,'-',3))/3600,
    lon_dd = safe_cast_double(split_part(lon,'-',1))
        + safe_cast_double(split_part(lon,'-',2))/60
        + safe_cast_double(split_part(lon,'-',3))/3600;
