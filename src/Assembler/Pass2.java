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
        try {
            ArrayList<String> fileLines = new ArrayList<>();
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


                if(temp.startsWith("H"))
                {
                    fileLines.add(temp);
                    //continue;
                }
                else if(temp.startsWith("E"))
                {
                    fileLines.add(sb.toString()+sb1.toString());
                    fileLines.add(temp);
                    break;
                }
                else
                {

                    if(sz == 0)
                    {
                        sb.append("T ");
                        sz += temp.length();
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
                        if(!flag)
                        {
                            sb.append(Integer.toHexString(Pass1.programStart).toUpperCase());
                            flag = true;
                        }

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
