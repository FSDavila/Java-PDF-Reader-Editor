package com.davila.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.print.DocFlavor.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.davila.exception.FileException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component("cacheManager")
@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CacheManager {
	private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);
	
	/**
	 * This method will get the local disk cache directory's full path as a File object
	 * @param folderName Name of the local disk cache folder
	 * @return Local disk directory as a File object
	 */
	public static File getTargetDiskFolder(String folderName) {
		java.net.URL url = CacheManager.class.getResource("/");
		File diskCacheDirectory = null;
		try {
			diskCacheDirectory = Paths.get(url.toURI()).toFile();
			diskCacheDirectory = diskCacheDirectory.getParentFile();
			diskCacheDirectory = new File(diskCacheDirectory.getPath(), folderName);
			
			if (!diskCacheDirectory.exists()) {	
				if (diskCacheDirectory.mkdirs()) {
					System.out.println("Directory created successfully.");
				}
			}
		} catch (URISyntaxException e) {
			throw new FileException("Error while saving file to disk: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return diskCacheDirectory;
	}
	
	/**
	 * This method is used to save a file to disk
	 * 
	 * @param inputStream the InputStream of the file to be saved
	 * @param fileNameHash the hash code of the file to be saved
	 * @param the local disk cache directory
	 * @return The saved file as a FileInputStream
	 */
	public File saveOrGetFileInDisk(InputStream inputStream, String fileNameHash, File diskFolder) throws FileException {
		File targetFile = new File(diskFolder, fileNameHash);
        // Check if the file already exists
        if (targetFile.exists()) {
        	// Returns the already cached file (file name is the original filename's hash to avoid collision in files with the same name)
        	logger.info("File with FileID {} found in the local disk cache.", targetFile.getName());
			return targetFile;
        }

        try {
            // Create output stream to save the file
            OutputStream outputStream = new FileOutputStream(targetFile);
            int read;
            byte[] bytes = new byte[1024];

            // Write bytes from input stream to output stream
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // Flush and close streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            // Return a FileInputStream for the saved file
            logger.info("File with FileID {} added in the local disk cache.", targetFile.getName());
            return targetFile;
        } catch (IOException e) {
            throw new FileException("Error while processing the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	/**
	 * This method is used to delete a file from disk
	 * 
	 * @param path The path where the file is saved
	 * @return Boolean with info of if the operation suceeded or not
	 */
    public boolean deleteFileFromDisk(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
            	logger.info("File with FileID {} deleted from local disk cache.", file.getName());
                return true;
            } else {
            	logger.info("File with FileID {} is restricted from being deleted from local disk cache folder.", file.getName());
                return false;
            }
        } else {
        	logger.info("File with FileID {} does not exist.", file.getName());
            return false;
        }
    }
}
