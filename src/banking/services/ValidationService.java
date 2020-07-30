package banking.services;

public class ValidationService {

    public static int LuhnAlgorithm(String number) {
        long[] longs = new long[number.length()];
        for (int i = 0; i < number.length(); i++) {
            longs[i] = Long.parseLong(String.valueOf(number.charAt(i)));
        }
        int sum = 0;
        for (int i = 0; i < longs.length; i++) {
            if (i % 2 == 0) {
                longs[i] *= 2;
            }
            longs[i] -= longs[i] > 9 ? 9 : 0;
            sum += longs[i];
        }
        return sum % 10 != 0 ? 10 - (sum % 10) : 0;
    }
}
