package AssemblerCore;

import java.util.HashSet;

/**
 * Created by Krietallo on 5/14/2017.
 */
public class SymbolTable {

    private final HashSet<Symbol> symbols = new HashSet<Symbol>();

    public  HashSet<Symbol> getHashSetOfCSECT(int nCSECT)
    {
        HashSet<Symbol> CSECTsymbols = new HashSet<Symbol>();
        int sz = symbols.size();
        for(Symbol s : symbols)
        {
            if(s.getCSETNumber() == nCSECT)
                CSECTsymbols.add(s);
        }
        return CSECTsymbols;
    }
    public void insertInHashSet(Symbol symb)
    {
        symbols.add(symb);
    }

}
