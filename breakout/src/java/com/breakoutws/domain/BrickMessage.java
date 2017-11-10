/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

/**
 *
 * @author kevin
 */
public class BrickMessage {
    private String name;
    private BrickMessageType messageType;

    public BrickMessage(String name, BrickMessageType messageType) {
        this.name = name;
        this.messageType = messageType;
    }

    public String getName() {
        return name;
    }

    public BrickMessageType getMessageType() {
        return messageType;
    }
}
