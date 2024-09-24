/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tabelaverdade.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author luizc
 */
public class Util {
    public static void ERROR(String message){
        JFrame f = new JFrame();
        f.setAlwaysOnTop(true);
        f.requestFocus();
        
        JOptionPane.showMessageDialog(f, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void WARNING(String message){
        JFrame f = new JFrame();
        f.setAlwaysOnTop(true);
        f.requestFocus();
        
        
        JOptionPane.showMessageDialog(f, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
