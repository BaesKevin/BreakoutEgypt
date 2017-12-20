/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import java.io.Serializable;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Bjarne Deketelaere
 */
public class User implements Serializable{
    private int userId=0;
    private String username;
    private String email;
    private String hash;
    private int gold,diamonds;
    public User(String username){
        this(username, "","");
    }
    public User(String username, String email, String hashorpass, int gold, int diamonds, boolean isHash) {
        if(isHash) {
            this.setHash(hashorpass);
        } else {            
            this.setHashFromPassword(hashorpass);
        }
        this.setUsername(username);
        this.setEmail(email);
        this.setDiamonds(diamonds);
        this.setGold(gold);     
    }
    public User(String username,String email,String password,int gold,int diamonds){
        this.setEmail(email);
        this.setHashFromPassword(password);
        this.setUsername(username);
        this.setDiamonds(diamonds);
        this.setGold(gold);
    }
    public User(String username,String email,String password){
        this(username,email,password,0,0);
    }
    public User(String email,String password){
        this("",email,password);
    }
    
    public int getUserId(){
        return this.userId;
    }
    
    public void setUserId(int id){
        this.userId=id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }
    
    private void setHashFromPassword(String password) {
        this.hash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void setHash(String hash) {
        this.hash = hash;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.username);
        hash = 37 * hash + Objects.hashCode(this.email);
        hash = 37 * hash + Objects.hashCode(this.hash);
        hash = 37 * hash + this.gold;
        hash = 37 * hash + this.diamonds;
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
        final User other = (User) obj;
        if (this.gold != other.gold) {
            return false;
        }
        if (this.diamonds != other.diamonds) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }

}
