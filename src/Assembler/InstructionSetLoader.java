package Assembler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Created by louay on 3/25/2017.
 */
public class InstructionSetLoader {

    private static InstructionSetLoader loader = null;
    private final Hashtable<String, Instruction> instructionSet;
    private final  Hashtable<String ,Integer> Regs = new Hashtable<>();


    private InstructionSetLoader() {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("InstructionSet.txt")));
        this.instructionSet = new Hashtable<>();
        for (Object obj : fileReader.lines().toArray()) {
            String str = (String) obj;
            Instruction inst = new Instruction(str.split(":")[0], str.split(":")[1], str.split(":")[2]);
            this.instructionSet.put(str.split(":")[0], inst);
        }
        BufferedReader fileReader2 = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Registers.txt")));
        for (Object obj : fileReader2.lines().toArray()) {
            String str = (String) obj;
            Regs.put(str.split(":")[0], Integer.parseInt(str.split(":")[1]));
        }
    }

    public static InstructionSetLoader getLoader() {
        if (loader == null) {
            loader = new InstructionSetLoader();
        }
        return loader;
    }

    public int getFormatType(String line) throws Exception {
        if (line.charAt(0) == '.') {
            return 0;
        }

        String mnemonic = line.substring(9, 15).replaceAll("\\s+", "");

        if (mnemonic.charAt(0) == '+' && this.instructionSet.containsKey(mnemonic.substring(1)) && this.instructionSet.get(mnemonic.substring(1)).equals("3/4")) {
            return 4;
        } else if (this.instructionSet.containsKey(mnemonic)) {
            String format = this.instructionSet.get(mnemonic).frmt;
            if (format.equals("3/4")) {
                return 3;
            } else return Integer.parseInt(format);
        } else {
            throw new Exception("Unknown instruction");
        }
    }
    public String getInstOpCode(String instruction)
    {
        return instructionSet.get(instruction).opcode;
    }
    public int getRegOpCode(String register)
    {
        return Regs.get(register);
    }
}
