/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author samf
 */
class Transition {

    String action;
    int end;
    int start;

    public Transition(int start, int end, String action) {
        this.start = start;
        this.end = end;
        this.action = action;
    }
}

class State {

    String type;
    int no;
    List<Transition> trans;

    public State(int no, String type) {
        this.no = no;
        this.type = type;
        trans = new ArrayList<>();
    }

    public void addTransition(int tNo, String action) {
        Transition t = new Transition(no, tNo, action);
        trans.add(t);
    }
    @Override
    public String toString() {
        return type + " " + no;
    }
}

public class ContentManager {

    Pattern statePat;
    Pattern transPat;
    Map<Integer, State> states;
    int currentState;
    boolean isBlocked;
  
    ContentManager() {
        statePat = Pattern.compile("\\s*(\\d+) (\\w+):\\s*");
        transPat = Pattern.compile("\\s+(\\d+):(.+)");
        //states = new State[1000];
        states = new HashMap<>();
        currentState = 0;
        isBlocked = false;
       
    }

    void read(String fname) {
        try {
            BufferedReader r = new BufferedReader(new FileReader("content/" + fname));
            String line;
            State current = null;
            while ((line = r.readLine()) != null) {
                Matcher m = statePat.matcher(line);
                if (m.matches()) {
                    int stateNo = Integer.parseInt(m.group(1));
                    String type = m.group(2);
                    //System.out.println("State "+stateNo+" "+type);
                    current = new State(stateNo, type);
                    states.put(stateNo, current);
                }
                m = transPat.matcher(line);
                if (m.matches()) {
                    int stateNo = Integer.parseInt(m.group(1));
                    String action = m.group(2).trim();
                    current.addTransition(stateNo, action);
                  //  System.out.println("Transition "+stateNo+ " "+action);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContentManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContentManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    boolean blocked() {
        return isBlocked;
    }
    
    List<String> getOptions() {
        State current = states.get(currentState);
        List<Transition> trans = current.trans;
        List<String> s = new ArrayList<>();
        for (Transition t : trans ) {
            s.add(t.action);
        }
        return s;
    }
    
    void chooseOption(String s) {
        System.out.println("Received chooseOption "+s);
        State current = states.get(currentState);
        List<Transition> trans = current.trans;
        Transition chosen = null;
        for (Transition t : trans) {
            if (t.action.equals(s)) {
                chosen = t;
            }
        }
        if (chosen==null) {
            System.out.println("ERROR invalid choice "+s);
        }
        else {
            System.out.println("Successful: "+chosen.action);
            currentState = chosen.end;
            isBlocked = false;
        }
    }

    String next() {
        State current = states.get(currentState);
        List<Transition> trans = current.trans;
        if (trans.size() == 1) {
            Transition t = trans.get(0);
            currentState = t.end;
            return t.action;
        } else if (trans.size() > 1) {
            isBlocked = true;
            return null;
        } else {
            System.out.println("ERROR, shouldn't get here");
            return null;
        }
    }

    boolean finished() {
        State current = states.get(currentState);
        return current.type.equals("FIN");
    }

    void start() {
        currentState = 0;
    }

    public static void main(String args[]) {
        ContentManager m = new ContentManager();
        m.read("content.txt");
        //System.out.println(m.finished());
        
        /*
        while (!m.finished()) {
            if (!m.blocked()) {
                String out = m.next();
                if (out != null) {
                    System.out.println(out);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ContentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("All finished!");*/
    }

    void goToDefaultState() {
        currentState = 3;
        isBlocked = false;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
