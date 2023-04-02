package Helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ValidationHandler {
    private ValidationHandler() {
    }

    public static int intValidator(String promptMessage, String errorMessage, String shellIndicator, Integer lowerBound, Integer upperBound) {
        int requestedValue;
        Scanner scanner = new Scanner(System.in);

        if (shellIndicator != null)
            System.out.print(shellIndicator + "> ");
        System.out.print(promptMessage);
        requestedValue = scanner.nextInt();
        while ((lowerBound != null && requestedValue < lowerBound) || (upperBound != null && requestedValue > upperBound)) {
            System.out.println("Error: " + errorMessage);
            if (shellIndicator != null)
                System.out.print(shellIndicator + "> ");
            System.out.print(promptMessage);
            requestedValue = scanner.nextInt();
        }

        return requestedValue;
    }

    public static Double doubleValidator(String promptMessage, String errorMessage, String shellIndicator, Double lowerBound, Double upperBound) {
        Double requestedValue;
        Scanner scanner = new Scanner(System.in);

        if (shellIndicator != null)
            System.out.print(shellIndicator + "> ");
        System.out.print(promptMessage);
        requestedValue = scanner.nextDouble();
        while ((lowerBound != null && requestedValue < lowerBound) || (upperBound != null && requestedValue > upperBound)) {
            System.out.println("Error: " + errorMessage);
            if (shellIndicator != null)
                System.out.print(shellIndicator + "> ");
            System.out.print(promptMessage);
            requestedValue = scanner.nextDouble();
        }

        return requestedValue;
    }

    public static String stringValidator(String promptMessage, String errorMessage, String shellIndicator, String regex) {
        String inputString;
        Scanner scanner = new Scanner(System.in);

        if (shellIndicator != null)
            System.out.print(shellIndicator + "> ");
        System.out.print(promptMessage);
        inputString = scanner.next();
        while (!Pattern.compile(regex).matcher(inputString).matches()) {
            System.out.println("Error: " + errorMessage);
            if (shellIndicator != null)
                System.out.print(shellIndicator + "> ");
            System.out.print(promptMessage);
            inputString = scanner.next();
        }

        return inputString;
    }

    public static LocalDate dateValidator(String promptMessage, String errorMessage, String shellIndicator, int numberOfYearsInThePast) {
        String inputString;
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (shellIndicator != null)
            System.out.print(shellIndicator + "> ");
        System.out.print(promptMessage);
        inputString = scanner.next();
        // parsing the input string in the format outlined above and
        // comparing it to the current time - 'numberOfYearsInThePast'
        while (LocalDate.parse(inputString, formatter).compareTo(LocalDate.now().minusYears(numberOfYearsInThePast)) > 0) {
            System.out.println("Error: " + errorMessage);
            if (shellIndicator != null)
                System.out.print(shellIndicator + "> ");
            System.out.print(promptMessage);
            inputString = scanner.next();
        }

        return LocalDate.parse(inputString, formatter);
    }

    public static Set<Integer> choicesValidator(String shellIndicator, int lowerBound, int upperBound) {
        Scanner scanner = new Scanner(System.in);

        if (shellIndicator != null)
            System.out.print(shellIndicator + "> ");

        System.out.print("Enter the options you wish to select: ");
        String line = scanner.nextLine();

        String[] splitChoiceArray = line.trim().split("\\s+");
        Set<Integer> parsedChoices = new HashSet<Integer>();

        Integer currentChoice;
        for (int i = 0; i < splitChoiceArray.length; i++) {
            currentChoice = Integer.parseInt(splitChoiceArray[i]);

            if ((currentChoice < lowerBound || currentChoice > upperBound) && currentChoice != -1) {
                System.out.println("Error: Invalid option ID: '" + currentChoice + "'!");
                continue;
            }

            parsedChoices.add(currentChoice);
        }

        return parsedChoices;
    }
}
