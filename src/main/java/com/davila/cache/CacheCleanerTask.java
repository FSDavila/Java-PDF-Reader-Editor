package com.davila.cache;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheCleanerTask implements Runnable {

    private final File cacheDirectory;
    
    private static final Logger logger = LoggerFactory.getLogger(CacheCleanerTask.class);

    public CacheCleanerTask(File cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }

    @Override
    public void run() {
    	logger.info("Initializing disk cache cleaning routine...");
        if (cacheDirectory.exists() && cacheDirectory.isDirectory()) {
            File[] files = cacheDirectory.listFiles();
            logger.info("Found {} files in the disk cache directory. Initializing deletion...", files.length);
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.delete()) {
                        	logger.debug("Deleted file: " + file.getAbsolutePath());
                        } else {
                        	logger.debug("Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
                logger.info("Finished cleaning the disk cache directory.", files.length);
            }
        } else {
        	logger.error("Cache directory does not exist or is not a directory.");
        }
    }
}