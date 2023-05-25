import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Now {
    JFrame frame;
    private JPanel mainPanel;
    private JButton chooseDataToBackupButton, chooseOutputLocationButton, backupButton, backButton;
    private JCheckBox outChosenCheckBox, dataChosenCheckBox;
    private File srcFileOrDir, destDir;

    public Now() {
        chooseDataToBackupButton.addActionListener(e -> chooseData());
        chooseOutputLocationButton.addActionListener(e -> chooseOutputLocation());
        backupButton.addActionListener(e -> backup());
        backButton.addActionListener(e -> {
            new Main().openFrame();
            frame.dispose();
        });
    }

    private void chooseData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showDialog(null, "Choose");
        if(result == JFileChooser.APPROVE_OPTION) {
            srcFileOrDir = new File(fileChooser.getSelectedFile().getAbsolutePath());
            dataChosenCheckBox.setSelected(true);
        }
    }

    private void chooseOutputLocation() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showDialog(null, "Choose");
        if(result == JFileChooser.APPROVE_OPTION) {
            destDir = new File(fileChooser.getSelectedFile().getAbsolutePath());
            outChosenCheckBox.setSelected(true);
        }
    }

    private void backup(){
        if (dataChosenCheckBox.isSelected() & outChosenCheckBox.isSelected()) {
            try {
                final Path dest = destDir.toPath().resolve(srcFileOrDir.toPath().getFileName());
                Files.walk(srcFileOrDir.toPath()).forEach(source -> {
                    Path destination = dest.resolve(srcFileOrDir.toPath().relativize(source));
                    try {
                        if (!Files.isDirectory(destination)) {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error occurred while copying files.", "ERROR!", JOptionPane.ERROR_MESSAGE);
                    }
                });
                JOptionPane.showMessageDialog(null, "Backup was created successfully at " + destDir.getAbsolutePath(), "Success!", JOptionPane.INFORMATION_MESSAGE);

                dataChosenCheckBox.setSelected(false);
                outChosenCheckBox.setSelected(false);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occurred while copying files.", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "You have to choose data and output locations", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void openFrame() {
        frame = new JFrame("YurBackup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setContentPane(mainPanel);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame.setBounds(toolkit.getScreenSize().width / 2 - 325, toolkit.getScreenSize().height / 2 - 200, 650, 400);
    }
}
