package tools;

import java.util.ArrayList;

public class DataConverter {

    private static final int DEFAULT_LENGTH_LENGTH = 5;
    private static final int DEFAULT_PACKAGE_LENGTH_LENGTH = 7;

    public static String formatForOutput(String tag, String... values) {

        String output = mergeValues(values);

        output = tag + output;

        if (output.length() > Math.pow(10, DEFAULT_PACKAGE_LENGTH_LENGTH) - 1) {
            throw new NullPointerException("Whole package too long for output");
        }

        // Adding the whole package length
        output = String.format("%0" + DEFAULT_PACKAGE_LENGTH_LENGTH + "d", output.length()) + output;

        return output;
    }

    private static String mergeValues(String... strings) {
        String output = "";
        for (String element : strings) {
            if (element==null) {
                element = "null";
            }
            if (element.length()>Math.pow(10,DEFAULT_LENGTH_LENGTH)-1) {
                throw new NullPointerException("Element too long for output");
            }
            output += String.format("%0" + DEFAULT_LENGTH_LENGTH + "d",element.length());
            output += element;
        }
        return output;
    }

    public static String [] splitValues(String input) {
        ArrayList<String> strings = new ArrayList<>();
        int pointer = 0;
        while (pointer!=input.length()) {
            String length_s = input.substring(pointer,pointer + DEFAULT_LENGTH_LENGTH);
            int length;
            try {
                length = Integer.parseInt(length_s);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new NumberFormatException();
            }

            pointer += DEFAULT_LENGTH_LENGTH;
            String newString = input.substring(pointer,pointer+length);
            strings.add(newString);
            pointer = pointer + length;
        }
        String [] output = strings.stream().toArray(String[]::new);
        return output;
    }
}
