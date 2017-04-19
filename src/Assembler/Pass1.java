package Assembler;

import Assembler.Line.AssemblyLine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by louay on 3/25/2017.
 */
public class Pass1 {

    public final static Hashtable<String, Integer> SYMTAB = new Hashtable<String, Integer>();
    public static int programLength;
    public static int programStart;

    protected static final ArrayList<AssemblyLine> assemblyLines = new ArrayList<>();

    private final static String spacesPadding = "                                                                      ";

    private Pass1() { }

    public static void generatePass1Files(File file) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(file.getPath()));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals("")) {
                    lines.remove(i);
                    i--;
                }
            }
            int address = 0;

            ArrayList<String> listingFileLines = new ArrayList<>();
            ArrayList<String> SYMTAB_Lines = new ArrayList<>();

            for (String line : lines) {
                AssemblyLine al = null;
                try {
                    al = AssemblyLine.getAssemblyLineInstance(address, line.toUpperCase() + spacesPadding);
                    listingFileLines.add(al.toString());
                    try {
                        address = al.getNextAddress();
                    } catch (Exception e) {
                        listingFileLines.add("---- END OF FILE ----");
                    }
                    String tempLabel = al.getLabel();
                    if (tempLabel != null) {
                        if (SYMTAB.containsKey(tempLabel))
                            listingFileLines.add("****** ERROR :: Symbol " + tempLabel + " is already defined ******");

                        else {
                            SYMTAB.put(tempLabel, al.getAddress());
                            SYMTAB_Lines.add(Integer.toHexString(al.getAddress()) + "\t\t\t" + tempLabel);
                        }

                    }
                    assemblyLines.add(al);
                } catch (Exception e) {
                    //printing unknown command to listing file.
                    String lineWithPadding = line + spacesPadding;
                    String label = lineWithPadding.substring(0, 8).replaceAll("\\s+", "");
                    String mnemonic = lineWithPadding.substring(9, 15).replaceAll("\\s+", "");
                    String operand = lineWithPadding.substring(17, 35).replaceAll("\\s+", "");
                    String comment = lineWithPadding.substring(35, 66).replaceAll("\\s+", "");
                    StringBuilder sb = new StringBuilder();
                    sb.append(Pass2.padStringWithZeroes(Integer.toHexString(address), 5));
                    for (int i = sb.toString().length(); i <= 6; i++) {
                        sb.append(" ");
                    }
                    sb.append("\t");
                    sb.append(label);
                    for (int i = sb.toString().length(); i <= 15; i++) {
                        sb.append(" ");
                    }
                    sb.append("\t");
                    sb.append(mnemonic);
                    for (int i = sb.toString().length(); i <= 22; i++) {
                        sb.append(" ");
                    }
                    sb.append("\t");
                    sb.append(operand);
                    for (int i = sb.toString().length(); i <= 41; i++) {
                        sb.append(" ");
                    }
                    sb.append("\t");
                    sb.append(comment);
                    listingFileLines.add(sb.toString());
                    listingFileLines.add("****** ERROR :: Unknown Instruction: " + mnemonic + " ******");
                }
            }

            Path listingFile = Paths.get("ListingFile.txt");
            Files.write(listingFile, listingFileLines, Charset.forName("UTF-8"));

            Path SYMTAB_File = Paths.get("Symbol Table File.txt");
            Files.write(SYMTAB_File, SYMTAB_Lines, Charset.forName("UTF-8"));

            System.out.println("done");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
