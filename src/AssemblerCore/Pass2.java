package AssemblerCore;

import AssemblerCore.Line.AssemblyLine;
import AssemblerCore.Line.Directive;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by louay on 4/15/2017.
 */
public class Pass2 {

    public static final ArrayList<String> MRecords = new ArrayList<>();
    public static final HashSet<String> externalRef = new HashSet<>();

    private static final ArrayList<String> fileLines = new ArrayList<>();
    private static final Hashtable<String, Symbol> symbols = new Hashtable<>();

    public static int baseValue = -1;
    public static String nameCSECT;

    public static void generateObjectCodes() {

        MRecords.clear();
        baseValue = -1;
        fileLines.clear();
        Directive.reset();

        int recordSize = 0;
        String currentObjCode = "";
        StringBuilder objCodeSB = new StringBuilder();
        String programStart = Integer.toHexString(Directive.globalProgramStart).toUpperCase();
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
                    /*Case END or CSECT**/
                    /*It will add the last line of T before it then will the END line**/
                    else if (currentObjCode.startsWith("E ")) {
                        if (recordSize > 0) {
                            String tRecSize = Pass2.padStringWithZeroes(Integer.toHexString(recordSize / 2), 2);
                            fileLines.add("T" + " " + currentTRecordStart + " " + tRecSize + " " + objCodeSB.toString());
                        }
                        fileLines.addAll(MRecords);
                        MRecords.clear();
                        fileLines.add(currentObjCode);
                        currentTRecordStart = "000000";
                        objCodeSB = new StringBuilder();
                        recordSize = 0;
                    }
                    /*Case External Reference or definition*/
                    else if (currentObjCode.startsWith("R ") || currentObjCode.startsWith("D ")) {
                        fileLines.add(currentObjCode);
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
                        errorMsg = "Error. Base relative needed but no base was specified at " + Pass2.padStringWithZeroes(Integer.toHexString(al.getAddress()), 5);
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
        } else if (str.charAt(0) == 'F' || str.charAt(0) == 'f' ) {
            return str.substring(2).toUpperCase();
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

    public static void addToHashTable(HashSet<Symbol> symbols) {
        Pass2.symbols.clear();
        for (Symbol s : symbols) {
            Pass2.symbols.put(s.getSymbolName(), s);
        }
    }

    public static Symbol getSymbol(String symbol) throws Exception {
        if (isSymbolExists(symbol)) {
            return Pass2.symbols.get(symbol);
        } else {
            throw new Exception("Symbol " + symbol + " is not found.");
        }
    }

    public static int getSymbolValue(String symbol) throws Exception {
        if (isSymbolExists(symbol)) {
            return Pass2.symbols.get(symbol).getValue();
        } else if (externalRef.contains(symbol)) {
            return 0;
        } else {
            throw new Exception("Symbol " + symbol + " is not found.");
        }
    }

    public static int calculateOperandValue(String str) throws Exception {
        ArrayList<String> tokens = getTokens(str);
        return Pass1.calculateInfix(tokens);
    }

    private static ArrayList<String> getTokens(String str) throws Exception {
        int n = str.length();
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            boolean flag = false;
            while (i < n && (Character.isLetterOrDigit(str.charAt(i)) || str.charAt(i) == '*')) {
                sb.append(str.charAt(i));
                i++;
                flag = true;
            }
            if (flag) {
                if (AssemblyLine.isInteger(sb.toString()) || sb.toString().equals("*")) {
                    tokens.add(sb.toString());
                } else if (symbols.containsKey(sb.toString())) {
                    tokens.add(Integer.toString(symbols.get(sb.toString()).getValue()));
                } else if (Pass2.externalRef.contains(sb.toString())) {
                    tokens.add("0");
                } else {
                    throw new Exception("Forward reference or symbol not found: " + sb.toString());
                }
            }
            if (i < n) {
                tokens.add(str.charAt(i) + "");
            }
        }
        return tokens;
    }

    private static boolean isSymbolExists(String symbol) {
        return Pass2.symbols.containsKey(symbol);
    }
}
