package AssemblerGUI;

import AssemblerCore.Pass1;
import AssemblerCore.Pass2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by louay on 4/19/2017.
 */
public class MainGUI extends JDialog {

    private File selectedFile;
    private JButton chooseFileButton;
    private JTextField textField1;
    private JButton startPass1Button;
    private JButton startPass2Button;
    private JPanel mainPanel;
    private JTextArea listingFileArea;
    private JTextArea symbolTableArea;
    private JTextArea htmeRecordsArea;

    public MainGUI() {
        setModal(true);
        startPass1Button.setEnabled(false);
        startPass2Button.setEnabled(false);
        textField1.setEnabled(false);
        textField1.setText("No file selected");
        listingFileArea.setText("Listing file will be visible here");
        symbolTableArea.setText("Symbol table will be visible here");
        htmeRecordsArea.setText("HTME records will be visible here");
        listingFileArea.setEditable(false);
        symbolTableArea.setEditable(false);
        htmeRecordsArea.setEditable(false);
        listingFileArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        symbolTableArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        htmeRecordsArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        addActionListenersToUiComponents();
    }

    public static void main(String[] args) {
        setUIFlavour();
        JFrame frame = new JFrame("SIC/XE Assembler");
        frame.setContentPane(new MainGUI().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static void setUIFlavour() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionListenersToUiComponents() {
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    startPass1Button.setEnabled(true);
                    startPass2Button.setEnabled(false);
                    textField1.setText(selectedFile.getAbsolutePath());
                    listingFileArea.setText("Listing file will be visible here");
                    symbolTableArea.setText("Symbol table will be visible here");
                    htmeRecordsArea.setText("HTME records will be visible here");
                }
            }
        });

        startPass1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pass1.generatePass1Files(selectedFile);
                listingFileArea.setText(Pass1.getListingFileLines());
                symbolTableArea.setText(Pass1.getSymTableLines());
                if (Pass1.isSuccess()) {
                    startPass2Button.setEnabled(true);
                    htmeRecordsArea.setText("HTME records will be visible here");
                } else {
                    startPass2Button.setEnabled(false);
                    htmeRecordsArea.setText("Error in Pass 1. Please fix your code.");
                    JOptionPane.showMessageDialog(null, "There was an error in your file. You'll not be able to run pass 2. Check the errors in the listing file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        startPass2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pass2.generateObjectCodes();
                htmeRecordsArea.setText(Pass2.getHTMELines());
                startPass2Button.setEnabled(false);
            }
        });
    }
}
