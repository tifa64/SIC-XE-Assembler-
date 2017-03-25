package Assembler.Formats;

/**
 * Created by louay on 3/25/2017.
 */
public class Format3 extends Format {

    protected Format3(String label, String operation, String operand, String comment) {
        super(label, operation, operand, comment);
    }

    @Override
    public int getType() {
        return 3;
    }
}
