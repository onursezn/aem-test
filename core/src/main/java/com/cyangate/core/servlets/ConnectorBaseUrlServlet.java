package com.cyangate.core.servlets;

import com.cyangate.core.services.OTMMConnectorService;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Connector Base URL Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/otmmconnector/baseurl"
        }
)
public class ConnectorBaseUrlServlet extends SlingSafeMethodsServlet {

    @Reference
    private OTMMConnectorService otmmConnectorService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String baseUrl = otmmConnectorService.getBaseUrl();

        response.setContentType("text/plain");
        response.getWriter().write(baseUrl);
    }
}
