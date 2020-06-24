import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame introFrame = new JFrame("Cover Page");
                JPanel introPanel = new JPanel();
                introPanel.setLayout(new BorderLayout());

                JButton startProgram = new JButton("Start Program");
                startProgram.setFont(new Font("Arial", Font.BOLD, 50));
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(startProgram);

                BufferedImage myPicture = null;
                try
                {
                    myPicture = ImageIO.read(this.getClass().getResource("image.png"));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                JLabel picLabel = new JLabel(new ImageIcon(myPicture));
                introPanel.add(picLabel, BorderLayout.CENTER);

                introPanel.add(buttonPanel, BorderLayout.SOUTH);
                introFrame.add(introPanel);
                introFrame.pack();
                introFrame.setExtendedState( introFrame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
                introFrame.setVisible(true);
                introFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                startProgram.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent event)
                    {
                        introFrame.dispose();
                        GUI.createAndShowGUI();
                    }
                });
            }
        });

    }
}
