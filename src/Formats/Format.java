package Formats;

/**
 * Created by louay on 3/25/2017.
 */
public abstract class Format {

    public static Format getFormat(int address, String command){
        String label = command.substring(0, 8);
        String operation = command.substring(9, 15);
        String operand = command.substring(17, 35);
        String comment = command.substring(35, 66);
        return new Format1(); //to avoid errors for now
    }
}
