package io.github._127_0_0_l.core.models;

public record VehicleAdvert(
        String name,
        String description,
        String params,
        String city,
        String url,
        int pricePrimary,
        int priceSecondary,
        int year
) {
}
