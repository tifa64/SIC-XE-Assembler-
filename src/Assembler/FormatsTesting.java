package Assembler;

import Assembler.Line.AssemblyLine;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by louay on 3/25/2017.
 */
public class FormatsTesting {

    private final static  String spacesPadding = "                                                                      ";

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get(file.getPath()));
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).equals("")) {
                        lines.remove(i);
                        i--;
                    }
                }
                ArrayList<AssemblyLine> instructions = new ArrayList<>();
                for (String line : lines){
                    instructions.add(AssemblyLine.getAssemblyLineInstance(0, line + spacesPadding));
                }
                System.out.println("done");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
