import java.util.ArrayList;

/**
 * Created by Krietallo on 3/24/2017.
 */
public class Main {
    public static void main(String[] args) {
        AddressGenerator ag = new AddressGenerator();
        ArrayList<CodeParsing> cf = new ArrayList<CodeParsing>();

        ag.readFile("Copy.asm");
        ArrayList<String> records = ag.get_records();
        ArrayList<CodeParsing> array_of_cp = ag.get_array_of_cp();
        //System.out.println(records.size());
        ag.formated_code();

        System.out.println(array_of_cp.size());
        int sz = ag.get_number_of_lines();

        //for(int i = 0 ; i < sz; i++)
        //System.out.println(ag.records.get(i));

        for(int i = 0 ; i < array_of_cp.size() ; i++)
        {
            if(array_of_cp.get(i).get_Label() != null)
                System.out.print(array_of_cp.get(i).get_Label()+' ');

            if(array_of_cp.get(i).get_Mnemonic() != null)
                System.out.print(array_of_cp.get(i).get_Mnemonic()+' ');

            if(array_of_cp.get(i).get_Operand() != null)
                System.out.print(array_of_cp.get(i).get_Operand()+' ');

            if(array_of_cp.get(i).get_Comment() != null)
                System.out.println(array_of_cp.get(i).get_Comment());

        }


    }
}
