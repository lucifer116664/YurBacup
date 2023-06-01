import javax.swing.*;
import java.awt.*;

/**
 * <code>Main</code> class enables user to choose what to do next
 * @author lucifer116664
 */
public class Main {
    private JFrame frame;
    private JPanel mainPanel;
    private JRadioButton backupNowRadioButton, backupSystematicallyRadioButton;
    private JButton nextButton;

    /**
     * Creates <code>ButtonGroup</code> for <code>JRadioButton</code> elements and adds action listener
     */
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

    /**
     * Creates and makes visible the frame
     */
    public void openFrame() {
        frame = new JFrame("YurBackup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setContentPane(mainPanel);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame.setBounds(toolkit.getScreenSize().width / 2 - 325, toolkit.getScreenSize().height / 2 - 200, 650, 400);
    }

    public static void main(String[] args) {
        new Main().openFrame();
    }
}
