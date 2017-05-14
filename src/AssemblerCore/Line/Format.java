package AssemblerCore.Line;

import AssemblerCore.Pass1;
import AssemblerCore.Pass2;
import AssemblerCore.Symbol;

/**
 * Created by louay on 3/26/2017.
 */
public abstract class Format extends AssemblyLine {
    protected final String label, mnemonic, operand, comment;

    protected Format(int address, String line) {
        super(address, line);
        this.label = line.substring(0, 8).replaceAll("\\s+", "");
        this.mnemonic = line.substring(9, 15).replaceAll("\\s+", "");
        this.operand = line.substring(17, 35).replaceAll("\\s+", "");
        this.comment = line.substring(35, 66).replaceAll("\\s+", "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Pass2.padStringWithZeroes(Integer.toHexString(this.address), 5));
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
    public void checkOperand() throws Exception {
        if (!this.mnemonic.equals("RSUB") && this.operand.length() == 0) {
            throw new Exception("No Operand");
        }
    }

    @Override
    public Symbol getSymbol() throws Exception {
        return new Symbol(label, address, 'R', Pass1.nameCSECT, Pass1.isExternalDef(mnemonic));
    }
}
