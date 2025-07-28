import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;

abstract class User {
    protected String name;
    protected int umur;
    protected String alamat;
    protected String username;
    private String password;

    public User(String name, int umur, String alamat, String username, String password) {
        this.name = name;
        this.umur = umur;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
    }

    public abstract boolean login(String username, String password);

    public String getName() { return this.name; }
    public int getUmur() { return this.umur; }
    public String getAlamat() { return this.alamat; }
    public String getUsername() { return this.username; }

    protected boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

class Pembeli extends User {
    private double saldo;
    private String tanggalRegistrasi;
    private boolean statusAkun;
    private ArrayList<String> checkoutList;

    public Pembeli(String nama, int umur, String alamat, String username, String password) {
        super(nama, umur, alamat, username, password);
        this.saldo = 0.0;
        this.tanggalRegistrasi = LocalDateTime.now().toString();
        this.statusAkun = true;
        this.checkoutList = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && checkPassword(password);
    }

    public boolean addToCheckout(String item) {
        if (checkoutList.size() >= 10) {
            return false;
        }
        checkoutList.add(item);
        return true;
    }

    public void processCheckout() {
        System.out.println("Processing checkout for items in cart");
    }

    public double getSaldo() { return saldo; }
    public String getTanggalRegis() { return tanggalRegistrasi; }
    public boolean getStatusAkun() { return statusAkun; }
    public ArrayList<String> getCheckoutList() { return checkoutList; }
}

class Farmer extends User {
    private ArrayList<String> vegetables;
    private double rating;

    public Farmer(String nama, int umur, String alamat, String username, String password) {
        super(nama, umur, alamat, username, password);
        this.vegetables = new ArrayList<>();
        this.rating = 0.0;
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && checkPassword(password);
    }

    public boolean addProduct(String itemName) {
        if (vegetables.size() >= 20) {
            return false;
        }
        vegetables.add(itemName);
        return true;
    }

    public boolean discardProduct(String itemName) {
        return vegetables.remove(itemName);
    }

    public ArrayList<String> getVegetables() {
        return vegetables;
    }

    public double getRating() { return rating; }
}

interface PaymentProcessor {
    boolean processPayment(double saldo, String paymentMethod, Scanner scanner);
    String getPaymentMethod();
}

class COD implements PaymentProcessor {
    @Override
    public boolean processPayment(double saldo, String paymentMethod, Scanner scanner) {
        System.out.print("Enter delivery address: ");
        String address = scanner.nextLine();
        return validateAddress(address);
    }
    @Override
    public String getPaymentMethod() { return "Cash On Delivery"; }
    public boolean validateAddress(String address) {
        return address != null && address.length() > 5;
    }
}

class Cashless implements PaymentProcessor {
    @Override
    public boolean processPayment(double saldo, String paymentMethod, Scanner scanner) {
        System.out.print("Enter 16-digit card number: ");
        String cc = scanner.nextLine();
        return validateCardDetail(cc);
    }
    @Override
    public String getPaymentMethod() { return "Cashless Payment"; }
    public boolean validateCardDetail(String cc) {
        return cc != null && cc.length() == 16;
    }
}

public class Main {
    private ArrayList<Pembeli> pembelis;
    private ArrayList<Farmer> farmers;
    private final int MAX_USER = 10;

    public Main() {
        this.pembelis = new ArrayList<>();
        this.farmers = new ArrayList<>();
    }

    public void initiateSystem() {
        addPembeli(new Pembeli("John", 25, "Address 1", "buyer1", "pass123"));
        addPembeli(new Pembeli("Doe", 22, "Address 2", "buyer2", "pass234"));
        addFarmer(new Farmer("Jane", 30, "Farm Address", "farmer1", "pass456"));
        addFarmer(new Farmer("Smith", 40, "Farm 2", "farmer2", "pass567"));
        System.out.println("System initiated successfully");
    }

    private boolean addPembeli(Pembeli p) {
        if (pembelis.size() >= MAX_USER) {
            return false;
        }
        pembelis.add(p);
        return true;
    }

    private boolean addFarmer(Farmer f) {
        if (farmers.size() >= MAX_USER) {
            return false;
        }
        farmers.add(f);
        return true;
    }

