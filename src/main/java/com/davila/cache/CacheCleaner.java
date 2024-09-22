package com.davila.cache;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.davila.PdfEditorApiApplication;

public class CacheCleaner {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    private static final Logger logger = LoggerFactory.getLogger(CacheCleaner.class);

    public void startCleanupRoutine(File cacheDirectory, long interval, TimeUnit timeUnit) {
        CacheCleanerTask task = new CacheCleanerTask(cacheDirectory);
        logger.info("Disk cache cleaning routine initialized - Interval between purges: {} {}", interval, timeUnit.name());
        scheduler.scheduleAtFixedRate(task, 1, interval, timeUnit);
    }

    public void stopCleanupRoutine() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}