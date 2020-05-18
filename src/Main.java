import javafx.util.Pair;

import javax.swing.*;
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
                GUI.createAndShowGUI();
            }
        });

    }
}
