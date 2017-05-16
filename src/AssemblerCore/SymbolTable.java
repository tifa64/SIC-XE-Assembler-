package AssemblerCore;

import java.util.HashSet;

/**
 * Created by Krietallo on 5/14/2017.
 */
public class SymbolTable {

    private static final HashSet<Symbol> symbols = new HashSet<>();

    public static HashSet<Symbol> getHashSetOfCSECT(String nCSECT) {
        HashSet<Symbol> CSECTsymbols = new HashSet<>();
        for (Symbol s : symbols) {
            if (s.getCSECTName().equals(nCSECT))
                CSECTsymbols.add(s);
        }
        return CSECTsymbols;
    }

    public static void insertInHashSet(Symbol symb) {
        symbols.add(symb);
    }

    public static void clearHashSet() {
        symbols.clear();
    }

    public static boolean containsKey(String nCSECT, String symb) {
        HashSet<Symbol> CSECTsymbols = getHashSetOfCSECT(nCSECT);
        for (Symbol s : CSECTsymbols) {
            if (s.getSymbolName().equals(symb))
                return true;
        }
        return false;
    }

    public static Symbol getSymbol(String nCSECT, String symb) {
        HashSet<Symbol> CSECTsymbols = getHashSetOfCSECT(nCSECT);
        for (Symbol s : CSECTsymbols) {
            if (s.getSymbolName().equals(symb))
                return s;
        }
        return null;
    }

    public static boolean symbolIsEqu(String symb) throws Exception {
        Symbol tempSymbol = getSymbol(Pass2.nameCSECT, symb);
        if (tempSymbol != null)
            return tempSymbol.getIsEqu();
        else if (Pass2.externalRef.contains(symb))
            return false;

        throw new Exception("An EQU Symbol " + symb + " isn't immediate");
    }

}
