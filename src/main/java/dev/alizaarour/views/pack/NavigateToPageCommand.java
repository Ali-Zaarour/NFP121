package dev.alizaarour.views.pack;

import dev.alizaarour.views.components.MainContentPanel;
import dev.alizaarour.views.helper.PageName;

public class NavigateToPageCommand implements Command {

    private final MainContentPanel mainContent;
    private final PageName pageName;

    public NavigateToPageCommand(MainContentPanel mainContent, PageName pageName) {
        this.mainContent = mainContent;
        this.pageName = pageName;
    }

    @Override
    public void execute() {
        mainContent.showPage(pageName);
    }
}