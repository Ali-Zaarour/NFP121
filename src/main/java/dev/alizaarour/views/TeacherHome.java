package dev.alizaarour.views;

import dev.alizaarour.views.components.MainContentPanel;
import dev.alizaarour.views.helper.PageName;
import dev.alizaarour.views.pack.PanelFactory;
import dev.alizaarour.views.pack.TeacherPanelFactory;

import java.awt.*;

public class TeacherHome extends BaseFrame {
    private MainContentPanel mainContent;
    private PanelFactory panelFactory;

    public TeacherHome() {
        super("teacher home page", 1200, 800, true);
    }

    @Override
    protected void initVariable() {
        mainContent = new MainContentPanel();
        panelFactory = new TeacherPanelFactory(mainContent);
    }

    @Override
    protected void createComponents() {
        setLayout(new BorderLayout());

        add(panelFactory.createNavbar(), BorderLayout.NORTH);
        add(panelFactory.createSidebar(), BorderLayout.WEST);

        mainContent.addPage(PageName.TEACHER_MANAGE_COURSES);
        mainContent.addPage(PageName.TEACHER_CREATE_COURSE);
        mainContent.addPage(PageName.TEACHER_MANAGE_MEETING);
        mainContent.addPage(PageName.PROFILE);

        add(mainContent, BorderLayout.CENTER);
    }
}
