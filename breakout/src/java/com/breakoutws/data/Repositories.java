/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.data;

/**
 *
 * @author BenDB
 */
public class Repositories {
    
    public static final HighscoreRepo highscoreRepository = new StaticDummyHighscoreRepo();

    public static HighscoreRepo getHighscoreRepository() {
        return highscoreRepository;
    }
    
}
