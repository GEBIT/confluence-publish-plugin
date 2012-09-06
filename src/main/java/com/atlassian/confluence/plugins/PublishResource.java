package com.atlassian.confluence.plugins;

import com.atlassian.confluence.content.service.BlogPostService;
import com.atlassian.confluence.content.service.PageService;
import com.atlassian.confluence.content.service.blogpost.BlogPostProvider;
import com.atlassian.confluence.content.service.page.ContentPermissionProvider;
import com.atlassian.confluence.core.BodyContent;
import com.atlassian.confluence.core.DefaultSaveContext;
import com.atlassian.confluence.core.SaveContext;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.ContentPermission;
import com.atlassian.core.util.map.EasyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * A REST resource for controlling the publish to blog post process.
 */
@Path("/publish")
public class PublishResource
{
    private static final Logger log = LoggerFactory.getLogger(PublishResource.class);

    private final PageManager pageManager;
    private final AttachmentManager attachmentManager;

    public PublishResource(PageManager pageManager, AttachmentManager attachmentManager)
    {
        this.pageManager = pageManager;
        this.attachmentManager = attachmentManager;
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

        log.error("publish [ " + id + " ]");

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
                log.error("could not copy attachments from [ "+page+" ] to [ "+post+" ]: "+e.getLocalizedMessage(), e);
            }

            return Response.ok(EasyMap.build("url",post.getUrlPath())).build();
//            return Response.ok().build();
        }
        return Response.status(404).build();

    }

    @GET
    public Response get()
    {
        return Response.ok().build();
    }

}
