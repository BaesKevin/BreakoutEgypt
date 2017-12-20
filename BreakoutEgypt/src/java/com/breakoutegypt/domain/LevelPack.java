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
    private List<Level> levels;
    private int defaultOpenLevels;
    private int totalLevels; // necessary for showLevelServlet

    
    public LevelPack(String name, String description, List<Level> levels, int defaultOpenLevels, int totalLevels) {
        this(0, name, description, levels, defaultOpenLevels, totalLevels);
    }

    public LevelPack(int id, String name, String description, List<Level> levels,int defaultOpenLevels, int totalLevels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.levels = levels;
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

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public int getDefaultOpenLevels() {
        return defaultOpenLevels;
    }

    public void setDefaultOpenLevels(int defaultOpenLevels) {
        this.defaultOpenLevels = defaultOpenLevels;
    }
    
    public Level getLevelByNumber(int number){
        Level level = null;
        
        for(Level l : levels){
            if(l.getLevelNumber() == number){
                level = l;
                break;
            }
        }
        
        return level;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }
    
    
}
