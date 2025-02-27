package dev.alizaarour.views;

import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.utils.DataAction;
import dev.alizaarour.config.StandardApplicationProperties;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

@Getter
@Setter
public abstract class BaseFrame extends JFrame {

    public BaseFrame() {
    }

    public BaseFrame(String frameName, int width, int height, boolean save) {
        runComponent(frameName, width, height, save);
    }

    protected void runComponent(String frameName, int width, int height, boolean save) {
        initializeFrame(width, height);
        initVariable();
        createComponents();
        centerFrameOnScreen();
        setIconImageToFrame();
        setVisible(true);
        //setResizable(false);
        setTitle("Online training center : " + frameName);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveOnClose(save);
            }
        });
    }

    // Initialize the frame with default properties
    private void initializeFrame(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Center the frame on the screen
    private void centerFrameOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    private void setIconImageToFrame() {
        String imagePath = "/images/online-learning.png";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage();
            setIconImage(image);
        } else {
            System.err.println("Image not found: " + imagePath);
        }
    }

    protected void saveOnClose(boolean save) {
        if (save) {
            try {
                DataAction.serialize(ApplicationInitializer.dataSchema, StandardApplicationProperties.getInstance().getDataPath() + "/data_schema.ser");
                System.out.println("Data saved.");
            } catch (Exception x) {
                System.err.println("Error saving: " + x.getMessage());
            }
        }
    }

    protected abstract void createComponents();

    protected abstract void initVariable();
}
