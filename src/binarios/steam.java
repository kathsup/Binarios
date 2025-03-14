/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class steam {

    private static final String FOLDER = "steam";
    private static final String CODES_FILE = FOLDER + "/codes.stm";
    private static final String GAMES_FILE = FOLDER + "/games.stm";
    private static final String PLAYERS_FILE = FOLDER + "/player.stm";
    private static final String DOWNLOADS_FOLDER = FOLDER + "/downloads";

    private RandomAccessFile codesFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playersFile;

    public steam() {
        try {
            File steamFolder = new File(FOLDER);
            if (!steamFolder.exists()) {
                steamFolder.mkdir();
            }

            File downloadsFolder = new File(DOWNLOADS_FOLDER);
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdir();
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
    
    public void addPlayer(Calendar nacimiento) {
        try {
            codesFile.seek(4);
            int playerCode = codesFile.readInt();
            codesFile.seek(4);
            codesFile.writeInt(playerCode + 1);

            playersFile.seek(playersFile.length());
            playersFile.writeInt(playerCode);
            playersFile.writeInt(nacimiento.get(Calendar.YEAR));
            playersFile.writeInt(nacimiento.get(Calendar.MONTH));
            playersFile.writeInt(nacimiento.get(Calendar.DAY_OF_MONTH));
            playersFile.writeInt(0);

            System.out.println("Jugador agregado con éxito. Código: " + playerCode);
        } catch (IOException e) {
            System.out.println("Error al agregar jugador: " + e.getMessage());
        }
    }

    public boolean downloadGame(int gameCode, int playerCode, String os) {
        try {
            gamesFile.seek(0);
            boolean gameFound = false;
            int minAge = 0;
            boolean availableWindows = false, availableMac = false, availableLinux = false;

            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int currentGameCode = gamesFile.readInt();
                minAge = gamesFile.readInt();
                availableWindows = gamesFile.readBoolean();
                availableMac = gamesFile.readBoolean();
                availableLinux = gamesFile.readBoolean();

                if (currentGameCode == gameCode) {
                    gameFound = true;
                    break;
                }
            }

            if (!gameFound) {
                System.out.println("Error!! El videojuego no existe.");
                return false;
            }

            boolean osCompatible = switch (os.toLowerCase()) {
                case "windows" -> availableWindows;
                case "mac" -> availableMac;
                case "linux" -> availableLinux;
                default -> false;
            };

            if (!osCompatible) {
                System.out.println("Error!! El videojuego no es compatible con " + os);
                return false;
            }

            playersFile.seek(0);
            boolean playerFound = false;
            int birthYear = 0, birthMonth = 0, birthDay = 0, downloadCount = 0;

            while (playersFile.getFilePointer() < playersFile.length()) {
                int currentPlayerCode = playersFile.readInt();
                birthYear = playersFile.readInt();
                birthMonth = playersFile.readInt();
                birthDay = playersFile.readInt();
                downloadCount = playersFile.readInt();

                if (currentPlayerCode == playerCode) {
                    playerFound = true;
                    break;
                }
            }

            if (!playerFound) {
                System.out.println("Error!! El cliente no existe.");
                return false;
            }

            Calendar today = Calendar.getInstance();
            int currentYear = today.get(Calendar.YEAR);
            int currentMonth = today.get(Calendar.MONTH);
            int currentDay = today.get(Calendar.DAY_OF_MONTH);

            int age = currentYear - birthYear;
            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age--;
            }

            if (age < minAge) {
                System.out.println("Error: El jugador no cumple con la edad mínima requerida.");
                return false;
            }

            codesFile.seek(8);
            int downloadCode = codesFile.readInt();
            codesFile.seek(8);
            codesFile.writeInt(downloadCode + 1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(today.getTime());

            String downloadFileName = DOWNLOADS_FOLDER + "/download_" + downloadCode + ".stm";
            RandomAccessFile df = new RandomAccessFile(downloadFileName, "rw");
            df.seek(0);
            df.writeBytes("[FECHA DE DOWNLOAD: " + formattedDate + "]\n");
            df.writeBytes("Código de descarga: " + downloadCode + "\n");
            df.writeBytes("Código de juego: " + gameCode + "\n");
            df.writeBytes("Código de cliente: " + playerCode + "\n");
            df.writeBytes("Sistema operativo: " + os + "\n");
            df.close();

            long playerPosition = playersFile.getFilePointer() - 4;
            playersFile.seek(playerPosition);
            playersFile.writeInt(downloadCount + 1);

            System.out.println("Descarga registrada: " + downloadFileName);
            return true;
        } catch (IOException e) {
            System.out.println("Error al descargar juego: " + e.getMessage());
            return false;
        }
    }
}
