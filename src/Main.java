import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private JPanel mainPanel;
    private JRadioButton backupNowRadioButton, backupSystematicallyRadioButton;
    private JButton nextButton;

    public Main() {
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(backupNowRadioButton);
        btnGroup.add(backupSystematicallyRadioButton);

        nextButton.addActionListener(e -> {
            if(backupNowRadioButton.isSelected()) {
                new Now().openFrame();
                frame.dispose();
            }
            else {
                new Systematically().openFrame();
                frame.dispose();
            }
        });
    }

    public void openFrame() {
        frame = new JFrame("YurBackup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setContentPane(mainPanel);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame.setBounds(toolkit.getScreenSize().width / 2 - 300, toolkit.getScreenSize().height / 2 - 200, 600, 400);
    }

    public static void main(String[] args) {
        new Main().openFrame();
    }
}
