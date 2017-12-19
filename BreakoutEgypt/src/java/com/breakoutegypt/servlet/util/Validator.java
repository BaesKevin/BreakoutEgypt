/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author BenDB
 */
public class Validator {

//    private List<String> errors = new ArrayList();
    private List<String> validatePassphrase(String passphrase) {
        List<String> errors = new ArrayList();
        if (passphrase.length() < 12) {
            errors.add("Your passphrase must contain at least 12 characters!");
        }
        return errors;
    }

    private List<String> validateEmail(String email) {
        List<String> errors = new ArrayList();
        try {
            InternetAddress emailaddres = new InternetAddress(email);
            emailaddres.validate();
        } catch (AddressException ex) {
            errors.add("Invalid emailaddress!");
        }
        return errors;
    }

    private List<String> validateUsername(String username) {
        List<String> errors = new ArrayList();
        if (username.length() < 5) {
            errors.add("Username to short!");
        }
        return errors;
    }

    public List<String> isValidForm(String username, String passphrase, String email) {
        List<String> errors = new ArrayList();
        errors.addAll(validateUsername(username));
        errors.addAll(validateEmail(email));
        errors.addAll(validatePassphrase(passphrase));
        return errors;
    }

    public boolean isInteger(String s) {
        Scanner sc = new Scanner(s.trim());
        if (!sc.hasNextInt(10)) {
            return false;
        }
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(10);
        return !sc.hasNext();
    }

}
