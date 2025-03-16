/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SteamGUI extends JFrame {

    private steam steamSystem;
    private int playerCode;
    private String username;
    private String password;

    public SteamGUI() {
        steamSystem = new steam();
        initUI();
        /*try {
            steamSystem.eliminarTodo();
        } catch (IOException ex) {
            Logger.getLogger(SteamGUI.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    private void initUI() {
        setTitle("Steam - Inicio de Sesión");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Usuario:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Contraseña:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(e -> {
            username = userField.getText();
            password = new String(passField.getPassword());

            try {
                String userType = steamSystem.getPlayer(username, password);
                playerCode = steamSystem.codigoPlayer(username, password);

                if (userType != null && userType.equals("Admin") && playerCode > 0) {
                    openAdminPanel();
                    this.dispose();
                } else if (userType != null && userType.equals("Normal") && playerCode > 0) {
                    openUserPanel();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                }
            } catch (IOException ex) {
                Logger.getLogger(SteamGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            openRegisterUserFrame();
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
    }

    private void openAdminPanel() {
        JFrame adminFrame = new JFrame("Panel de Administrador");
        adminFrame.setSize(600, 400);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JButton managePlayersButton = new JButton("Gestionar Usuarios");
        JButton manageGamesButton = new JButton("Gestionar Videojuegos");
        manageGamesButton.addActionListener(e -> {
            openAddGame();
        });
        JButton reportsButton = new JButton("Ver Reportes");
        JButton logoutButton = new JButton("Cerrar Sesión");

        managePlayersButton.addActionListener(e -> openManagePlayersPanel());

        panel.add(managePlayersButton);
        panel.add(manageGamesButton);
        panel.add(reportsButton);
        panel.add(logoutButton);

        adminFrame.add(panel);
        adminFrame.setVisible(true);
    }

    private void openManagePlayersPanel() {
        JFrame playersFrame = new JFrame("Gestionar Usuarios");
        playersFrame.setSize(500, 400);
        playersFrame.setLocationRelativeTo(null);
        playersFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton addUserButton = new JButton("Registrar Usuario");
        JButton modifyUserButton = new JButton("Mostrar Usuarios");
        JButton deleteUserButton = new JButton("Eliminar Usuario");
        JButton backButton = new JButton("Regresar");

        addUserButton.addActionListener(e -> openRegisterUserFrame());
        modifyUserButton.addActionListener(e -> {
            try {
                steamSystem.listPlayers();
            } catch (IOException ex) {
                Logger.getLogger(SteamGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        deleteUserButton.addActionListener(e -> {
            int codigoBorrar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el codigo del jugador que desea eliminar:"));
            try {
                steamSystem.deletePlayer(codigoBorrar);
            } catch (IOException ex) {
                Logger.getLogger(SteamGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.addActionListener(e -> playersFrame.dispose());

        panel.add(addUserButton);
        panel.add(modifyUserButton);
        panel.add(deleteUserButton);
        panel.add(backButton);

        playersFrame.add(panel);
        playersFrame.setVisible(true);
    }

    private void openRegisterUserFrame() {
        JFrame registerFrame = new JFrame("Registrar Usuario");
        registerFrame.setSize(400, 300);
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel nameLabel = new JLabel("Nombre:");
        JTextField nameField = new JTextField();
        JLabel userLabel = new JLabel("Usuario:");
        JTextField userField = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField();
        JLabel dateLabel = new JLabel("Fecha de nacimiento:");
        JTextField dateField = new JTextField();
        JLabel typeLabel = new JLabel("Tipo de usuario:");
        JTextField typeField = new JTextField();

        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String user = userField.getText();
            String password = new String(passwordField.getPassword());
            Calendar date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date fecha = sdf.parse(dateField.getText());
                date.setTime(fecha);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy.");
            }
            String type = null;

            if (typeField.getText().equals("Normal") || typeField.getText().equals("Admin")) {
                type = typeField.getText();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese un tipo de usuario valido: \n1. Normal\n2. Admin");
            }

            if (!name.isEmpty() && !user.isEmpty() && !password.isEmpty() && type != null) {
                try {
                    steamSystem.addPlayer(user, password, name, date, type);
                    JOptionPane.showMessageDialog(registerFrame, "Usuario registrado exitosamente.");
                    registerFrame.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(registerFrame, "Error al registrar el usuario: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(registerFrame, "Por favor, complete todos los campos.");
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(typeLabel);
        panel.add(typeField);
        panel.add(registerButton);

        registerFrame.add(panel);
        registerFrame.setVisible(true);
    }

    private void openAddGame() {
        JFrame addGameFrame = new JFrame("Añadir Juego");
        addGameFrame.setSize(600, 400);
        addGameFrame.setLocationRelativeTo(null);
        addGameFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField nameField = new JTextField();
        JComboBox<String> osComboBox = new JComboBox<>(new String[]{"Windows", "Mac", "Linux"});
        JTextField ageField = new JTextField();
        JTextField priceField = new JTextField();

        panel.add(new JLabel("Nombre del Juego:"));
        panel.add(nameField);

        panel.add(new JLabel("Sistema Operativo (W/M/L):"));
        panel.add(osComboBox);

        panel.add(new JLabel("Edad Mínima:"));
        panel.add(ageField);

        panel.add(new JLabel("Precio:"));
        panel.add(priceField);

        JButton addButton = new JButton("Agregar Juego");
        panel.add(addButton);

        addGameFrame.add(panel);
        addGameFrame.setVisible(true);

        addButton.addActionListener(e -> {
            try {
                String nombre = nameField.getText();
                char so = osComboBox.getSelectedItem().toString().charAt(0);
                int edadMin = Integer.parseInt(ageField.getText());
                double precio = Double.parseDouble(priceField.getText());

                steamSystem.addGame(nombre, so, edadMin, precio);

                JOptionPane.showMessageDialog(null, "Juego agregado exitosamente.");
                addGameFrame.dispose(); // Cerrar la ventana después de agregar el juego
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingresa números válidos para Edad y Precio.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar el juego: " + ex.getMessage());
            }
        });
    }

    private void openUserPanel() {
        JFrame userFrame = new JFrame("Panel de Usuario Normal");
        userFrame.setSize(600, 400);
        userFrame.setLocationRelativeTo(null);
        userFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton viewCatalogButton = new JButton("Ver Catálogo de Juegos");
        viewCatalogButton.addActionListener(e -> {
            catalogPanel();
        });
        JButton downloadsButton = new JButton("Juegos Descargados");
        downloadsButton.addActionListener(e -> {
            steamSystem.mostrarDescargasJugador(username);
        });
        JButton configureProfileButton = new JButton("Configurar Perfil");
        configureProfileButton.addActionListener(e -> {
            profilePanel();
        });

        panel.add(viewCatalogButton);
        panel.add(downloadsButton);
        panel.add(configureProfileButton);

        userFrame.add(panel);
        userFrame.setVisible(true);
    }
    
    private void profilePanel() {
        try {
            JFrame infoFrame = new JFrame("Información del Juego");
            infoFrame.setSize(1000, 400);
            infoFrame.setLayout(new BorderLayout(2, 1));
            infoFrame.setLocationRelativeTo(this);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panelInfo = new JPanel(new GridLayout(6, 2, 10, 10));

            JLabel codeLabel = new JLabel("Código:");
            JLabel code = new JLabel(String.valueOf(steamSystem.codigoPlayer(username, password)));
            JLabel nameLabel = new JLabel("Nombre:");
            JLabel name = new JLabel(steamSystem.nombrePlayer(username, password));
            JLabel userLabel = new JLabel("Usuario:");
            JLabel user = new JLabel(steamSystem.usuarioPlayer(username, password));
            JLabel dateLabel = new JLabel("Fecha de nacimiento:");
            JLabel date = new JLabel(String.valueOf(steamSystem.fechaPlayer(username, password)));
            JLabel downloadsLabel = new JLabel("Juegos descargados:");
            JLabel downloads = new JLabel(String.valueOf(steamSystem.descargasPlayer(username, password)));
            JLabel typeLabel = new JLabel("Tipo de usuario:");
            JLabel type = new JLabel(steamSystem.tipoUsuarioPlayer(username, password));

            panelInfo.add(codeLabel);
            panelInfo.add(code);
            panelInfo.add(nameLabel);
            panelInfo.add(name);
            panelInfo.add(userLabel);
            panelInfo.add(user);
            panelInfo.add(dateLabel);
            panelInfo.add(date);
            panelInfo.add(downloadsLabel);
            panelInfo.add(downloads);
            panelInfo.add(typeLabel);
            panelInfo.add(type);

            JPanel panelImagenDescarga = new JPanel();
            panelImagenDescarga.setLayout(new BorderLayout());
            String rutaImagen = steamSystem.rutaImagenPlayer(username, password);
            if (rutaImagen != null) {
                ImageIcon imageIcon = new ImageIcon(rutaImagen);
                JLabel imageLabel = new JLabel(imageIcon);
                panelImagenDescarga.add(imageLabel, BorderLayout.CENTER);
                JButton delete = new JButton("Eliminar Cuenta");
                delete.addActionListener(e -> {
                    try {
                        steamSystem.deletePlayer(playerCode);
                        this.dispose();
                    } catch (IOException ex) {
                        Logger.getLogger(SteamGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                panelImagenDescarga.add(delete, BorderLayout.SOUTH);
            }

            infoFrame.add(panelInfo, BorderLayout.WEST);
            infoFrame.add(panelImagenDescarga, BorderLayout.EAST);
            infoFrame.setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener la información del juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void catalogPanel() {
        JFrame catalogFrame = new JFrame("Catálogo de Videojuegos");
        catalogFrame.setSize(600, 400);
        catalogFrame.setLocationRelativeTo(null);
        catalogFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(new JScrollPane(panel), BorderLayout.CENTER);
        try {
            steamSystem.codesFile.seek(0);
            for (int i = 0; i <= steamSystem.codesFile.readInt(); i++) {
                int codigo = i;
                String juegoInfo = steamSystem.printGames(codigo);
                if (!juegoInfo.isEmpty()) {
                    JButton btnJuego = new JButton(juegoInfo);
                    btnJuego.addActionListener(e -> {
                        pantallaJuego(codigo);
                    });
                    panel.add(btnJuego);
                }
            }
            panel.revalidate();
            panel.repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        catalogFrame.add(new JScrollPane(panel));
        catalogFrame.setVisible(true);
    }

    private void pantallaJuego(int codigo) {
        try {
            JFrame infoFrame = new JFrame("Información del Juego");
            infoFrame.setSize(800, 400);
            infoFrame.setLayout(new BorderLayout(2, 1));
            infoFrame.setLocationRelativeTo(this);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panelInfo = new JPanel(new GridLayout(6, 2, 10, 10));

            JLabel codeLabel = new JLabel("Código:");
            JLabel code = new JLabel(String.valueOf(steamSystem.codigoJuego(codigo)));
            JLabel titleLabel = new JLabel("Título:");
            JLabel title = new JLabel(steamSystem.tituloJuego(codigo));
            JLabel soLabel = new JLabel("Sistema Operativo:");
            JLabel so = new JLabel(String.valueOf(steamSystem.osJuego(codigo)));
            JLabel ageLabel = new JLabel("Edad Mínima:");
            JLabel age = new JLabel(String.valueOf(steamSystem.edadMinJuego(codigo)));
            JLabel priceLabel = new JLabel("Precio:");
            JLabel price = new JLabel(String.valueOf(steamSystem.precioJuego(codigo)));
            JLabel downloadsLabel = new JLabel("Descargas:");
            JLabel downloads = new JLabel(String.valueOf(steamSystem.descargasJuego(codigo)));

            panelInfo.add(codeLabel);
            panelInfo.add(code);
            panelInfo.add(titleLabel);
            panelInfo.add(title);
            panelInfo.add(soLabel);
            panelInfo.add(so);
            panelInfo.add(ageLabel);
            panelInfo.add(age);
            panelInfo.add(priceLabel);
            panelInfo.add(price);
            panelInfo.add(downloadsLabel);
            panelInfo.add(downloads);
            
            JPanel panelImagenDescarga = new JPanel();
            panelImagenDescarga.setLayout(new BorderLayout());
            String rutaImagen = steamSystem.rutaImagenJuego(codigo);
            if (rutaImagen != null) {
                ImageIcon imageIcon = new ImageIcon(rutaImagen);
                JLabel imageLabel = new JLabel(imageIcon);
                panelImagenDescarga.add(imageLabel, BorderLayout.CENTER);
                JButton descargar = new JButton("Descargar Juego");
                descargar.addActionListener(e -> {
                    try {
                        steamSystem.downloadGame(steamSystem.codigoJuego(codigo), playerCode, steamSystem.osJuego(codigo), username);
                    } catch (IOException ex) {
                        Logger.getLogger(SteamGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                panelImagenDescarga.add(descargar, BorderLayout.SOUTH);
            }
            
            infoFrame.add(panelInfo, BorderLayout.WEST);
            infoFrame.add(panelImagenDescarga, BorderLayout.EAST);
            infoFrame.setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener la información del juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
