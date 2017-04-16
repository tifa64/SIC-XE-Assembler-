package Assembler.Line;

import Assembler.InstructionSetLoader;
import Assembler.Pass2;

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
        String instOpCode = Pass2.padStringWithZeroes(isl.getInstOpCode(mnemonic), 2);
        return instOpCode;
    }
}
