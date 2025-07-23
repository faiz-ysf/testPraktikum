import java.time.LocalDateTime;
import java.util.Scanner;

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
    private String[] checkoutList;
    private int checkoutCount;

    public Pembeli(String nama, int umur, String alamat, String username, String password) {
        super(nama, umur, alamat, username, password);
        this.saldo = 0.0;
        this.tanggalRegistrasi = LocalDateTime.now().toString();
        this.statusAkun = true;
        this.checkoutList = new String[10];
        this.checkoutCount = 0;
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && checkPassword(password);
    }

    public String[] browseProduct() {
        return new String[]{"Tomatoes", "Carrots", "Lettuce"};
    }

    public boolean addToCheckout(String item) {
        if (checkoutCount >= checkoutList.length) {
            return false;
        }
        checkoutList[checkoutCount++] = item;
        return true;
    }

    public void processCheckout() {
        System.out.println("Processing checkout for items in cart");
    }

    public double getSaldo() { return saldo; }
    public String getTanggalRegis() { return tanggalRegistrasi; }
    public boolean getStatusAkun() { return statusAkun; }
    public String[] getCheckoutList() { return checkoutList; }
    public int getCheckoutCount() { return checkoutCount; }
}

class Farmer extends User {
    private String[] vegetables;
    private int vegCount;
    private double rating;

    public Farmer(String nama, int umur, String alamat, String username, String password) {
        super(nama, umur, alamat, username, password);
        this.vegetables = new String[20];
        this.vegCount = 0;
        this.rating = 0.0;
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && checkPassword(password);
    }

    public boolean addProduct(String itemName) {
        if (vegCount >= vegetables.length) {
            return false;
        }
        vegetables[vegCount++] = itemName;
        return true;
    }

    public boolean discardProduct(String itemName) {
        for (int i = 0; i < vegCount; i++) {
            if (vegetables[i] != null && vegetables[i].equals(itemName)) {
                vegetables[i] = null;
                // Shift left
                for (int j = i; j < vegCount - 1; j++) {
                    vegetables[j] = vegetables[j + 1];
                }
                vegetables[--vegCount] = null;
                return true;
            }
        }
        return false;
    }

    public String[] getVegetables() {
        String[] result = new String[vegCount];
        for (int i = 0; i < vegCount; i++) {
            result[i] = vegetables[i];
        }
        return result;
    }

    public double getRating() { return rating; }
}

interface PaymentProcessor {
    boolean processPayment(double saldo, String paymentMethod);
    String getPaymentMethod();
}

class COD implements PaymentProcessor {
    @Override
    public boolean processPayment(double saldo, String paymentMethod) {
        return validateAddress("Default Address");
    }
    @Override
    public String getPaymentMethod() { return "Cash On Delivery"; }
    public boolean validateAddress(String address) {
        return address != null && address.length() > 5;
    }
}

class Cashless implements PaymentProcessor {
    @Override
    public boolean processPayment(double saldo, String paymentMethod) {
        return validateCardDetail("1234567890123456");
    }
    @Override
    public String getPaymentMethod() { return "Cashless Payment"; }
    public boolean validateCardDetail(String cc) {
        return cc != null && cc.length() == 16;
    }
}

public class Main {
    private Pembeli[] pembelis;
    private Farmer[] farmers;
    private int pembeliCount;
    private int farmerCount;
    private final int MAX_USER = 10;

    public Main() {
        this.pembelis = new Pembeli[MAX_USER];
        this.farmers = new Farmer[MAX_USER];
        this.pembeliCount = 0;
        this.farmerCount = 0;
    }

    public void initiateSystem() {
        addPembeli(new Pembeli("John", 25, "Address 1", "buyer1", "pass123"));
        addPembeli(new Pembeli("Doe", 22, "Address 2", "buyer2", "pass234"));
        addFarmer(new Farmer("Jane", 30, "Farm Address", "farmer1", "pass456"));
        addFarmer(new Farmer("Smith", 40, "Farm 2", "farmer2", "pass567"));
        System.out.println("System initiated successfully");
    }

    private boolean addPembeli(Pembeli p) {
        if (pembeliCount >= MAX_USER) {
            return false;
        }
        pembelis[pembeliCount++] = p;
        return true;
    }

    private boolean addFarmer(Farmer f) {
        if (farmerCount >= MAX_USER) {
            return false;
        }
        farmers[farmerCount++] = f;
        return true;
    }

    private Pembeli findPembeli(String username) {
        for (int i = 0; i < pembeliCount; i++) {
            if (pembelis[i].getUsername().equals(username)) {
                return pembelis[i];
            }
        }
        return null;
    }

    private Farmer findFarmer(String username) {
        for (int i = 0; i < farmerCount; i++) {
            if (farmers[i].getUsername().equals(username)) {
                return farmers[i];
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
                            System.out.println("0. Logout");
                            System.out.print("Choose option: ");
                            buyerMenu = scanner.nextInt();
                            scanner.nextLine();
                            switch (buyerMenu) {
                                case 1:
                                    String[] products = buyer.browseProduct();
                                    System.out.println("Available products:");
                                    for (String product : products) {
                                        System.out.println("- " + product);
                                    }
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
                                    String[] list = buyer.getCheckoutList();
                                    int count = buyer.getCheckoutCount();
                                    for (int i = 0; i < count; i++) {
                                        System.out.println("- " + list[i]);
                                    }
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
                    break;
                case 2:
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
                                    String[] vegs = farmer.getVegetables();
                                    System.out.println("Your products:");
                                    for (String veg : vegs) {
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
                    break;
                case 3:
                    if (pembeliCount >= MAX_USER) {
                        System.out.println("Buyer registration full.");
                        break;
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
                    break;
                case 4:
                    if (farmerCount >= MAX_USER) {
                        System.out.println("Farmer registration full.");
                        break;
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

    public static void main(String[] args) {
        Main system = new Main();
        system.initiateSystem();
        system.processCustomerRequest();
    }
}
