package io.github._127_0_0_l.core.models;

import java.util.List;

public record Selector(
        String selector,
        List<Selector> innerSelectors,
        boolean isMultiple,
        String fieldName,
        ContentType contentType
) {
}
