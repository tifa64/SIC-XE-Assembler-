package Assembler.Line;

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
        sb.append(Integer.toHexString(this.address).toUpperCase());
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
}
