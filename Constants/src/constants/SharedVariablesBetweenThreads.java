/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import constants.Constants.Items;

/**
 *
 * @author Jens
 */
public class SharedVariablesBetweenThreads {
    private static SharedVariablesBetweenThreads instance;
    
    public volatile Items lastDeniedOrderedItem;
    
    public static SharedVariablesBetweenThreads getInstance() {
        if (instance == null) {
            instance = new SharedVariablesBetweenThreads();
        }
        return instance;
    }

}
