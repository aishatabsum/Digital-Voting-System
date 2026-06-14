

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class DashboardUI extends JFrame {
    private voter currentUser;
    private Election currentElection;
    private ElectionDAO eDao = new ElectionDAO();
    private VoteDAO vDao = new VoteDAO();
    private VotingService service = new VotingService();
    private VoterDAO voterDao= new VoterDAO();
   

    // --- MODERN COOL-TECH COLOR PALETTE ---
    private final Color COLOR_BG         = new Color(248, 249, 250); // Soft pearl white
    private final Color COLOR_CARD_BG    = new Color(255, 255, 255); // Crisp pure white
    private final Color COLOR_PRIMARY    = new Color(43, 108, 176);  // Deep corporate blue
    private final Color COLOR_SUCCESS    = new Color(51, 153, 102);   //  green
    private final Color COLOR_SUCCESS_HOVER = new Color(29, 131, 71);
    private final Color COLOR_DANGER     = new Color(200, 89, 90);   // Crimson red
    private final Color COLOR_DANGER_HOVER = new Color(190, 58, 54);
    private final Color COLOR_TEXT_MAIN  = new Color(45, 55, 72);    // Dark slate gray
    private final Color COLOR_BORDER     = new Color(226, 232, 240); // Soft border gray

    // --- TYPOGRAPHY ---
    private final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 14);

    public DashboardUI(voter user) {
        this.currentUser = user;
        this.currentElection = eDao.getActiveElection(user.getDeptId());

        // Frame Setup
        setTitle("E-Voting Portal ");
        setSize(460, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       setResizable(false);

        // Main Wrapper
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        mainPanel.setBackground(Color.GRAY);
        setContentPane(mainPanel);

        // 1. --- HEADER SECTION ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        headerPanel.setBackground(COLOR_BG);
        
        JLabel lblWelcome = new JLabel("Welcome  " + currentUser.getName(), SwingConstants.CENTER);
        lblWelcome.setFont(FONT_HEADING);
        lblWelcome.setForeground(COLOR_TEXT_MAIN);
        
        JLabel lblRole = new JLabel(user.isAdmin() ? "ADMIN DASHBOARD" : "VOTER DASHBOARD", SwingConstants.CENTER);
        lblRole.setFont(FONT_SUBTITLE);
        lblRole.setForeground(COLOR_PRIMARY);
        
        headerPanel.add(lblWelcome);
        headerPanel.add(lblRole);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. --- CORE CONTENT CARD AREA (Scrollable Wrapper if needed, but grid fits beautifully) ---
        JPanel contentCard = new JPanel(new GridBagLayout());
        contentCard.setBackground(Color.LIGHT_GRAY);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(25,25,25,25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 0, 8, 0);

        
        if (currentUser.isAdmin()) {
            buildAdminUI(contentCard, gbc);
        }
        else {
            buildVoterUI(contentCard, gbc);
        }
        mainPanel.add(contentCard, BorderLayout.CENTER);

        // 3. --- FOOTER SECTION ---
        JButton btnLogout = createStyledButton("Logout ", COLOR_DANGER, COLOR_DANGER_HOVER);
        btnLogout.addActionListener(e -> {
            new loginUI().setVisible(true);
            this.dispose();
        });
        mainPanel.add(btnLogout, BorderLayout.SOUTH);
    }

    private void buildVoterUI(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx=0;
        gbc.gridy=0;
        JLabel lblSec = createSectionHeader("ACTIVE DIGITAL BALLOT");
        panel.add(lblSec, gbc);
        gbc.gridy++;

        if (currentElection != null) {
            String nameofDept = currentUser.getDepartmentNameById(currentUser.getDeptId());
            JLabel lblInfo = new JLabel("CAST YOUR VOTE SECURELY FROM "+ nameofDept);
            lblInfo.setFont(new Font("Segoe UI",Font.BOLD, 12));
            lblInfo.setForeground(Color.BLACK);
            panel.add(lblInfo, gbc);
            gbc.gridy++;
            
            JComboBox<String> candidateBox = new JComboBox<>();
            candidateBox.setFont(FONT_BODY);
            candidateBox.setBackground(COLOR_BG);
            candidateBox.setForeground(COLOR_TEXT_MAIN);
            candidateBox.setBorder(new LineBorder(COLOR_BORDER, 1));

            eDao.populateCandidateDropdown(currentElection.getElectionId(), candidateBox);
            panel.add(candidateBox, gbc);
            gbc.gridy++;

            JButton btnVote = createStyledButton("Submit Vote", COLOR_SUCCESS, COLOR_SUCCESS_HOVER);
            btnVote.addActionListener(e -> handleVoteLogic(candidateBox));
            panel.add(btnVote, gbc);
            gbc.gridy++;
        } else {
            JLabel lblNoElection = new JLabel("<html><body style='text-align: center; color:#9CA3AF;'>No live elections found for your assigned department.<br>Please wait for administrator dispatch.</body></html>", SwingConstants.CENTER);
            lblNoElection.setFont(FONT_BODY);
            panel.add(lblNoElection, gbc);
            gbc.gridy++;
        }
    }

    private void buildAdminUI(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx=0;
        gbc.gridy=0;
        JLabel lblSec = createSectionHeader("ADMIN PANEL");
        panel.add(lblSec, gbc);
        gbc.gridy++;
        
        JButton btnNewElection = createStyledButton("Initialize New Election ", new Color(255,255,153), new Color(255,255,204));
        btnNewElection.addActionListener(e -> showCreateElectionPopup());
        panel.add(btnNewElection,gbc);
        gbc.gridy++;

        JLabel lblSelect = new JLabel("Live Election Results :");
        lblSelect.setFont(FONT_BODY);
        lblSelect.setForeground(Color.BLACK);
        gbc.insets = new Insets(18, 0, 4, 0);
        panel.add(lblSelect, gbc);
        gbc.gridy++;

        String[] depts = {"Computer Science", "Electrical Engineering", "Business Administration"};
        JComboBox<String> deptBox = new JComboBox<>(depts);
        deptBox.setFont(FONT_BODY);
        deptBox.setBackground(COLOR_BG);
        deptBox.setForeground(COLOR_TEXT_MAIN);
        deptBox.setBorder(new LineBorder(COLOR_BORDER,1));

        gbc.insets = new Insets(4, 0, 12, 0);
        panel.add(deptBox, gbc);
        gbc.gridy++;
gbc.insets=new Insets(4,0,4,0);
        JButton btnResults = createStyledButton("Top 3 Candidates ", new Color(255,255,153), new Color(255,255,204));
        btnResults.addActionListener(e -> {
            int selectedDeptId = deptBox.getSelectedIndex() + 1;
            Election deptElection = eDao.getActiveElection(selectedDeptId);
            
            if (deptElection != null) {
                String report = service.viewLiveResults(currentUser, deptElection);
                showCustomMessage("Election Standings Report", report, JOptionPane.INFORMATION_MESSAGE);
            } else {
                showCustomMessage("System Status", "No active election records found for this department segment.", JOptionPane.WARNING_MESSAGE);
            }
        });
        panel.add(btnResults, gbc);
        gbc.gridy=5;
    }

    private void handleVoteLogic(JComboBox<String> box) {
        String name = (String) box.getSelectedItem();
        if (name == null || name.contains("--")) return;

  if(voterDao.hasVoted(currentUser.getStudentId(), currentElection.getElectionId())){
showCustomMessage("Security Alert", "You have already voted in this election!.", JOptionPane.ERROR_MESSAGE);
return;
  }
       
        int confirm = JOptionPane.showConfirmDialog(this, "Are you certain you want to commit your vote to: " + name + "?", "Confirm Ballot Entry", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int cId = eDao.getCandidateIdByName(name, currentElection.getElectionId());
            if (vDao.castVote(currentUser.getStudentId(), cId, currentElection.getElectionId())) {
                showCustomMessage("Success", "Ballot successfully compiled .", JOptionPane.INFORMATION_MESSAGE);
                box.setEnabled(false);
            }
        }
    }

    private void showCreateElectionPopup() {
        JTextField deptField = new JTextField("1");
        JTextField yearField = new JTextField("2026"); 

        deptField.setBackground(COLOR_BG); 
        deptField.setForeground(COLOR_TEXT_MAIN);
         deptField.setCaretColor(Color.WHITE);
        yearField.setBackground(COLOR_BG); 
        yearField.setForeground(COLOR_TEXT_MAIN);
         yearField.setCaretColor(Color.WHITE);

        Object[] message = {
            " Department ID (1-3):", deptField,
            " Electoral  Year:", yearField,
            "Effective Operational Date:", LocalDate.now().toString(),
            "Automatic Expiration Date:", LocalDate.now().plusMonths(1).toString()
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Configure New Election Node", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int dId = Integer.parseInt(deptField.getText());
                int yr = Integer.parseInt(yearField.getText());
                String res = service.adminCreateElection(currentUser, dId, yr, LocalDate.now().toString(), LocalDate.now().plusMonths(1).toString());
                showCustomMessage("Process Complete", res, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showCustomMessage("Input Fault", "Error compiling configuration inputs. Ensure values are integers.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- PREMIUM SWING COMPONENT FACTORIES ---
    private JButton createStyledButton(String text, Color bg, Color hoverBg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.BLACK);
        if(bg.equals(COLOR_PRIMARY)|| bg.equals(COLOR_DANGER)){
            btn.setForeground(COLOR_TEXT_MAIN);
        }
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 36));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JLabel createSectionHeader(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,20));
        lbl.setForeground(COLOR_TEXT_MAIN);
        lbl.setBorder(new EmptyBorder(4,4,8,0));
        return lbl;
    }

    private void showCustomMessage(String title, String message, int messageType) {
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.background", COLOR_CARD_BG);
        UIManager.put("OptionPane.messageForground", COLOR_TEXT_MAIN);
        UIManager.put("Panel.background", COLOR_CARD_BG);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}