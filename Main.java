import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }
}

class Item {
    private String itemName;
    private double currentBid;
    private User currentBidder;
    private boolean isAuctionClosed;

    public Item(String itemName) {
        this.itemName = itemName;
        this.currentBid = 0.0;
        this.currentBidder = null;
        this.isAuctionClosed = false;
    }

    public String getItemName() {
        return itemName;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public User getCurrentBidder() {
        return currentBidder;
    }

    public void placeBid(User bidder, double bidAmount) {
        if (!isAuctionClosed) {
            if (bidAmount > currentBid) {
                currentBid = bidAmount;
                currentBidder = bidder;
                System.out.println("Bid placed successfully.");
            } else {
                System.out.println("Bid amount must be higher than the current bid.");
            }
        } else {
            System.out.println("Auction for " + itemName + " is closed. Bid cannot be placed.");
        }
    }

    public void closeAuction() {
        isAuctionClosed = true;
        System.out.println("Auction for " + itemName + " is closed.");
    }

    public boolean isAuctionClosed() {
        return isAuctionClosed;
    }

    public User determineWinner() {
        if (isAuctionClosed) {
            return currentBidder;
        } else {
            System.out.println("Auction for " + itemName + " is still open.");
            return null;
        }
    }
}

class AuctionSystem {
    private List<User> users;
    private List<Item> items;
    private User currentUser;

    public AuctionSystem() {
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.currentUser = null;
    }

    public void registerUser(String username, String password) {
        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("User registered successfully.");
    }

    public void loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                currentUser = user;
                System.out.println("Login successful.");
                return;
            }
        }
        System.out.println("Invalid username or password.");
    }

    public void logoutUser() {
        currentUser = null;
        System.out.println("Logout successful.");
    }

    public void listItem(String itemName) {
        if (currentUser != null) {
            Item newItem = new Item(itemName);
            items.add(newItem);
            System.out.println("Item listed successfully.");
        } else {
            System.out.println("Please log in before listing an item.");
        }
    }

    public void placeBid(String itemName, double bidAmount) {
        if (currentUser != null) {
            for (Item item : items) {
                if (item.getItemName().equals(itemName)) {
                    item.placeBid(currentUser, bidAmount);
                    return;
                }
            }
            System.out.println("Item not found.");
        } else {
            System.out.println("Please log in before placing a bid.");
        }
    }

    public void closeAuctionForItem(String itemName) {
        for (Item item : items) {
            if (item.getItemName().equals(itemName)) {
                item.closeAuction();
                User winner = item.determineWinner();
                if (winner != null) {
                    System.out.println("The winner of the auction for " + itemName + " is " + winner.getUsername());
                } else {
                    System.out.println("No winner determined for " + itemName + ".");
                }
                return;
            }
        }
        System.out.println("Item not found.");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

public class Main {
    public static void main(String[] args) {
        AuctionSystem auctionSystem = new AuctionSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Logout");
                System.out.println("4. List Item");
                System.out.println("5. Place Bid");
                System.out.println("6. Exit");
                System.out.println("7. Close Auction for Item");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String regUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String regPassword = scanner.nextLine();
                        auctionSystem.registerUser(regUsername, regPassword);
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();
                        auctionSystem.loginUser(loginUsername, loginPassword);
                        break;

                    case 3:
                        auctionSystem.logoutUser();
                        break;

                    case 4:
                        if (auctionSystem.getCurrentUser() != null) {
                            System.out.print("Enter item name: ");
                            String itemName = scanner.nextLine();
                            auctionSystem.listItem(itemName);
                        } else {
                            System.out.println("Please log in before listing an item.");
                        }
                        break;

                    case 5:
                        if (auctionSystem.getCurrentUser() != null) {
                            System.out.print("Enter item name: ");
                            String bidItemName = scanner.nextLine();
                            System.out.print("Enter bid amount: ");
                            double bidAmount = scanner.nextDouble();
                            auctionSystem.placeBid(bidItemName, bidAmount);
                        } else {
                            System.out.println("Please log in before placing a bid.");
                        }
                        break;

                    case 6:
                        System.out.println("Exiting the auction system. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;

                    case 7:
                        System.out.print("Enter item name to close the auction: ");
                        String closeItemName = scanner.nextLine();
                        auctionSystem.closeAuctionForItem(closeItemName);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (Exception e) {
                System.out.println("Entered Invalid Input");
            }
        }
    }
}
