package com.ethankisiel.simplefinanceledger.Utils;

public class JSONStringFormatter
{
    public static String formatJSONString(String input)
    {
        StringBuilder output = new StringBuilder();
        int tabAmount = 0;

        for (int i = 0; i < input.length(); i++)
        {
            char currentChar = input.charAt(i);

//            if (i == 0)
//            {
//                output.append(input.charAt(i));
//                output.append('\n');
//                tabAmount++;
//
//                continue;
//            }
            if (currentChar == '{' || currentChar == '[')
            {
                output.append('\n');
                for (int j = 0; j < tabAmount; j++)
                {
                    output.append('\t');
                }

                tabAmount++;
                output.append(input.charAt(i));
                output.append('\n');

                for (int j = 0; j < tabAmount; j++)
                {
                    output.append('\t');
                }

            }
            else if (currentChar == '}' || currentChar == '}')
            {
                output.append('\n');
                tabAmount--;

                for (int j = 0; j < tabAmount; j++)
                {
                    output.append('\t');
                }

                output.append(input.charAt(i));

            }
            else if (input.charAt(i) == ',')
            {
                output.append(input.charAt(i));
                output.append('\n');
                for (int j = 0; j < tabAmount; j++)
                {
                    output.append('\t');
                }
            }
            else
            {
                output.append(currentChar);
            }
        }

        return output.toString();
    }
}
