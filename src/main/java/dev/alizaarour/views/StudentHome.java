package dev.alizaarour.views;

import dev.alizaarour.views.components.MainContentPanel;
import dev.alizaarour.views.helper.PageName;
import dev.alizaarour.views.pack.PanelFactory;
import dev.alizaarour.views.pack.StudentPanelFactory;

import java.awt.*;

public class StudentHome extends BaseFrame {
    private MainContentPanel mainContent;
    private PanelFactory panelFactory;

    public StudentHome() {
        super("student home page", 1200, 800, true);
    }

    @Override
    protected void initVariable() {
        mainContent = new MainContentPanel();
        panelFactory = new StudentPanelFactory(mainContent);
    }

    @Override
    protected void createComponents() {
        setLayout(new BorderLayout());

        add(panelFactory.createNavbar(this), BorderLayout.NORTH);
        add(panelFactory.createSidebar(), BorderLayout.WEST);

        mainContent.addPage(PageName.STUDENT_DASHBOARD);
        mainContent.addPage(PageName.STUDENT_COURSES);
        mainContent.addPage(PageName.STUDENT_RESULTS);
        mainContent.addPage(PageName.PROFILE);

        add(mainContent, BorderLayout.CENTER);
    }
}