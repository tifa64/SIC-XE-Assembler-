package AssemblerCore;

/**
 * Created by louay on 5/13/2017.
 */
public class Symbol {
    private final String symbol;
    private final int value;
    private final char type;

    public Symbol(String symbol, int value, char type) {
        this.symbol = symbol;
        this.value = value;
        this.type = type;
    }
}
