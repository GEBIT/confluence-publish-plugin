package it.com.atlassian.confluence.plugins.publish;

import com.atlassian.confluence.it.User;
import com.atlassian.confluence.pageobjects.page.content.CreateBlog;
import com.atlassian.confluence.pageobjects.page.content.CreatePage;
import com.atlassian.confluence.pageobjects.page.content.ViewPage;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPublishPage extends AbstractIntegrationTest
{

    @Test
    public void testMenuItemExistsForPage()
    {
        CreatePage createPage = confluence.loginAndCreatePage(User.ADMIN, SPACE);
        createPage.setTitle("new page");
        createPage.getEditor().getContent().type("This is a page");
        assertTrue(contains(createPage.save().openToolsMenu().getMenuContainer(), By.id("publish-asblog")));
    }

    @Test
    public void testMenuItemDoesNotExistForBlog()
    {
        CreateBlog createBlog = confluence.loginAndCreateBlog(User.ADMIN, SPACE);
        createBlog.setTitle("new page");
        createBlog.getEditor().getContent().type("This is a blog");
        assertFalse(contains(createBlog.save().openToolsMenu().getMenuContainer(),By.id("publish-asblog")));
    }

    @Test
    public void testSimplePageNoAttachments()
    {
        CreatePage createPage = confluence.loginAndCreatePage(User.ADMIN, SPACE);
        createPage.setTitle("new page");
        createPage.getEditor().getContent().type("This is a simple page");
        createPage.save().openToolsMenu().getMenuContainer().findElement(By.id("publish-asblog")).click();
        ViewPage viewPage = confluence.getPageBinder().bind(PublishPageDialog.class).submit();
        assertEquals("blogpost", viewPage.getMetadata("content-type"));
        assertEquals("This is a simple page", viewPage.getTextContent());
    }

    // TODO: test pages with attachments
    // TODO: test permissions
}
