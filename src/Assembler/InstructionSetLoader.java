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

    public int getFormatType(String operation) throws Exception {
        if (operation.charAt(0) == '+' && this.instructionSet.containsKey(operation.substring(1)) && this.instructionSet.get(operation.substring(1)).equals("3/4")){
            return 4;
        } else if (this.instructionSet.containsKey(operation)){
            String format = this.instructionSet.get(operation);
            if (format.equals("3/4")){
                return 3;
            }
            else return Integer.parseInt(format);
        } else {
            throw new Exception("Unknown instruction");
        }
    }
}
