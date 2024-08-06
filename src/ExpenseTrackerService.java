import java.io.*;
import java.util.*;

public class ExpenseTrackerService {
    private Map<String, User> users;
    private Map<String, List<Expense>> userExpenses;

    public ExpenseTrackerService() {
        users = new HashMap<>();
        userExpenses = new HashMap<>();
        loadUserData();
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        User user = new User(username, password);
        users.put(username, user);
        userExpenses.put(username, new ArrayList<>());
        saveUserData();
        return true;
    }

    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.validatePassword(password);
    }

    public void addExpense(String username, Expense expense) {
        List<Expense> expenses = userExpenses.get(username);
        expenses.add(expense);
        saveUserData();
    }

    public List<Expense> getExpenses(String username) {
        return userExpenses.get(username);
    }

    public Map<String, Double> getCategoryWiseSum(String username) {
        Map<String, Double> categoryWiseSum = new HashMap<>();
        for (Expense expense : userExpenses.get(username)) {
            categoryWiseSum.put(expense.getCategory(), categoryWiseSum.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        return categoryWiseSum;
    }

    private void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data.ser"))) {
            users = (Map<String, User>) ois.readObject();
            userExpenses = (Map<String, List<Expense>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_data.ser"))) {
            oos.writeObject(users);
            oos.writeObject(userExpenses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
