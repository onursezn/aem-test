package com.cyangate.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
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
import java.util.Random;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/createImageComponent",
                "sling.servlet.methods=POST"
        }
)
public class CreateImageComponentServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 999999998L;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String imageUrl = request.getParameter("imageUrl");
        String pageUrl = request.getParameter("pageUrl");
        String imageName = request.getParameter("imageName");
        if (imageUrl == null || imageUrl.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("Image URL is required.");
            return;
        }

        ResourceResolver resourceResolver = request.getResourceResolver();
        String currentPageUrl = getCurrentPageUrl(pageUrl);
        try {
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

            long randomNumber = new Random().nextInt(90000000);
            Page page = pageManager.getContainingPage(currentPageUrl);
            Resource containerResource = resourceResolver.getResource(page.getContentResource().getPath()+"/root/container/container");

            Resource componentResource = resourceResolver.create(containerResource, "image_" + randomNumber, null);

            ModifiableValueMap properties = componentResource.adaptTo(ModifiableValueMap.class);
            if (properties != null) {
                properties.put("fileReference", imageUrl);
                properties.put("imageName", imageName);
                properties.put("sling:resourceType", "aemconnector-new/components/image");
            }
            resourceResolver.commit();
            response.getWriter().write("Image component created successfully.");
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error creating component: " + e.getMessage());
        }
    }

    private String getCurrentPageUrl(String pageUrl) {
        int index = pageUrl.indexOf("editor.html");
        if (index != -1) {
            String pagePath = pageUrl.substring(index + "editor.html".length());
            return pagePath.replace(".html", "");
        }
        return null;
    }
}
