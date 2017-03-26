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

    private Pass1() { }

    protected final static ArrayList<AssemblyLine> instructions = new ArrayList<>();
    protected final static Hashtable<String, Integer> SYMTAB = new Hashtable<String, Integer> ();

    private final static String spacesPadding = "                                                                      ";

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
                } catch (Exception e) {
                    listingFileLines.add(e.getMessage() + "\n");
                }
                instructions.add(al);

                try {
                    address = al.getNextAddress();
                } catch (Exception e) {
                    listingFileLines.add("---- END OF FILE ----");
                }
                String tempLabel = al.getLabel();
                if(tempLabel != null)
                {
                    if (SYMTAB.containsKey(tempLabel))
                        listingFileLines.add("****** ERROR :: Symbol " + tempLabel + " is already defined ******");

                    else
                    {
                        SYMTAB.put(tempLabel, al.getAddress());
                        SYMTAB_Lines.add(Integer.toHexString(al.getAddress())+"\t\t\t"+tempLabel);
                    }

                }
            }

            Path listingFile = Paths.get("ListingFile.txt");
            Files.write(listingFile, listingFileLines, Charset.forName("UTF-8"));

            Path SYMTAB_File = Paths.get("Symbol Table File.txt");
            Files.write(SYMTAB_File, SYMTAB_Lines, Charset.forName("UTF-8"));

            System.out.println("done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
