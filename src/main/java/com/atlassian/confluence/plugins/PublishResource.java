package com.atlassian.confluence.plugins;

import com.atlassian.confluence.content.service.BlogPostService;
import com.atlassian.confluence.content.service.PageService;
import com.atlassian.confluence.content.service.blogpost.BlogPostProvider;
import com.atlassian.confluence.content.service.page.ContentPermissionProvider;
import com.atlassian.confluence.core.BodyContent;
import com.atlassian.confluence.core.DefaultSaveContext;
import com.atlassian.confluence.core.SaveContext;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.ContentPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
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

    public PublishResource(PageManager pageManager)
    {
        this.pageManager = pageManager;
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

        log.error("publish [ " + id + " ]");

        final Page page = pageManager.getPage(id);
        if (page != null)
        {
            BlogPost post = new BlogPost();
            post.setTitle("Test");
            post.setBodyAsString("Content");
            // this throws an NPE if you don't suppress events - GAH
            pageManager.saveContentEntity(post,new DefaultSaveContext(false,true,false));

        }

        return Response.ok().build();
    }

    @GET
    public Response get()
    {
        return Response.ok().build();
    }

}
