package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JacksonJavaTimeConfiguration implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public JacksonJavaTimeConfiguration() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}