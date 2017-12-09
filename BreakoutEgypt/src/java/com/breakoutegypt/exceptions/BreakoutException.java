/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.exceptions;

/**
 *
 * @author kevin
 */
public class BreakoutException extends RuntimeException{
    public BreakoutException(String msg, Exception e){
        super(msg, e);
    }
}
