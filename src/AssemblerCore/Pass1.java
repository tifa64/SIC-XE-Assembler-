package AssemblerCore;

import AssemblerCore.Line.AssemblyLine;
import AssemblerCore.Line.Literal;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static AssemblerCore.SymbolTable.*;

/**
 * Created by louay on 3/25/2017.
 */
public class Pass1 {

    public static final HashSet<String> literals = new HashSet<>();
    static final ArrayList<AssemblyLine> assemblyLines = new ArrayList<>();
    public static final HashSet<String> ExDef = new HashSet<String>();
    private static final ArrayList<String> listingFileLines = new ArrayList<>();
    private static final ArrayList<String> SYMTAB_Lines = new ArrayList<>();
    private static final String spacesPadding = "                                                                      ";
    public static int programLength;
    public static int programStart;
    private static boolean success;
    public static String nameCSECT;

    private Pass1() {
    }

    public static void generatePass1Files(File file) {
        ClearHahset();
        assemblyLines.clear();
        listingFileLines.clear();
        SYMTAB_Lines.clear();
        success = true;
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

            for (String line : lines) {
                AssemblyLine al = null;
                try {
                    al = AssemblyLine.getAssemblyLineInstance(address, line.toUpperCase() + spacesPadding);
                    listingFileLines.add(al.toString());
                    try {
                        address = al.getNextAddress();
                        al.checkOperand();
                    } catch (Exception e) {
                        if (e.getMessage().equals("No Operand")) {
                            listingFileLines.add("****** ERROR :: No Operand specified ******");
                            success = false;
                        } else if (e.getMessage().equals("LTORG") || e.getMessage().equals("End Of File")) {
                            address = insertLiterals(address);
                            if (e.getMessage().equals("End Of File")) {
                                listingFileLines.add("---- END OF FILE ----");
                            }
                        } else {
                            listingFileLines.add("****** ERROR :: " + e.getMessage() + " ******");
                            success = false;
                        }
                    }
                    String tempLabel = al.getLabel();
                    if (tempLabel != null) {
                        if (containsKey(nameCSECT, tempLabel)) {
                            listingFileLines.add("****** ERROR :: Symbol " + tempLabel + " is already defined ******");
                            success = false;
                        } else {
                            insertInHashSet(al.getSymbol());
                            SYMTAB_Lines.add(Pass2.padStringWithZeroes(Integer.toHexString(al.getSymbol().getValue()), 6) + "\t\t" + tempLabel + "\t\t" + al.getSymbol().getType() + "\t\t" + nameCSECT);
                        }

                    }
                    assemblyLines.add(al);
                } catch (Exception e) {
                    success = false;
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

    public static boolean isSuccess() {
        return success;
    }

    public static String getListingFileLines() {
        StringBuilder sb = new StringBuilder();
        for (String str : listingFileLines) {
            sb.append(str).append("\n");
        }
        return sb.toString();
    }

    public static String getSymTableLines() {
        StringBuilder sb = new StringBuilder();
        for (String str : SYMTAB_Lines) {
            sb.append(str).append("\n");
        }
        return sb.toString();
    }


    public static boolean isExternalDef(String lbl)
    {
        if(ExDef.contains(lbl))
            return true;

        return false;
    }

    public static int calculateOperandValue(String str) throws Exception {
        int result = 0;
        ArrayList<String> tokens = getTokens(str);
        if (validateExpression(tokens)) {
            result = calculateInfix(tokens);
        } else {
            throw new Exception("Invalid Expression");
        }


        return result;
    }

    public static char getExpressionType(String str) throws Exception {
        ArrayList<String> tokens = getTokens(str);

        Stack<Pair<String, Character>> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (String token : tokens) {
            if (AssemblyLine.isInteger(token)) {
                operands.push(new Pair<>(token, 'A'));
            } else if (token.equals("(") || token.equals(")") || token.equals("+") || token.equals("-")) {
                if (token.equals(")")) {
                    while (operators.peek() != '(') {
                        Pair b = operands.pop();
                        Pair a = operands.pop();
                        char op = operators.pop();
                        switch (op) {
                            case '+':
                                if (a.getValue().equals('R') && b.getValue().equals('R')) {
                                    throw new Exception("Relative + Relative");
                                } else if (a.getValue().equals('R') && b.getValue().equals('A')) {
                                    operands.push(new Pair<>("ay 7aga", 'R'));
                                } else if (a.getValue().equals('A') && b.getValue().equals('R')) {
                                    operands.push(new Pair<>("ay 7aga", 'R'));
                                } else {
                                    operands.push(new Pair<>("ay 7aga", 'A'));
                                }
                                break;
                            case '-': {
                                if (a.getValue().equals('A') && b.getValue().equals('R')) {
                                    throw new Exception("Absolute - Relative");
                                } else if (a.getValue().equals('R') && b.getValue().equals('A')) {
                                    operands.push(new Pair<>("ay 7aga", 'R'));
                                } else {
                                    operands.push(new Pair<>("ay 7aga", 'A'));
                                }
                                break;
                            }
                        }
                    }
                    operators.pop();
                } else {
                    operators.push(token.charAt(0));
                }
            } else {
                operands.push(new Pair<>(token, 'R'));
            }
        }

        while (!operators.isEmpty()) {
            Pair b = operands.pop();
            Pair a = operands.pop();
            char op = operators.pop();
            switch (op) {
                case '+':
                    if (a.getValue().equals('R') && b.getValue().equals('R')) {
                        throw new Exception("Relative + Relative");
                    } else if (a.getValue().equals('R') && b.getValue().equals('A')) {
                        operands.push(new Pair<>("ay 7aga", 'R'));
                    } else if (a.getValue().equals('A') && b.getValue().equals('R')) {
                        operands.push(new Pair<>("ay 7aga", 'R'));
                    } else {
                        operands.push(new Pair<>("ay 7aga", 'A'));
                    }
                    break;
                case '-': {
                    if (a.getValue().equals('A') && b.getValue().equals('R')) {
                        throw new Exception("Absolute - Relative");
                    } else if (a.getValue().equals('R') && b.getValue().equals('A')) {
                        operands.push(new Pair<>("ay 7aga", 'R'));
                    } else {
                        operands.push(new Pair<>("ay 7aga", 'A'));
                    }
                    break;
                }
            }
        }

        return operands.pop().getValue();
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
                } else if (containsKey(nameCSECT, sb.toString())) {
                    tokens.add(Integer.toString(getSymbol(nameCSECT ,sb.toString()).getValue()));
                } else {
                    throw new Exception("Forward reference");
                }
            }
            if (i < n) {
                tokens.add(str.charAt(i) + "");
            }
        }
        return tokens;
    }

