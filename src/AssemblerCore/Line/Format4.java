package AssemblerCore.Line;

import AssemblerCore.InstructionSetLoader;
import AssemblerCore.Pass1;
import AssemblerCore.Pass2;

import java.util.BitSet;

import static AssemblerCore.SymbolTable.symbolIsEqu;

/**
 * Created by louay on 3/25/2017.
 */
public class Format4 extends Format {

    private static final int n = 0, i = 1, x = 2, b = 3, p = 4, e = 5;

    private final String mnemonicProper;

    Format4(int address, String line) {
        super(address, line);
        this.mnemonicProper = this.mnemonic.substring(1);
    }

    @Override
    public int getNextAddress() {

        if (operand.charAt(0) == '=')
            Pass1.literals.add(operand);

        return this.address + 4;
    }

    @Override
    public String getObjectCode() throws Exception {
        String opcodeBin = Integer.toBinaryString(Integer.parseInt(InstructionSetLoader.getLoader().getInstOpCode(mnemonicProper), 16));
        opcodeBin = Pass2.padStringWithZeroes(opcodeBin, 8);

        String addressHex;
        BitSet nixbpe = new BitSet(6);
        nixbpe.set(e);
        String value = operand;

        String symbolOperand = operand.split(",")[0];
        if (!AssemblyLine.isInteger(symbolOperand.substring(1))) {
            if (symbolOperand.charAt(0) == '@' || symbolOperand.charAt(0) == '#') {
                if (symbolIsEqu(symbolOperand.substring(1, symbolOperand.length())) && symbolOperand.charAt(0) != '#') {
                    throw new Exception("An EQU Symbol " + symbolOperand + " isn't immediate");
                }
            } else if (symbolIsEqu(symbolOperand)) {
                throw new Exception("An EQU Symbol " + symbolOperand + " isn't immediate");
            }
        }

        if (operand.endsWith(",X")) {
            nixbpe.set(x);
            value = operand.substring(0, operand.length() - 2);
        }

        if (operand.charAt(0) == '@') {
            nixbpe.set(n);
            value = value.substring(1);
            addressHex = Integer.toHexString(Pass2.getSymbolValue(value));
        } else if (operand.charAt(0) == '#') {
            nixbpe.set(i);
            value = value.substring(1);
            if (value.charAt(0) <= '9' && value.charAt(0) >= '0') {
                addressHex = Integer.toHexString(Integer.parseInt(value));
            } else {
                addressHex = Integer.toHexString(Pass2.getSymbolValue(value));
            }
        } else {
            addressHex = Integer.toHexString(Pass2.getSymbolValue(value));
            nixbpe.set(n);
            nixbpe.set(i);
        }

        addressHex = Pass2.padStringWithZeroes(addressHex, 5);


        StringBuilder sb = new StringBuilder();
        sb.append(opcodeBin.substring(0, 6));
        sb.append(nixbpe.get(n) ? "1" : "0");
        sb.append(nixbpe.get(i) ? "1" : "0");
        sb.append(nixbpe.get(x) ? "1" : "0");
        sb.append(nixbpe.get(b) ? "1" : "0");
        sb.append(nixbpe.get(p) ? "1" : "0");
        sb.append(nixbpe.get(e) ? "1" : "0");

        if (!isInteger(value)) {
            //inserting M record
            StringBuilder mRecordSB = new StringBuilder();
            mRecordSB.append("M ");
            String mAddressHex = Pass2.padStringWithZeroes(Integer.toHexString(this.address + 1).toUpperCase(), 6);
            mRecordSB.append(mAddressHex);
            mRecordSB.append(" 05 +");
            String mRecordExtra;
            if(!Pass2.externalRef.contains(symbolOperand))
                mRecordExtra = Pass2.nameCSECT;
            else if (symbolOperand.charAt(0) == '@' || symbolOperand.charAt(0) == '#') {
                mRecordExtra = symbolOperand.substring(1);
            } else {
                mRecordExtra = symbolOperand;
            }
            mRecordSB.append(mRecordExtra);

            Pass2.MRecords.add(mRecordSB.toString());
        }

        return (Integer.toHexString(Integer.parseInt(sb.toString(), 2)) + addressHex).toUpperCase();
    }
}
