package practicaltest01example.eim.systems.cs.pub.ro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class ProcessingThread extends Thread {
    Context context;
    boolean isRunning = true;
    double arithmeticMean, geometicMean;
    Random random = new Random();
    String[] actionTypes = {"Action 1", "Action 2", "Action 3"};

    public ProcessingThread(Context context, int firstNumber, int secondNumber) {
        this.context = context;

        arithmeticMean = (double) (firstNumber + secondNumber) / 2;
        geometicMean = Math.sqrt(firstNumber * secondNumber);
    }

    @Override
    public void run() {
        Log.d("Processing Thread", "Thread has started! PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());
        while (isRunning) {
            sendMessage();
            sleep();
        }
    }

    public void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(actionTypes[random.nextInt(actionTypes.length)]);
        intent.putExtra("BR extra", new Date(System.currentTimeMillis()) + " " + arithmeticMean + " " + geometicMean);
        context.sendBroadcast(intent);
    }

    public void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
