package Assembler.Line.Formats;

import Assembler.Line.AssemblyLine;

/**
 * Created by louay on 3/25/2017.
 */
public class Format2 extends AssemblyLine {

    protected Format2(int address, String label, String operation, String operand, String comment) {
        super(address, label, operation, operand, comment);
    }

    @Override
    public int getType() {
        return 2;
    }
}
