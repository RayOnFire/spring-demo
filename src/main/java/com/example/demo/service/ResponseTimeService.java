package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * Created by Ray on 2017/7/5.
 */
@Service
public class ResponseTimeService {
    private long totalTime = 0;
    private long minTime = Long.MAX_VALUE;
    private long maxTime = 0;
    private long requests = 0;

    public void add(long time) {
        totalTime += time;
        if (time > maxTime) {
            maxTime = time;
        }
        if (time < minTime) {
            minTime = time;
        }
        requests++;
    }

    public void clear() {
        totalTime = 0;
        minTime = Long.MAX_VALUE;
        maxTime = 0;
        requests = 0;
    }

    public void printInfo() {
        System.out.println(String.format("Total requests: %d", requests));
        System.out.println(String.format("Max Response Time: %dms", maxTime));
        System.out.println(String.format("Min Response Time: %dms", minTime));
        System.out.println(String.format("Avg Response Time: %dms", totalTime / requests));
    }

    public long getTotalTime() {
        return totalTime;
    }
}
