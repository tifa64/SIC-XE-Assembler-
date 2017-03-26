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
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(this.address));
        sb.append("\t\t");
        sb.append(this.line);
        sb.append("\n");
        return sb.toString();
    }
}
