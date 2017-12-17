/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kevin
 */
public class ServerClientMessageRepository {

    private Set<Message> brickMessages;
    private List<Message> powerupMessages;
    private List<Message> powerdownMessages;

    public ServerClientMessageRepository() {
        this.brickMessages = new HashSet();
        this.powerupMessages = new ArrayList();
        this.powerdownMessages = new ArrayList();
    }
    
    public void addBrickMessage(Message m) {
        brickMessages.add(m);
    }

    public Set<Message> getBrickMessages() {
        return brickMessages;
    }

    public void clearBrickMessages() {
        brickMessages.clear();
    }

    public void addPowerupMessages(Message m) {
        powerupMessages.add(m);
    }

    public void clearPowerupMessages() {
        powerupMessages.clear();
    }

    public List<Message> getPowerupMessages() {
        return powerupMessages;
    }
    
    public void addPowerdownMessages(Message m) {
        powerdownMessages.add(m);
    }

    public void clearPowerdownMessages() {
        powerdownMessages.clear();
    }

    public List<Message> getPowerdownMessages() {
        return powerdownMessages;
    }
}
