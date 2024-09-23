package com.davila.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON response
public class SignatureReport {
	
    @JsonProperty("signingTime")
    private String signingTime; // Use String to handle GeneralizedTime format

    @JsonProperty("subjectInfo")
    private String subjectInfo;

    @JsonProperty("certificateIsValid")
    private boolean certificateIsValid;

    @JsonProperty("signatureHasIntegrity")
    private boolean signatureHasIntegrity;
    
    public String getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(String signingTime) {
        this.signingTime = signingTime;
    }

	public String getSubjectInfo() {
		return subjectInfo;
	}

	public void setSubjectInfo(String subjectInfo) {
		this.subjectInfo = subjectInfo;
	}

	public boolean isCertificateIsValid() {
		return certificateIsValid;
	}

	public void setCertificateIsValid(boolean certificateIsValid) {
		this.certificateIsValid = certificateIsValid;
	}

	public boolean isSignatureHasIntegrity() {
		return signatureHasIntegrity;
	}

	public void setSignatureHasIntegrity(boolean signatureHasIntegrity) {
		this.signatureHasIntegrity = signatureHasIntegrity;
	}	
    
    
}
