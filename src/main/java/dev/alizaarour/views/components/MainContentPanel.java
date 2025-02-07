package dev.alizaarour.views.components;


import dev.alizaarour.views.helper.PageName;
import dev.alizaarour.views.pack.PageFactory;

import javax.swing.*;
import java.awt.*;

public class MainContentPanel extends JPanel {
    private final CardLayout cardLayout;

    public MainContentPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Color.WHITE);
    }

    public void addPage(PageName pageName) {
        add(PageFactory.getPage(pageName).getPagePanel(), pageName.getValue());
    }

    public void showPage(PageName pageName) {
        cardLayout.show(this, pageName.getValue());
    }
}
