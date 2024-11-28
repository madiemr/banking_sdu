import java.util.List;
import java.util.Scanner;

public class Administrator extends User {

    public Administrator(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void showMenu(Scanner scanner, InMemoryDatabase database) {
        boolean running = true;
        while (running) {
            System.out.println("Administrator Menu:");
            System.out.println("1. View All Clients");
            System.out.println("2. View All Transactions");
            System.out.println("3. View and Process Card Requests");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewClients(database);
                case 2 -> viewTransactions();
                case 3 -> viewAndProcessCardRequests(scanner, database);
                case 4 -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void viewClients(InMemoryDatabase database) {
        List<User> users = database.getAllUsers();
        System.out.println("Clients:");
        for (User user : users) {
            if (user instanceof Client) {
                System.out.println("Name: " + user.getName() + ", Username: " + user.getUsername());
            }
        }
    }

    private void viewTransactions() {
        System.out.println("Viewing all transactions (this can be expanded further).");
        // Here you can implement logic for showing transaction history.
    }
    private void viewAndProcessCardRequests(Scanner scanner, InMemoryDatabase database) {
        List<CardRequest> requests = database.getCardRequests();
        if (requests.isEmpty()) {
            System.out.println("No card requests available.");
            return;
        }

        System.out.println("Card Requests:");
        for (int i = 0; i < requests.size(); i++) {
            System.out.println((i + 1) + ". " + requests.get(i));
        }

        System.out.print("Enter request number to process or 0 to exit: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) return;

        if (choice < 1 || choice > requests.size()) {
            System.out.println("Invalid request number.");
            return;
        }

        CardRequest request = requests.get(choice - 1);

        System.out.print("Approve or Reject (approve/reject): ");
        String decision = scanner.nextLine().toLowerCase();

        if (decision.equals("approve")) {
            request.setRequestStatus("Approved");
            System.out.println("Request approved: " + request);
        } else if (decision.equals("reject")) {
            request.setRequestStatus("Rejected");
            System.out.println("Request rejected: " + request);
        } else {
            System.out.println("Invalid decision.");
        }
    }
}
