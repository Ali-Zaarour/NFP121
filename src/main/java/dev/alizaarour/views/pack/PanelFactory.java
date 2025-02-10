package dev.alizaarour.views.pack;

import dev.alizaarour.views.BaseFrame;
import dev.alizaarour.views.components.MainContentPanel;
import dev.alizaarour.views.components.NavbarPanel;
import dev.alizaarour.views.components.SidebarPanel;

import javax.swing.*;
import java.util.List;

public abstract class PanelFactory {
    protected MainContentPanel mainContent;

    public PanelFactory(MainContentPanel mainContent) {
        this.mainContent = mainContent;
    }

    public NavbarPanel createNavbar(BaseFrame current) {
        return new NavbarPanel(current);
    }

    public SidebarPanel createSidebar() {
        return new SidebarPanel(getSidebarButtons());
    }

    public abstract List<JButton> getSidebarButtons();
}
