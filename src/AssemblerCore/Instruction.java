package AssemblerCore;

/**
 * Created by Krietallo on 4/15/2017.
 */
public class Instruction {

    public final String inst, format, opcode;
    public final int int_opcode;


    public Instruction(String inst, String frmt, String opcode) {
        this.inst = inst;
        this.format = frmt;
        this.opcode = opcode;
        int_opcode = Integer.parseInt(opcode, 16);
    }


}
