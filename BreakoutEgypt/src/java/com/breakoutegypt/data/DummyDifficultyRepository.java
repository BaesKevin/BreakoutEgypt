/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.levelprogression.Difficulty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class DummyDifficultyRepository implements DifficultyRepository {

    private List<Difficulty> difficulties;

    public DummyDifficultyRepository() {
        difficulties = new ArrayList<>();
        difficulties.add(new Difficulty("easy", 50, Difficulty.INFINITE_LIVES, true, 8000, 80, 15));
        difficulties.add(new Difficulty("medium", 65, 3, true, 6000, 50, 10));
        difficulties.add(new Difficulty("hard", 80, 3, false, 4000, 30, 5));
        difficulties.add(new Difficulty("brutal", 100, 1, false, 2000, 0, 0));
    }

    @Override
    public List<Difficulty> findAll() {
        return difficulties;
    }

    @Override
    public Difficulty findByName(String name) {
        Difficulty difficulty = null;
        for (Difficulty d: difficulties) {
            if (d.getName().equals(name)) {
                difficulty = d;
                break;
            }
        }
        
        return difficulty;
        
    }

}
