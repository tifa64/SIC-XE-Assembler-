package AssemblerCore.Line;

import AssemblerCore.Pass2;
import AssemblerCore.Symbol;

/**
 * Created by louay on 3/26/2017.
 */
public class Comment extends AssemblyLine {

    protected Comment(int address, String line) {
        super(address, line);
    }

    @Override
    public int getNextAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Pass2.padStringWithZeroes(Integer.toHexString(this.address), 5));
        for (int i = sb.toString().length(); i <= 6; i++) {
            sb.append(" ");
        }
        sb.append("\t");
        sb.append(this.line);
        return sb.toString();
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String getObjectCode() {
        return "";
    }

    @Override
    public void checkOperand() throws Exception {

    }

    @Override
    public Symbol getSymbol() throws Exception {
        return null;
    }
}
