/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.matrixlab.pisces.metastore.core;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexmy
 */
public class Utils {
    
    public static Properties getProperties() {
        Properties props = new Properties();
        Utils utils = new Utils();
        
        try {
            props.load(utils.getClass().getClassLoader().getResourceAsStream(Consts.CONFIG_PROPS));
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return props;
    }
    
    public static Properties getProperties(String fileName) {
        Properties props = new Properties();
        Utils utils = new Utils();
        
        try {
            props.load(utils.getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return props;
    }
    
}
