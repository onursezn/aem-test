package com.cyangate.core.servlets;

import com.day.cq.dam.api.AssetManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.InputStream;
import java.net.URL;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/saveExternalImage",
                "sling.servlet.methods=GET"
        }
)
public class ImportImageServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // Obtain the ResourceResolver from the request
        ResourceResolver resolver = request.getResourceResolver();

        // Define the external image URL and the target DAM path
        String imageUrl = "http://thirdparty.com/path/to/image.jpg";
        String damPath = "/content/dam/myfolder/image.jpg";

        // Call the helper method to save the image into AEM Assets
        boolean success = saveExternalImage(resolver, imageUrl, damPath);

        // Provide a simple response
        try {
            if (success) {
                response.getWriter().write("Image saved successfully at " + damPath);
            } else {
                response.getWriter().write("Failed to save image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads an image from the external URL and creates an asset in AEM DAM.
     *
     * @param resolver ResourceResolver to adapt to AssetManager.
     * @param imageUrl URL of the external image.
     * @param damPath  Target path in the DAM (e.g., /content/dam/myfolder/image.jpg).
     * @return boolean indicating success or failure.
     */
    private boolean saveExternalImage(ResourceResolver resolver, String imageUrl, String damPath) {
        AssetManager assetManager = resolver.adaptTo(AssetManager.class);
        if (assetManager != null) {
            try (InputStream in = new URL(imageUrl).openStream()) {
                // Create or update the asset with the image content
                assetManager.createAsset(damPath, in, "image/jpeg", true);
                return true;
            } catch (Exception e) {
                // Log error details in a real implementation
                e.printStackTrace();
            }
        }
        return false;
    }
}
