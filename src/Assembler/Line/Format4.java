package Assembler.Line;

/**
 * Created by louay on 3/25/2017.
 */
public class Format4 extends Format {

    private final String mnemonicProper;

    protected Format4(int address, String line) {
        super(address, line);
        this.mnemonicProper = this.mnemonic.substring(1);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public int getNextAddress() {
        return this.address + 4;
    }

    @Override
    public String getObjectCode() {
        return null;
    }
}
