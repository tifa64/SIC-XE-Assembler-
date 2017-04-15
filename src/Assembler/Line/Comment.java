package Assembler.Line;

/**
 * Created by louay on 3/26/2017.
 */
public class Comment extends AssemblyLine {


    protected Comment(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getNextAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(this.address).toUpperCase());
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
}
