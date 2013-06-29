package enginetest;

import aurora.engine.V1.UI.AFadeLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Test {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final AFadeLabel lbl = new AFadeLabel("Start");
          JButton btn = new JButton("BUTTON");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 25));


        frame.getContentPane().add(lbl, BorderLayout.CENTER);
        frame.getContentPane().add(btn, BorderLayout.SOUTH);
        frame.setVisible(true);

//        lbl.setText("Sammy");


        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lbl.setText( "RAND " + Double.toString(Math.random()));

            }
        });
    }
}