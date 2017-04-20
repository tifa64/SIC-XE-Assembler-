package AssemblerCore;

import AssemblerCore.Line.AssemblyLine;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by louay on 4/15/2017.
 */
public class Pass2 {

    public static final ArrayList<String> MRecords = new ArrayList<>();
    private static final ArrayList<String> fileLines = new ArrayList<>();
    public static int baseValue = -1;

    public static void generateObjectCodes() {
        MRecords.clear();
        baseValue = -1;
        fileLines.clear();

        int recordSize = 0;
        String currentObjCode = "";
        StringBuilder objCodeSB = new StringBuilder();
        String programStart = Integer.toHexString(Pass1.programStart).toUpperCase();
        String currentTRecordStart = padStringWithZeroes(programStart, 6);
        boolean successFlag = true;
        boolean firstResFlag = true;
        String errorMsg = "";
        try {
            for (AssemblyLine al : Pass1.assemblyLines) {
                try {
                    currentObjCode = al.getObjectCode();
                    /*Case START**/
                    if (currentObjCode.startsWith("H")) {
                        fileLines.add(currentObjCode);
                    }
                    /*Case END**/
                    /*It will add the last line of T before it then will the END line**/
                    else if (currentObjCode.startsWith("E ")) {
                        String tRecSize = Pass2.padStringWithZeroes(Integer.toHexString(recordSize / 2), 2);
                        fileLines.add("T" + " " + currentTRecordStart + " " + tRecSize + " " + objCodeSB.toString());
                        fileLines.addAll(MRecords);
                        fileLines.add(currentObjCode);
                        break;
                    }
                    /*Case Comment**/
                    else if (currentObjCode.length() == 0) {
                        continue;
                    }
                    /*Other wise it will continue the T record till the sz is bigger than 30 bytes (60 characters)**/
                    else {
                        firstResFlag = true;
                        if (recordSize + currentObjCode.length() <= 60) {
                            recordSize += currentObjCode.length();
                            objCodeSB.append(currentObjCode).append(" ");
                        } else {
                            String tRecSize = Pass2.padStringWithZeroes(Integer.toHexString(recordSize / 2), 2);
                            fileLines.add("T" + " " + currentTRecordStart + " " + tRecSize + " " + objCodeSB.toString());
                            currentTRecordStart = padStringWithZeroes(Integer.toHexString(al.getAddress()).toUpperCase(), 6);
                            objCodeSB = new StringBuilder().append(currentObjCode).append(" ");
                            recordSize = currentObjCode.length();
                        }
                    }
                } catch (Exception m) {
                    if (m.getMessage().equals("NO BASE")) {
                        /*Case NOBASE but base relative mode is needed*/
                        successFlag = false;
                        errorMsg = "Error. Base relative needed but no base was specified.";
                        break;
                    } else if (m.getMessage().substring(0, 7).equals("Reserve")) {
                        /*Case RESW or RESB**/
                        if (firstResFlag) {
                            String tRecSize = Pass2.padStringWithZeroes(Integer.toHexString(recordSize / 2), 2);
                            fileLines.add("T" + " " + currentTRecordStart + " " + tRecSize + " " + objCodeSB.toString());
                            objCodeSB = new StringBuilder();
                            recordSize = 0;
                            firstResFlag = false;
                        }
                        currentTRecordStart = padStringWithZeroes(Integer.toHexString(al.getNextAddress()).toUpperCase(), 6);
                    } else {
                        successFlag = false;
                        errorMsg = m.getMessage();
                        m.printStackTrace();
                        break;
                    }
                }
            }
            Path htmeRecordsFile = Paths.get("HTME.txt");
            if (!successFlag) {
                fileLines.clear();
                fileLines.add(errorMsg);
            }
            Files.write(htmeRecordsFile, fileLines, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String padStringWithZeroes(String str, int requiredLength) {
        if (str.length() < requiredLength) {
            StringBuilder sb = new StringBuilder();
            for (int i = str.length(); i < requiredLength; i++) {
                sb.append("0");
            }
            str = sb.append(str).toString();
        }
        return str.toUpperCase();
    }

    public static String getHTMELines() {
        StringBuilder sb = new StringBuilder();
        for (String str : fileLines) {
            sb.append(str).append("\n");
        }
        return sb.toString();
    }
}
