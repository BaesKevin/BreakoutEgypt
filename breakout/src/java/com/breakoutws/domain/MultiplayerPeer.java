/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class MultiplayerPeer {
    private Session session;
    private User user;
    private String paddleName;
    
    public MultiplayerPeer(Session session, User user){
        this.session = session;
        this.user = user;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaddleName() {
        return paddleName;
    }

    public void setPaddleName(String paddleName) {
        this.paddleName = paddleName;
    }
    
    
}
