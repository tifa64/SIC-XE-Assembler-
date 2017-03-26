package Assembler.Line;

import Assembler.InstructionSetLoader;

/**
 * Created by louay on 3/25/2017.
 */
public abstract class AssemblyLine {

    protected final String line;
    protected final int address;

    protected AssemblyLine(int address, String line) {
        this.address = address;
        this.line = line;
    }

    public static AssemblyLine getAssemblyLineInstance(int address, String line) throws Exception {

        switch (InstructionSetLoader.getLoader().getFormatType(line)) {
            case -1:
                return new Directive(address, line);
            case 0:
                return new Comment(address, line);
            case 1:
                return new Format1(address, line);
            case 2:
                return new Format2(address, line);
            case 3:
                return new Format3(address, line);
            case 4:
                return new Format4(address, line);
            default:
                throw new Exception("Unknown instruction");
        }
    }

    public abstract int getType();

    public abstract int getNextAddress() throws Exception;

    public int getAddress() {
        return address;
    }

    public abstract String getLabel();


}
