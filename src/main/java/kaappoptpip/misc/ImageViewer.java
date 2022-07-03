package kaappoptpip.misc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class ImageViewer {
    private JFrame frame;

    public ImageViewer () {
        frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon()));
        frame.pack();
        frame.setVisible(true);
    }

    public void displayImage (BufferedImage img) {
        ((JLabel) frame.getContentPane().getComponent(0)).setIcon(new ImageIcon(img));
        frame.pack();
    }
}
