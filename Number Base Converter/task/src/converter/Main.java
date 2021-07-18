package converter;
import java.util.Scanner;
import java.util.Arrays;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;

public class Main {
    private static String convert(String[] base, String number) {
        String convertNumber = new String();
        int fromBase = Integer.parseInt(base[0]);
        int toBase = Integer.parseInt(base[1]);
        BigDecimal decimalNumber = new BigDecimal("0");
        if (fromBase != 10) {
            decimalNumber = convertFrom(fromBase, number);
        } else {
            decimalNumber = new BigDecimal(number);
        }
        if (toBase != 10) {
            convertNumber = convertTo(toBase,decimalNumber.toBigInteger());
        } else {
            convertNumber = decimalNumber.toBigInteger().toString();
        }
        return convertNumber;
    }
    
    private static String convertFraction(String[] base, String number) {
        String convertNumber = new String();
        int fromBase = Integer.parseInt(base[0]);
        int toBase = Integer.parseInt(base[1]);
        BigDecimal decimalNumber = new BigDecimal("0.0");
        if (fromBase != 10) {
            decimalNumber = convertFractionFrom(fromBase, number);
        } else {
            decimalNumber = new BigDecimal("0."+number);
        }
        if (toBase != 10) {
            convertNumber = convertFractionTo(toBase,decimalNumber);
        } else {
            convertNumber = decimalNumber.toString();
        }
        return convertNumber;
    }
    
    private static String convertTo(int base, BigInteger number) {
        StringBuilder string = new StringBuilder();
        BigInteger bigBase = new BigInteger(String.valueOf(base));
        while (number.divide(bigBase).compareTo(BigInteger.ZERO) > 0){
            if (number.remainder(bigBase).compareTo(BigInteger.TEN) < 0) {
               string.insert(0, String.valueOf(number.remainder(bigBase))); 
            } else {
                char symbol = Character.forDigit(number.remainder(bigBase).intValue(), base);
                string.insert(0, String.valueOf(symbol));
            }
            number = number.divide(bigBase);
        }
        if (number.compareTo(BigInteger.ZERO) > 0) {
           if (number.remainder(bigBase).compareTo(BigInteger.TEN) < 0) {
               string.insert(0, String.valueOf(number.remainder(bigBase))); 
            } else {
                char symbol = Character.forDigit(number.remainder(bigBase).intValue(), base);
                string.insert(0, String.valueOf(symbol));
            } 
        }
        return string.toString();
    }
    
    private static String convertFractionTo(int base, BigDecimal number) {
        StringBuilder string = new StringBuilder("0.");
        BigDecimal bigBase = new BigDecimal(String.valueOf(base));
        int step = 0;
        while ((number.multiply(bigBase).ulp().intValue() < 1)&&(step < 5)) {
            BigDecimal integerPart = number.multiply(bigBase).setScale(0, RoundingMode.DOWN);
            char symbol = Character.forDigit(integerPart.intValue(), base);
            string.append(String.valueOf(symbol));
            number = number.multiply(bigBase).subtract(integerPart);
            step++;
        }
        return string.toString();
    }
    
    private static BigDecimal convertFrom(int base, String number) {
        BigDecimal result = new BigDecimal("0.0");
        BigDecimal bigBase = new BigDecimal(String.valueOf(base));
        for (int i = 0; i < number.length(); i++) {
            char symbol = number.charAt(i);
            BigDecimal addNumber = bigBase.pow(number.length() - 1 - i).multiply(new 
                                   BigDecimal(String.valueOf(Character.digit(symbol, base))));
            result = result.add(addNumber);
        }
        return result;
    }
    
    private static BigDecimal convertFractionFrom(int base, String number) {
        BigDecimal result = new BigDecimal("0.0");
        BigDecimal bigBase = new BigDecimal(String.valueOf(base));
        for (int i = 0; i < number.length(); i++) {
            char symbol = number.charAt(i);
            BigDecimal addNumber = BigDecimal.ONE.divide(bigBase.pow(1 + i), MathContext.DECIMAL32).multiply(new 
                                   BigDecimal(String.valueOf(Character.digit(symbol, base))));
            result = result.add(addNumber);
        }
        return result;
    }
   
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in); 
        String command;
        do {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            command = scanner.nextLine();
            if (!command.equals("/exit")) {
                String[] base = new String[2];
                base = command.split(" ");
                do {
                    //base = command.split(" ");
                    System.out.print("Enter number in base " + base[0] + " to convert to base " + base[1]);
                    System.out.println(" (To go back type /back)");
                    command = scanner.nextLine();
                    if (!command.equals("/back")) {
                        String[] number = new String[2];
                        number = command.split("\\.");
                        StringBuilder result = new StringBuilder("");
                        if (number.length > 0) {
                            result.append(convert(base, number[0]));
                        } else {
                            result.append(convert(base, command));
                        }
                        if (number.length > 1) {
                             String fraction = convertFraction(base, number[1]);
                             result.append("." + fraction.substring(2));
                        }
                        System.out.println("Conversion result: " + result);
                    }
                } while (!command.equals("/back"));
            }
        } while (!command.equals("/exit"));        
    }
}
