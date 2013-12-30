package aurora.engine.V1.Logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.log4j.Logger;

public class AThreadWorker implements Runnable {

    private Thread runner;

    private ActionListener toDo;

    private int sleep = 0;

    private boolean canRun = false;

    private boolean once;

    private ActionListener doAfter;

    static final Logger logger = Logger.getLogger(AThreadWorker.class);

    public AThreadWorker() {
    }

    public void setAction(ActionListener act) {
        this.toDo = act;
    }

    public void setAction(ActionListener act, int sleep) {
        this.toDo = act;
        this.sleep = sleep;
    }

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

    public AThreadWorker(ActionListener act, ActionListener after, int sleep) {
        this.toDo = act;
        this.doAfter = after;
        this.sleep = sleep;

    }

    public synchronized void start() {

        if (logger.isDebugEnabled()) {
            logger.debug("Starting New Background Thread...");
        }

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

            toDo.actionPerformed(new ActionEvent(this, 0, "ThreadWorker"));
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                logger.error(e, e);
            }

            if (once) {
                canRun = false;
            }

        }
        if (doAfter != null) {
            doAfter.actionPerformed(null);
        }

        runner = null;

    }
}
