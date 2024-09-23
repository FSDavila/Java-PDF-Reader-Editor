package com.davila.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON response
public class VerificationReport {
	
	@JsonProperty("originalDocumentDigest")
    private String originalDocumentDigest;
    
    @JsonProperty("numberOfSignatures")
    private Integer numberOfSignatures = 0;
    
    @JsonProperty("signatureReports")
    private List<SignatureReport> signatureReports = new ArrayList<SignatureReport>();
    
    @JsonProperty("allSignaturesValid")
    private boolean allSignaturesValid = true;

	public boolean isAllSignaturesValid() {
		return allSignaturesValid;
	}

	public void setAllSignaturesValid(boolean allSignaturesValid) {
		this.allSignaturesValid = allSignaturesValid;
	}

	public String getOriginalDocumentDigest() {
		return originalDocumentDigest;
	}

	public void setOriginalDocumentDigest(String originalDocumentDigest) {
		this.originalDocumentDigest = originalDocumentDigest;
	}

	public Integer getNumberOfSignatures() {
		return numberOfSignatures;
	}

	public void setNumberOfSignatures(Integer numberOfSignatures) {
		this.numberOfSignatures = numberOfSignatures;
	}

	public List<SignatureReport> getSignatureReports() {
		return signatureReports;
	}

	public void setSignatureReports(List<SignatureReport> signatureReports) {
		this.signatureReports = signatureReports;
	}
}
