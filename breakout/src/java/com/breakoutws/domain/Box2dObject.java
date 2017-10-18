/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import javax.json.JsonObject;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public interface Box2dObject {
    public Body getBody();
        public String getJsonKey();
    public JsonObject toJson();
}
