package dev.alizaarour.views.pack;

import dev.alizaarour.views.components.MainContentPanel;
import dev.alizaarour.views.components.SidebarPanel;
import dev.alizaarour.views.helper.PageName;

import javax.swing.*;
import java.util.List;

public class TeacherPanelFactory extends PanelFactory {

    public TeacherPanelFactory(MainContentPanel mainContent) {
        super(mainContent);
    }

    @Override
    public List<JButton> getSidebarButtons() {
        return List.of(
                SidebarPanel.createSidebarButton("Manage Courses", new NavigateToPageCommand(mainContent, PageName.TEACHER_MANAGE_COURSES)),
                SidebarPanel.createSidebarButton("Create Courses", new NavigateToPageCommand(mainContent, PageName.TEACHER_CREATE_COURSE)),
                SidebarPanel.createSidebarButton("Manage Meeting", new NavigateToPageCommand(mainContent, PageName.TEACHER_MANAGE_MEETING)),
                SidebarPanel.createSidebarButton("Profile", new NavigateToPageCommand(mainContent, PageName.PROFILE))
        );
    }
}