import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        int[] array = new int[]{scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
        Arrays.stream(array).forEach(i -> {
            System.out.println(LocalDate.of(year, 1, 1).plusDays(i - 1));
        });

    }
}