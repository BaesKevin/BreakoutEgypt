/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import javax.json.JsonObject;

/**
 *
 * @author kevin
 */
public interface PlayerConnection {
    void send(JsonObject json);
}