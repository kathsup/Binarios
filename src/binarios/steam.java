/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author 50494
 */
public class steam {
    private static final String FOLDER = "steam";
    private static final String CODES_FILE = FOLDER + "/codes.stm";
    private static final String GAMES_FILE = FOLDER + "/games.stm";
    private static final String PLAYERS_FILE = FOLDER + "/player.stm";
    
    private RandomAccessFile codesFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playersFile;

    public steam() {
        try {
            File steamFolder = new File(FOLDER);
            if (!steamFolder.exists()){ 
                steamFolder.mkdir();
            }
            else{
                File downloads = new File(FOLDER + "/downloads");
                downloads.mkdir();
            }
          
            codesFile = new RandomAccessFile(CODES_FILE, "rw");
            gamesFile = new RandomAccessFile(GAMES_FILE, "rw");
            playersFile = new RandomAccessFile(PLAYERS_FILE, "rw");

            if (codesFile.length() == 0) {
                codesFile.writeInt(1);
                codesFile.writeInt(1);
                codesFile.writeInt(1);
            }
        } catch (IOException e) {
            System.out.println("Error al inicializar Steam: " + e.getMessage());
        }
    }
  }

