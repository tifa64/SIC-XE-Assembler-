package Assembler.Line;

/**
 * Created by louay on 3/26/2017.
 */
public class Directive extends AssemblyLine {


    protected Directive(int address, String label, String operation, String operand, String comment) {
        super(address, label, operation, operand, comment);
    }

    @Override
    public int getType() {
        return -1;
    }
}
