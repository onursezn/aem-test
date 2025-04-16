package com.cyangate.core.services.impl;

import com.cyangate.core.config.OTMMConnectorConfiguration;
import com.cyangate.core.services.OTMMConnectorService;
import lombok.Getter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = OTMMConnectorService.class)
@Designate(ocd = OTMMConnectorConfiguration.class)
public class OTMMConnectorServiceImpl implements OTMMConnectorService {

    @Getter
    private String folderPath;

    @Getter
    private String baseUrl;

    @Activate
    void activate (OTMMConnectorConfiguration config) {
        this.folderPath = config.folderPath();
        this.baseUrl = config.baseUrl();
    }

}
