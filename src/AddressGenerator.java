/**
 * Created by Krietallo on 3/22/2017.
 */
import java.util.*;
import java.io.*;

public class AddressGenerator {

    /* Members*/

    private static int number_of_lines = 0;
    private static ArrayList<CodeParsing> array_of_cp = new  ArrayList<CodeParsing>();
    private static ArrayList<String> records = new ArrayList<String>();



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
            //System.out.println("g");
            //return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            //return null;
        }



    }


    /*Parsing the code and putting the result in the object "AraayList array_of_cp"*/
    public void formated_code()
    {
        for(int i = 0 ; i < number_of_lines; i++)
        {
            CodeParsing temp_cf = new CodeParsing();
            temp_cf = temp_cf.string_parse(records.get(i), temp_cf);
            array_of_cp.add(temp_cf);


            //System.out.println(array_of_cp.size());

        }
    }

    /*Getters*/

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

    public void leftmostbit(char c, int n)
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
    }

    public void remainder_extention(int n, String s)
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
    }


    /*The function od addition*/
    String add(String loc, String bytee)
    {
        String result_remainder = new String(), result = new String(), string_remainder = new String(), string_carry = new String();
        int carry = 0, remainder = 0;
        int locsz, bytesz, k, p = 0, q = 0, diff;
        locsz = loc.length();
        bytesz = bytee.length();
        diff = locsz - bytesz;
        result = loc.substring(0,diff);
        for(int i = bytesz - 1 ; i >= 0 ; i--)
        {

            leftmostbit(loc.charAt(diff+i), p);
            leftmostbit(bytee.charAt(i), q);


            k = p + q + carry;

            if(k > 15)
                remainder = k%16;

            else
                remainder = k;

            carry = k/16;

            if (remainder > 9)
                remainder_extention(remainder, string_remainder);

            else
                string_remainder = Integer.toString(remainder);

            string_carry = Integer.toString(carry);

            result_remainder += string_remainder;
        }
        if(carry > 0)
            result = add(result, string_carry);

        return result + result_remainder;
    }
    
}

