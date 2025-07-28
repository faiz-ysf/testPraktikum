import java.util.ArrayList;
import java.util.List;

// Interface BarangDagangan
interface BarangDagangan {
    String getNama();
    double getHarga();
    String caraMakan();
    String toString();
}

// Abstract class untuk mengurangi duplikasi
abstract class AbstractBarangDagangan implements BarangDagangan {
    protected String nama;
    protected double harga;

    public AbstractBarangDagangan(String nama, double harga) {
        this.nama = nama;
        this.harga = harga;
    }

    @Override
    public String getNama() {
        return nama;
    }

    @Override
    public double getHarga() {
        return harga;
    }

    @Override
    public String toString() {
        return nama;
    }
}

// Jeruk
class Jeruk extends AbstractBarangDagangan {
    public Jeruk(double harga) {
        super("Jeruk", harga);
    }

    @Override
    public String caraMakan() {
        return "Dikupas atau dua boleh dipotong, tapi kulitnya jangan dimakan ya.";
    }
}

// Apel
class Apel extends AbstractBarangDagangan {
    public Apel(double harga) {
        super("Apel", harga);
    }

    @Override
    public String caraMakan() {
        return "Dicuci dulu, kulitnya bisa dimakan.";
    }
}

// Rendang
class Rendang extends AbstractBarangDagangan {
    public Rendang(double harga) {
        super("Rendang", harga);
    }

    @Override
    public String caraMakan() {
        return "Langsung dimakan, biasanya dengan nasi.";
    }
}

// DagingAyam
class DagingAyam extends AbstractBarangDagangan {
    public DagingAyam(double harga) {
        super("Daging Ayam", harga);
    }

    @Override
    public String caraMakan() {
        return "Dimasak dulu sebelum dimakan.";
    }
}

// Generic Toko
class Toko<T extends BarangDagangan> {
    private List<T> dagangan;

    public Toko(List<T> dagangan) {
        this.dagangan = dagangan;
    }

    public void cetakDeskripsi() {
        for (T barang : dagangan) {
            System.out.println("Nama: " + barang.getNama());
            System.out.println("Cara makan: " + barang.caraMakan());
            System.out.println("Harga: " + barang.getHarga() + " per kilo");
            System.out.println();
        }
    }

    public int hitungJumlahDagangan() {
        return dagangan.size();
    }
}

// Main class
public class OOPractice {
    public static void main(String[] args) {
        Apel apel = new Apel(55200.0);
        Jeruk jeruk = new Jeruk(42500.0);
        Rendang rendangPadang = new Rendang(25000.0);
        DagingAyam ayamKremes = new DagingAyam(30000.0);

        ArrayList<BarangDagangan> dagangan = new ArrayList<>();
        dagangan.add(apel);
        dagangan.add(jeruk);
        dagangan.add(rendangPadang);
        dagangan.add(ayamKremes);

        Toko<BarangDagangan> kantinBalqebun = new Toko<>(dagangan);
        kantinBalqebun.cetakDeskripsi();
        System.out.println("Jumlah dagangan: " + kantinBalqebun.hitungJumlahDagangan());
    }
}