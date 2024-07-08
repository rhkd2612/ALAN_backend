package com.inha.endgame.core.async;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseWorker {
    private final ResponseQueue responseQueue;

    @Async("asyncResponseTaskExecutor")
    public void processTasks() {
        List<ResponseTask> tasks;
        while ((tasks = responseQueue.pollTask()) != null) {
            tasks.forEach(task -> {
                try {
                    task.run();
                } catch (RuntimeException e) {
                    if (task.isResend())
                        responseQueue.submitTask(task);
                }
            });
        }
    }
}