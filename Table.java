package Phylosofy;

import java.util.concurrent.CountDownLatch;

public class Table extends Thread {
    private  final int PHILOSOPHY_COUNT = 5;
    private Fork[] forks;
    private Philosopher[] philosophers;
    private CountDownLatch cdl;

    public Table() {
        forks = new Fork[PHILOSOPHY_COUNT];
        philosophers = new Philosopher[PHILOSOPHY_COUNT];
        this.cdl = new CountDownLatch(PHILOSOPHY_COUNT);
    }

    @Override
    public void run() {
        System.out.println("Заседание открыто");
        try {
            thinkingProcess();
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Все наелись.");
    }

    public synchronized boolean tryGetForks(int leftFork, int rightFork){
        if(!forks[leftFork].isUsing() && !forks[rightFork].isUsing()){
            forks[leftFork].setUsing(true);
            forks[rightFork].setUsing(true);
            return true;
        }
        return false;
    }

    public void putForks(int leftFork, int rightFork){
        forks[leftFork].setUsing(false);
        forks[rightFork].setUsing(false);
    }

    private void init(){
        for(int i = 0; i < PHILOSOPHY_COUNT; i++){
            forks[i] = new Fork();
        }
        for (int i = 0; i < PHILOSOPHY_COUNT; i++) {
            philosophers[i] = new Philosopher("Philosopher " + i, this, i,
                    (i + 1) % PHILOSOPHY_COUNT, cdl);
        }
    }

    private void thinkingProcess(){
        for(Philosopher philosopher : philosophers){
            philosopher.start();
        }
    }
}
