package dev.alizaarour.views;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@Getter
@Setter
public abstract class BaseFrame extends JFrame {

    public BaseFrame() {
    }

    public BaseFrame(String frameName) {
        runComponent(frameName);
    }

    protected void runComponent(String frameName) {
        initializeFrame();
        initVariable();
        createComponents();
        centerFrameOnScreen();
        setIconImageToFrame();
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setTitle("Online training center : " + frameName);
    }

    // Initialize the frame with default properties
    private void initializeFrame() {
        setSize(500, 500);
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

    protected abstract void createComponents();

    protected abstract void initVariable();
}
