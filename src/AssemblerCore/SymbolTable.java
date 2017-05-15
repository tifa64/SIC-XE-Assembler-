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
            if(s.getCSECTName().equals(nCSECT) || s.getCSECTName().equals("!"))
                CSECTsymbols.add(s);
        }
        return CSECTsymbols;
    }
    public static void insertInHashSet(Symbol symb)
    {
        symbols.add(symb);
    }

    public static void ClearHahset()
    {
        symbols.clear();
    }

    public static boolean containsKey(String nCSECT, String symb)
    {
        HashSet<Symbol> CSECTsymbols = getHashSetOfCSECT(nCSECT);
        for(Symbol s : CSECTsymbols)
        {
            if(s.getSymbolName().equals(symb))
                return true;
        }
        return false;
    }

    public static Symbol getSymbol(String nCSECT, String symb)
    {
        HashSet<Symbol> CSECTsymbols = getHashSetOfCSECT(nCSECT);
        for(Symbol s : CSECTsymbols)
        {
            if(s.getSymbolName().equals(symb))
                return s;
        }
        return null;
    }

}
