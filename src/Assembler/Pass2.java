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
    private static boolean flag1 = false , flag2 = false;
    /**flag 1 is for when it is the first time to start the HTME record and flag2 is for when new T record is begun it ingores the
     * last address of the previous T record
     */
    private static String tempObjectOcde = new String();
    /** tempObjectOcde to hold the current Onject Code*/
    private static StringBuilder sb = new StringBuilder();
    private static StringBuilder sb1 = new StringBuilder();
    /**sb contains T and length of the record**/
    /**sb1 contains the ObjectCode of the instruction**/
    private static String progStart = Integer.toHexString(Pass1.programStart).toUpperCase();


    public static void generateObjectCodes() {



        try {
            ArrayList<String> fileLines = new ArrayList<>();

            /**Case RESW or RESB**/
            for (AssemblyLine al : Pass1.assemblyLines) {
                try {
                    tempObjectOcde = al.getObjectCode();
                }catch (Exception m)
                {
                    sb.append(Integer.toHexString(sz/2).toUpperCase() + " ");
                    fileLines.add("T" + " " + sb.toString()+sb1.toString());
                    sb = new StringBuilder();
                    sb1 = new StringBuilder();
                    sz = 0;
                    flag2 = false;
                }

                /**Case START**/
                if(tempObjectOcde.startsWith("H"))
                {
                    fileLines.add(tempObjectOcde);
                }

                /**Case END**/
                /**It will add the last line of T before it then will the END line**/
                else if(tempObjectOcde.startsWith("E"))
                {
                    sb.append(Integer.toHexString(sz/2).toUpperCase() + " ");
                    fileLines.add("T" + " " +sb.toString()+sb1.toString());
                    fileLines.add(tempObjectOcde);
                    break;
                }
                else if(tempObjectOcde.length() == 0)
                    continue;
                /**Other wise it will continue the T record till the sz is bigger than 30 bytes (60 characters)**/
                else
                {

                    if(!flag1)
                    {

                        if(progStart.length()%2 == 1)
                        {
                            StringBuilder sb = new StringBuilder();
                            sb.append("0");
                            sb.append(progStart);
                            progStart = sb.toString();
                        }
                        sb1.append(progStart + " ");
                        sz += Integer.toHexString(Pass1.programStart).length();
                        flag1 = true;
                        flag2 = false;
                    }

                    else if(sz + tempObjectOcde.length() <= 60)
                    {
                        if(!flag2)
                        {
                            flag2 = true;
                            continue;
                        }
                        sz += tempObjectOcde.length();
                        sb1.append(tempObjectOcde + " ");


                    }
                    else
                    {

                        sb.append(Integer.toHexString(sz/2).toUpperCase() + " ");

                        fileLines.add("T" + " " +sb.toString()+sb1.toString());
                        sb = new StringBuilder();
                        sb1 = new StringBuilder();
                        sz = 0;
                        flag2 = false;
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
