package it.unibo.oop.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Second example of reactive GUI.
 */
public final class ConcurrentGUI extends JFrame {
    
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel currentNumber = new JLabel();
    private final JButton btUp = new JButton("Up");
    private final JButton btDown = new JButton("Down");
    private final JButton btStop = new JButton("Stop");

    
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final var panel = new JPanel();
        panel.add(this.currentNumber);
        panel.add(this.btUp);
        panel.add(this.btDown);
        panel.add(this.btStop);
        this.add(panel);
        this.setVisible(true);

        final var agent = new Agent();
        this.btUp.addActionListener(event -> agent.increaseCounting());
        this.btDown.addActionListener(event -> agent.decreaseCounting());
        this.btStop.addActionListener(event -> {
            agent.stopCounting();
            this.btDown.setEnabled(false);
            this.btUp.setEnabled(false);
        });

        new Thread(agent).start();
    }

    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile boolean isDown; //Initially false: counter goes up by default
        private int counter;

        public void run() {
            while (!this.stop) {
                try {
                    final var nextNumber = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.currentNumber.setText(nextNumber));
                    this.counter = this.isDown ?
                    this.counter - 1:
                    this.counter + 1;
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException e) {
                    e.printStackTrace();
                }
            } 
        }

        public void stopCounting() {
            this.stop = true;
        }

        public void decreaseCounting() {
            this.isDown = true;
        }

        public void increaseCounting() {
            this.isDown = false;
        }
        
    }

}
