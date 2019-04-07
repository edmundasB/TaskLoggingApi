package com.task.logging;

import com.task.logging.endpoint.StatusLoggingEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.annotation.PostConstruct;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(StatusLoggingEndpoint.class);
    }

    @PostConstruct
    public void init() {
        this.SwaggerConfig();
    }
    private void SwaggerConfig() {
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);

        BeanConfig swaggerConfigBean = new BeanConfig();
        swaggerConfigBean.setConfigId("Frugalis Swagger Jersey Example");
        swaggerConfigBean.setTitle("Using Swagger ,Jersey And Spring Boot ");
        swaggerConfigBean.setVersion("v1");
        swaggerConfigBean.setContact("frugalisAdmin");
        swaggerConfigBean.setSchemes(new String[] { "http", "https" });
        swaggerConfigBean.setBasePath("/api");
        swaggerConfigBean.setResourcePackage("com.task.logging.endpoint");
        swaggerConfigBean.setPrettyPrint(true);
        swaggerConfigBean.setScan(true);
    }


}
