package AssemblerCore;

/**
 * Created by louay on 5/13/2017.
 */
public class Symbol {
    private final String symbol;
    private final int value;
    private final char type;
    private final String cset;
    private final boolean exdef;


    public Symbol(String symbol, int value, char type, String cset, boolean exdef) {
        this.symbol = symbol;
        this.value = value;
        this.type = type;
        this.cset = cset;
        this.exdef = exdef;
    }

    public String getSymbolName()
    {
        return symbol;
    }
    public int getValue() {
        return value;
    }

    public char getType() {
        return type;
    }

    public String getCSETNamme() {
        return cset;
    }

    public boolean isExDef() {

        return exdef;
    }

}
