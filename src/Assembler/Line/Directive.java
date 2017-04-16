package Assembler.Line;

import Assembler.Pass1;
import Assembler.Pass2;

/**
 * Created by louay on 3/26/2017.
 */
public class Directive extends AssemblyLine {
    protected final String label, mnemonic, operand, comment;
    protected final int address;


    protected Directive(int address, String line) {
        super(address, line);
        this.label = line.substring(0, 8).replaceAll("\\s+", "");
        this.mnemonic = line.substring(9, 15).replaceAll("\\s+", "");
        this.operand = line.substring(17, 35).replaceAll("\\s+", "");
        this.comment = line.substring(35, 66).replaceAll("\\s+", "");
        if (mnemonic.equals("START")) {
            this.address = Integer.parseInt(operand, 16);
        } else {
            this.address = super.address;
        }
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public int getNextAddress() throws Exception {
        switch (mnemonic) {
            case "START": {
                Pass1.programStart = this.address;
                return this.address;
            }
            case "END": {
                Pass1.programLength = this.address - Pass1.programStart;
                throw new Exception("End Of File");
            }
            case "RESB": {
                int decimal = Integer.parseInt(operand);
                return this.address + decimal;
            }
            case "RESW": {
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
                int decimal = Integer.parseInt(operand);
                if (decimal < -8388608 || decimal > 8388607)
                    throw new Exception("Out of range");
                return this.address + 3;
            }
            case "BASE":
                return this.address;
            default:
                throw new Exception("Unknown Directive");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mnemonic.equals("BASE")) {
            sb.append("");
        } else {
            sb.append(Integer.toHexString(this.address).toUpperCase());
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
        sb.append(this.operand);
        for (int i = sb.toString().length(); i <= 41; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        sb.append(this.comment);
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
            case "START":
                return  "H" + " " + this.label +
                        " " + Pass2.padStringWithZeroes(this.operand, 6) +
                        " " + Pass2.padStringWithZeroes(Integer.toHexString(Pass1.programLength), 6);
            case "END":
                return ("E" + " " + Pass2.padStringWithZeroes(Integer.toHexString(Pass1.programStart), 6));
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
                int decimal = Integer.parseInt(operand);
                if (decimal < -8388608 || decimal > 8388607)
                    throw new Exception("Out of range");
                String hexa = Pass2.padStringWithZeroes(Integer.toHexString(decimal), 6);
                return hexa;
            }
            case "BASE": {
                if (AssemblyLine.isInteger(operand)) {
                    Pass2.baseValue = Integer.parseInt(operand);
                } else {
                    Pass2.baseValue = Pass1.SYMTAB.get(operand);
                }
                return "";
            }
            default:
                throw new Exception("Unknown Directive");
        }
    }

    @Override
    public int getAddress() {
        return address;
    }
}
