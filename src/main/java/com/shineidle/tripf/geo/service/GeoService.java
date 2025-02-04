package com.shineidle.tripf.geo.service;

public interface GeoService {
    /**
     * Google지도 API - 도시값으로 국가 조회
     */
    String getCountryByCity(String city);
}