    private Pembeli findPembeli(String username) {
        for (Pembeli p : pembelis) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    private Farmer findFarmer(String username) {
        for (Farmer f : farmers) {
            if (f.getUsername().equals(username)) {
                return f;
            }
        }
        return null;
    }

    public void processCustomerRequest() {
        Scanner scanner = new Scanner(System.in);
        int mainChoice;
        do {
            System.out.println("\nMarketplace System Menu");
            System.out.println("1. Login as Buyer");
            System.out.println("2. Login as Farmer");
            System.out.println("3. Register as Buyer");
            System.out.println("4. Register as Farmer");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    buyerLoginMenu(scanner);
                    break;
                case 2:
                    farmerLoginMenu(scanner);
                    break;
                case 3:
                    registerBuyer(scanner);
                    break;
                case 4:
                    registerFarmer(scanner);
                    break;
                case 0:
                    System.out.println("Exiting system.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (mainChoice != 0);
        scanner.close();
    }

    private void buyerLoginMenu(Scanner scanner) {
        System.out.print("Enter username: ");
        String buyerUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String buyerPassword = scanner.nextLine();
        Pembeli buyer = findPembeli(buyerUsername);
        if (buyer != null && buyer.login(buyerUsername, buyerPassword)) {
            System.out.println("Login successful");
            int buyerMenu;
            do {
                System.out.println("\nBuyer Menu");
                System.out.println("1. Browse Products");
                System.out.println("2. Add to Checkout");
                System.out.println("3. View Checkout List");
                System.out.println("4. Checkout and Pay");
                System.out.println("0. Logout");
                System.out.print("Choose option: ");
                buyerMenu = scanner.nextInt();
                scanner.nextLine();
                switch (buyerMenu) {
                    case 1:
                        browseAllProducts();
                        break;
                    case 2:
                        System.out.print("Enter product name to add: ");
                        String item = scanner.nextLine();
                        boolean added = buyer.addToCheckout(item);
                        if (added) {
                            System.out.println("Item added to checkout.");
                        } else {
                            System.out.println("Checkout list full.");
                        }
                        break;
                    case 3:
                        System.out.println("Checkout List:");
                        for (String s : buyer.getCheckoutList()) {
                            System.out.println("- " + s);
                        }
                        break;
                    case 4:
                        processPaymentMenu(buyer, scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } while (buyerMenu != 0);
        } else {
            System.out.println("Login failed");
        }
    }

    private void farmerLoginMenu(Scanner scanner) {
        System.out.print("Enter username: ");
        String farmerUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String farmerPassword = scanner.nextLine();
        Farmer farmer = findFarmer(farmerUsername);
        if (farmer != null && farmer.login(farmerUsername, farmerPassword)) {
            System.out.println("Login successful");
            int farmerMenu;
            do {
                System.out.println("\nFarmer Menu");
                System.out.println("1. Add Product");
                System.out.println("2. Discard Product");
                System.out.println("3. View Products");
                System.out.println("0. Logout");
                System.out.print("Choose option: ");
                farmerMenu = scanner.nextInt();
                scanner.nextLine();
                switch (farmerMenu) {
                    case 1:
                        System.out.print("Enter product name to add: ");
                        String prod = scanner.nextLine();
                        boolean addSuccess = farmer.addProduct(prod);
                        if (addSuccess) {
                            System.out.println("Product added.");
                        } else {
                            System.out.println("Product list full.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter product name to discard: ");
                        String discard = scanner.nextLine();
                        boolean discardSuccess = farmer.discardProduct(discard);
                        if (discardSuccess) {
                            System.out.println("Product discarded.");
                        } else {
                            System.out.println("Product not found.");
                        }
                        break;
                    case 3:
                        System.out.println("Your products:");
                        for (String veg : farmer.getVegetables()) {
                            System.out.println("- " + veg);
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } while (farmerMenu != 0);
        } else {
            System.out.println("Login failed");
        }
    }

    private void registerBuyer(Scanner scanner) {
        if (pembelis.size() >= MAX_USER) {
            System.out.println("Buyer registration full.");
            return;
        }
        System.out.print("Enter name: ");
        String newBuyerName = scanner.nextLine();
        System.out.print("Enter age: ");
        int newBuyerAge = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter address: ");
        String newBuyerAddr = scanner.nextLine();
        System.out.print("Enter username: ");
        String newBuyerUser = scanner.nextLine();
        System.out.print("Enter password: ");
        String newBuyerPass = scanner.nextLine();
        Pembeli newBuyer = new Pembeli(newBuyerName, newBuyerAge, newBuyerAddr, newBuyerUser, newBuyerPass);
        boolean regBuyer = addPembeli(newBuyer);
        if (regBuyer) {
            System.out.println("Buyer registered successfully.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private void registerFarmer(Scanner scanner) {
        if (farmers.size() >= MAX_USER) {
            System.out.println("Farmer registration full.");
            return;
        }
        System.out.print("Enter name: ");
        String newFarmerName = scanner.nextLine();
        System.out.print("Enter age: ");
        int newFarmerAge = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter address: ");
        String newFarmerAddr = scanner.nextLine();
        System.out.print("Enter username: ");
        String newFarmerUser = scanner.nextLine();
        System.out.print("Enter password: ");
        String newFarmerPass = scanner.nextLine();
        Farmer newFarmer = new Farmer(newFarmerName, newFarmerAge, newFarmerAddr, newFarmerUser, newFarmerPass);
        boolean regFarmer = addFarmer(newFarmer);
        if (regFarmer) {
            System.out.println("Farmer registered successfully.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private void browseAllProducts() {
        System.out.println("All available products from all farmers:");
        for (Farmer f : farmers) {
            for (String veg : f.getVegetables()) {
                System.out.println("- " + veg + " (Farmer: " + f.getName() + ")");
            }
        }
    }

    private void processPaymentMenu(Pembeli buyer, Scanner scanner) {
        if (buyer.getCheckoutList().isEmpty()) {
            System.out.println("No items in checkout.");
            return;
        }
        System.out.println("Choose payment method:");
        System.out.println("1. Cash On Delivery");
        System.out.println("2. Cashless Payment");
        int payOpt = scanner.nextInt();
        scanner.nextLine();
        PaymentProcessor processor;
        if (payOpt == 1) {
            processor = new COD();
        } else if (payOpt == 2) {
            processor = new Cashless();
        } else {
            System.out.println("Invalid payment method.");
            return;
        }
        boolean paymentSuccess = processor.processPayment(buyer.getSaldo(), processor.getPaymentMethod(), scanner);
        if (paymentSuccess) {
            System.out.println("Payment successful. Checkout complete.");
            buyer.getCheckoutList().clear();
        } else {
            System.out.println("Payment failed.");
        }
    }

    public static void main(String[] args) {
        Main system = new Main();
        system.initiateSystem();
        system.processCustomerRequest();
    }
}