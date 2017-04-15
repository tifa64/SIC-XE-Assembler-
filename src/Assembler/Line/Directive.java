package Assembler.Line;

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

        if (mnemonic.equals("START")) {
            return this.address;
        } else if (mnemonic.equals("END")) {
            throw new Exception("End Of File");
        } else if (mnemonic.equals("RESB")) {
            int decimal = Integer.parseInt(operand);

            return this.address + decimal;
        } else if (mnemonic.equals("RESW")) {
            int decimal = Integer.parseInt(operand);
            decimal *= 3;

            return this.address + decimal;
        } else if (mnemonic.equals("BYTE")) {

            int intLenghtOfOperand = operand.length() - 3;

            //Case I : Character
            if (operand.charAt(0) == 'C')
                return this.address + intLenghtOfOperand;

            //Case II : Hexadecimal
            intLenghtOfOperand = (intLenghtOfOperand / 2) + (intLenghtOfOperand % 2);
            return this.address + intLenghtOfOperand;
        } else if (mnemonic.equals("WORD")) {
            int decimal = Integer.parseInt(operand);
            if (decimal < -8388608 || decimal > 8388607)
                throw new Exception("Out of range");
            return this.address + 3;
        }
        else if (mnemonic.equals("BASE")){
            return this.address;
        }
        else {
            throw new Exception("Unknown Directive");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mnemonic.equals("BASE")){
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
    public int getAddress() {
        return address;
    }
}
