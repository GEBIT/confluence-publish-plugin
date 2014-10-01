package it.com.atlassian.confluence.plugins.publish;

import com.atlassian.confluence.it.Space;
import com.atlassian.confluence.it.User;
import com.atlassian.confluence.it.plugin.TestPlugins;
import com.atlassian.confluence.it.rpc.ConfluenceRpc;
import com.atlassian.confluence.pageobjects.ConfluenceTestedProduct;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.testing.annotation.TestedProductClass;
import com.atlassian.webdriver.testing.runner.ProductContextRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

@TestedProductClass(ConfluenceTestedProduct.class)
@RunWith(ProductContextRunner.class)
public class AbstractIntegrationTest
{
    protected static final Space SPACE = new Space("TEST","Test Space");
    protected ConfluenceTestedProduct confluence;
    protected ConfluenceRpc rpc;
    private static String baseurl;

    @BeforeClass
    public static void setupConfluence() throws Exception {
        // if not set by amps lets set some sane defaults
        if (null==System.getProperty("baseurl.confluence"))
        {
            System.setProperty("baseurl.confluence", "http://localhost:1990/confluence");
            System.setProperty("http.confluence.port", "1990");
            System.setProperty("context.confluence.path", "confluence");
        }
        baseurl = System.getProperty("baseurl.confluence");
    }

    @Before
    public void setup() throws Exception
    {
        rpc = ConfluenceRpc.newInstance(baseurl, ConfluenceRpc.Version.V2_WITH_WIKI_MARKUP);
        confluence = TestedProductFactory.create(ConfluenceTestedProduct.class);
        rpc.logIn(User.ADMIN);
        try
        {
            rpc.getSpace(SPACE.getKey());
            rpc.removeSpace(SPACE.getKey());
        }
        catch (Exception e)
        {
            // ignore
        }
        rpc.createSpace(SPACE);
        TestPlugins.setupTestPluginsForWebDriver(rpc);
    }

    @After
    public void down() {
        rpc.removeSpace(SPACE.getKey());
    }

    protected boolean contains(SearchContext context, By by)
    {
        try
        {
            context.findElement(by);
            return true;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }

}
