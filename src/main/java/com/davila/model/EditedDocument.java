package com.davila.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class EditedDocument {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String editedDocument;
	
	public EditedDocument (String downloadId, String EditedContent) {
		this.editedDocument = EditedContent;
	}
	public String getEditedDocument() {
		return editedDocument;
	}
	public void setEditedDocument(String editedDocument) {
		this.editedDocument = editedDocument;
	}
	
}
