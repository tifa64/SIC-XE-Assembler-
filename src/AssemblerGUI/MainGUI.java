package AssemblerGUI;

import AssemblerCore.Pass1;
import AssemblerCore.Pass2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by louay on 4/19/2017.
 */
public class MainGUI extends JDialog{

    private File selectedFile;

    public static void main(String[] args) {
        setUIFlavour();
        JFrame frame = new JFrame("SIC/XE Assembler by Louay and Mostafa");
        frame.setContentPane(new MainGUI().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainGUI() {
        setModal(true);
        startPass1Button.setEnabled(false);
        startPass2Button.setEnabled(false);
        textField1.setEnabled(false);
        //listingFileArea.setEnabled(false);
        //symbolTableArea.setEnabled(false);
        //htmeRecordsArea.setEnabled(false);
        textField1.setText("No file selected");
        listingFileArea.setText("Listing file will be visible here");
        symbolTableArea.setText("Symbol table will be visible here");
        htmeRecordsArea.setText("HTME records will be visible here");
        addActionListenersToUiComponents();
    }

    private static void setUIFlavour() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionListenersToUiComponents(){
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
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
                if (Pass1.isSuccess()){
                    startPass2Button.setEnabled(true);
                }
            }
        });

        startPass2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pass2.generateObjectCodes();
                startPass1Button.setEnabled(false);
                startPass2Button.setEnabled(false);
                textField1.setEnabled(false);
                textField1.setText("No file selected");
            }
        });
    }

    private JButton chooseFileButton;
    private JTextField textField1;
    private JButton startPass1Button;
    private JButton startPass2Button;
    private JPanel mainPanel;
    private JTextArea listingFileArea;
    private JTextArea symbolTableArea;
    private JTextArea htmeRecordsArea;
}