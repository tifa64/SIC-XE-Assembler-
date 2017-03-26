package Assembler.Line;

/**
 * Created by louay on 3/26/2017.
 */
public abstract class Format extends AssemblyLine{
    protected final String label, mnemonic, operand, comment;

    protected Format(int address, String line) {
        super(address, line);
        this.label = line.substring(0, 8).replaceAll("\\s+","");
        this.mnemonic = line.substring(9, 15).replaceAll("\\s+","");
        this.operand = line.substring(17, 35).replaceAll("\\s+","");
        this.comment = line.substring(35, 66).replaceAll("\\s+","");
    }
}
