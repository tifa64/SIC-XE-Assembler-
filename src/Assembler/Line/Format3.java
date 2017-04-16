package Assembler.Line;

import Assembler.InstructionSetLoader;
import Assembler.Pass2;

import static Assembler.Pass1.SYMTAB;
import static Assembler.Pass2.baseValue;

/**
 * Created by louay on 3/25/2017.
 */
public class Format3 extends Format {


    protected Format3(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public int getNextAddress() {
        return this.address + 3;
    }

    @Override
    public String getObjectCode() throws Exception {

        String modifiedOperand = operand;
        InstructionSetLoader isl = InstructionSetLoader.getLoader();
        String instOpCode = isl.getInstOpCode(mnemonic);
        int intInstOpCode = Integer.parseInt(instOpCode, 16);
        String binInstOpCode = Integer.toBinaryString(intInstOpCode);
        boolean flag = false;

        if (binInstOpCode.length() < 8) {
            StringBuilder sb = new StringBuilder();
            for (int i = binInstOpCode.length(); i < 8; i++)
                sb.append("0");

            sb.append(binInstOpCode);
            binInstOpCode = sb.toString();
        }

        char n = '0', i = '0', x = '0', b = '0', p = '0', e = '0';


        if (operand.charAt(0) == '@') {
            modifiedOperand = operand.substring(1, operand.length());
            n = '1';
        } else if (operand.charAt(0) == '#') {
            modifiedOperand = operand.substring(1, operand.length());
            i = '1';
        } else {
            n = '1';
            i = '1';
        }
        if (operand.endsWith(",X")) {
            modifiedOperand = operand.substring(0, operand.length() - 2);
            x = '1';
        }

        int PC = getNextAddress();
        int TA;

        if (isInteger(modifiedOperand)) {
            TA = Integer.parseInt(modifiedOperand);
            flag = true;
        } else
            TA = SYMTAB.get(modifiedOperand);

        int displacement = TA - PC;

        if (!flag) {
            if (displacement >= -2048 && displacement <= 2047)
                p = '1';
            else {
                if (Pass2.baseValue == -1) {
                    throw new Exception("NO BASE");
                }
                displacement = TA - baseValue;
                b = '1';
            }
        } else {
            displacement = TA;
        }


        binInstOpCode = binInstOpCode.substring(0, 6);
        binInstOpCode += n;
        binInstOpCode += i;

        /*From Binary to hex*/
        int decimalRep = Integer.parseInt(binInstOpCode, 2);
        String tempHex1 = Integer.toHexString(decimalRep);

        if (tempHex1.length() == 1) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("0");
            sb3.append(tempHex1);
            tempHex1 = sb3.toString();
        }


        StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append(b);
        sb.append(p);
        sb.append(e);

        String middle = sb.toString();


        decimalRep = Integer.parseInt(middle, 2);
        String tempHex2 = Integer.toHexString(decimalRep);


        String tempHex3 = Integer.toHexString(displacement);
        if (tempHex3.length() < 3) {
            StringBuilder sb2 = new StringBuilder();

            for (int j = tempHex3.length(); j < 3; j++) {
                sb2.append("0");
            }
            sb2.append(tempHex3);
            tempHex3 = sb2.toString();
        } else if (tempHex3.length() > 3)
            tempHex3 = tempHex3.substring(5, 8);


        return (tempHex1 + tempHex2 + tempHex3).toUpperCase();

    }
}
