/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author snc
 */
public class UserLevelsFactory {
    
    public List<UserLevel> getAllUserLevelsForEasyForSomeUser() {
        
        UserLevel ul1 = new UserLevel(1, false, "The catacombs");
        UserLevel ul2 = new UserLevel(2, false, "The pyramid");
        UserLevel ul3 = new UserLevel(3, true, "Cheops");
        
        return Arrays.asList(new UserLevel[] {ul1, ul2, ul3});
    }
    
}
