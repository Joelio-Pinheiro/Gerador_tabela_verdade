/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tabelaverdade.Model;

/**
 *
 * @author joeli
 */
import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class FormulaRepository {
    private static HashSet<String> savedFormulas = new HashSet<>();
    private static final String filename = "FormulasSalvas.ser";

    public static HashSet<String> getSavedFormulas() {
        return savedFormulas;
    }

    public static void saveFormula(String formula) throws Exception {
        if (savedFormulas.contains(formula)) {
            throw new Exception("Fórmula já salva!");
        } else {
            savedFormulas.add(formula);
            serializeFormulas();
        }
    }

    public static void deleteFormula(String formula) {
        if (savedFormulas.contains(formula)) {
            savedFormulas.remove(formula);
            serializeFormulas();
        }
    }

    public static ArrayList loadFormulas() {
        deserializeFormulas();
         return new ArrayList<String>(getSavedFormulas());
    }

    private static void serializeFormulas() {
        try (FileOutputStream file = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(savedFormulas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deserializeFormulas() {
        try (FileInputStream file = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(file)) {
            savedFormulas = (HashSet<String>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearSerializedFormulas() {
        savedFormulas = new HashSet<>();
        serializeFormulas();
    }
}
