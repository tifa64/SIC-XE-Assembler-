package AssemblerCore.Line;

import AssemblerCore.Pass1;
import AssemblerCore.Pass2;
import AssemblerCore.Symbol;

/**
 * Created by Krietallo on 4/25/2017.
 */
public class Literal extends AssemblyLine {

    private final String mnemonic;

    public Literal(int address, String mnemonic) {
        super(address, mnemonic);
        this.mnemonic = mnemonic;
    }

    @Override
    public int getNextAddress() {
        int intLenghtOfMnemonic = mnemonic.length() - 4;

        //Case I : Character
        if (mnemonic.charAt(1) == 'C')
            return this.address + intLenghtOfMnemonic;

        //Case II : Hexadecimal
        intLenghtOfMnemonic = (intLenghtOfMnemonic / 2) + (intLenghtOfMnemonic % 2);
        return this.address + intLenghtOfMnemonic;
    }

    @Override
    public String getLabel() {
        return "*";
    }

    @Override
    public String getObjectCode() throws Exception {
        StringBuilder sb = new StringBuilder();
        String value = this.mnemonic.substring(3, this.mnemonic.length() - 1);
        //Case I : Character
        if (mnemonic.charAt(1) == 'C') {
            for (char c : value.toCharArray()) {
                sb.append(Integer.toHexString(c));
            }
        } else {
            //Case II : Hexadecimal
            sb.append(value);
        }
        /**Fixed odd lengths in BYTE**/
        String temp = sb.toString().toUpperCase();
        if (temp.length() % 2 == 1) {
            sb = new StringBuilder();
            sb.append("0");
            sb.append(temp);
        }

        return sb.toString().toUpperCase();
    }

    @Override
    public void checkOperand() throws Exception {

    }

    @Override
    public Symbol getSymbol() throws Exception {
        return new Symbol(mnemonic, address, 'R', Pass1.nameCSECT, Pass1.isExternalDef(mnemonic), false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Pass2.padStringWithZeroes(Integer.toHexString(this.address), 5));
        for (int i = sb.toString().length(); i <= 6; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        sb.append("*");
        for (int i = sb.toString().length(); i <= 15; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        sb.append(this.mnemonic);
        for (int i = sb.toString().length(); i <= 22; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
