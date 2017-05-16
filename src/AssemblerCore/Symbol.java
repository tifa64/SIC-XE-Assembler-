package AssemblerCore;

/**
 * Created by louay on 5/13/2017.
 */
public class Symbol {

    private final String symbolName;
    private final int value;
    private final char type;
    private final String cset;
    private final boolean exdef;
    private final boolean isEqu;

    public Symbol(String symbol, int value, char type, String cset, boolean exdef, boolean isEqu) {
        this.symbolName = symbol;
        this.value = value;
        this.type = type;
        this.cset = cset;
        this.exdef = exdef;
        this.isEqu = isEqu;
    }

    public int getValue() {
        return value;
    }

    public char getType() {
        return type;
    }

    public String getCSECTName() {
        return cset;
    }

    public boolean isExDef() {

        return exdef;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public boolean getIsEqu() {
        return isEqu;
    }
}
