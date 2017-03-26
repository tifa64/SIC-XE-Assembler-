package Assembler.Line;

/**
 * Created by louay on 3/26/2017.
 */
public class Directive extends AssemblyLine {


    protected Directive(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public int getNextAddress() {
        return 0; //lessa fiha kalam
    }
}
