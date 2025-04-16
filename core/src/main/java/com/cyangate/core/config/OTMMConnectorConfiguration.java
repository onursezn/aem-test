package com.cyangate.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "OTMM Connector Configuration",
        description = "Configuration for OTMM Connector"
)
public @interface OTMMConnectorConfiguration {

    @AttributeDefinition(
            name = "OTMM Folder Path",
            type = AttributeType.STRING
    )
    String folderPath() default "/content/dam/otmmconnector";

    @AttributeDefinition(
            name = "OTMM Base URL",
            type = AttributeType.STRING
    )
    String baseUrl() default "https://demo.cyangate.com:9443";
}
