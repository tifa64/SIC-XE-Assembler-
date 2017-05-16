package AssemblerCore.Line;

import AssemblerCore.InstructionSetLoader;
import AssemblerCore.Pass2;

/**
 * Created by louay on 3/25/2017.
 */
public class Format1 extends Format {


    protected Format1(int address, String line) {
        super(address, line);
    }

    @Override
    public int getNextAddress() {
        return this.address + 1;
    }

    @Override
    public String getObjectCode() {

        InstructionSetLoader isl = InstructionSetLoader.getLoader();
        return Pass2.padStringWithZeroes(isl.getInstOpCode(mnemonic), 2);
    }

    @Override
    public void checkOperand() throws Exception {}
}
