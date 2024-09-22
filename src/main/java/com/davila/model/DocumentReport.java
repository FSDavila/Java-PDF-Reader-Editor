package com.davila.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentReport {
	
	private String author;
	private String subject;
	private String title;
	private long creationDate;
	private String creator;
	private String producer;
	private int numberOfPages;
	private String pdfVersion;
	private int numberOfSignatures;
	
	private boolean setCanPrint;
	private boolean setCanModify;
	private boolean setCanModifyAnnotations;
	private boolean setCanFillInForm;
	private boolean setCanExtractContent;
	private boolean setCanAssembleDocument;
	private boolean setCanPrintFaithful;
	private boolean setCanExtractForAccessibility;
	private boolean hasEncryption;
	
	public boolean isSetCanPrint() {
		return setCanPrint;
	}
	public void setCanPrint(boolean setCanPrint) {
		this.setCanPrint = setCanPrint;
	}
	public boolean isSetCanModify() {
		return setCanModify;
	}
	public void setCanModify(boolean setCanModify) {
		this.setCanModify = setCanModify;
	}
	public boolean isSetCanModifyAnnotations() {
		return setCanModifyAnnotations;
	}
	public void setCanModifyAnnotations(boolean setCanModifyAnnotations) {
		this.setCanModifyAnnotations = setCanModifyAnnotations;
	}
	public boolean isSetCanFillInForm() {
		return setCanFillInForm;
	}
	public void setCanFillInForm(boolean setCanFillInForm) {
		this.setCanFillInForm = setCanFillInForm;
	}
	public boolean isSetCanExtractContent() {
		return setCanExtractContent;
	}
	public void setCanExtractContent(boolean setCanExtractContent) {
		this.setCanExtractContent = setCanExtractContent;
	}
	public boolean isSetCanAssembleDocument() {
		return setCanAssembleDocument;
	}
	public void setCanAssembleDocument(boolean setCanAssembleDocument) {
		this.setCanAssembleDocument = setCanAssembleDocument;
	}
	public boolean isSetCanPrintFaithful() {
		return setCanPrintFaithful;
	}
	public void setCanPrintFaithful(boolean setCanPrintFaithful) {
		this.setCanPrintFaithful = setCanPrintFaithful;
	}
	public boolean isSetCanExtractForAccessibility() {
		return setCanExtractForAccessibility;
	}
	public void setCanExtractForAccessibility(boolean setCanExtractForAccessibility) {
		this.setCanExtractForAccessibility = setCanExtractForAccessibility;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public int getNumberOfSignatures() {
		return numberOfSignatures;
	}
	public void setNumberOfSignatures(int numberOfSignatures) {
		this.numberOfSignatures = numberOfSignatures;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String string) {
		this.creationDate = string;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public String getPdfVersion() {
		return pdfVersion;
	}
	public void setPdfVersion(String pdfVersion) {
		this.pdfVersion = pdfVersion;
	}
	public boolean isHasEncryption() {
		return hasEncryption;
	}
	public void setHasEncryption(boolean hasEncryption) {
		this.hasEncryption = hasEncryption;
	}
}
