import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String line = s.nextLine();
        int number = s.nextInt();
        LocalDate date = LocalDate.parse(line);
        LocalDate dateNext = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
        int year = date.getYear();
        for (int i = 0; i < number; i++) {
            if (dateNext.getYear() == year) {
                System.out.println(dateNext);
            }
            dateNext = dateNext.plusDays(number);
        }
    }
}