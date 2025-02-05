package com.shineidle.tripf.domain.geo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeoResponseDto {
    private final String city;
    private final String country;
}
