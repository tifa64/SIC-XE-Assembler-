package Assembler.Line;

import Assembler.InstructionSetLoader;
import Assembler.Line.Formats.Format1;
import Assembler.Line.Formats.Format2;
import Assembler.Line.Formats.Format3;
import Assembler.Line.Formats.Format4;

/**
 * Created by louay on 3/25/2017.
 */
public abstract class AssemblyLine {

    protected final String label, operation, operand, comment;
    protected final int address;

    public static AssemblyLine getFormat(int address, String command) throws Exception {
        String label = command.substring(0, 8).replaceAll("\\s+","");
        String mnemonic = command.substring(9, 15).replaceAll("\\s+","");
        String operand = command.substring(17, 35).replaceAll("\\s+","");
        String comment = command.substring(35, 66).replaceAll("\\s+","");
        switch (InstructionSetLoader.getLoader().getFormatType(mnemonic)){
            case 1: return new Format1(address, label, mnemonic, operand, comment);
            case 2: return new Format2(address, label, mnemonic, operand, comment);
            case 3: return new Format3(address, label, mnemonic, operand, comment);
            case 4: return new Format4(address, label, mnemonic.substring(1), operand, comment);
            default: throw new Exception("Unknown instruction");
        }
    }

    protected AssemblyLine(int address, String label, String operation, String operand, String comment) {
        this.address = address;
        this.label = label;
        this.operation = operation;
        this.operand = operand;
        this.comment = comment;
    }

    public abstract int getType();
}
