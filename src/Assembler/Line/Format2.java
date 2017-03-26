package Assembler.Line;

/**
 * Created by louay on 3/25/2017.
 */
public class Format2 extends Format {


    protected Format2(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public int getNextAddress() {
        return this.address + 2;
    }
}
