/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import java.awt.Dimension;
import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class steam {

    private static final String FOLDER = "steam";
    private static final String CODES_FILE = FOLDER + "/codes.stm";
    private static final String GAMES_FILE = FOLDER + "/games.stm";
    private static final String PLAYERS_FILE = FOLDER + "/player.stm";

    private static final String DOWNLOADS_FOLDER = FOLDER + "/downloads";
    public RandomAccessFile codesFile;
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
        codesFile.seek(pos);
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
        String rutaImagen = "imagenes/" + nombre + ".png";
        gamesFile.writeUTF(rutaImagen);
        codesFile.seek(0);
        int juegos = codesFile.readInt();
        codesFile.seek(0);
        codesFile.writeInt(juegos + 1);
    }

    public void addPlayer(String username, String password, String nombre, Calendar nacimiento, String tipoUsuario) throws IOException {
        playersFile.seek(playersFile.length());
        int codigo = getCode(4);
        playersFile.writeInt(codigo);
        playersFile.writeUTF(username);
        playersFile.writeUTF(password);
        playersFile.writeUTF(nombre);
        long fecha = nacimiento.getTimeInMillis();
        playersFile.writeLong(fecha);
        playersFile.writeInt(0);
        String rutaImagen = "imagenes/imagenusuario.png";
        playersFile.writeUTF(rutaImagen);
        playersFile.writeUTF(tipoUsuario);
        carpetaJugador(username);
    }
    
    public boolean deletePlayer(int codigo) throws IOException {
        long posOriginal = playersFile.getFilePointer();
        playersFile.seek(0);

        boolean encontrado = false;
        long posicion = 0;

        while (playersFile.getFilePointer() < playersFile.length()) {
            posicion = playersFile.getFilePointer();
            int codigoActual = playersFile.readInt();

            if (codigoActual == codigo) {
                encontrado = true;
                break;
            }

            playersFile.readUTF();
            playersFile.readUTF();
            playersFile.readUTF();
            playersFile.readLong();
            playersFile.readInt();
            playersFile.readUTF();
            playersFile.readUTF();
        }

        if (encontrado) {
            playersFile.seek(posicion);
            playersFile.writeInt(-1);
        }

        playersFile.seek(posOriginal);

        return encontrado;
    }

    private long buscarPlayer(String name, String password) throws IOException {
        playersFile.seek(0);
        while (playersFile.getFilePointer() < playersFile.length()) {
            long pos = playersFile.getFilePointer();
            playersFile.seek(pos);
            playersFile.readInt();
            String user = playersFile.readUTF();
            String pass = playersFile.readUTF();
            playersFile.readUTF();
            playersFile.skipBytes(12);
            playersFile.readUTF();
            playersFile.readUTF();
            if (user.equals(name) && pass.equals(password)) {
                return pos;
            }
        }
        return 0;
    }

    public String getPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password));
        playersFile.readInt();
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.skipBytes(12);
        playersFile.readUTF();
        String tipoUsuario = playersFile.readUTF();
        return tipoUsuario;
    }

    public int codigoPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password));
        int codigo = playersFile.readInt();
        return codigo;
    }
    
    public String usuarioPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password) + 4);
        String username = playersFile.readUTF();
        return username;
    }
    
    public String nombrePlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password) + 4);
        playersFile.readUTF();
        playersFile.readUTF();
        String nombre = playersFile.readUTF();
        return nombre;
    }
    
    public Date fechaPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password) + 4);
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.readUTF();
        long edad = playersFile.readLong();
        Date fecha = new Date(edad);
        return fecha;
    }
    
    public int descargasPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password) + 4);
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.readLong();
        int descargas = playersFile.readInt();
        return descargas;
    }
    
    public String rutaImagenPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password) + 4);
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.skipBytes(12);
        String rutaImagen = playersFile.readUTF();
        return rutaImagen;
    }
    
    public String tipoUsuarioPlayer(String name, String password) throws IOException {
        playersFile.seek(buscarPlayer(name, password) + 4);
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.readUTF();
        playersFile.skipBytes(12);
        playersFile.readUTF();
        String tipoUsuario = playersFile.readUTF();
        return tipoUsuario;
    }
    
    public void listPlayers() throws IOException {
        playersFile.seek(0);
        while (playersFile.getFilePointer() < playersFile.length()) {
            playersFile.seek(playersFile.getFilePointer());
            int code = playersFile.readInt();
            String username = playersFile.readUTF();
            playersFile.readUTF();
            String name = playersFile.readUTF();
            Date dateS = new Date(playersFile.readLong());
            int descargas = playersFile.readInt();
            playersFile.readUTF();
            String tipoUsuario = playersFile.readUTF();
            if (code > 0) {
                JOptionPane.showMessageDialog(null, "Código: " + code 
                                 + "\nNombre: " + name
                                 + "\nUsuario: " + username
                                 + "\nFecha de Contratación: " + dateS
                                 + "\nJuegos Descargados: " + descargas
                                 + "\nTipo de Usuario: " + tipoUsuario + "\n");
            }
        }
    }

    private String direccionJugador(String nombre) {
        return DOWNLOADS_FOLDER + "/" + nombre;
    }

    private void carpetaJugador(String nombre) throws IOException {
        File carpeta = new File(direccionJugador(nombre));
        if(!carpeta.exists()) {
            carpeta.mkdir();
        }
    }
    
    private RandomAccessFile descargasJugador(String nombre) throws IOException {
        String dirPadre = direccionJugador(nombre);
        int descargaCodigo = getCode(8);
        String path = dirPadre + "/download_" + descargaCodigo + ".emp";
        return new RandomAccessFile(path, "rw");
    }
    
    public boolean downloadGame(int gameCode, int playerCode, char os, String usuario) {
        try {
            gamesFile.seek(0);
            boolean juegoExiste = false;
            int edadMin = 0;
            boolean esCompatible = false;

            while (gamesFile.getFilePointer() < gamesFile.length()) {
                gamesFile.seek(gamesFile.getFilePointer());
                int codigo = gamesFile.readInt();
                gamesFile.readUTF();
                char osJuego = gamesFile.readChar();
                edadMin = gamesFile.readInt();
                gamesFile.readDouble();
                gamesFile.skipBytes(4);
                gamesFile.readUTF();

                if (codigo == gameCode) {
                    juegoExiste = true;
                    esCompatible = (osJuego == os);
                    break;
                }
            }

            if (!juegoExiste) {
                JOptionPane.showMessageDialog(null, "El juego con código " + gameCode + " no existe.");
                return false;
            }

            if (!esCompatible) {
                JOptionPane.showMessageDialog(null, "El juego no está disponible para el sistema operativo especificado.");
                return false;
            }
            playersFile.seek(0);
            String username = null;
            boolean jugadorExiste = false;
            int edadJugador = 0;

            while (playersFile.getFilePointer() < playersFile.length()) {
                playersFile.seek(playersFile.getFilePointer());
                int codigo = playersFile.readInt();
                username = playersFile.readUTF();
                playersFile.readUTF();
                playersFile.readUTF();
                long nacimiento = playersFile.readLong();
                playersFile.readInt();
                playersFile.readUTF();
                playersFile.readUTF();

                if (codigo == playerCode) {
                    jugadorExiste = true;
                    Calendar fechaNacimiento = Calendar.getInstance();
                    fechaNacimiento.setTimeInMillis(nacimiento);
                    int añoNacimiento = fechaNacimiento.get(Calendar.YEAR);
                    int añoActual = Calendar.getInstance().get(Calendar.YEAR);
                    edadJugador = añoActual - añoNacimiento;
                    break;
                }
            }

            if (!jugadorExiste) {
                JOptionPane.showMessageDialog(null, "El cliente con código " + playerCode + " no existe.");
                return false;
            }

            if (edadJugador < edadMin) {
                JOptionPane.showMessageDialog(null, "El cliente no cumple con la edad mínima requerida para descargar este juego.");
                return false;
            }
            
            carpetaJugador(usuario);

            RandomAccessFile raf = descargasJugador(usuario);
            raf.seek(0);
            raf.writeLong(Calendar.getInstance().getTimeInMillis());
            raf.writeInt(gameCode);
            raf.writeInt(playerCode);
            raf.writeChar(os);
            JOptionPane.showMessageDialog(null, "Descarga creada exitosamente.");
            while (playersFile.getFilePointer() < playersFile.length()) {
                playersFile.seek(playersFile.getFilePointer());
                int codigo = playersFile.readInt();
                username = playersFile.readUTF();
                playersFile.readUTF();
                playersFile.readUTF();
                long nacimiento = playersFile.readLong();
                int descargas = playersFile.readInt();
                playersFile.seek(playersFile.getFilePointer() - 4);
                playersFile.writeInt(descargas + 1);
                playersFile.readUTF();
                playersFile.readUTF();
            }
            return true;

        } catch (IOException ex) {
            System.out.println("Error al descargar el juego: " + ex.getMessage());
            return false;
        }
    }
    
    public boolean mostrarDescargasJugador(String nombreUsuario) {
        try {
            File carpetaJugador = new File(direccionJugador(nombreUsuario));
            if (!carpetaJugador.exists()) {
                JOptionPane.showMessageDialog(null, "El jugador " + nombreUsuario + " no tiene descargas registradas.");
                return false;
            }

            File[] archivosDescargas = carpetaJugador.listFiles((dir, name) -> name.startsWith("download_") && name.endsWith(".emp"));

            if (archivosDescargas == null || archivosDescargas.length == 0) {
                JOptionPane.showMessageDialog(null, "El jugador " + nombreUsuario + " no tiene descargas registradas.");
                return false;
            }

            StringBuilder infoDescargas = new StringBuilder();
            infoDescargas.append("Descargas del jugador ").append(nombreUsuario).append(":\n\n");

            for (File archivoDescarga : archivosDescargas) {
                RandomAccessFile raf = new RandomAccessFile(archivoDescarga, "r");

                long timestamp = raf.readLong();
                int gameCode = raf.readInt();
                int playerCode = raf.readInt();
                char os = raf.readChar();

                Date fechaDescarga = new Date(timestamp);

                String nombreJuego = tituloJuego(gameCode);

                infoDescargas.append("- Descarga: ").append(archivoDescarga.getName()).append("\n");
                infoDescargas.append("  Fecha: ").append(fechaDescarga).append("\n");
                infoDescargas.append("  Juego: ").append(nombreJuego).append(" (Código: ").append(gameCode).append(")\n");
                infoDescargas.append("  Sistema: ").append(osJuego(os)).append("\n\n");

                raf.close();
            }

            // Mostrar la información en un JTextArea dentro de un JScrollPane
            JTextArea textArea = new JTextArea(infoDescargas.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(null, scrollPane,
                    "Descargas de " + nombreUsuario, JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (IOException ex) {
            System.out.println("Error al mostrar las descargas del jugador: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al leer las descargas del jugador.");
            return false;
        }
    }

    public void updatePriceFor(int code) {
        try {
            gamesFile.seek(0);
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                long pos = gamesFile.getFilePointer();
                gamesFile.seek(pos);
                int codigo = gamesFile.readInt();
                String name = gamesFile.readUTF();
                gamesFile.skipBytes(18);
                int tamaño = gamesFile.readInt();
                byte[] datosImagen = new byte[tamaño];
                gamesFile.readFully(datosImagen);

                if (codigo == code) {
                    gamesFile.seek(pos);
                    gamesFile.skipBytes(4);
                    gamesFile.readUTF();
                    gamesFile.skipBytes(6);
                    double precio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo precio del juego:"));
                    gamesFile.writeDouble(precio);
                    gamesFile.seek(0);
                    System.out.println("Precio actualizado exitosamente para el juego: " + name);
                    return;
                }
            }
            System.out.println("El juego con código " + code + " no existe.");
        } catch (IOException e) {
            System.out.println("Error al actualizar el precio: " + e.getMessage());
        }
    }

    public void reportForClient(int code, String txtFile) {
        try {
            playersFile.seek(0);
            while (playersFile.getFilePointer() < playersFile.length()) {
                int codigo = playersFile.readInt();
                String username = playersFile.readUTF();
                String password = playersFile.readUTF();
                String nombre = playersFile.readUTF();
                long nacimiento = playersFile.readLong();
                Date fecha = new Date(nacimiento);
                int descargas = playersFile.readInt();

                if (codigo == code) {
                    PrintWriter writer = new PrintWriter(new FileWriter(txtFile, false));
                    writer.println("Código del cliente: " + codigo);
                    writer.println("Nombre: " + nombre);
                    writer.println("Username: " + username);
                    writer.println("Fecha de nacimiento: " + fecha);
                    writer.println("Total de descargas: " + descargas);
                    writer.close();

                    JOptionPane.showMessageDialog(null, "REPORTE CREADO");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "NO SE PUEDE CREAR REPORTE");
        } catch (IOException e) {
            System.out.println("Error al crear reporte: " + e.getMessage());
        }
    }

    public String printGames(int code) throws IOException {
        String juegos = "";
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            gamesFile.seek(gamesFile.getFilePointer());
            int codigo = gamesFile.readInt();
            String titulo = gamesFile.readUTF();
            char osJuego = gamesFile.readChar();
            int edadMin = gamesFile.readInt();
            double precio = gamesFile.readDouble();
            int descargas = gamesFile.readInt();
            gamesFile.readUTF();
            if (codigo == code) {
                juegos = "Código: " + codigo + ", Nombre: " + titulo + ", Sistema Operativo: " + osJuego + ", Edad mínima: " + edadMin + ", Precio: " + precio + ", Descargas: " + descargas;
            }
        }
        return juegos;
    }

    public long buscarJuego(int code) throws IOException {
        int codigo;
        gamesFile.seek(0);
        while (gamesFile.getFilePointer() < gamesFile.length()) {
            long pos = gamesFile.getFilePointer();
            gamesFile.seek(pos);
            codigo = gamesFile.readInt();
            gamesFile.readUTF();
            gamesFile.skipBytes(18);
            gamesFile.readUTF();
            if (codigo == code) {
                return pos;
            }
        }
        return 0;
    }

    public int codigoJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code));
        int codigo = gamesFile.readInt();
        return codigo;
    }

    public String tituloJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code) + 4);
        String titulo = gamesFile.readUTF();
        return titulo;
    }

    public char osJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code) + 4);
        gamesFile.readUTF();
        char os = gamesFile.readChar();
        return os;
    }

    public int edadMinJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code) + 4);
        gamesFile.readUTF();
        gamesFile.skipBytes(2);
        int edadMin = gamesFile.readInt();
        return edadMin;
    }

    public double precioJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code) + 4);
        gamesFile.readUTF();
        gamesFile.skipBytes(6);
        double precio = gamesFile.readDouble();
        return precio;
    }

    public int descargasJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code) + 4);
        gamesFile.readUTF();
        gamesFile.skipBytes(14);
        int descargas = gamesFile.readInt();
        return descargas;
    }

    public String rutaImagenJuego(int code) throws IOException {
        gamesFile.seek(buscarJuego(code) + 4);
        gamesFile.readUTF();
        gamesFile.skipBytes(18);
        String rutaImagen = gamesFile.readUTF();
        return rutaImagen;
    }

    public void eliminarTodo() throws IOException {
        codesFile.setLength(0);
        gamesFile.setLength(0);
        playersFile.setLength(0);
    }
}
