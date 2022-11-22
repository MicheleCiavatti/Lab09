package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
public final class AnotherConcurrentGUI extends JFrame {

    private static final int SECONDS = 10;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel currentNumber = new JLabel();
    private final JButton btUp = new JButton("Up");
    private final JButton btDown = new JButton("Down");
    private final JButton btStop = new JButton("Stop");

    
    public AnotherConcurrentGUI() {
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

        final var agCounter = new Agent();
        this.btUp.addActionListener(event -> agCounter.increaseCounting());
        this.btDown.addActionListener(event -> agCounter.decreaseCounting());
        this.btStop.addActionListener(event -> {
            agCounter.stopCounting();
            this.btDown.setEnabled(false);
            this.btUp.setEnabled(false);
        });

        new Thread(agCounter).start();
        new Thread(() -> {
                for (int i = 0; i < AnotherConcurrentGUI.SECONDS; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }).start();
    }

    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile boolean isDown; //Initially false: counter goes up by default
        private int counter;

        public void run() {
            while (!this.stop) {
                try {
                    final var nextNumber = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.currentNumber.setText(nextNumber));
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
