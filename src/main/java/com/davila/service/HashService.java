package com.davila.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.catalina.Store;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.davila.cache.CacheManager;
import com.davila.exception.PDFException;
import com.davila.exception.VerificationException;
import com.davila.model.SignatureReport;
import com.davila.model.EditedDocument;
import com.davila.model.VerificationReport;

@Service("cryptoService")
public class HashService {
	
	private static final Logger logger = LoggerFactory.getLogger(HashService.class);
	
	/**
	 * Method will generate the hash code in algorithm SHA256 for the provided data
	 * 
	 * @param data The data to be hashed, in byte array format
	 * 
	 * @return generated digest in byte array format
	 */
    public static byte[] generateSHA256Hash(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(data);
		} catch (Exception e) {

		}
		return null;
    }
    
	/**
	 * Method will generate the hash code in algorithm SHA512 for the provided data
	 * 
	 * @param data The data to be hashed, in byte array format
	 * 
	 * @return generated digest in byte array format
	 */
    public static byte[] generateSHA512Hash(byte[] data)  {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			return digest.digest(data);
		} catch (Exception e) {

		}
		return null;
    }

}