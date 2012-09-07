package com.atlassian.confluence.plugins;

import com.atlassian.confluence.core.DefaultSaveContext;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.core.util.map.EasyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A REST resource for controlling the publish to blog post process.
 */
@Path("/publish")
public class PublishResource
{
    private static final Logger logger = LoggerFactory.getLogger(PublishResource.class);

    private final PageManager pageManager;
    private final AttachmentManager attachmentManager;
    private final SettingsManager settingsManager;

    public PublishResource(PageManager pageManager, AttachmentManager attachmentManager, SettingsManager settingsManager)
    {
        this.pageManager = pageManager;
        this.attachmentManager = attachmentManager;
        this.settingsManager = settingsManager;
    }

    /**
     * Posting to this resource with a single id query para
     *
     * @param id the page to publish
     */
    @PUT
    @Path("/{id}")
    public Response publishPageAsBlog(@PathParam("id") long id)
    {
        //TODO: Check AUTH
        //TODO: duplication check

        logger.error("publish [ " + id + " ]");

        final Page page = pageManager.getPage(id);
        if (page != null)
        {
            BlogPost post = new BlogPost();
            post.setTitle(page.getTitle());
            post.setBodyAsString(page.getBodyAsString());
            post.setSpace(page.getSpace());
            pageManager.saveContentEntity(post, new DefaultSaveContext(false, true, false));

            // Copy attachments
            // TODO update attachment links?
            // TODO update permissions?
            try
            {
                attachmentManager.copyAttachments(page,post);
            }
            catch (IOException e)
            {
                logger.error("could not copy attachments from [ "+page+" ] to [ "+post+" ]: "+e.getLocalizedMessage(), e);
            }

            try
            {
                return Response.created(new URI(settingsManager.getGlobalSettings().getBaseUrl()+post.getUrlPath())).build();
            }
            catch (URISyntaxException e)
            {
                logger.error("could not create URI [ "+post.getUrlPath()+" ]",e);
            }
        }
        return Response.status(404).build();

    }

    @GET
    public Response get()
    {
        return Response.ok().build();
    }

}
