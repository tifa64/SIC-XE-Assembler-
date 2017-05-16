package AssemblerCore.Line;

import AssemblerCore.InstructionSetLoader;

/**
 * Created by louay on 3/25/2017.
 */
public class Format2 extends Format {

    protected Format2(int address, String line) {
        super(address, line);
    }

    @Override
    public int getNextAddress() {
        return this.address + 2;
    }

    @Override
    public String getObjectCode() throws Exception {
        InstructionSetLoader isl = InstructionSetLoader.getLoader();
        String instOpCode = isl.getInstOpCode(mnemonic);

        String r1 = operand.split(",")[0];
        String r2;
        int r11 = isl.getRegOpCode(r1);
        int r22 = 0;
        if (r1.length() != operand.length()) {
            r2 = operand.split(",")[1];
            r22 = isl.getRegOpCode(r2);
        }
        return instOpCode + Integer.toHexString(r11) + Integer.toHexString(r22);
    }

    @Override
    public void checkOperand() throws Exception {
        if (this.operand.length() == 0) {
            throw new Exception("No Operand");
        }
        String[] operands = this.operand.split(",");
        if (operands.length < 2 && !this.mnemonic.equals("CLEAR") && !this.mnemonic.equals("TIXR")) {
            throw new Exception("No enough operands");
        }
        if (operands.length < 1) {
            throw new Exception("No enough operands");
        }
        String op1 = operands[0];
        InstructionSetLoader.getLoader().getRegOpCode(op1);
        if (operands.length != 1) {
            String op2 = operands[1];
            InstructionSetLoader.getLoader().getRegOpCode(op2);
        }
    }
}
