package Assembler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Created by Krietallo on 4/15/2017.
 */
public class Instruction {

    public final String inst, frmt, opcode;
    public final int int_opcode;


    public Instruction(String inst, String frmt, String opcode) {
        this.inst = inst;
        this.frmt = frmt;
        this.opcode = opcode;
        int_opcode = Integer.parseInt(opcode, 16);

    }




}
