package jw84_ym12_chatApp.view;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1566740803214762717L;

    private Image image;
    
    private Container container;
    /**
     * create a image and put it in the container that was provided
     * @param image the image file
     * @param container the swing container used to hold the image
     */
    public ImagePanel(Image image, Container container) {
        this.image = image;
        this.container = container;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width < container.getWidth() && height > container.getHeight()) {
            double scale = (container.getHeight() + 0.0) / height;
            height = container.getHeight();
            width = (int)(width * scale);
        } else if (width > container.getWidth() && height < container.getHeight()) {
            double scale = (container.getWidth() + 0.0) / width;
            width = container.getWidth();
            height = (int)(height * scale);
        } else if (width > container.getWidth() && height > container.getHeight()) {
            double wScale = (container.getWidth() + 0.0) / width;
            double hScale = (container.getHeight() + 0.0) / height;
            double scale = wScale < hScale ? wScale : hScale;
            width = (int)(width * scale);
            height = (int)(height * scale);
        }
        g.drawString("Hello from Jiawei", 0, 0);
        g.drawImage(image, 0, 0, width, height, null);
    }
}
