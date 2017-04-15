package Assembler.Line;

import Assembler.InstructionSetLoader;

/**
 * Created by louay on 3/25/2017.
 */
public class Format1 extends Format {


    protected Format1(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getNextAddress() {
        return this.address + 1;
    }

    @Override
    public String getObjectCode() {

        InstructionSetLoader isl = InstructionSetLoader.getLoader();
        String instOpCode = isl.getInstOpCode(mnemonic);
        return instOpCode;
    }
}
