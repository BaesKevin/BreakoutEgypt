/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.levelprogression.Difficulty;
import java.util.List;

/**
 *
 * @author kevin
 */
public interface DifficultyRepository {
    List<Difficulty> findAll();
    Difficulty findByName(String difficulty);
    Difficulty getById(int id);
}
