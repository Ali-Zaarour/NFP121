package dev.alizaarour.views.pack;

import dev.alizaarour.views.components.MainContentPanel;
import dev.alizaarour.views.components.SidebarPanel;
import dev.alizaarour.views.helper.PageName;

import javax.swing.*;
import java.util.List;

public class StudentPanelFactory extends PanelFactory {
    public StudentPanelFactory(MainContentPanel mainContent) {
        super(mainContent);
    }

    @Override
    public List<JButton> getSidebarButtons() {
        return List.of(
                SidebarPanel.createSidebarButton("Dashboard", new NavigateToPageCommand(mainContent, PageName.STUDENT_DASHBOARD)),
                SidebarPanel.createSidebarButton("My Courses", new NavigateToPageCommand(mainContent, PageName.STUDENT_COURSES)),
                SidebarPanel.createSidebarButton("Results", new NavigateToPageCommand(mainContent, PageName.STUDENT_RESULTS)),
                SidebarPanel.createSidebarButton("Profile", new NavigateToPageCommand(mainContent, PageName.PROFILE))

        );
    }
}