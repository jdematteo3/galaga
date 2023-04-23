/*
class Galaga
Simply initiates the game. Mostly pre-written.
 */
package gradleproject1;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Galaga extends JFrame {

    public Galaga() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("CSP Galaga");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new Galaga();
            ex.setVisible(true);
        });
    }
}
