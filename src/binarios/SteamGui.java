/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SteamGui extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel, adminPanel, normalUserPanel;
    private JButton btnLogin, btnRegister, btnLogoutAdmin, btnLogoutUser;
    //private JButton btnRegisterPlayer, btnModifyPlayer, btnDeletePlayer;
    private JButton btnAddGame, btnModifyGame, btnDeleteGame;
    private JButton btnViewCatalog, btnDownloadGame, btnViewProfile;
    private JButton btnViewReports;
    private String currentUsername;

    private ArrayList<User> users;
    steam s = new steam();

    public SteamGui() {

        setTitle("Steam");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        users = new ArrayList<>();
        users.add(new User("admin", "admin", "admin")); // Usuario administrador por defecto

        loginPanel = createLoginPanel();

        adminPanel = createAdminPanel();

        normalUserPanel = createNormalUserPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(normalUserPanel, "user");

        add(mainPanel);

        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Registrar");

        btnLogin.addActionListener(e -> handleLogin(txtUsername.getText(), new String(txtPassword.getPassword())));
        btnRegister.addActionListener(e -> handleRegister(txtUsername.getText(), new String(txtPassword.getPassword())));

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(new JLabel());
        panel.add(btnLogin);
        panel.add(new JLabel());
        panel.add(btnRegister);

        return panel;
    }

    private JPanel createAdminPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(4, 1));

        btnAddGame = new JButton("Agregar Juego");
        btnModifyGame = new JButton("Modificar juego");
        btnDeleteGame = new JButton("Borrar juego");
        btnViewReports = new JButton("Ver reportes ");

        leftPanel.add(btnAddGame);
        leftPanel.add(btnModifyGame);
        leftPanel.add(btnDeleteGame);
        leftPanel.add(btnViewReports);

        JPanel rightPanel = new JPanel();
        JButton btnConfigureAdmin = new JButton("Configuraciones");
        rightPanel.add(btnConfigureAdmin);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        panel.add(splitPane, BorderLayout.CENTER);

        btnLogoutAdmin = new JButton("Logout");
        panel.add(btnLogoutAdmin, BorderLayout.SOUTH);

        btnLogoutAdmin.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        btnAddGame.addActionListener(e -> agregarJuego());

        return panel;
    }

    private void agregarJuego() {
        try {

            String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del juego:", "Agregar Juego", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String soInput = JOptionPane.showInputDialog(this, "Ingrese el sistema operativo (W para Windows, M para Mac, L para Linux):", "Agregar Juego", JOptionPane.QUESTION_MESSAGE);
            if (soInput == null || soInput.length() != 1 || !"WML".contains(soInput.toUpperCase())) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un carácter válido: W, M o L.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            char so = soInput.toUpperCase().charAt(0);

            String edadMinInput = JOptionPane.showInputDialog(this, "Ingrese la edad mínima requerida:", "Agregar Juego", JOptionPane.QUESTION_MESSAGE);
            if (edadMinInput == null || !edadMinInput.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una edad mínima válida (número entero).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int edadMin = Integer.parseInt(edadMinInput);

            String precioInput = JOptionPane.showInputDialog(this, "Ingrese el precio del juego:", "Agregar Juego", JOptionPane.QUESTION_MESSAGE);
            if (precioInput == null || !precioInput.matches("\\d+(\\.\\d{1,2})?")) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un precio válido (número decimal).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio = Double.parseDouble(precioInput);

            s.addGame(nombre, so, edadMin, precio);

            JOptionPane.showMessageDialog(this, "El juego se agregó correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al agregar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createNormalUserPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(4, 1));
        btnViewCatalog = new JButton("Ver juegos");
        btnDownloadGame = new JButton("Descargar Juego");
        btnViewProfile = new JButton("Ver perfil");
        JButton btnDeleteAccount = new JButton("Borrar cuenta");

        leftPanel.add(btnViewCatalog);
        leftPanel.add(btnDownloadGame);
        leftPanel.add(btnViewProfile);
        leftPanel.add(btnDeleteAccount);

        JPanel rightPanel = new JPanel();
        JButton btnConfigure = new JButton("Configuracion");
        rightPanel.add(btnConfigure);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        panel.add(splitPane, BorderLayout.CENTER);

        btnLogoutUser = new JButton("Logout");
        panel.add(btnLogoutUser, BorderLayout.SOUTH);

        btnLogoutUser.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        btnDeleteAccount.addActionListener(e -> handleDeleteAccount());

        return panel;
    }

    private void handleDeleteAccount() {

        int response = JOptionPane.showConfirmDialog(this,
                "seguro desea borrar la cuena?",
                "Confirmar borrar",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {

            User currentUser = findUser(currentUsername);

            if (currentUser != null) {

                users.remove(currentUser);

                JOptionPane.showMessageDialog(this, "cuenta borrada exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);

                currentUsername = null;

                cardLayout.show(mainPanel, "login");
            } else {

                JOptionPane.showMessageDialog(this, "Error: Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleLogin(String username, String password) {

        User user = findUser(username, password);
        if (user != null) {
            currentUsername = user.getUsername();
            if (user.getRole().equals("admin")) {
                cardLayout.show(mainPanel, "admin");
            } else {
                cardLayout.show(mainPanel, "user");
            }
        } else {
            JOptionPane.showMessageDialog(this, "invalido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ingresar contrasena o usuario valido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (findUser(username) != null) {
            JOptionPane.showMessageDialog(this, "usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            users.add(new User(username, password, "user"));
            JOptionPane.showMessageDialog(this, "Usuario ya registrado exitosamente!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private User findUser(String username, String password) {

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private User findUser(String username) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SteamGui gui = new SteamGui();
            gui.setVisible(true);
        });
    }
}

class User {

    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