    private static int calculateInfix(ArrayList<String> tokens) {
        Stack<Integer> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (String token : tokens) {
            if (AssemblyLine.isInteger(token)) {
                operands.push(Integer.parseInt(token));
            } else {
                if (token.equals(")")) {
                    while (operators.peek() != '(') {
                        int b = operands.pop();
                        int a = operands.pop();
                        char op = operators.pop();
                        switch (op) {
                            case '+':
                                operands.push(a + b);
                                break;
                            case '-':
                                operands.push(a - b);
                                break;
                        }
                    }
                    operators.pop();
                } else {
                    operators.push(token.charAt(0));
                }
            }
        }

        while (!operators.isEmpty()) {
            int b = operands.pop();
            int a = operands.pop();
            char op = operators.pop();
            switch (op) {
                case '+':
                    operands.push(a + b);
                    break;
                case '-':
                    operands.push(a - b);
                    break;
            }
        }

        return operands.pop();
    }

    private static int insertLiterals(int address) throws Exception {
        if (!literals.isEmpty()) {
            for (String lit : literals) {
                Literal literal = new Literal(address, lit);
                listingFileLines.add(literal.toString());
                assemblyLines.add(literal);
                insertInHashSet(literal.getSymbol());
                SYMTAB_Lines.add(Pass2.padStringWithZeroes(Integer.toHexString(address), 6) + "\t\t\t" + lit);
                address = literal.getNextAddress();
            }
            literals.clear();
        }
        return address;
    }

    private static boolean validateExpression(ArrayList<String> tokens) {

        return true;
    }

}
