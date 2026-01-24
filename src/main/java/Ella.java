import java.util.Scanner;

public class Ella {

    private static final String INDENT = "    ";

    public static void main(String[] args) {
        
        String logo =
          "███████ ██      ██       █████  \n"
        + "██      ██      ██      ██   ██ \n"
        + "█████   ██      ██      ███████ \n"
        + "██      ██      ██      ██   ██ \n"
        + "███████ ███████ ███████ ██   ██ \n";
        System.out.println("Hello I am\n" + logo);
        System.out.println("What can I do for you?");

        

        Scanner sc = new Scanner(System.in);

        while (true) {
            String line = sc.nextLine();
            if (line.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            System.out.println(INDENT + line);
        }

        sc.close();
    }
}
