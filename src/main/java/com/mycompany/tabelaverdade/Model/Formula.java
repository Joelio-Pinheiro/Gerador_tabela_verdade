package com.mycompany.tabelaverdade.Model;

import com.mycompany.tabelaverdade.Model.FormulaValidator;
import com.mycompany.tabelaverdade.Model.LogicOperations;
import java.util.HashMap;
import java.util.Stack;

import com.mycompany.tabelaverdade.Model.LogicValue;

public class Formula {
    private String formula;
    private String rawFormula;
    private FormulaValidator.ValidationInfo formulaInfos = null;

    public Formula(String formula) throws Exception{
        formula = formula.trim();
        formulaInfos = FormulaValidator.validate(formula);

        if (formulaInfos != null) this.formula = formula;
        else throw new Exception("Formula inválida");

        this.rawFormula = formula;
        
        infixToPostfix();
    }

    public String toString() {return formula;}

    public LogicValue evaluate(HashMap<Character, LogicValue> atomicValues){
        Stack<LogicValue> logicValues = new Stack<LogicValue>();

        for (char c: formula.toCharArray()){
            FormulaValidator.CharTypes cType = FormulaValidator.classifyChar(c);

            if (cType == FormulaValidator.CharTypes.ATOMIC){
                //Se o operadando c não tiver um valor lógica no hashmap "atomicValues", o valor padrão 0 é dado a ele
                LogicValue v = atomicValues.get(c);
                if (v == null) atomicValues.put(c, new LogicValue(0));

                logicValues.push(atomicValues.get(c));
            }
            else{ //É um operador (na notação pós-fixa só tem átomos e operadores)
                switch (c){
                    case '*':
                        logicValues.push(LogicOperations.AND(logicValues.pop(), logicValues.pop()));
                        break;
                    case '+':
                        logicValues.push(LogicOperations.OR(logicValues.pop(), logicValues.pop()));
                        break;
                    case '~':
                        logicValues.push(LogicOperations.NOT(logicValues.pop()));
                        break;
                    case '>':
                        //Não me confiarei na ordem de avalição de argumentos passados para funções
                        LogicValue second = logicValues.pop();
                        LogicValue first = logicValues.pop();

                        logicValues.push(LogicOperations.IMP(first, second));
                        break;
                    case '=':
                        logicValues.push(LogicOperations.EQ(logicValues.pop(), logicValues.pop()));
                }
            }
        }

        return logicValues.pop();
    }

    private void infixToPostfix (){ //Algoritmo usado: ShuntingYard (Levemente alterado para funcionar nesse contexto)
        StringBuilder r = new StringBuilder(this.formula.length());
        Stack<Character> s = new Stack<>();
        
        for (char c : this.formula.toCharArray()) {
            if (c == ' ') continue;
            
            FormulaValidator.CharTypes cType = FormulaValidator.classifyChar(c);

            if (cType == FormulaValidator.CharTypes.ATOMIC) {
                r.append(c);
            }
            else if (cType == FormulaValidator.CharTypes.OPEN) {
                s.push(c);
            }
            else if (cType == FormulaValidator.CharTypes.CLOSE) {
                while (!s.isEmpty() && s.peek() != '(') {
                    r.append(s.pop());
                }
                if (!s.isEmpty() && s.peek() == '(') {
                    s.pop();
                }
            }
            else {
                while (!s.isEmpty() && LogicOperations.getPriority(c) <= LogicOperations.getPriority(s.peek())) {
                    r.append(s.pop());
                }
                s.push(c);
            }
        }

        while (!s.isEmpty()) {
            r.append(s.pop());
        }
       
        this.formula = r.toString();
    }
    
    public FormulaValidator.ValidationInfo getFormulaInfos() { return this.formulaInfos; }
    public String getFormula() { return this.rawFormula; }
    public String getFormulaPostFix() { return this.formula; }
}