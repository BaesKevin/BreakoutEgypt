/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public interface Message {
    
    public String getName();
    
    // 0 = all players, 1 player 1, 2 player 2,...
    public int getRecipientIndex();
    public MessageType getMessageType();
    public JsonObjectBuilder toJson();
}
