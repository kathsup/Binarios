/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package binarios;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SteamGui extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel, adminPanel, normalUserPanel;
    private JButton btnLogin, btnRegister, btnLogoutAdmin, btnLogoutUser;
    private JButton btnRegisterPlayer, btnModifyPlayer, btnDeletePlayer;
    private JButton btnAddGame, btnModifyGame, btnDeleteGame;
    private JButton btnViewCatalog, btnDownloadGame, btnViewProfile;
    private JButton btnViewReports, btnConfigure;
    private Image imagenUsuario;

    private ArrayList<User> users;

    public SteamGui() {
        setTitle("Sistema de Administración de Juego");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imagenUsuario = new ImageIcon("C:/Users/50494/OneDrive/Documents/NetBeansProjects/Binarios/imagenes/imagenusuario.png").getImage();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        users = new ArrayList<>();
        users.add(new User("admin", "admin", "admin"));

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

        JLabel lblUsername = new JLabel("Usuario:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();
        btnLogin = new JButton("Ingresar");
        btnRegister = new JButton("Registrarse");

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
        JPanel panel = new JPanel(new GridLayout(8, 1));

        btnRegisterPlayer = new JButton("Registrar Jugador");
        btnModifyPlayer = new JButton("Modificar Jugador");
        btnDeletePlayer = new JButton("Eliminar Jugador");
        btnAddGame = new JButton("Añadir Juego");
        btnModifyGame = new JButton("Modificar Juego");
        btnDeleteGame = new JButton("Eliminar Juego");
        btnViewReports = new JButton("Mostrar Reportes");
        btnLogoutAdmin = new JButton("Logout");

        panel.add(btnRegisterPlayer);
        panel.add(btnModifyPlayer);
        panel.add(btnDeletePlayer);
        panel.add(btnAddGame);
        panel.add(btnModifyGame);
        panel.add(btnDeleteGame);
        panel.add(btnViewReports);
        panel.add(btnLogoutAdmin);

        btnLogoutAdmin.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    private JPanel createNormalUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        btnViewCatalog = new JButton("Catálogo de Juego");
        btnDownloadGame = new JButton("Descargar Juego");
        btnViewProfile = new JButton("Ver Perfil");

        leftPanel.add(btnViewCatalog);
        leftPanel.add(btnDownloadGame);
        leftPanel.add(btnViewProfile);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new ImagePanel(), BorderLayout.CENTER);

        btnConfigure = new JButton("Configurar Botón");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnConfigure);

        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        panel.add(splitPane, BorderLayout.CENTER);

        btnLogoutUser = new JButton("Logout");
        panel.add(btnLogoutUser, BorderLayout.SOUTH);

        btnLogoutUser.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenUsuario != null) {
                int width = getWidth() / 2;
                int height = getHeight() / 2;
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 4;

                g.drawImage(imagenUsuario, x, y, width, height, this);
            }
        }
    }

    private void handleLogin(String username, String password) {
        User user = findUser(username, password);
        if (user != null) {
            if (user.getRole().equals("admin")) {
                cardLayout.show(mainPanel, "admin");
            } else {
                cardLayout.show(mainPanel, "user");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales Inválidas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un usuario o una contraseña válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (findUser(username) != null) {
            JOptionPane.showMessageDialog(this, "Usuario existente.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            users.add(new User(username, password, "user"));
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
}