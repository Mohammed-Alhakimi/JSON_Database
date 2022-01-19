import java.util.Locale;
import java.util.Scanner;

class StringProcessor extends Thread {

    final Scanner scanner = new Scanner(System.in); // use it to read string from the standard input

    @Override
    public void run() {

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.equals(line.toUpperCase(Locale.ROOT))) {
                System.out.println(line.toUpperCase(Locale.ROOT));
            }
        }
        System.out.println("FINISHED");
    }
}