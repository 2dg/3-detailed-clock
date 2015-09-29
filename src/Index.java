/**
 * Index.java
 * #3: Detailed Clock
 * @author Raphael Mu
 * 9/7/15
 */

import javax.swing.*;
import java.awt.*;

public class Index {
	public static void main(String[] args) throws InterruptedException {
		JFrame f = new JFrame("International Clock");
		f.setLayout(new GridLayout(1, 3));
		f.add(new Clock("EST"));
		f.add(new Clock("PDT"));
		f.add(new Clock("JST"));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setPreferredSize(new Dimension(1800, 700));
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
