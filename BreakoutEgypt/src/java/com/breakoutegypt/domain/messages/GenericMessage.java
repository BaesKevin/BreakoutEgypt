/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public abstract class GenericMessage  implements  Message{
    private int recipientIndex;
    private MessageType messageType;
    private String name;
    
    public GenericMessage(int recipientIndex, String name,MessageType messageType){
        this.recipientIndex = recipientIndex;
        this.messageType = messageType;
        this.name = name;
    }

    @Override
    public int getRecipientIndex() {
        return recipientIndex;
    }
    
    @Override 
    public void setRecipientIndex(int i) {
        this.recipientIndex = i;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JsonObjectBuilder toJson() {
         JsonObjectBuilder msgObjectBuilder = Json.createObjectBuilder();
         msgObjectBuilder.add("playerIndex", recipientIndex);
         
         return msgObjectBuilder;
    }
}
