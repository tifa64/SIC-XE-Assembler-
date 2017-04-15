package Assembler.Line;

import Assembler.InstructionSetLoader;

/**
 * Created by louay on 3/25/2017.
 */
public class Format2 extends Format {


    protected Format2(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public int getNextAddress() {
        return this.address + 2;
    }

    @Override
    public String getObjectCode() {
        InstructionSetLoader temp = InstructionSetLoader.getLoader();
        String instOpCode = temp.getInstOpCode(mnemonic);

        String r1 = operand.split(",")[0];
        String r2;
        int r11 = temp.getRegOpCode(r1);
        if(r1.length() != operand.length())
        {
            r2 = operand.split(",")[1];
            int r22 = temp.getRegOpCode(r2);
        }
        else {
            r2 = "0";
        }
        return instOpCode+r1+r2;
    }
}
