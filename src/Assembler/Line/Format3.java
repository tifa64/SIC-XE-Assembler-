
package Assembler.Line;
import Assembler.InstructionSetLoader;
import Assembler.Pass1;

import java.util.BitSet;

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
    public String getObjectCode() {

        String modifiedOperand = operand;
        InstructionSetLoader isl = InstructionSetLoader.getLoader();
        String instOpCode = isl.getInstOpCode(mnemonic);
        int intInstOpCode = Integer.parseInt(instOpCode, 16);
        String binInstOpCode = Integer.toBinaryString(intInstOpCode);

        char n = 0, i = 0, x = 0, b = 0, p = 0, e = 0;



        if(operand.charAt(0) == '@')
        {
            modifiedOperand = operand.substring(1,operand.length());
            n = '1';
        }


        else if(operand.charAt(0) == '#')
        {
            modifiedOperand = operand.substring(1,operand.length());
            i = '1';
        }


        else
        {
            n = '1';
            i = '1';
        }
        if(operand.endsWith(",X"))
        {
            modifiedOperand = operand.substring(0,operand.length()-1);
            x = '1';
        }

        int PC = getNextAddress();
        int TA = SYMTAB.get(modifiedOperand);

        int displacement = TA - PC;

        if(displacement >= -2048 && displacement <= 2047)
            p = '1';
        else
        {
            displacement = TA - baseValue;
            b = '1';
        }


        binInstOpCode.replace(binInstOpCode.charAt(6), n);
        binInstOpCode.replace(binInstOpCode.charAt(7),i);

        /*From Binary to hex*/
        int decimalRep = Integer.parseInt(instOpCode, 2);
        String tempHex1 = Integer.toHexString(decimalRep);


        StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append(b);
        sb.append(p);
        sb.append(e);

        String middle = sb.toString();


        decimalRep = Integer.parseInt(middle, 2);
        String tempHex2 = Integer.toHexString(decimalRep);

        String tempHex3 = Integer.toHexString(displacement);

        return tempHex1 + tempHex2 + tempHex3;

    }
}
