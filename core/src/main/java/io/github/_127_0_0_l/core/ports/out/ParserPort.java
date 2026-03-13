package io.github._127_0_0_l.core.ports.out;

import java.util.List;

public interface ParserPort {
    List<String> parse(Long sourceId, String html);
}
