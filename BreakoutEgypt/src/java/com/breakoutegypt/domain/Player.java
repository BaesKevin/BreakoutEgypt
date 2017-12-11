/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.levelprogression.LevelProgressManager;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.Objects;

/**
 *
 * @author kevin
 */

// voorstel: hier levelprogressie bijhouden, een map tussen GameType en ints lijkt me voldoende
// de int houdt dan het hoogst bereikte level bij. Op deze manier kan in Game bij het stoppen van het
// spelletje aan het level zijn id vragen en zo bijhouden in de map. In de LevelFactorys kunnen we 
// een extra field defaultMaxLevel maken. als dit bv. 5 is dan zijn de eerste 5 levels altijd beschikbaar.
public class Player {
    private User user;
    private Paddle paddle;
    private PlayerConnection conn;
    private LevelProgressManager levelPackProgressions;
    
//    private Session session;
    
    public Player(User user){
        this.user = user;
        this.levelPackProgressions = new LevelProgressManager();
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public PlayerConnection getConnection() {
        return conn;
    }

    public void setConnection(PlayerConnection conn) {
        this.conn = conn;
    }
    
    public LevelProgressManager getProgressions(){
        return levelPackProgressions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.user);
        return hash;
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
        final Player other = (Player) obj;
        if (!user.getUsername().equals(other.getUser().getUsername())) {
            return false;
        }
        return true;
    }

    
    
}
