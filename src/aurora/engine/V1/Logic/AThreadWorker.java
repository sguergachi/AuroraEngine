package aurora.engine.V1.Logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public class AThreadWorker implements Runnable {

    private Thread runner;

    private ActionListener toDo;

    private int sleep = 0;

    private boolean canRun = true;

    private boolean once;
    private  ActionListener doAfter;

    public AThreadWorker(ActionListener act) {
        this.toDo = act;

    }
    public AThreadWorker(ActionListener act, ActionListener after) {
        this.toDo = act;
        this.doAfter = after;
    }

    public AThreadWorker(ActionListener act, int sleep) {
        this.toDo = act;
        this.sleep = sleep;


    }
    public AThreadWorker(ActionListener act,ActionListener after, int sleep) {
        this.toDo = act;
        this.doAfter = after;
        this.sleep = sleep;


    }

    public void start() {
        System.out.println("Starting New Background Thread...");
        if (!canRun) {
            runner = null;
        }

        if (runner == null) {
            runner = new Thread(this);
        }

        canRun = true;
        runner.start();

    }

    public void stop() {
        canRun = false;
    }

    public Boolean isStopped() {
        return !canRun;
    }

    public void startOnce() {
        once = true;
        start();
    }

    @Override
    public void run() {
        while (runner == Thread.currentThread() && canRun) {

            toDo.actionPerformed(null);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (once) {
                canRun = false;
            }

        }
        if (doAfter != null) {
            doAfter.actionPerformed(null);
        }
    }
}
