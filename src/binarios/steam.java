/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
            if (!steamFolder.exists()) {
                steamFolder.mkdir();
            } else {
                File downloads = new File(FOLDER + "/downloads");
                downloads.mkdir();
            }

            codesFile = new RandomAccessFile(CODES_FILE, "rw");
            gamesFile = new RandomAccessFile(GAMES_FILE, "rw");
            playersFile = new RandomAccessFile(PLAYERS_FILE, "rw");

            initCodes();
        } catch (IOException e) {
            System.out.println("Error al inicializar Steam: " + e.getMessage());
        }
    }

    private void initCodes() throws IOException {
        if (codesFile.length() == 0) {
            codesFile.writeInt(1);
            codesFile.writeInt(1);
            codesFile.writeInt(1);
        }
    }

    private int getCode(int pos) throws IOException {
        codesFile.seek(0);
        codesFile.skipBytes(pos);
        int code = codesFile.readInt();
        codesFile.seek(0);
        codesFile.writeInt(code + 1);
        return code;
    }
    
    public void addGame(String nombre, char so, int edadMin, double precio) throws IOException {
        gamesFile.seek(gamesFile.length());
        int codigo = getCode(0);
        gamesFile.writeInt(codigo);
        gamesFile.writeUTF(nombre);
        gamesFile.writeChar(so);
        gamesFile.writeInt(edadMin);
        gamesFile.writeDouble(precio);
        gamesFile.writeInt(0);
        byte[] imagenBytes = cargarImagen(nombre);
        if (imagenBytes != null) {
            gamesFile.writeInt(imagenBytes.length);
            gamesFile.write(imagenBytes);
        } else {
            gamesFile.writeInt(0);
        }
    }
    
    private byte[] cargarImagen(String nombre) {
        File imagen = new File("imagenes/" + nombre + ".jpg");
        byte[] buffer = null;
        try (FileInputStream fis = new FileInputStream(imagen)) {
            buffer = new byte[(int) imagen.length()];
            fis.read(buffer);
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen: " + nombre + ".jpg");
        }
        return buffer;
    }
}
