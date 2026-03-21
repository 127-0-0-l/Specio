package io.github._127_0_0_l.core.ports.out;

import io.github._127_0_0_l.core.models.VehicleAdvert;

import java.util.List;

public interface ParserPort {
    List<VehicleAdvert> parse(Long sourceId, String html);
}
