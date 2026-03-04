package io.github._127_0_0_l.core.models;

public record ContentSource(
        Long id,
        String name,
        String source
) {
        public ContentSource (String name, String source){
                this(null, name, source);
        }
}
