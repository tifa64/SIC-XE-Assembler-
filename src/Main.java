import Assembler.Pass1;

import javax.swing.*;

/**
 * Created by louay on 3/26/2017.
 */
public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            Pass1.generatePass1Files(fileChooser.getSelectedFile());
        }
    }
}
