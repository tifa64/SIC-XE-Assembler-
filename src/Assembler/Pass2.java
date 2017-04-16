package Assembler;

import Assembler.Line.AssemblyLine;

import java.io.IOException;
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

    private static int recordSize = 0;
    private static boolean startFlag = false;
    private static String currentObjCode = new String();
    private static StringBuilder TRecordsSB = new StringBuilder();
    private static StringBuilder objCodeSB = new StringBuilder();
    private static String programStart = Integer.toHexString(Pass1.programStart).toUpperCase();

    public static void generateObjectCodes() {
        try {
            ArrayList<String> fileLines = new ArrayList<>();
            /**Case RESW or RESB**/
            for (AssemblyLine al : Pass1.assemblyLines) {
                try {
                    currentObjCode = al.getObjectCode();
                } catch (Exception m) {
                    TRecordsSB.append(Integer.toHexString(recordSize / 2).toUpperCase() + " ");
                    fileLines.add("T" + " " + TRecordsSB.toString() + objCodeSB.toString());
                    TRecordsSB = new StringBuilder();
                    objCodeSB = new StringBuilder();
                    recordSize = 0;
                }

                /**Case START**/
                if (currentObjCode.startsWith("H")) {
                    fileLines.add(currentObjCode);
                }
                /**Case END**/
                /**It will add the last line of T before it then will the END line**/
                else if (currentObjCode.startsWith("E")) {
                    TRecordsSB.append(Integer.toHexString(recordSize / 2).toUpperCase() + " ");
                    fileLines.add("T" + " " + TRecordsSB.toString() + objCodeSB.toString());
                    fileLines.add(currentObjCode);
                    break;
                }
                /**Case Comment**/
                else if (currentObjCode.length() == 0)
                    continue;
                /**Other wise it will continue the T record till the sz is bigger than 30 bytes (60 characters)**/
                else {
                    if (!startFlag) {
                        if (programStart.length() % 2 == 1) {
                            StringBuilder sbTemp = new StringBuilder();
                            sbTemp.append("0");
                            sbTemp.append(programStart);
                            programStart = sbTemp.toString();
                        }
                        objCodeSB.append(programStart + " ");
                        recordSize += programStart.length();
                        startFlag = true;
                    } else if (recordSize + currentObjCode.length() <= 60) {
                        /**Because  when new T record is begun it ignores the last Object Code of the previous T record**/
                        if (recordSize == 0) {
                            recordSize += 1;
                            continue;
                        }
                        if (recordSize == 1) {
                            recordSize--;
                        }
                        recordSize += currentObjCode.length();
                        objCodeSB.append(currentObjCode + " ");
                    } else {
                        TRecordsSB.append(Integer.toHexString(recordSize / 2).toUpperCase() + " ");
                        fileLines.add("T" + " " + TRecordsSB.toString() + objCodeSB.toString());
                        TRecordsSB = new StringBuilder();
                        objCodeSB = new StringBuilder();
                        recordSize = 0;
                    }
                }
            }
            Path listingFile = Paths.get("HTME.txt");
            Files.write(listingFile, fileLines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
