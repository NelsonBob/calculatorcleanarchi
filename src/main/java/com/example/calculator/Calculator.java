package com.example.calculator;


import java.io.*;
        import java.nio.file.*;
        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import java.util.*;

// Define the Operation as a Strategy Pattern
interface Operation {
    double execute(double a, double b);
    String getName();
}

class Addition implements Operation {
    @Override
    public double execute(double a, double b) {
        return a + b;
    }

    @Override
    public String getName() {
        return "addition";
    }
}

class Subtraction implements Operation {
    @Override
    public double execute(double a, double b) {
        return a - b;
    }

    @Override
    public String getName() {
        return "soustraction";
    }
}

class Multiplication implements Operation {
    @Override
    public double execute(double a, double b) {
        return a * b;
    }

    @Override
    public String getName() {
        return "multiplication";
    }
}

class OperationFactory {
    public static Operation getOperation(String opSymbol) {
        switch (opSymbol) {
            case "+":
                return new Addition();
            case "-": return new Subtraction();
            case "*": return new Multiplication();
            default:
                return null;
        }
    }
}

// Logger class to handle logging
class Logger {
    private boolean logging;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss:SSSSSS");

    Logger(boolean logging) {
        this.logging = logging;
    }

    void log(String message) {
        if (logging) {
            String timestamp = LocalDateTime.now().format(dateTimeFormatter);
            System.out.println("[" + timestamp + "][log] " + message);
        }
    }
}

// FileReader class to read and parse file
class FileReader {
    List<Double> readFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<Double> numbers = new ArrayList<>();
        for (String line : lines) {
            numbers.add(Double.parseDouble(line));
        }
        return numbers;
    }
}

public class Calculator {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java Calculator <filename> <+|-|*> [-log]");
            System.exit(1);
        }

        String filename = args[0];
        String opSymbol = args[1];
        boolean logging = args.length > 2 && args[2].equalsIgnoreCase("-log");

        Logger logger = new Logger(logging);
        FileReader fileReader = new FileReader();
        List<Double> numbers = fileReader.readFile(filename);

        Operation operation = OperationFactory.getOperation(opSymbol);
        if (operation == null) {
            System.err.println("Invalid operation: " + opSymbol);
            System.exit(1);
        }

        double result = numbers.get(0);
        logger.log("started");
        logger.log("applying operation " + operation.getName());
        for (int i = 1; i < numbers.size(); i++) {
            logger.log("parsed value = " + numbers.get(i));
            result = operation.execute(result, numbers.get(i));
            System.out.println((opSymbol.equals("+") ? "" : opSymbol) + numbers.get(i) + " = " + result);
            logger.log("accumulation : " + result + " on line " + (i + 1));
        }
        System.out.println("-------");
        System.out.println("Total = " + result);
        logger.log("end of program");
    }
}


