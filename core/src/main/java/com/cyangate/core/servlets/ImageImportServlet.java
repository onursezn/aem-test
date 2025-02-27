package com.cyangate.core.servlets;

import com.day.cq.dam.api.AssetManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/saveExternalImage",
                "sling.servlet.methods=POST"
        }
)
public class ImageImportServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 10000001L;
    private static final String DAM_FOLDER = "/content/dam/otmm";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String imageUrl = request.getParameter("imageUrl");
        String imageName = request.getParameter("imageName");

        if (imageUrl == null || imageName == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing parameters: imageUrl and/or imageName");
            return;
        }

        // /content/dam/importedImages/0187f9af19023edf35f3fcebcee2ed3662a527e5.jpg
        String damPath = DAM_FOLDER + "/" + imageName;

        try (ResourceResolver resolver = request.getResourceResolver()) {
            AssetManager assetManager = resolver.adaptTo(AssetManager.class);
            if (assetManager != null) {
                try (InputStream in = new URL(imageUrl).openStream()) {

                    assetManager.createAsset(damPath, in, "image/jpeg", true);
                    response.setStatus(SlingHttpServletResponse.SC_OK);
                    response.getWriter().write("Asset created at: " + damPath);
                    return;
                } catch (Exception e) {
                    response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Error creating asset: " + e.getMessage());
                }
            } else {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("AssetManager not available");
            }
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error obtaining ResourceResolver: " + e.getMessage());
        }
    }
}
