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
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.util.encoders.Hex;
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
public class CryptoService {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
	
	/**
	 * Method will generate the hash code in algorithm SHA256 for the provided data
	 * 
	 * @param data The data to be hashed, in byte array format
	 * 
	 * @return generated digest in byte array format
	 */
    public static byte[] generateSHA256Hash(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
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
			MessageDigest digest = MessageDigest.getInstance("SHA-512", "BC");
			return digest.digest(data);
		} catch (Exception e) {

		}
		return null;
    }

}