/**
 * Created by Krietallo on 3/22/2017.
 */
import java.util.*;
import java.io.*;

public class AddressGenerator {

    /* Members*/

    private static int number_of_lines = 0; //# of lines in the code*/
    private static ArrayList<CodeParsing> array_of_cp = new  ArrayList<CodeParsing>(); //Address -- Label -- Mnemonic -- Operand -- Comment
    private static ArrayList<String> records = new ArrayList<String>(); //To save each line as a string



    /*Reading File and puting each line into ArrayList of strings "reader"*/

    public void readFile(String filename)
    {

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
                number_of_lines++;
            }
            reader.close();

        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }



    }


    /********************Parsing the code and putting the result in the object "AraayList array_of_cp"********************/
    public void formated_code()
    {
        for(int i = 0 ; i < number_of_lines; i++)
        {
            CodeParsing temp_cf = new CodeParsing();
            temp_cf = temp_cf.string_parse(records.get(i), temp_cf);
            array_of_cp.add(temp_cf);



        }
    }

    /********************Getters********************/

    public ArrayList<CodeParsing> get_array_of_cp()
    {
        return array_of_cp;
    }

    public ArrayList<String> get_records()
    {
        return records;
    }

    public int get_number_of_lines(){
        return  number_of_lines;
    }


    /********************Hexadecimels Incrementation********************/

    /********************Helping functions********************/
    public int leftmostbit(char c, int n)
    {
        switch(c)
        {
            case 'A':
                n = 10;
                break;

            case 'B':
                n = 11;
                break;

            case 'C':
                n = 12;
                break;

            case 'D':
                n = 13;
                break;

            case 'E':
                n = 14;
                break;

            case 'F':
                n = 15;
                break;

            default:
                n = c -'0';
        }
        return n;
    }

    public String remainder_extention(int n, String s)
    {
        switch(n)
        {
            case 10:
                s = "A";
                break;

            case 11:
                s = "B";
                break;

            case 12:
                s = "C";
                break;

            case 13:
                s = "D";
                break;

            case 14:
                s = "E";
                break;

            case 15:
                s = "F";
                break;
        }
        return s;
    }


    /********************The function of addition********************/
    public String add(String loc, String bytee)
    {
        String result_remainder = new String(), result = new String(), temp = new String(), reverse = new String();
        String string_remainder = new String(), string_carry = new String(), ans = new String(), final_ans = new String();
        int carry = 0, remainder = 0, ansdiff = 0;
        int locsz, bytesz, k, p = 0, q = 0, diff;
        locsz = loc.length();
        bytesz = bytee.length();
        diff = locsz - bytesz;
        if(diff < 0)
            return add(bytee, loc);
        result = loc.substring(0,diff);
        for(int i = bytesz - 1 ; i >= 0 ; i--)
        {

           p = leftmostbit(loc.charAt(diff+i), p);
           q = leftmostbit(bytee.charAt(i), q);


            k = p + q + carry;

            if(k > 15)
                remainder = k%16;

            else
                remainder = k;

            carry = k/16;

            if (remainder > 9)
                string_remainder = remainder_extention(remainder, string_remainder);

            else
                string_remainder = Integer.toString(remainder);

            string_carry = Integer.toString(carry);

            result_remainder += string_remainder;
        }
        if(carry > 0)
            result = add(result, string_carry);

        ans = result +  result_remainder;
        //return ans;

        ansdiff = ans.length() - bytesz;
        temp = ans.substring(ansdiff, ans.length());
        reverse = new StringBuffer(temp).reverse().toString();

        final_ans = ans.substring(0, ansdiff);
        final_ans += reverse;
        return final_ans;
    }
    /*Convert Decimel to Hex*/
    public String convertDecToHex(int decimalNumber)
    {
        return  Integer.toHexString(decimalNumber);
    }



    /********************The Directives********************/
    public Boolean isReserveByte(String Mnemonic)
    {
        if(Mnemonic.equals("RESB"))
            return true;
        else
            return false;
    }

    public Boolean isReserveWord(String Mnemonic)
    {
        if(Mnemonic.equals("RESW"))
            return true;
        else
            return false;
    }

    public Boolean isByte(String Mnemonic)
    {
        if(Mnemonic.equals("BYTE"))
            return true;
        else
            return false;
    }

    public Boolean isWord(String Mnemonic)
    {
        if(Mnemonic.equals("WORD"))
            return true;
        else
            return false;
    }

    /********************The address generator********************/
    public void adrs_gen()
    {
        array_of_cp.get(0).set_Address(array_of_cp.get(0).get_Operand());
        array_of_cp.get(1).set_Address(array_of_cp.get(0).get_Operand());

        for(int i = 2 ; i < number_of_lines ; i++)
        {

            String Mnemonic;
            Mnemonic = array_of_cp.get(i-1).get_Mnemonic();
            if(isReserveByte(Mnemonic))
            {

                //Getting integer value then parse it to string
                int int_temp_resb_value = Integer.parseInt(array_of_cp.get(i-1).get_Operand());
                String hex_string_temp_resb_value = "";
                hex_string_temp_resb_value = convertDecToHex(int_temp_resb_value);//Converting it from decimel to hex
                array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), hex_string_temp_resb_value)); // Increment Address
            }

            else if(isReserveWord(Mnemonic))
            {
                //Getting integer value then parse it to string
                int int_temp_resb_value = Integer.parseInt(array_of_cp.get(i-1).get_Operand()) * 3; // Multiply it with 3
                String hex_string_temp_resb_value = new String ();
                //Converting it from decimel to hex
                hex_string_temp_resb_value = convertDecToHex(int_temp_resb_value);
                array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), hex_string_temp_resb_value)); // Increment Address
            }

            else if(isWord(Mnemonic))
            {
                int int_temp_word_value = Integer.parseInt(array_of_cp.get(i-1).get_Operand()); // Convert value of word to int
                if(int_temp_word_value == 0)
                {
                    array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), "3"));
                    continue;
                }

                int_temp_word_value *= 3; // Multiply it by 3 to get its value in terms of Bytes

                String hex_string_temp_word_value = convertDecToHex(int_temp_word_value); // Convert it to string
                array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), hex_string_temp_word_value)); // Increment Address
            }

            else if(isByte(Mnemonic))
            {
                //Case I : Hex
                {
                    //Length of hex

                    int int_temp_length_OX_value =  array_of_cp.get(i-1).get_Operand().length() - 3; // Because we don't need C or single quotes
                    String string_temp_length_OX_value = Integer.toString(int_temp_length_OX_value); // Convert it to string
                    if(array_of_cp.get(i-1).get_Operand().charAt(0) == 'X')
                    {
                        int_temp_length_OX_value /=2; // every 2 Hexa worth 1 byte
                        string_temp_length_OX_value = Integer.toString(int_temp_length_OX_value); // Convert it to string

                        array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), string_temp_length_OX_value)); // Increment Address


                    }
                  // Case II : Character, we put the length as it is because every character worth 1 byte
                    else
                        array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), string_temp_length_OX_value)); // Increment Address


                }
            }
            else if(array_of_cp.get(i-1).get_Comment().length() > 0 || array_of_cp.get(i-1).get_Label().charAt(0) == '.')
            {
                //System.out.println(array_of_cp.get(i-1).get_Label().charAt(0));
                array_of_cp.get(i).set_Address(array_of_cp.get(i-1).get_Address()); // Case of comment don't increment the address counter
            }

            else
            {
                array_of_cp.get(i).set_Address(add(array_of_cp.get(i-1).get_Address(), "3")); // Will be modified according to the format type
            }



        }
    }

}

