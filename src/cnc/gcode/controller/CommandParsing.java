/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc.gcode.controller;

import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author patrick
 */
public class CommandParsing {

    public static class Parameter
    {
        public final char letter;
        public final double value;

        private Parameter(char letter, double value) {
            this.letter = letter;
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.letter;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Parameter other = (Parameter) obj;
            if (this.letter != other.letter) {
                return false;
            }
            return true;
        }
        
    }
    
    public static final char[] axesName = {'X', 'Y', 'Z', 'F'};
    
    private boolean error = false;
    private ArrayList<Parameter> parameters = new ArrayList<>();

    public CommandParsing(String command) {
        String save = "";
        char letter = 0;
        int comment = 0;
        for (char c : (command + " ").toCharArray()) {
            if ((c < '0' || c > '9') && c != '.' && c != '-') {
                if (letter != 0) {
                    //End of number
                    double number=0.0;
                    try {
                        number=Tools.strtod(save);
                    } catch (ParseException ex) {
                        error = true;
                    }
                    Parameter p= new Parameter(letter, number);
                    
                    if (parameters.contains(p)) {
                        error = true;
                    } else {
                        parameters.add(p);
                    }
                    
                    save = "";
                    letter = 0;
                }
            } else if (letter != 0) {
                //Number
                save += c;
                continue;
            } else if (comment == 0) {
                //Number without letter!
                error = true;
            }
            if (c == ' ' || c == '\t') {
                continue; //Space
                //Space
            }
            if (c == '(' || c == ')') {
                //Comment
                if (c == '(') {
                    comment++;
                }
                if (c == ')') {
                    comment--;
                }
                if (comment < 0) {
                    error = true;
                }
                continue;
            }
            if (comment != 0) {
                continue; //in a comment
                //in a comment
            }
            if (c == ';') {
                break; //Start of line comment
                //Start of line comment
            }
            if (Character.isUpperCase(c)) {
                //Start Number
                letter = c;
                save = "";
                continue;
            }
            error = true;
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < parameters.size(); i++) {
            s += "" + parameters.get(i).letter + parameters.get(i).value + " ";
        }
        return s;
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }
    
    public int size()
    {
        return parameters.size();
    }
    
    public boolean contains(char letter)
    {
        return parameters.contains(new Parameter(letter, 0.0));
    }
    
    public Parameter get(int index)
    {
        return parameters.get(index);
    }
    
    public Parameter get(char letter)
    {
        return parameters.get(parameters.indexOf(new Parameter(letter, 0.0)));
    }

    public boolean iserror()
    {
        return error;
    }
    
    
}