package Assembler.Formats;

/**
 * Created by louay on 3/25/2017.
 */
public class Format2 extends Format {

    protected Format2(String label, String operation, String operand, String comment) {
        super(label, operation, operand, comment);
    }

    @Override
    public int getType() {
        return 2;
    }
}
