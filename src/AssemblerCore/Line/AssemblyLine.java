package AssemblerCore.Line;

import AssemblerCore.InstructionSetLoader;
import AssemblerCore.Pass2;
import AssemblerCore.Symbol;

import java.util.ArrayList;


/**
 * Created by louay on 3/25/2017.
 */
public abstract class AssemblyLine {

    protected final String line;
    protected final int address;

    AssemblyLine(int address, String line) {
        this.address = address;
        this.line = line;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static AssemblyLine getAssemblyLineInstance(int address, String line) throws Exception {

        switch (InstructionSetLoader.getLoader().getFormatType(line)) {
            case -1:
                return new Directive(address, line);
            case 0:
                return new Comment(address, line);
            case 1:
                return new Format1(address, line);
            case 2:
                return new Format2(address, line);
            case 3:
                return new Format3(address, line);
            case 4:
                return new Format4(address, line);
            default:
                throw new Exception("Unknown instruction");
        }
    }

    static ArrayList<String> getExtRefTokens(String str) throws Exception {
        int n = str.length();
        ArrayList<String> tokens = new ArrayList<>();
        char sign = '+';
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            boolean flag = false;
            while (i < n && (Character.isLetterOrDigit(str.charAt(i)) || str.charAt(i) == '*')) {
                sb.append(str.charAt(i));
                i++;
                flag = true;
            }
            if (flag) {
                if (Pass2.externalRef.contains(sb.toString())) {
                    tokens.add(sign + sb.toString());
                }
            }
            if (i < n) {
                if (str.charAt(i) == '+' || str.charAt(i) == '(') {
                    sign = '+';
                } else if (str.charAt(i) == '-') {
                    sign = '-';
                }
            }
        }
        return tokens;
    }

    public abstract int getNextAddress() throws Exception;

    public abstract String getLabel();

    public abstract String getObjectCode() throws Exception;

    public abstract void checkOperand() throws Exception;

    public abstract Symbol getSymbol() throws Exception;

    public int getAddress() {
        return this.address;
    }
}
