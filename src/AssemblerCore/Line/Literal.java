package AssemblerCore.Line;

/**
 * Created by Krietallo on 4/25/2017.
 */
public class Literal extends AssemblyLine{


    protected Literal(int address, String line) {
        super(address, line);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getNextAddress() throws Exception {
        return 0;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String getObjectCode() throws Exception {
        return null;
    }
}
