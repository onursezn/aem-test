package com.cyangate.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/updateImageComponent",
                "sling.servlet.methods=POST"
        }
)
public class UpdateImageComponentServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 99999999L;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String componentPath = request.getParameter("componentPath");
        String imageUrl = request.getParameter("imageUrl");
        String imageName = request.getParameter("imageName");
        if (componentPath == null || imageUrl == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing parameters");
            return;
        }
        try (ResourceResolver resolver = request.getResourceResolver()) {
            Resource resource = resolver.getResource(componentPath);
            if (resource == null || !"aemconnector/components/image".equals(resource.adaptTo(ModifiableValueMap.class).get("sling:resourceType", String.class))) {
                response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Invalid component type");
                return;
            } else {
                ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
                if (properties != null) {
                    properties.put("fileReferenceConnector", imageUrl);
                    properties.put("fileReference", "/content/dam/aemconnector/asset.jpg");
                    properties.put("imageName", imageName);
                    resolver.commit();
                    response.setStatus(SlingHttpServletResponse.SC_OK);
                    return;
                }
            }

            response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Component not found");
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating component: " + e.getMessage());
        }
    }
}
