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
}
