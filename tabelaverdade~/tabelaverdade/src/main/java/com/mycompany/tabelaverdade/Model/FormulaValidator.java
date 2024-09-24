package com.mycompany.tabelaverdade.Model;

import java.util.HashMap;
import java.util.Stack;

public class FormulaValidator {

    public enum CharTypes {
        ATOMIC,
        OPERATOR,
        OPEN, //"Abridor de expressão", esse é o '('
        CLOSE, //"Fechador de expressão", esse é o ')'
        UNKNOWN
    }

    static public class ValidationInfo {

        public HashMap<Integer, Integer> subFormulasIdxs;
        public HashMap<Integer, Character> operatorsIdxs;
        public HashMap<Integer, Character> atomicIdxs;

        public ValidationInfo() {
            this.subFormulasIdxs = new HashMap<Integer, Integer>();
            this.operatorsIdxs = new HashMap<Integer, Character>();
            this.atomicIdxs = new HashMap<Integer, Character>();
        }
    }

    //Campos static
    private static final char[] chars_atomicFormulas = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static final char[] chars_operators = {'~', '*', '+', '>', '='};

    //HashMap para acesso otimizado dos caracteres possíveis de uma fórmula
    private static final HashMap<Character, Integer> map_atomicFormulas = new HashMap<Character, Integer>();
    private static final HashMap<Character, Integer> map_operators = new HashMap<Character, Integer>();

    static {
        for (int i = 0; i < chars_atomicFormulas.length; i++) {
            map_atomicFormulas.put(chars_atomicFormulas[i], 1);
        }
        for (int i = 0; i < chars_operators.length; i++) {
            map_operators.put(chars_operators[i], 1);
        }
    }

    //*--
    public static CharTypes classifyChar(char c) {
        if (c == '(') {
            return CharTypes.OPEN;
        } else if (c == ')') {
            return CharTypes.CLOSE;
        } else if (map_atomicFormulas.get(c) != null) {
            return CharTypes.ATOMIC;
        } else if (map_operators.get(c) != null) {
            return CharTypes.OPERATOR;
        } else {
            return CharTypes.UNKNOWN;
        }
    }

    public static ValidationInfo validate(String formula) { //Validador baseado em indução
        formula = formula.trim();

        ValidationInfo info = new ValidationInfo();

        //Casos base
        if (formula.length() <= 0) {
            return null;
        }
        if (formula.length() == 1 && classifyChar(formula.charAt(0)) == CharTypes.ATOMIC) {
            info.atomicIdxs.put(0, formula.charAt(0));

            return info;
        }

        //Casos para tamanho de formula > 1
        int openedParenthesis = 0;
        int closedParenthesis = 0;

        Stack<Integer> subFormulasStartIndexes = new Stack<Integer>();

        CharTypes previousCharType = null;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);

            if (c == ' ') {
                continue;
            }

            CharTypes charType = classifyChar(c);

            switch (charType) {
                case ATOMIC:
                    if (previousCharType == CharTypes.CLOSE || previousCharType == CharTypes.ATOMIC) {
                        return null;
                    }

                    info.atomicIdxs.put(i, c);

                    break;

                case OPEN:
                    openedParenthesis += 1;

                    subFormulasStartIndexes.push(i);
                    info.subFormulasIdxs.put(i, -1);

                    if (previousCharType == null) {
                        break;
                    }
                    if (previousCharType != CharTypes.OPERATOR && previousCharType != CharTypes.OPEN) {
                        return null;
                    }

                    break;

                case CLOSE:
                    closedParenthesis += 1;

                    if (closedParenthesis > openedParenthesis) {
                        return null;
                    }
                    if (previousCharType == null) {
                        return null;
                    }
                    if (previousCharType != CharTypes.ATOMIC && previousCharType != CharTypes.CLOSE) {
                        return null;
                    }

                    info.subFormulasIdxs.put(subFormulasStartIndexes.pop(), i);

                    break;

                case OPERATOR:
                    info.operatorsIdxs.put(i, c);

                    if (i == formula.length() - 1) {
                        return null;
                    }

                    if (c == '~') { //Caso para operador unário (apenas temos o NOT)
                        if (previousCharType == null) {
                            break;
                        }
                        if (previousCharType != CharTypes.OPERATOR && previousCharType != CharTypes.OPEN) {
                            return null;
                        }
                    } else { //Caso para operadores binários (restante)
                        if (previousCharType == null) {
                            return null;
                        }
                        if (previousCharType != CharTypes.ATOMIC && previousCharType != CharTypes.CLOSE) {
                            return null;
                        }
                    }

                    break;

                case UNKNOWN:
                    return null;
            }

            previousCharType = charType;
        }

        if (openedParenthesis == closedParenthesis) {
            return info;
        } else {
            return null;
        }
    }
}
