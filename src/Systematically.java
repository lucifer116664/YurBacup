import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Systematically {
    JFrame frame;
    private JPanel mainPanel;
    private JButton backupSystematicallyButton, chooseDataToBackupButton, chooseOutputLocationButton, backButton;
    private JCheckBox dataChosenCheckBox, outChosenCheckBox;
    private JSpinner timeSpinner, unitsOfTimeSpinner;
    private File srcFileOrDir, destDir;

    public Systematically() {
        String[] unitsOfTimeStrings = {"second(s)", "minute(s)", "hour(s)", "day(s)", "week(s)", "month(s)"};

        timeSpinner.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        unitsOfTimeSpinner.setModel(new SpinnerListModel(unitsOfTimeStrings));

        chooseDataToBackupButton.addActionListener(e -> chooseData());
        chooseOutputLocationButton.addActionListener(e -> chooseOutputLocation());
        backupSystematicallyButton.addActionListener(e -> backupSystematicallyAction());
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

    private void backupSystematicallyAction() {
        if (dataChosenCheckBox.isSelected() & outChosenCheckBox.isSelected()) {
            Thread thread = new Thread(new repeatSystematically());
            thread.start();

            JOptionPane.showMessageDialog(null, "Program will backup selected data every " + timeSpinner.getValue() + unitsOfTimeSpinner.getValue(), "Success!", JOptionPane.INFORMATION_MESSAGE);

            dataChosenCheckBox.setSelected(false);
            outChosenCheckBox.setSelected(false);
        }
        else {
            JOptionPane.showMessageDialog(null, "You have to choose data and output locations", "Warning!", JOptionPane.WARNING_MESSAGE);
        }

    }

    private class repeatSystematically implements Runnable {
        @Override
        public void run() {
            int delay = 1000;
            switch ((String) unitsOfTimeSpinner.getValue()) {
                case "second(s)" -> delay *= (int) timeSpinner.getValue();
                case "minute(s)" -> delay *= (60 * (int) timeSpinner.getValue());
                case "hour(s)" -> delay *= (60 * 60 * (int) timeSpinner.getValue());
                case "day(s)" -> delay *= (60 * 60 * 24 * (int) timeSpinner.getValue());
                case "week(s)" -> delay *= (60 * 60 * 24 * 7 * (int) timeSpinner.getValue());
                case "month(s)" -> delay *= (60 * 60 * 24 * 7 * 30 * (int) timeSpinner.getValue());
            }
            while (true) {
                backup();
                try {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null, e.getStackTrace(), "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void backup(){
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

            dataChosenCheckBox.setSelected(false);
            outChosenCheckBox.setSelected(false);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while copying files.", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openFrame() {
        frame = new JFrame("YurBackup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setContentPane(mainPanel);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame.setBounds(toolkit.getScreenSize().width / 2 - 300, toolkit.getScreenSize().height / 2 - 200, 600, 400);
    }
}
