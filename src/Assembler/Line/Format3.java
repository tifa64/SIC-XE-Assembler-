
package Assembler.Line;
import java.util.BitSet;

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

        BitSet bits1 = new BitSet(6);

        if(operand.charAt(0) == '@')
            bits1.set(0);

        else if(operand.charAt(0) == '#')
        {
            bits1.set(1);

        }


        else
        {
            bits1.set(0);
            bits1.set(1);
        }
        if(operand.endsWith(",X"))
            bits1.set(2);
        
        return null;

    }
}
