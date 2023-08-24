package com.mybnb.mybnb.interfaces;

import java.util.ArrayList;
import java.util.Objects;

public class TableOutput {

    public static void formatTable(ArrayList<TableFormat> list) {
        // Create a 2D array to hold the data
        String[][] data = new String[list.size()][];

        // Populate the data array with row values from each listing
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i).getRowValues();
        }

        TableOutput.outputTable(list.get(0).getHeaders(), data);
    }

    public static void outputTable(String[] headers, String[][] data) {
        int numColumns = headers.length;
        int[] columnWidths = new int[numColumns];

        for (int i = 0; i < numColumns; i++) {
            columnWidths[i] = Math.max(headers[i].length(), getMaxDataLength(data, i));
        }

        for (int i = 0; i < numColumns; i++) {
            System.out.print(padWithCharacter(headers[i], columnWidths[i], ' '));
            if (i < numColumns - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < columnWidths[i]; j++) {
                System.out.print("-");
            }
            if (i < numColumns - 1) {
                System.out.print("-+-");
            }
        }
        System.out.println();

        for (String[] row : data) {
            for (int i = 0; i < numColumns; i++) {
                System.out.print(padWithCharacter(row[i], columnWidths[i], ' '));
                if (i < numColumns - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    private static int getMaxDataLength(String[][] data, int columnIndex) {
        int maxLength = 0;
        for (String[] row : data) {
            if (row[columnIndex] != null) {
                maxLength = Math.max(maxLength, row[columnIndex].length());
            }
        }
        return maxLength;
    }

    private static String padWithCharacter(String input, int length, char paddingChar) {
        return String.format("%-" + length + "s", input).replace(' ', paddingChar);
    }

}
