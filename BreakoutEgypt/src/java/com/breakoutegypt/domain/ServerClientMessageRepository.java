/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ServerClientMessageRepository {

    private List<Message> brickMessages;
    private List<Message> powerupMessages;

    public ServerClientMessageRepository() {
        this.brickMessages = new ArrayList();
        this.powerupMessages = new ArrayList();
    }
    
    public void addBrickMessage(Message m) {
        brickMessages.add(m);
    }

    public List<Message> getBrickMessages() {
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
}
