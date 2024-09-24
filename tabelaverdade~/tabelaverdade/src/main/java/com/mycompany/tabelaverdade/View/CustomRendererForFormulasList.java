/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tabelaverdade.View;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

/**
 *
 * @author luizc
 */
public class CustomRendererForFormulasList extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        label.setForeground(Color.WHITE);

        if (isSelected) {
            label.setBackground(Color.GREEN);
            label.setForeground(Color.BLACK);
        }
        else{
            label.setBackground(Color.RED);
        }
        
        return label;
    }
}
