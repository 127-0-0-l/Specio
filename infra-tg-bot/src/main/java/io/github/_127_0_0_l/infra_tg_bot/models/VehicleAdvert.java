package io.github._127_0_0_l.infra_tg_bot.models;

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
