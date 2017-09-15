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
    String endState;
    String startState;

    public Transition(String start, String end, String action) {
        this.startState = start;
        this.endState = end;
        this.action = action;
    }
}

class State {

    String type;
    String stateName;
    List<Transition> trans;

    public State(String stateName, String type) {
        this.stateName = stateName;
        this.type = type;
        trans = new ArrayList<>();
    }

    public void addTransition(String endState, String action) {
        Transition t = new Transition(stateName, endState, action);
        trans.add(t);
    }
    @Override
    public String toString() {
        return type + " " + stateName;
    }
}

public class ContentManager {

    Pattern statePat;
    Pattern transPat;
    Pattern startPat;
    Pattern defaultPat;
    
    Map<String, State> states;
    String currentState;
    String startState;
    String defaultState;
    List<String> fileNames;
    boolean isBlocked;
  
    ContentManager() {
        statePat = Pattern.compile("\\s*(\\w+) (\\w+):\\s*");
        transPat = Pattern.compile("\\s+(\\w+):(.+)");
        startPat = Pattern.compile("\\s*start: (\\w+)\\s*");
        defaultPat = Pattern.compile("\\s*default: (\\w+)\\s*");
        fileNames = new ArrayList<String>();
        //states = new State[1000];
        states = new HashMap<>();
        isBlocked = false;
       
    }

    
    void read() {
        try {
            BufferedReader r = new BufferedReader(new FileReader("content/config.txt"));
            String line;
            while ((line = r.readLine()) != null) {
                Matcher m = startPat.matcher(line);
                if (m.matches()) {
                    startState = m.group(1);
                    continue;
                }
                m = defaultPat.matcher(line);
                if (m.matches()) {
                    defaultState = m.group(1);
                    continue;
                }
                fileNames.add(line.trim());
                
            }
            for (String fname : fileNames) {
                read(fname);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContentManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }
    private void read(String fname) {
        try {
            BufferedReader r = new BufferedReader(new FileReader("content/" + fname));
            String line;
            State current = null;
            while ((line = r.readLine()) != null) {           
                Matcher m = statePat.matcher(line);
                if (m.matches()) {
                    String state = m.group(2);
                    if (states.containsKey(state)) {
                        System.err.println("Duplicate states! Please check files");
                        System.exit(0);
                    }
                    String type = m.group(1);
                    System.out.println("State "+state+" "+type);
                    current = new State(state, type);
                    states.put(state, current);
                }
                m = transPat.matcher(line);
                if (m.matches()) {
                    String state = m.group(1);
                    String action = m.group(2).trim();
                    current.addTransition(state, action);
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
            currentState = chosen.endState;
            isBlocked = false;
        }
    }

    String next() {
        State current = states.get(currentState);
        List<Transition> trans = current.trans;
        if (trans.size() == 1) {
            Transition t = trans.get(0);
            currentState = t.endState;
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
        currentState = startState;
    }


    void goToDefaultState() {
        currentState = defaultState;
        isBlocked = false;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
