import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpenseTrackerGUI {
    private JFrame frame;
    private ExpenseTrackerService service;
    private String loggedInUser;

    public ExpenseTrackerGUI() {
        service = new ExpenseTrackerService();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new CardLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(220, 220, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel loginImage = new JLabel(new ImageIcon("login.png"));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        loginPanel.add(loginImage, gbc);

        gbc.gridwidth = 1;

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 179, 113));
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(30, 144, 255));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(registerButton, gbc);

        frame.add(loginPanel, "Login");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel mainImage = new JLabel(new ImageIcon("main.png")); 
        mainPanel.add(mainImage, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));

        JLabel dateLabel = new JLabel("Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(dateLabel, gbc);

        JTextField dateField = new JTextField(20);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(dateField, gbc);

        JLabel categoryLabel = new JLabel("Category:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(categoryLabel, gbc);

        JTextField categoryField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(categoryField, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(amountLabel, gbc);

        JTextField amountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(amountField, gbc);

        JButton addButton = new JButton("Add Expense");
        addButton.setBackground(new Color(255, 140, 0));
        addButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(addButton, gbc);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(178, 34, 34));
        exitButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(exitButton, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Date", "Category", "Amount"}, 0);
        JTable expenseTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        mainPanel.add(new JScrollPane(summaryArea), BorderLayout.SOUTH);

        frame.add(mainPanel, "Main");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (service.loginUser(username, password)) {
                    loggedInUser = username;
                    frame.setTitle("Expense Tracker - " + loggedInUser);
                    ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
                    refreshExpenses(tableModel);
                    refreshSummary(summaryArea);
                    usernameField.setText("");
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (service.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(frame, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
                    String category = categoryField.getText();
                    double amount = Double.parseDouble(amountField.getText());
                    service.addExpense(loggedInUser, new Expense(date, category, amount));
                    refreshExpenses(tableModel);
                    refreshSummary(summaryArea);
                    categoryField.setText("");
                    amountField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInUser = null;
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");
            }
        });

        frame.setVisible(true);
    }

    private void refreshExpenses(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Expense> expenses = service.getExpenses(loggedInUser);
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate()), expense.getCategory(), expense.getAmount()});
        }
    }

    private void refreshSummary(JTextArea summaryArea) {
        summaryArea.setText("");
        Map<String, Double> categoryWiseSum = service.getCategoryWiseSum(loggedInUser);
        for (Map.Entry<String, Double> entry : categoryWiseSum.entrySet()) {
            summaryArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }
}