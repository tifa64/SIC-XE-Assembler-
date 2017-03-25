package Assembler.Formats;

import Assembler.InstructionSetLoader;

/**
 * Created by louay on 3/25/2017.
 */
public abstract class Format {

    protected final String label, operation, operand, comment;

    public static Format getFormat(int address, String command) throws Exception {
        String label = command.substring(0, 8).replaceAll("\\s+","");
        String operation = command.substring(9, 15).replaceAll("\\s+","");
        String operand = command.substring(17, 35).replaceAll("\\s+","");
        String comment = command.substring(35, 66).replaceAll("\\s+","");
        switch (InstructionSetLoader.getLoader().getFormatType(operation)){
            case 1: return new Format1(label, operation, operand, comment);
            case 2: return new Format2(label, operation, operand, comment);
            case 3: return new Format3(label, operation, operand, comment);
            case 4: return new Format4(label, operation.substring(1), operand, comment);
            default: throw new Exception("Unknown instruction");
        }
    }

    protected Format(String label, String operation, String operand, String comment) {
        this.label = label;
        this.operation = operation;
        this.operand = operand;
        this.comment = comment;
    }

    public abstract int getType();
}
