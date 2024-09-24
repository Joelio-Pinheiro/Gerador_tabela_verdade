package com.mycompany.tabelaverdade.Model;

import com.mycompany.tabelaverdade.Model.LogicValue;


public class LogicOperations{
    public enum Priority{
        NOT(4, '~'),
        AND(3, '*'),
        OR(2, '+'),
        IMP(1, '>'),
        EQ(0, '=');

        public int priority;
        public char identifier;

        Priority(int priority, char identifier) {
            this.priority = priority;
            this.identifier = identifier;
        }

        public int getPriority() {return this.priority;}
    }

    public static int getPriority(char operator){
        for (Priority p: Priority.values()){
            if (p.identifier == operator) return p.priority;
        }

        return -1;
    }

    public static LogicValue NOT(LogicValue val){
        LogicValue result = new LogicValue(0);

        if (val.get() == 0) result.set(1);
        
        return result;
    }

    public static LogicValue AND(LogicValue left, LogicValue right){
        LogicValue result = new LogicValue(0);

        if (left.get() == 1 && right.get() == 1) result.set(1);

        return result;
    }

    public static LogicValue OR(LogicValue left, LogicValue right){
        LogicValue result = new LogicValue(0);

        if (left.get() == 1 || right.get() == 1) result.set(1);

        return result;
    }

    public static LogicValue IMP(LogicValue left, LogicValue right){
        LogicValue result = new LogicValue(0);

        if (OR( NOT(left), right ).get() == 1) result.set(1);
        
        return result;
    }
    
    public static LogicValue EQ(LogicValue left, LogicValue right){
        LogicValue result = new LogicValue(0);

        if (left.get() == right.get()) result.set(1);

        return result;
    }
}
