/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package binarios;

import javax.swing.SwingUtilities;

/**
 *
 * @author Lenovo
 */
public class Binarios {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SteamGui gui = new SteamGui();
            gui.setVisible(true);
        });
    }
}

