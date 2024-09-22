package com.davila;

import java.io.File;
import java.security.Security;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.davila.cache.CacheCleaner;
import com.davila.cache.CacheManager;

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class PdfEditorApiApplication {
	private static String DISKCACHE_DIR = "disk-cache";
	//-Dorg.apache.pdfbox.rendering.UsePureJavaCMYKConversion=true
	private static final Logger logger = LoggerFactory.getLogger(PdfEditorApiApplication.class);
	
	public static void main(String[] args) {
		Security.insertProviderAt(new BouncyCastleProvider(), 1);
		
		SpringApplication.run(PdfEditorApiApplication.class, args);
		
		CacheCleaner cacheCleaner = new CacheCleaner();
		File diskFolder = CacheManager.getTargetDiskFolder(DISKCACHE_DIR + File.separator);
		logger.info("Initializing local disk cache in directory: " + diskFolder.getAbsolutePath());
        // Schedule to cleanup the disk cache folder every 1 hour
        cacheCleaner.startCleanupRoutine(diskFolder, 1, TimeUnit.HOURS);
	}
	
	
}


