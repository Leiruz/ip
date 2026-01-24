import java.util.Scanner;

import javax.sound.sampled.Line;

public class Ella {
    private static final String INDENT = "    ";

    private static final String LINE = "\n____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        
        String logo =
          "███████ ██      ██       █████  \n"
        + "██      ██      ██      ██   ██ \n"
        + "█████   ██      ██      ███████ \n"
        + "██      ██      ██      ██   ██ \n"
        + "███████ ███████ ███████ ██   ██ \n";
        System.out.println(logo);

        Scanner sc = new Scanner(System.in);

        String[] tasks = new String[MAX_TASKS];
        
        int taskCount = 0;

        System.out.println("Hello! I'm Ella\nWhat can I do for you?" + LINE);

        while (true) {
            String line = sc.nextLine().trim();

            if (line.equals("bye") || line.equals("Bye")) {
                System.out.println(LINE + "\nBye. Hope to see you again soon!" + LINE);
                break;
            }

            else if (line.equals("list") || line.equals("List")) {
                if (taskCount == 0) {
                    System.out.println("(no tasks yet)" + LINE);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < taskCount; i++) {
                        sb.append(i + 1).append(". ").append(tasks[i]).append("\n");
                    }
                    // remove last newline so the box looks tidy
                    System.out.println(sb.toString().trim() + LINE);
                }
                
            } else {
                // treat anything else as a task to add
                if (taskCount >= MAX_TASKS) {
                    System.out.println("Sorry, I can only store up to " + MAX_TASKS + " tasks." + LINE);
                    continue;
                }
                tasks[taskCount] = line;
                taskCount++;

                System.out.println("     added: " + line + LINE);
            }

            //System.out.println(INDENT + line);
        }
        sc.close();
    }
}
