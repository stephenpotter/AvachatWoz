/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samf
 */
public class MySleeper {
 static void sleep(long ms) {   
     try {
         Thread.sleep(ms);
     } catch (InterruptedException ex) {
         Logger.getLogger(MySleeper.class.getName()).log(Level.SEVERE, null, ex);
     }
 }
}
