package com.atlassian.confluence.plugins;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.core.DefaultSaveContext;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.labels.Labelable;
import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.sal.api.ApplicationProperties;
//import com.atlassian.sal.api.UrlMode;
import com.atlassian.user.User;
//import com.atlassian.sal.confluence.ConfluenceApplicationProperties;

/**
 * A REST resource for controlling the publish to blog post process.
 */
@Path("/publish")
public class PublishResource
{
    private static final Logger logger = LoggerFactory.getLogger(PublishResource.class);

    private final PageManager pageManager;
    private final AttachmentManager attachmentManager;
    private final LabelManager labelManager;
    private final PermissionManager permissionManager;
    private final SpacePermissionManager spacePermissionManager;
    private final ApplicationProperties applicationProperties;

    public PublishResource(PageManager pageManager,
                           AttachmentManager attachmentManager,
                           LabelManager labelManager,
                           PermissionManager permissionManager,
                           SpacePermissionManager spacePermissionManager,
                           ApplicationProperties applicationProperties)
    {
        this.pageManager = pageManager;
        this.attachmentManager = attachmentManager;
        this.labelManager = labelManager;
        this.permissionManager = permissionManager;
        this.spacePermissionManager = spacePermissionManager;
        this.applicationProperties = applicationProperties;
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

        //TODO: duplication check

        logger.error("publish [ " + id + " ]");

        final Page page = pageManager.getPage(id);
        if (page != null)
        {

            if (!isPermitted(page)) {
                return Response.status(401).build();
            }

            BlogPost post = new BlogPost();
            post.setTitle(page.getTitle());
            post.setContent(page.getContent());
            post.setSpace(page.getSpace());
            DefaultSaveContext defaultSaveContext = new DefaultSaveContext();
            defaultSaveContext.setUpdateLastModifier(true);
            pageManager.saveContentEntity(post, defaultSaveContext);

            // Copy labels
            copyLabels(page, post);

            // Copy attachments
            try
            {
                copyAttachments(page,post);
            }
            catch (IOException e)
            {
                logger.error("could not copy attachments from [ "+page+" ] to [ "+post+" ]: "+e.getLocalizedMessage(), e);
            }

            try
            {
//            	return Response.created(new URI(applicationProperties.getBaseUrl(UrlMode.ABSOLUTE)+post.getUrlPath())).build();
                return Response.created(new URI(applicationProperties.getBaseUrl()+post.getUrlPath())).build();
            }
            catch (URISyntaxException e)
            {
                logger.error("could not create URI [ "+post.getUrlPath()+" ]",e);
            }
        }
        return Response.status(404).build();

    }

    private void copyAttachments(Page page, BlogPost post) throws IOException {
    	List<Attachment> attachments = attachmentManager.getAttachments(page);
    	for (Attachment attachment : attachments) {
    		Attachment target = new Attachment(attachment.getFileName(), attachment.getContentType(), attachment.getFileSize(), attachment.getComment());
			post.addAttachment(target);
			attachmentManager.saveAttachment(target, null, attachment.getContentsAsStream());
		}
	}

	@GET
    public Response get()
    {
        return Response.ok().build();
    }

    private void copyLabels(Labelable original, ContentEntityObject copy)
    {
        List<Label> labels = original.getLabels();
        for (Label label : labels)
        {
            labelManager.addLabel(copy, label);
        }
    }

    private boolean isPermitted(Page page)
    {
        // anonymous users are not allowed to publish blogs
        User user = AuthenticatedUserThreadLocal.getUser();
        if (user == null) {
            return false;
        }

        //check for add blogpost permission
        if (!spacePermissionManager.hasPermission(SpacePermission.EDITBLOG_PERMISSION, page.getSpace(), user)) {
            return false;
        }

        //check view permission for page
        return permissionManager.hasPermission(user, Permission.VIEW, page);
    }

}
