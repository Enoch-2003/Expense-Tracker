
import javax.swing.SwingUtilities;

public class ExpenseTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new ExpenseTrackerGUI();
            }
        });
    }
}
