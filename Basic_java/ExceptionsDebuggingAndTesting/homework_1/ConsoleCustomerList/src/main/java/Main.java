import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

public class Main {
    private static final Logger logger =
            LogManager.getLogger(Main.class);
    private static final String ADD_COMMAND = "add Василий Петров " +
            "vasily.petrov@gmail.com +79215637722";

    //add Михаил Серов michael.serov@mail.ru +79432967571 // for test
    //add Алиса Колдунова Михайловна alisa.koldunova@yandex.ru +79432967571 // for test
    //add 1!!!! 2&&&& 3$$$$ Текст_телефона // for test

    private static final String COMMAND_EXAMPLES = "\t" + ADD_COMMAND + "\n" +
            "\tlist\n\tcount\n\tremove Василий Петров";
    private static final String COMMAND_ERROR = "Wrong command! Available command examples: \n" +
            COMMAND_EXAMPLES;
    private static final String helpText = "Command examples:\n" + COMMAND_EXAMPLES;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerStorage executor = new CustomerStorage();
        logger.info(" Program start! ");

        while (true) {
            System.out.println("Enter command: add; list; remove; count; help; exit");
            String command = scanner.nextLine();
            String[] tokens = command.split("\\s+", 2);

            switch (tokens[0]) {
                case "add":
                    logger.debug("add command");
                    try {
                        executor.addCustomer(tokens[1]);
                    } catch (InputException exception) {
                        System.out.println(" We caught exception " + exception);
                        logger.error(" Code error detected! - " + exception );
                    }
                    break;
                case "list":
                    logger.debug("list command");
                    executor.listCustomers();
                    break;
                case "remove":
                    logger.debug("remove command");
                    executor.removeCustomer(tokens[1]);
                    break;
                case "count":
                    logger.debug("count command");
                    System.out.println("There are " + executor.getCount() + " customers");
                    break;
                case "help":
                    logger.debug("help command");
                    System.out.println(helpText);
                    break;
                case "exit":
                    logger.info("exit command");
                    System.out.println("Program closed.");
                    return;
                default:
                    logger.debug("default command - income text is error");
                    System.out.println(COMMAND_ERROR);
                    break;
            }
        }
    }
}
