package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class ResponseCalculateFeesCreditCard {
    private final Map<String, ResponseCalculateFeesCardBrand> brands = new HashMap<>();

    @JsonAnySetter
    public void setBrand(String brandName, ResponseCalculateFeesCardBrand brandData) {
        brands.put(brandName.toLowerCase(), brandData);
    }

    public Map<String, ResponseCalculateFeesCardBrand> getBrands() {
        return brands;
    }

    public ResponseCalculateFeesCardBrand getBrand(String brandName) {
        return brands.get(brandName.toLowerCase());
    }
}
