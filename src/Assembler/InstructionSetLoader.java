package Assembler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Created by louay on 3/25/2017.
 */
public class InstructionSetLoader {

    private static InstructionSetLoader loader = null;

    public static InstructionSetLoader getLoader(){
        if (loader == null){
            loader = new InstructionSetLoader();
        }
        return loader;
    }

    private final Hashtable<String,String> instructionSet;

    private InstructionSetLoader(){
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("InstructionSet.txt")));
        this.instructionSet = new Hashtable<>();
        for (Object obj : fileReader.lines().toArray()){
            String str = (String)obj;
            this.instructionSet.put(str.split(":")[0], str.split(":")[1]);
        }
    }

    public int getFormatType(String line) throws Exception {
        if (line.charAt(0) == '.'){
            return 0;
        }

        String mnemonic = line.substring(9, 15).replaceAll("\\s+","");

        if (mnemonic.charAt(0) == '+' && this.instructionSet.containsKey(mnemonic.substring(1)) && this.instructionSet.get(mnemonic.substring(1)).equals("3/4")){
            return 4;
        } else if (this.instructionSet.containsKey(mnemonic)){
            String format = this.instructionSet.get(mnemonic);
            if (format.equals("3/4")){
                return 3;
            }
            else return Integer.parseInt(format);
        } else {
            throw new Exception("Unknown instruction");
        }
    }
}
