package AssemblerCore;

/**
 * Created by louay on 5/13/2017.
 */
public class Symbol {

    private final String symbolName;
    private final int value;
    private final char type;
    private final String cset;
    private final boolean isEqu;

    public Symbol(String symbol, int value, char type, String cset, boolean isEqu) {
        this.symbolName = symbol;
        this.value = value;
        this.type = type;
        this.cset = cset;
        this.isEqu = isEqu;
    }

    public int getValue() {
        return value;
    }

    char getType() {
        return type;
    }

    String getCSECTName() {
        return cset;
    }

    String getSymbolName() {
        return symbolName;
    }

    boolean getIsEqu() {
        return isEqu;
    }
}
