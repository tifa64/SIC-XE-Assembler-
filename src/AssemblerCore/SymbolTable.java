package AssemblerCore;

import java.util.HashSet;

/**
 * Created by Krietallo on 5/14/2017.
 */
public class SymbolTable {

    private static final HashSet<Symbol> symbols = new HashSet<Symbol>();

    public static HashSet<Symbol> getHashSetOfCSECT(String nCSECT)
    {
        HashSet<Symbol> CSECTsymbols = new HashSet<Symbol>();
        int sz = symbols.size();
        for(Symbol s : symbols)
        {
            if(s.getCSETNamme().equals(nCSECT))
                CSECTsymbols.add(s);
        }
        return CSECTsymbols;
    }
    public static void insertInHashSet(Symbol symb)
    {
        symbols.add(symb);
    }

}
