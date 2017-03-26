package Assembler.Line;

/**
 * Created by louay on 3/25/2017.
 */
public class Format3 extends Format {


    protected Format3(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public int getNextAddress() {
        return this.address + 3;
    }
}
