package com.task.logging;

import com.task.logging.endpoint.StatusLoggingEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(StatusLoggingEndpoint.class);
        configureSwagger();
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }


    public void configureSwagger() {
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setConfigId("Task-managing-api");
        config.setTitle("Task managing api");
        config.setVersion("1.0.0");
        config.setBasePath("/");
        config.setResourcePackage("com.task.logging");
        config.setScan(true);
    }

}
