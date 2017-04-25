package AssemblerCore.Line;

import AssemblerCore.Pass2;

/**
 * Created by Krietallo on 4/25/2017.
 */
public class Literal extends AssemblyLine{

    protected final String mnemonic;
    protected Literal(int address, String mnemonic) {
        super(address, mnemonic);
        this.mnemonic = mnemonic;
    }

    @Override
    public int getType() {
        return -2;
    }

    @Override
    public int getNextAddress() throws Exception {
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

        return mnemonic.substring(2,mnemonic.length()-1);
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
