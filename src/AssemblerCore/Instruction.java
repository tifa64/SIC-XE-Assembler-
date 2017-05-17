package AssemblerCore;

/**
 * Created by Krietallo on 4/15/2017.
 */
public class Instruction {

    final String inst, format, opcode;
    private final int int_opcode;


    Instruction(String inst, String frmt, String opcode) {
        this.inst = inst;
        this.format = frmt;
        this.opcode = opcode;
        int_opcode = Integer.parseInt(opcode, 16);
    }


}
