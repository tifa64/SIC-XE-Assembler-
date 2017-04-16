package Assembler;

import Assembler.Line.AssemblyLine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by louay on 4/15/2017.
 */
public class Pass2 {

    public static int baseValue = -1;
    private static int sz = 0;
    private static boolean flag = false;
    private static String temp = new String();
    private static StringBuilder sb = new StringBuilder();
    private static StringBuilder sb1 = new StringBuilder();

    public static void generateObjectCodes() {

        /**sb contains T and length of the record**/
        /**sb1 contains the ObjectCode of the instruction**/
        try {
            ArrayList<String> fileLines = new ArrayList<>();

            /**Case RESW or RESB**/
            for (AssemblyLine al : Pass1.assemblyLines) {
                try {
                    temp = al.getObjectCode();
                }catch (Exception m)
                {
                    sb.append(Integer.toHexString(sz/2).toUpperCase() + " ");
                    fileLines.add(sb.toString()+sb1.toString());
                    sb = new StringBuilder();
                    sb1 = new StringBuilder();
                    sz = 0;
                }

                /**Case START**/
                if(temp.startsWith("H"))
                {
                    fileLines.add(temp);
                }

                /**Case END**/
                /**It will add the last line of T before it then will the END line**/
                else if(temp.startsWith("E"))
                {
                    sb.append(Integer.toHexString(sz/2).toUpperCase() + " ");
                    fileLines.add(sb.toString()+sb1.toString());
                    fileLines.add(temp);
                    break;
                }
                /**Other wise it will continue the T record till the sz is bigger than 30 bytes (60 characters)**/
                else
                {

                    if(sz == 0)
                    {
                        sb.append("T ");
                        sz += temp.length();
                        /**Since it can't see the starting address so we retrive it from Pass1 and set flag to true so that we won't
                         * repeat that
                         */
                        if(!flag)
                        {
                            sb1.append(Integer.toHexString(Pass1.programStart).toUpperCase() + " ");
                            sz += Integer.toHexString(Pass1.programStart).length();
                            flag = true;
                        }
                        sb1.append(temp + " ");
                    }
                    else if(sz + temp.length() <= 60)
                    {
                        sz += temp.length();
                        sb1.append(temp + " ");
                    }
                    else
                    {

                        sb.append(Integer.toHexString(sz/2).toUpperCase() + " ");

                        fileLines.add(sb.toString()+sb1.toString());
                        sb = new StringBuilder();
                        sb1 = new StringBuilder();
                        sz = 0;
                    }
                }


            }
            Path listingFile = Paths.get("HTME.txt");
            Files.write(listingFile, fileLines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
