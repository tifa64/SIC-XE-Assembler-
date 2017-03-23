/**
 * Created by Krietallo on 3/23/2017.
 */


public class CodeParsing {

    private String Label = "", Mnemonic = "", Operand = "", Comment = "";

    /*Getters*/
    public String get_Label()
    {
        return Label;
    }

    public String get_Mnemonic()
    {
        return Mnemonic;
    }

    public String get_Operand()
    {
        return Operand;
    }

    public String get_Comment()
    {
        return Comment;
    }

    /*Setters*/

    public void set_Label(String Label)
    {
        this.Label = Label;
    }

    public void set_Mnemonic(String Mnemonic)
    {
        this.Mnemonic = Mnemonic;
    }

    public void set_Operand(String Operand)
    {
        this.Operand = Operand;
    }

    public void set_Comment(String Comment)
    {
        this.Comment = Comment;
    }

    public CodeParsing string_parse(String line, CodeParsing cp)
    {
        String delims = "[ ]+";
        String[] tokens = line.split("\\s+");
        int len = tokens.length;


        if(len > 0)
            cp.set_Label(tokens[0]);

        if(len > 1)
            cp.set_Mnemonic(tokens[1]);

        if(len > 2)
            cp.set_Operand(tokens[2]);

        if(len > 3)
        {
            for(int i = 0 ; i < line.length() ; i++)
            {
                if(line.charAt(i) == '.')
                {
                    cp.set_Comment(line.substring(i, line.length()-1));
                    break;
                }
            }
        }


        return cp;

    }

}
