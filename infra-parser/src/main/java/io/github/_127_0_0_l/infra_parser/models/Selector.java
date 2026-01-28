package io.github._127_0_0_l.infra_parser.models;

import java.util.List;

public record Selector(
        String selector,
        List<Selector> innerSelectors,
        boolean isMultiple,
        String fieldName,
        ContentType contentType
) {
    public boolean hasInnerSelectors(){
        return innerSelectors != null && !innerSelectors.isEmpty();
    }
}
