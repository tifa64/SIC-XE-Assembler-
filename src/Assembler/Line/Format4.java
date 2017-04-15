package Assembler.Line;

import Assembler.InstructionSetLoader;
import Assembler.Pass1;

import java.util.BitSet;

/**
 * Created by louay on 3/25/2017.
 */
public class Format4 extends Format {

    private static final int n = 0, i = 1, x = 2, b = 3, p = 4, e = 5;

    private final String mnemonicProper;

    protected Format4(int address, String line) {
        super(address, line);
        this.mnemonicProper = this.mnemonic.substring(1);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public int getNextAddress() {
        return this.address + 4;
    }

    @Override
    public String getObjectCode() {
        String opcodeBin = Integer.toBinaryString(Integer.parseInt( InstructionSetLoader.getLoader().getInstOpCode(mnemonicProper),2));
        if (opcodeBin.length() < 8){
            StringBuilder sb = new StringBuilder();
            for (int i = opcodeBin.length(); i < 8; i++){
                sb.append("0");
            }
            sb.append(opcodeBin);
            opcodeBin = sb.toString();
        }

        String addressHexa;
        BitSet nixbpe = new BitSet(6);
        nixbpe.set(e);
        if(operand.charAt(0) == '@') {
            nixbpe.set(n);
            addressHexa = Integer.toHexString(Pass1.SYMTAB.get(operand.substring(1)));
        }
        else if(operand.charAt(0) == '#') {
            nixbpe.set(i);
            addressHexa = Integer.toHexString(Integer.parseInt(operand.substring(1)));
        } else {
            addressHexa = Integer.toHexString(Pass1.SYMTAB.get(operand));
            nixbpe.set(n);
            nixbpe.set(i);
        }

        if (addressHexa.length() < 20){
            StringBuilder sb = new StringBuilder();
            for (int i = addressHexa.length(); i < 20; i++){
                sb.append("0");
            }
            sb.append(addressHexa);
            addressHexa = sb.toString();
        }

        if(operand.endsWith(",X"))
            nixbpe.set(x);

        StringBuilder sb = new StringBuilder();
        sb.append(opcodeBin.substring(0,6));
        sb.append(nixbpe.get(n));
        sb.append(nixbpe.get(i));
        sb.append(nixbpe.get(x));
        sb.append(nixbpe.get(b));
        sb.append(nixbpe.get(p));
        sb.append(nixbpe.get(e));

        return Integer.toHexString(Integer.parseInt(sb.toString(), 2)) + addressHexa;
    }
}
