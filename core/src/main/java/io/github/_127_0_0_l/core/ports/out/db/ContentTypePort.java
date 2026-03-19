package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ContentType;

import java.util.List;

public interface ContentTypePort {
    boolean create(ContentType contentType);

    boolean delete(String contentType);

    List<String> getAll();
}
