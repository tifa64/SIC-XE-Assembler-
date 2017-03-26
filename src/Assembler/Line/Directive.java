package Assembler.Line;

/**
 * Created by louay on 3/26/2017.
 */
public class Directive extends AssemblyLine {
    protected final String label, mnemonic, operand, comment;
    protected final int address;

    protected Directive(int address, String line) {
        super(address, line);
        this.label = line.substring(0, 8).replaceAll("\\s+","");
        this.mnemonic = line.substring(9, 15).replaceAll("\\s+","");
        this.operand = line.substring(17, 35).replaceAll("\\s+","");
        this.comment = line.substring(35, 66).replaceAll("\\s+","");
        if (mnemonic.equals("START")){
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
    public int getNextAddress() {
        return this.address; //lessa fiha kalam
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(this.address));
        sb.append("\t");
        sb.append(this.label);
        sb.append("\t");
        sb.append(this.mnemonic);
        sb.append("\t");
        sb.append(this.operand);
        sb.append("\t");
        sb.append(this.comment);
        sb.append("\n");
        return sb.toString();
    }
}
