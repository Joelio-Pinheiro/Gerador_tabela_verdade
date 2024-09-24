/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tabelaverdade.Controller;

/**
 *
 * @author joeli
 */
import com.mycompany.tabelaverdade.Model.FormulaRepository;
import java.util.ArrayList;
import com.mycompany.tabelaverdade.Model.Formula;
import com.mycompany.tabelaverdade.Model.FormulaValidator;
import com.mycompany.tabelaverdade.Model.LogicValue;
import com.mycompany.tabelaverdade.utils.Util;
import javax.swing.table.DefaultTableModel;
import com.mycompany.tabelaverdade.View.ResultadoExpressao;
import java.util.HashMap;
import java.util.HashSet;

public class CalculadoraController {
    
    public static final int MAX_VARIAVEIS = 5;
    public Formula formula = null;
    private String[] variables;
    private String[][] variablesTruthValues;
    
    
    
    public boolean isFormulaAlreadyOpen(HashSet<String> openedFormulas, String formula) {
        return openedFormulas.contains(formula);
    }

    public String[] extractVariables(Formula formula) throws Exception {
        FormulaValidator.ValidationInfo formulaInfos = formula.getFormulaInfos();
        HashSet<String> variablesSet = new HashSet<>();

        for (Character c : formulaInfos.atomicIdxs.values()) {
            variablesSet.add(c.toString());
        }

        if (variablesSet.size() > MAX_VARIAVEIS) {
            throw new Exception("Quantidade de fórmulas atômicas deve ser menor ou igual a 5!");
        }

        return variablesSet.toArray(new String[0]);
    }

    public String[][] populateVariablesTruthValues(String[] variables) {
        String[][] truthValues = new String[(int) Math.pow(2, variables.length)][variables.length];
        int totalIterationsPerRow = (int) Math.pow(2, variables.length);

        for (int i = 0; i < variables.length; i++) {
            int interval = (int) Math.pow(2, variables.length - (i + 1));
            String value = "Verdadeiro";

            int counter = 0;
            for (int j = 0; j < totalIterationsPerRow; j++) {
                if (counter == interval) {
                    value = value.equals("Verdadeiro") ? "Falso" : "Verdadeiro";
                    counter = 0;
                }

                truthValues[j][i] = value;
                counter += 1;
            }
        }
        return truthValues;
    }

    public void evaluateFormula(Formula formula, String[] variables, String[][] variablesTruthValues, DefaultTableModel tableModel) {
        if (variablesTruthValues == null) return;

        for (int row = 0; row < variablesTruthValues.length; row++) {
            HashMap<Character, LogicValue> atomicValues = new HashMap<>();

            for (int column = 0; column < variables.length; column++) {
                int logicValue = tableModel.getValueAt(row, column).equals("Verdadeiro") ? 1 : 0;
                atomicValues.put(variables[column].charAt(0), new LogicValue(logicValue));
            }

            LogicValue result = formula.evaluate(atomicValues);
            tableModel.setValueAt(result.get() == 1 ? "Verdadeiro" : "Falso", row, variables.length);
        }
    }
    
    
    public void saveFormula(String formula) {
        try {
            FormulaRepository.saveFormula(formula);
            Util.WARNING("Fórmula salva com sucesso!");
        } catch (Exception e) {
            Util.ERROR(e.getMessage());
        }
    }

    public void deleteFormula(String formula) {
        FormulaRepository.deleteFormula(formula);
    }

    public ArrayList<String> loadFormulas() {
        return FormulaRepository.loadFormulas();
    }
}
