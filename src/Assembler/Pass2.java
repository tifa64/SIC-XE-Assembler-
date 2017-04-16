package Assembler;

import Assembler.Line.AssemblyLine;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by louay on 4/15/2017.
 */
public class Pass2 {

    public static int baseValue = -1;
    public static final ArrayList<String> MRecords = new ArrayList<>();

    private static int recordSize = 0;
    private static boolean startFlag = false;
    private static String currentObjCode = "";
    private static StringBuilder TRecordsSB = new StringBuilder();
    private static StringBuilder objCodeSB = new StringBuilder();
    private static String programStart = Integer.toHexString(Pass1.programStart).toUpperCase();
    private static String currentTRecordStart = padStringWithZeroes(programStart,6);

    public static void generateObjectCodes() {
        try {
            ArrayList<String> fileLines = new ArrayList<>();

            for (AssemblyLine al : Pass1.assemblyLines) {
                try {
                    currentObjCode = al.getObjectCode();
                } catch (Exception m) {
                    /*Case RESW or RESB**/
                    TRecordsSB.append(Integer.toHexString(recordSize / 2).toUpperCase()).append(" ");
                    fileLines.add("T" + " " + currentTRecordStart + " " + TRecordsSB.toString() + objCodeSB.toString());
                    currentTRecordStart = padStringWithZeroes(Integer.toHexString(al.getNextAddress()).toUpperCase(), 6);
                    TRecordsSB = new StringBuilder();
                    objCodeSB = new StringBuilder();
                    recordSize = 0;
                }

                /*Case START**/
                if (currentObjCode.startsWith("H")) {
                    fileLines.add(currentObjCode);
                }
                /*Case END**/
                /*It will add the last line of T before it then will the END line**/
                else if (currentObjCode.startsWith("E")) {
                    TRecordsSB.append(Integer.toHexString(recordSize / 2).toUpperCase()).append(" ");
                    fileLines.add("T" + " " + currentTRecordStart + " " + TRecordsSB.toString() + objCodeSB.toString());
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
                    if (!startFlag) {
                        if (programStart.length() % 2 == 1) {
                            StringBuilder sbTemp = new StringBuilder();
                            sbTemp.append("0");
                            sbTemp.append(programStart);
                            programStart = sbTemp.toString();
                        }
                        //objCodeSB.append(programStart).append(" ");
                        objCodeSB.append(currentObjCode).append(" ");
                        recordSize += currentObjCode.length();
                        startFlag = true;
                    } else if (recordSize + currentObjCode.length() <= 60) {
                        /*Because  when new T record is begun it ignores the last Object Code of the previous T record**/
                        if (recordSize == 0) {
                            recordSize += 1;
                            continue;
                        }
                        if (recordSize == 1) {
                            recordSize--;
                        }
                        recordSize += currentObjCode.length();
                        objCodeSB.append(currentObjCode).append(" ");
                    } else {
                        TRecordsSB.append(Integer.toHexString(recordSize / 2).toUpperCase()).append(" ");
                        fileLines.add("T" + " " + currentTRecordStart + " " + TRecordsSB.toString() + objCodeSB.toString());
                        currentTRecordStart = padStringWithZeroes(Integer.toHexString(al.getAddress()).toUpperCase(), 6);
                        TRecordsSB = new StringBuilder();
                        objCodeSB = new StringBuilder();
                        recordSize = 0;
                    }
                }
            }
            Path htmeRecordsFile = Paths.get("HTME.txt");
            Files.write(htmeRecordsFile, fileLines, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String padStringWithZeroes(String str, int requiredLength){
        if (str.length() < requiredLength){
            StringBuilder sb = new StringBuilder();
            for (int i = str.length(); i < requiredLength; i++){
                sb.append("0");
            }
            str = sb.append(str).toString();
        }
        return str;
    }
}
