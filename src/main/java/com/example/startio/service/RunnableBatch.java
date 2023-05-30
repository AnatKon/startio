package com.example.startio.service;

import com.example.startio.domain.FileType;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RunnableBatch  implements Runnable {

    private boolean started = true;
    private Queue<String> queue = new ConcurrentLinkedQueue<>();
    private FileType fileType;
    private int batchLimit;

    public RunnableBatch(FileType fileType, int batchLimit) {
        this.fileType = fileType;
        this.batchLimit = batchLimit;
    }

    @Override
    public void run() {
        while (!queue.isEmpty() || started) {
            String s = queue.poll();
            if (s == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            } else {
                String[] values = s.split(",");
                if (values.length == 0) {
                    return;
                }
                switch (fileType) {
                    case requests:
                        RequestsInsertService requestsInsertService = new RequestsInsertService(batchLimit);
                        requestsInsertService.insert(values);
                        break;
                    case impressions:
                        //ImpressionInsertService impressionInsertService = new ImpressionInsertService(batchLimit);
                        //impressionInsertService.insert(values);
                        break;
                    case clicks:
                        //ClicksInsertService clicksInsertService = new ClicksInsertService(batchLimit);
                        //clicksInsertService.insert(values);
                        break;
                }
            }
        }
    }

    public void send(String value) {
        queue.add(value);
    }

    public void stop() {
        started = false;
    }
}
