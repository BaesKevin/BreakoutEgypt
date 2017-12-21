/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class DummyDifficultyRepository implements DifficultyRepository {

    private Map<GameDifficulty, Difficulty> difficulties;

    public DummyDifficultyRepository() {
        difficulties = new LinkedHashMap();
        difficulties.put(GameDifficulty.EASY, new Difficulty("easy", 50, Difficulty.INFINITE_LIVES, true, 8000, 80, 15));
        difficulties.put(GameDifficulty.MEDIUM, new Difficulty("medium", 65, 3, true, 6000, 50, 10));
        difficulties.put(GameDifficulty.HARD, new Difficulty("hard", 80, 3, false, 4000, 30, 5));
        difficulties.put(GameDifficulty.BRUTAL, new Difficulty("brutal", 100, 1, false, 2000, 0, 0));
    }

    @Override
    public List<Difficulty> findAll() {
        List<Difficulty> all = new ArrayList();
        all.addAll(difficulties.values());

        return all;
    }

    @Override
    public Difficulty findByName(GameDifficulty diff) {
        return difficulties.get(diff);
    }

}
