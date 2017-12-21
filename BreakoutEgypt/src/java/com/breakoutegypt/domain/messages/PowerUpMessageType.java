/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

/**
 *
 * @author BenDB
 */
public enum PowerUpMessageType implements MessageType{
    ADDFLOOR, ACTIVATEFLOOR, REMOVEFLOOR,
    ADDBROKENPADDLE, ACTIVATEBROKENPADDLE, REMOVEBROKENPADDLE, 
    ADDACIDBALL, REMOVEACIDBALL,
    NOSUCHPOWERUP, NULLMESSAGE, GENERICPOWERUP
}
