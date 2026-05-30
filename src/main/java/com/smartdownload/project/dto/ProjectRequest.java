package com.smartdownload.project.dto;

public class ProjectRequest {

    private String title;
    private String description;
    private String language;
    private String level;
    private Double price;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
