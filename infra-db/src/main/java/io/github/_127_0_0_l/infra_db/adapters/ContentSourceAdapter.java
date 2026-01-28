package io.github._127_0_0_l.infra_db.adapters;

import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import org.springframework.stereotype.Component;

@Component
public class ContentSourceAdapter implements ContentSourcePort {
    @Override
    public boolean create(ContentSource source) {
        return false;
    }

    @Override
    public boolean update(ContentSource source) {
        return false;
    }

    @Override
    public boolean delete(String sourceId) {
        return false;
    }

    @Override
    public ContentSource get(String sourceId) {
        return new ContentSource("google", "https://google.com");
    }
}
