package Assembler;

import java.util.Hashtable;

/**
 * Created by Krietallo on 3/26/2017.
 */
public class SymbolTable {

    private final Hashtable<String, Integer> SYMTAB = new Hashtable<String, Integer> ();

    public  Hashtable<String, Integer> get_SYMTAB()
    {
        return SYMTAB;
    }

    

}
