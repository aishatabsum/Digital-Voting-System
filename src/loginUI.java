
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class loginUI extends JFrame {
    private JTextField txtStudentId;
    private JPasswordField txtPassword;
    private VoterDAO vDao = new VoterDAO();

    // --- COORDINATED COOL-TECH COLOR PALETTE ---
    private final Color COLOR_BG         = new Color(248, 249, 250); // Soft pearl white
    private final Color COLOR_CARD_BG    = new Color(255, 255, 255); // Pure white card
    private final Color COLOR_PRIMARY    = new Color(43, 108, 176);  // Deep corporate blue
    private final Color COLOR_PRIMARY_HOVER = new Color(31, 80, 134);
    private final Color COLOR_TEXT_MAIN  = new Color(45, 55, 72);    // Dark slate gray
    private final Color COLOR_BORDER     = new Color(226, 232, 240); // Soft border gray

    // --- TYPOGRAPHY ---
    private final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_FIELD    = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 15);

    public loginUI() {
        // Frame Setup
        setTitle("Secure User Authentication");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Base Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        setContentPane(mainPanel);

        // 1. --- HEADER AREA ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setBackground(COLOR_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("E-Voting Portal", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT_MAIN);

        JLabel lblSub = new JLabel("UNIVERSITY ELECTORAL AUTHENTICATION", SwingConstants.CENTER);
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(new Color(51,51,0));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. --- CREDENTIAL CARD CONTAINER ---
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(0,51,102));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(30, 25, 30, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Student ID Label
        JLabel lblId = new JLabel("STUDENT IDENTIFICATION NUMBER");
        lblId.setFont(FONT_LABEL);
        lblId.setForeground(new Color(204,255,255));
        gbc.insets = new Insets(0, 0, 6, 0);
        cardPanel.add(lblId, gbc);
        gbc.gridy++;

        // Student ID Input Field
        txtStudentId = new JTextField();
        styleInputField(txtStudentId);
        gbc.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(txtStudentId, gbc);
        gbc.gridy++;

        // Password Label
        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setFont(FONT_LABEL);
        lblPass.setForeground(new Color(204,255,255));
        gbc.insets = new Insets(0, 0, 6, 0);
        cardPanel.add(lblPass, gbc);
        gbc.gridy++;

        // Password Input Field
        txtPassword = new JPasswordField();
        styleInputField(txtPassword);
        gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(txtPassword, gbc);
        gbc.gridy++;

        // Authenticate Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(FONT_BUTTON);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(btnLogin.getPreferredSize().width, 45));

        // Smooth Button Hover FX
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnLogin.setBackground(COLOR_PRIMARY_HOVER); }
            @Override public void mouseExited(MouseEvent e) { btnLogin.setBackground(COLOR_PRIMARY); }
        });

        btnLogin.addActionListener(e -> handleLoginLogic());
        cardPanel.add(btnLogin, gbc);

        mainPanel.add(cardPanel, BorderLayout.CENTER);
    }

    private void styleInputField(JTextField field) {
        field.setFont(FONT_FIELD);
        field.setForeground(COLOR_TEXT_MAIN);
        field.setCaretColor(COLOR_PRIMARY);
        field.setBackground(COLOR_CARD_BG);
        // Clean line borders instead of default 3D boxes
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    private void handleLoginLogic() {
        String inputId = txtStudentId.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (inputId.isEmpty() || password.isEmpty()) {
            showCustomMessage("Input Missing", "Please fill both authentication fields.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int studentId = Integer.parseInt(inputId);
            voter user = vDao.login(studentId, password);

            if (user != null) {
                
                // Smooth transition directly to our new dashboard frame
                new DashboardUI(user).setVisible(true);
                this.dispose();
            } else {
                showCustomMessage("Access Denied", "Invalid credential for user. Please retry.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showCustomMessage("Format Error", "Identification index must consist exclusively of integers.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCustomMessage(String title, String message, int messageType) {
        UIManager.put("OptionPane.messageFont", FONT_FIELD);
        UIManager.put("OptionPane.background", COLOR_CARD_BG);
        UIManager.put("Panel.background", COLOR_CARD_BG);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        // Enforce smooth font rendering system-wide
        System.setProperty("awtextra.absolutePositioning", "false");
        SwingUtilities.invokeLater(() -> new loginUI().setVisible(true));
    }
}