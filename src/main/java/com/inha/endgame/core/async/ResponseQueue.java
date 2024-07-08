package com.inha.endgame.core.async;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ResponseQueue {
    private final BlockingQueue<ResponseTask> taskQueue = new LinkedBlockingQueue<>();
    private final ConcurrentLinkedQueue<List<ResponseTask>> runQueue = new ConcurrentLinkedQueue<>();
    private final ResponseWorker worker;
    private static final int BATCH_SIZE = 10; // 한 번에 쓰레드가 처리하는 양
    private static final int TIME_OUT = 50; // 최대로 기다리는 시간

    public ResponseQueue(@Lazy ResponseWorker worker) {
        this.worker = worker;
    }

    public void submitTask(ResponseTask task) {
        taskQueue.offer(task);

        if(taskQueue.size() > BATCH_SIZE)
            processBatch();
    }

    @Scheduled(fixedRate = TIME_OUT)
    public void schedulingProcessing() {
        processBatch();
    }

    private void processBatch() {
        List<ResponseTask> tasks = new ArrayList<>();
        taskQueue.drainTo(tasks, BATCH_SIZE);
        if (!tasks.isEmpty()) {
            runQueue.add(tasks);
            worker.processTasks();
        }
    }

    public List<ResponseTask> pollTask() {
        return runQueue.poll();
    }
}