package com.davila.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DocumentText {
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private int page;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private int line;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;

    public DocumentText(int page, int line, String text) {
        this.page = page;
        this.line = line;
        this.text = text;
    }

    // Getters and setters

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextOccurrence{" +
                "page=" + page +
                ", line=" + line +
                ", text='" + text + '\'' +
                '}';
    }
}