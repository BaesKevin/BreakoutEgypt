/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import java.util.List;

/**
 *
 * @author kevin
 */
public class LevelPack {
    private int id;
    private String name;
    private String description;
    private int defaultOpenLevels;
    private int totalLevels; // necessary for showLevelServlet

    
    public LevelPack(String name, String description, int defaultOpenLevels, int totalLevels) {
        this(0, name, description, defaultOpenLevels, totalLevels);
    }

    public LevelPack(int id, String name, String description,int defaultOpenLevels, int totalLevels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultOpenLevels = defaultOpenLevels;
        this.totalLevels = totalLevels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultOpenLevels() {
        return defaultOpenLevels;
    }

    public void setDefaultOpenLevels(int defaultOpenLevels) {
        this.defaultOpenLevels = defaultOpenLevels;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }
    
    
}
