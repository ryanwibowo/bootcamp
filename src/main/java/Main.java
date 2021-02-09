import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        List<String> strings = new ArrayList<>();
        int count = 1;
        Scanner in = new Scanner(System.in);
        for (int i=0; i < count; i++) {
            System.out.print("Enter your name: ");
            String name = in.nextLine();
            String time = LocalTime.now().toString();
            strings.add(name );
            System.out.println("Name is: " + name);
            System.out.println("Time is: " + time);
            if (name.equals("name")) {
                continue;
            }
            count++;
        }
        in.close();
    }
}
