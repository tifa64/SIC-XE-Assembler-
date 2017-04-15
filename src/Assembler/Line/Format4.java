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
        String opcodeBin = Integer.toBinaryString(Integer.parseInt(InstructionSetLoader.getLoader().getInstOpCode(mnemonicProper), 16));
        if (opcodeBin.length() < 8) {
            StringBuilder sb = new StringBuilder();
            for (int i = opcodeBin.length(); i < 8; i++) {
                sb.append("0");
            }
            sb.append(opcodeBin);
            opcodeBin = sb.toString();
        }

        String addressHexa;
        BitSet nixbpe = new BitSet(6);
        nixbpe.set(e);
        String value = operand;
        if (operand.endsWith(",X")) {
            nixbpe.set(x);
            value = operand.substring(0, operand.length() - 2);
        }

        if (operand.charAt(0) == '@') {
            nixbpe.set(n);
            value = value.substring(1);
            addressHexa = Integer.toHexString(Pass1.SYMTAB.get(value));
        } else if (operand.charAt(0) == '#') {
            nixbpe.set(i);
            value = value.substring(1);
            if (value.charAt(0) <= '9' && value.charAt(0) >= '0') {
                addressHexa = Integer.toHexString(Integer.parseInt(value));
            } else {
                addressHexa = Integer.toHexString(Pass1.SYMTAB.get(value));
            }
        } else {
            addressHexa = Integer.toHexString(Pass1.SYMTAB.get(value));
            nixbpe.set(n);
            nixbpe.set(i);
        }

        if (addressHexa.length() < 5) {
            StringBuilder sb = new StringBuilder();
            for (int i = addressHexa.length(); i < 5; i++) {
                sb.append("0");
            }
            sb.append(addressHexa);
            addressHexa = sb.toString();
        }


        StringBuilder sb = new StringBuilder();
        sb.append(opcodeBin.substring(0, 6));
        sb.append(nixbpe.get(n) ? "1" : "0");
        sb.append(nixbpe.get(i) ? "1" : "0");
        sb.append(nixbpe.get(x) ? "1" : "0");
        sb.append(nixbpe.get(b) ? "1" : "0");
        sb.append(nixbpe.get(p) ? "1" : "0");
        sb.append(nixbpe.get(e) ? "1" : "0");

        return (Integer.toHexString(Integer.parseInt(sb.toString(), 2)) + addressHexa).toUpperCase();
    }
}
