package com.shineidle.tripf.geo.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FeedErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeoServiceImpl implements GeoService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.api.key}")
    private String apiKey;

    @Override
    @Cacheable(value = "locationCache", key = "'city:' + #city")
    public String getCountryByCity(String city) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + city + "&key=" + apiKey + "&language=ko";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        if (results != null && !results.isEmpty()) {
            for (Map<String, Object> component : (List<Map<String, Object>>) results.get(0).get("address_components")) {
                List<String> types = (List<String>) component.get("types");
                if (types.contains("country")) {
                    return (String) component.get("long_name");
                }
            }
        }
        throw new GlobalException(FeedErrorCode.COUNTRY_NOT_FOUND);
    }
}
