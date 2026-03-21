package io.github._127_0_0_l.infra_filter.models;

public record VehicleAdvert(
        String name,
        String description,
        String city,
        String url,
        int pricePrimary,
        int priceSecondary,
        int year
) {
}
