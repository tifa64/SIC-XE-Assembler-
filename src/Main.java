import java.util.ArrayList;

/**
 * Created by Krietallo on 3/24/2017.
 */
public class Main {
    public static void main(String[] args) {
        AddressGenerator ag = new AddressGenerator();
        CodeParsing cp = new CodeParsing();
        ArrayList<CodeParsing> cf = new ArrayList<CodeParsing>();

        ag.readFile("Copy.asm");
        ArrayList<String> records = ag.get_records();
        ArrayList<CodeParsing> array_of_cp = ag.get_array_of_cp();
        ag.formated_code();

        System.out.println(array_of_cp.size());
        int sz = ag.get_number_of_lines();


        ag.adrs_gen();
        for(int i = 0 ; i < sz ; i++)
        {
            System.out.print(array_of_cp.get(i).get_Address()+"    ");

            if(cp.isValid(array_of_cp.get(i).get_Label()))
                System.out.print(array_of_cp.get(i).get_Label()+"    ");

            if(cp.isValid(array_of_cp.get(i).get_Mnemonic()))
                System.out.print(array_of_cp.get(i).get_Mnemonic()+"    ");

            if(cp.isValid(array_of_cp.get(i).get_Operand()))
                System.out.print(array_of_cp.get(i).get_Operand()+"    ");

            if(cp.isValid(array_of_cp.get(i).get_Comment()))
                System.out.print(array_of_cp.get(i).get_Comment());

            System.out.println();

        }


    }
}
