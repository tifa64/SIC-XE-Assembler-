package AssemblerCore.Line;

import AssemblerCore.Pass1;
import AssemblerCore.Pass2;
import AssemblerCore.Symbol;
import AssemblerCore.SymbolTable;

import java.util.ArrayList;

/**
 * Created by louay on 3/26/2017.
 */
public class Directive extends AssemblyLine {

    public static int globalProgramStart = 0;

    private static boolean firstCSECTflag = true;
    private static int lastSavedAddress = 0;
    private final String label, mnemonic, operand, comment;

    protected final int address;


    Directive(int address, String line) {
        super(address, line);
        this.label = line.substring(0, 8).replaceAll("\\s+", "");
        this.mnemonic = line.substring(9, 15).replaceAll("\\s+", "");
        this.operand = line.substring(17, 35).replaceAll("\\s+", "");
        this.comment = line.substring(35, 66).replaceAll("\\s+", "");
        if (mnemonic.equals("START")) {
            this.address = Integer.parseInt(operand, 16);
        } else if (mnemonic.equals("CSECT")) {
            this.address = 0;
        } else {
            this.address = super.address;
        }
    }

    public static void reset() {
        firstCSECTflag = true;
    }

    @Override
    public int getNextAddress() throws Exception {
        switch (mnemonic) {
            case "START": {
                Pass1.nameCSECT = label;
                Pass1.programsStart = this.address;
                globalProgramStart = this.address;
                Pass1.ExDef.add(label);
                return this.address;
            }
            case "END": {
                Pass1.programLength.put(Pass1.nameCSECT, this.address - Pass1.programsStart);
                throw new Exception("End Of File");
            }
            case "RESB": {
                if (!AssemblyLine.isInteger(operand)) {
                    throw new Exception("Operand not integer");
                }
                int decimal = Integer.parseInt(operand);
                return this.address + decimal;
            }
            case "RESW": {
                if (!AssemblyLine.isInteger(operand)) {
                    throw new Exception("Operand not integer");
                }
                int decimal = Integer.parseInt(operand);
                decimal *= 3;
                return this.address + decimal;
            }
            case "BYTE": {
                int intLenghtOfOperand = operand.length() - 3;

                //Case I : Character
                if (operand.charAt(0) == 'C')
                    return this.address + intLenghtOfOperand;

                //Case II : Hexadecimal
                intLenghtOfOperand = (intLenghtOfOperand / 2) + (intLenghtOfOperand % 2);
                return this.address + intLenghtOfOperand;
            }
            case "WORD": {
                if (AssemblyLine.isInteger(operand)) {
                    int decimal = Integer.parseInt(operand);
                    if (decimal < -8388608 || decimal > 8388607)
                        throw new Exception("Out of range");
                }
                return this.address + 3;
            }
            case "BASE": {
                if (operand.charAt(0) == '=')
                    Pass1.literals.add(operand);
            }
            case "NOBASE":
                return this.address;

            case "LTORG":
                throw new Exception("LTORG");

            case "ORG": {
                if (operand.length() > 0) {
                    int address = Pass1.calculateOperandValue(operand);
                    lastSavedAddress = address;
                    return lastSavedAddress;
                }
                return lastSavedAddress;
            }


            case "EQU":
                return this.address;

            case "CSECT": {
                Pass1.programLength.put(Pass1.nameCSECT, super.address - Pass1.programsStart);
                Pass1.programsStart = 0;
                Pass1.insertLiterals(Pass1.address);
                Pass1.nameCSECT = label;
                Pass1.address = 0;
                Pass1.ExDef.add(label);
                return 0;
            }

            case "EXTDEF": {
                String externalDefinitions = operand + comment;
                String[] tokens = externalDefinitions.split("[,]");
                for (String s : tokens)
                    Pass1.ExDef.add(s);

                return this.address;
            }
            case "EXTREF":
                return this.address;

            default:
                throw new Exception("Unknown Directive");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mnemonic.equals("BASE") || mnemonic.equals("NOBASE") || mnemonic.equals("LTORG")) {
            sb.append("");
        } else {
            sb.append(Pass2.padStringWithZeroes(Integer.toHexString(this.address), 5));
        }
        for (int i = sb.toString().length(); i <= 6; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        sb.append(this.label);
        for (int i = sb.toString().length(); i <= 15; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        sb.append(this.mnemonic);
        for (int i = sb.toString().length(); i <= 22; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        if (mnemonic.equals("EXTDEF") || mnemonic.equals("EXTREF") || mnemonic.equals("EQU")) {
            sb.append(this.operand + this.comment);
        } else {
            sb.append(this.operand);
            for (int i = sb.toString().length(); i <= 41; i++) {
                sb.append(" ");
            }
            sb.append("\t");
            sb.append(this.comment);
        }
        return sb.toString();
    }

    @Override
    public String getLabel() {
        if (label.length() == 0)
            return null;
        return label;
    }

    @Override
    public String getObjectCode() throws Exception {
        switch (mnemonic) {
            case "START": {
                Pass2.addToHashTable(SymbolTable.getHashSetOfCSECT(this.label));
                Pass2.externalRef.clear();
                Pass2.nameCSECT = this.label;
                return "H" + " " + this.label +
                        " " + Pass2.padStringWithZeroes(this.operand, 6) +
                        " " + Pass2.padStringWithZeroes(Integer.toHexString(Pass1.programLength.get(this.label)), 6);
            }
            case "END": {
                StringBuilder sb = new StringBuilder();
                sb.append("E ");
                if (firstCSECTflag) {
                    firstCSECTflag = false;
                    sb.append(Pass2.padStringWithZeroes(Integer.toHexString(globalProgramStart), 6));
                }
                sb.append("\n");
                return sb.toString();
            }
            case "RESB":
            case "RESW": {
                throw new Exception("Reserve directive, breaking T record");
                //return "";
            }
            case "BYTE": {
                StringBuilder sb = new StringBuilder();
                String value = this.operand.substring(2, this.operand.length() - 1);
                //Case I : Character
                if (operand.charAt(0) == 'C') {
                    for (char c : value.toCharArray()) {
                        sb.append(Integer.toHexString(c));
                    }
                } else {
                    //Case II : Hexadecimal
                    sb.append(value);
                }
                /**Fixed odd lengths in BYTE**/
                String temp = sb.toString().toUpperCase();
                if (temp.length() % 2 == 1) {
                    sb = new StringBuilder();
                    sb.append("0");
                    sb.append(temp);
                }

                return sb.toString().toUpperCase();
            }
            case "WORD": {
                if (AssemblyLine.isInteger(operand)) {
                    int decimal = Integer.parseInt(operand);
                    if (decimal < -8388608 || decimal > 8388607)
                        throw new Exception("Out of range");
                    String hexa = Pass2.padStringWithZeroes(Integer.toHexString(decimal), 6);
                    return hexa;
                } else {
                    Pass1.getExpressionType(operand);
                    String hexa = Pass2.padStringWithZeroes(Integer.toHexString(Pass2.calculateOperandValue(operand)), 6);
                    ArrayList<String> extRefTokens = getExtRefTokens(operand);
                    for (String str : extRefTokens) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("M ");
                        sb.append(Pass2.padStringWithZeroes(Integer.toHexString(this.address), 6));
                        sb.append(" 06 ");
                        sb.append(str);
                        Pass2.MRecords.add(sb.toString());
                    }
                    return hexa;
                }
            }
            case "BASE": {
                if (AssemblyLine.isInteger(operand)) {
                    Pass2.baseValue = Integer.parseInt(operand);
                } else {
                    Pass2.baseValue = Pass2.getSymbol(operand).getValue();
                }
                return "";
            }
            case "NOBASE": {
                Pass2.baseValue = -1;
                return "";
            }
            case "LTORG":
            case "EQU":
            case "ORG": {
                return "";
            }
            case "CSECT": {
                Pass2.addToHashTable(SymbolTable.getHashSetOfCSECT(this.label));
                Pass2.externalRef.clear();
                StringBuilder sb = new StringBuilder();
                sb.append("E ");
                if (firstCSECTflag) {
                    firstCSECTflag = false;
                    sb.append(Pass2.padStringWithZeroes(Integer.toHexString(globalProgramStart), 6));
                }
                sb.append("\n\n").append("H ");
                sb.append(this.label).append(" 000000 ");
                sb.append(Pass2.padStringWithZeroes(Integer.toHexString(Pass1.programLength.get(this.label)), 6));
                Pass2.nameCSECT = this.label;
                return sb.toString();
            }
            case "EXTREF": {
                StringBuilder sb = new StringBuilder();
                sb.append("R ");
                String[] refrences = (this.operand + this.comment).split(",");
                for (String ref : refrences) {
                    sb.append(ref).append(" ");
                    if (Pass1.isExternalDef(ref))
                        Pass2.externalRef.add(ref);
                    else
                        throw new Exception("Symbol " + ref + " doesn't have an external definition");
                }
                return sb.toString();
            }
            case "EXTDEF": {
                StringBuilder sb = new StringBuilder();
                sb.append("D ");
                String[] definitions = (this.operand + this.comment).split(",");
                for (String ref : definitions) {
                    sb.append(ref).append(" ");
                    String address = Pass2.padStringWithZeroes(Integer.toHexString(Pass2.getSymbol(ref).getValue()), 6);
                    sb.append(address).append(" ");
                }
                return sb.toString();
            }
            default:
                throw new Exception("Unknown Directive");
        }
    }

    @Override
    public void checkOperand() throws Exception {
        if ((!this.mnemonic.equals("LTORG") && (!this.mnemonic.equals("NOBASE")) && (!this.mnemonic.equals("CSECT"))) && this.operand.length() == 0) {
            throw new Exception("No Operand");
        }
    }

    @Override
    public Symbol getSymbol() throws Exception {
        int value;
        char type = 'R';
        if (mnemonic.equals("EQU")) {
            if (operand.equals("*")) {
                value = this.address;
            } else {
                value = Pass1.calculateOperandValue(operand+comment);
            }
            type = Pass1.getExpressionType(operand+comment);
        } else {
            value = this.address;
        }
        return new Symbol(label, value, type, Pass1.nameCSECT, mnemonic.equals("EQU"));

    }

    @Override
    public int getAddress() {
        return address;
    }
}
