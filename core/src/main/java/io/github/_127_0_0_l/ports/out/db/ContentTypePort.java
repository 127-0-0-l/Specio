package io.github._127_0_0_l.ports.out.db;

import io.github._127_0_0_l.models.ContentType;

import java.util.List;

public interface ContentTypePort {
    boolean createContentType(ContentType contentType);

    boolean deleteContentType(String contentType);

    List<String> getContentTypes();
}
