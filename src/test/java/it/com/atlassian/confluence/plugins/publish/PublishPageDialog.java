package it.com.atlassian.confluence.plugins.publish;

import com.atlassian.confluence.pageobjects.component.dialog.AbstractDialog;
import com.atlassian.confluence.pageobjects.page.content.ViewPage;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;
import com.atlassian.pageobjects.elements.PageElementFinder;
import com.atlassian.pageobjects.elements.query.Poller;
import com.google.inject.Inject;
import org.openqa.selenium.By;


public class PublishPageDialog extends AbstractDialog
{
    public static final String SELECTOR = "meta[name='ajs-content-type'][content='blogpost']";
    private @ElementBy(cssSelector = ".button-panel-submit-button") PageElement submitButton;

    @Inject
    private PageElementFinder finder;

    public PublishPageDialog()    {
        super("publish-asblog-dialog");
    }

    public ViewPage submit() {
        Poller.waitUntilFalse(finder.find(By.cssSelector(SELECTOR)).timed().isPresent());
        submitButton.click();
        Poller.waitUntilTrue(finder.find(By.cssSelector(SELECTOR)).timed().isPresent());
        return pageBinder.bind(ViewPage.class);
    }
}
