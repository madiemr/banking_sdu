import java.util.Scanner;

public class BankingSystem {
    private static BankingSystem instance;
    private InMemoryDatabase database;
    private NotificationService notificationService;


    private BankingSystem() {
        database = new InMemoryDatabase();
        notificationService = new NotificationService();

    }

    public static BankingSystem getInstance() {
        if (instance == null) {
            instance = new BankingSystem();
        }
        return instance;
    }

    public void start() {
        for (User user : database.getAllUsers()) {
            notificationService.addObserver(user);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Banking System!");
        boolean running = true;

        while (running) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> login(scanner);
                case 2 -> register(scanner);
                case 3 -> {
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = database.authenticate(username, password);
        if (user != null) {
            System.out.println("Welcome, " + user.getName() + "!");
            if (user instanceof Client) {
                ((Client) user).showMenu(scanner, database);
            } else if (user instanceof Administrator) {
                ((Administrator) user).showMenu(scanner, database);
            }
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private void register(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        Client client = new Client(name, username, password);
        database.addUser(client);
        System.out.println("Registration successful! You can now log in.");
    }
}