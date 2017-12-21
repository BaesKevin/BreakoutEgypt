/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.data.DummyLevelProgressionRepository;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.Player;
import java.io.Serializable;

/**
 *
 * @author kevin
 */
public class LevelPackProgress implements Serializable {

    private GameType type;
    private String difficulty;
    private LevelProgress progress;
    private int id;
    // speler kent enkel zijn eigen voortgang dus hier geen player

    public LevelPackProgress(GameType type, String difficulty) {
        this.type = type;
        this.difficulty = difficulty;
        this.progress = DummyLevelProgressionRepository.getDefault(type);
    }
    
    public LevelPackProgress(GameType type, String difficulty, LevelProgress lp) {
        this.type = type;
        this.difficulty = difficulty;
        this.progress = lp;
    }

    public LevelProgress getLevelProgress() {
        return new LevelProgress(progress);
    }
    
    public void incrementLevel() {
        this.progress.incrementHighestLevelReached();
    }

    public void incrementLevel(Player p, LevelPack lp, Difficulty d) {
        this.progress.incrementHighestLevelReached(p, lp, d);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final LevelPackProgress other = (LevelPackProgress) obj;

        return this.type == other.type && this.difficulty.equals(other.difficulty);
    }

    public void sethighestLevelReached(int levelid, Player p, LevelPack lp, Difficulty difficulty) {
        this.progress.sethighestLevelReached(levelid, p, lp, difficulty);
    }

}
