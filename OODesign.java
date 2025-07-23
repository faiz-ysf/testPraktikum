public abstract class User {
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

    public String getName() { return this.name;}

    public int getUmur() { return this.umur;}

    public String getAlamat() {  return this.alamat;}

    public String getUsername() { return this.username;} 

    public String getPassword() { return this.password; }
    
}


public class Pembeli extends User {
    private double saldo;
    public String tanggalRegistrasi;
    private boolean statusAkun;
    private String[] checkoutList;

    public Pembeli(String nama, int umur, String alamat, String username, String password) {
        super(nama, umur, alamat, username, password);
        this.saldo = 0.0;
        this.tanggalRegistrasi = "2025-07-23";
        this.statusAkun = true;
        this.checkoutList = new String[10];
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && getPassword().equals(password);
    }

    public String[] browseProduct() {
        return new String[]{"Tomatoes", "Carrots", "Lettuce"};
    }

    public String[] checkout(String[] checkoutList) {
        this.checkoutList = checkoutList;
        return this.checkoutList;
    }

    public void processCheckout() {
        System.out.println("Processing checkout for items in cart");
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTanggalRegis() {
        return tanggalRegistrasi;
    }

    public boolean getStatusAkun() {
        return statusAkun;
    }

    public String[] getCheckoutList() {
        return checkoutList;
    }
}

public class Farmer extends User {
    private String[] vegetables;
    private double rating;

    public Farmer(String nama, int umur, String alamat, String username, String password) {
        super(nama, umur, alamat, username, password);
        this.vegetables = new String[20];
        this.rating = 0.0;
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && getPassword().equals(password);
    }

    public String[] addProduct(String[] vegetables, String itemName) {
        for (int i = 0; i < vegetables.length; i++) {
            if (vegetables[i] == null) {
                vegetables[i] = itemName;
                break;
            }
        }
        this.vegetables = vegetables;
        return this.vegetables;
    }

    public String[] discardProduct(String[] vegetables, String itemName) {
        for (int i = 0; i < vegetables.length; i++) {
            if (vegetables[i] != null && vegetables[i].equals(itemName)) {
                vegetables[i] = null;
                break;
            }
        }
        this.vegetables = vegetables;
        return this.vegetables;
    }

    public String[] getVegetables() {
        return vegetables;
    }

    public double getRating() {
        return rating;
    }
}

public interface PaymentProcessor {
    boolean processPayment(double saldo, String paymentMethod);
    String getPaymentMethod();
}

public class COD implements PaymentProcessor {
    
    @Override
    public boolean processPayment(double saldo, String paymentMethod) {
        return validateAddress("Default Address");
    }

    @Override
    public String getPaymentMethod() {
        return "Cash On Delivery";
    }

    public boolean validateAddress(String address) {
        return address != null && address.length() > 5;
    }
}

public class Cashless implements PaymentProcessor {
    
    @Override
    public boolean processPayment(double saldo, String paymentMethod) {
        return validateCardDetail("1234567890123456");
    }

    @Override
    public String getPaymentMethod() {
        return "Cashless Payment";
    }

    public boolean validateCardDetail(String cc) {
        return cc != null && cc.length() == 16;
    }
}

import java.util.Scanner;

public class Main {
    private String[] pembeli;
    private String[] farmer;

    public Main() {
        this.pembeli = new String[10];
        this.farmer = new String[10];
    }

    public void initiateSystem() {
        pembeli[0] = "buyer1";
        pembeli[1] = "buyer2";
        farmer[0] = "farmer1";
        farmer[1] = "farmer2";
        System.out.println("System initiated successfully");
    }

    public void processCustomerRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the marketplace system");
        System.out.println("1. Login as Buyer");
        System.out.println("2. Login as Farmer");
        System.out.print("Choose option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            Pembeli buyer = new Pembeli("John", 25, "Address 1", "buyer1", "pass123");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            if (buyer.login(username, password)) {
                System.out.println("Login successful");
                String[] products = buyer.browseProduct();
                System.out.println("Available products:");
                for (String product : products) {
                    System.out.println("- " + product);
                }
            } else {
                System.out.println("Login failed");
            }
        } else if (choice == 2) {
            Farmer farmer = new Farmer("Jane", 30, "Farm Address", "farmer1", "pass456");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            if (farmer.login(username, password)) {
                System.out.println("Login successful");
                String[] vegetables = new String[5];
                vegetables = farmer.addProduct(vegetables, "Tomatoes");
                vegetables = farmer.addProduct(vegetables, "Carrots");
                
                System.out.println("Your products:");
                for (String veg : vegetables) {
                    if (veg != null) {
                        System.out.println("- " + veg);
                    }
                }
            } else {
                System.out.println("Login failed");
            }
        }
        
        scanner.close();
    }

    public static void main(String[] args) {
        Main system = new Main();
        system.initiateSystem();
        system.processCustomerRequest();
    }
}
