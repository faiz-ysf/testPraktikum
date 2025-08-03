import java.util.*;

interface Pembayaran {
    double sisaPembayaran();
    boolean bisaDaftar();
    boolean bisaDaftarAkademik();
}

class PenyediaBeasiswa {
    String namaInstitusi;
    boolean sudahCair;
    int hariTerlambat;

    PenyediaBeasiswa(String namaInstitusi) {
        this.namaInstitusi = namaInstitusi;
        this.sudahCair = false;
        this.hariTerlambat = 0;
    }

    void cairkanBeasiswa() {
        sudahCair = true;
    }

    void tambahHariTerlambat(int hari) {
        hariTerlambat += hari;
    }
}

abstract class Mahasiswa implements Pembayaran {
    String nama;
    double ukt;
    double sudahBayar;
    double uangSaku;

    Mahasiswa(String nama, double ukt) {
        this.nama = nama;
        this.ukt = ukt;
        this.sudahBayar = 0;
        this.uangSaku = 0;
    }

    @Override
    public double sisaPembayaran() {
        return Math.max(0, ukt - sudahBayar - uangSaku);
    }

    void bayar(double jumlah) {
        sudahBayar += jumlah;
    }

    void tambahUangSaku(double jumlah) {
        uangSaku += jumlah;
    }

    @Override
    public boolean bisaDaftarAkademik() {
        return bisaDaftar();
    }
}

class MahasiswaFullBayar extends Mahasiswa {
    MahasiswaFullBayar(String nama, double ukt) {
        super(nama, ukt);
    }

    @Override
    public boolean bisaDaftar() {
        return sudahBayar >= ukt;
    }
}

class MahasiswaCicilan extends Mahasiswa {
    double minimalCicilan;

    MahasiswaCicilan(String nama, double ukt, double minimalCicilan) {
        super(nama, ukt);
        this.minimalCicilan = minimalCicilan;
    }

    @Override
    public boolean bisaDaftar() {
        return sudahBayar >= minimalCicilan;
    }
}

class MahasiswaBeasiswa extends Mahasiswa {
    double persenBeasiswa;
    PenyediaBeasiswa penyedia;

    MahasiswaBeasiswa(String nama, double ukt, double persenBeasiswa, PenyediaBeasiswa penyedia) {
        super(nama, ukt);
        this.persenBeasiswa = persenBeasiswa;
        this.penyedia = penyedia;
    }

    @Override
    public boolean bisaDaftar() {
        return penyedia.sudahCair || sudahBayar >= (ukt * (1 - persenBeasiswa));
    }

    @Override
    public boolean bisaDaftarAkademik() {
        return true;
    }
}

class MahasiswaUangSaku extends Mahasiswa {
    PenyediaBeasiswa penyedia;

    MahasiswaUangSaku(String nama, double ukt, double uangSaku, PenyediaBeasiswa penyedia) {
        super(nama, ukt);
        this.uangSaku = uangSaku;
        this.penyedia = penyedia;
    }

    @Override
    public boolean bisaDaftar() {
        return (sudahBayar + (penyedia.sudahCair ? uangSaku : 0)) >= ukt;
    }
}

public class implementasiUKT {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Mahasiswa> daftarMahasiswa = new ArrayList<>();
        Map<String, PenyediaBeasiswa> penyediaMap = new HashMap<>();

        System.out.print("Masukkan jumlah mahasiswa: ");
        int n = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < n; i++) {
            System.out.println("Data Mahasiswa ke-" + (i + 1));
            System.out.print("Nama: ");
            String nama = sc.nextLine();
            System.out.print("UKT: ");
            double ukt = Double.parseDouble(sc.nextLine());
            System.out.print("Tipe (1=Full, 2=Cicilan, 3=Beasiswa, 4=Uang Saku): ");
            int tipe = Integer.parseInt(sc.nextLine());

            if (tipe == 1) {
                MahasiswaFullBayar m = new MahasiswaFullBayar(nama, ukt);
                System.out.print("Jumlah yang sudah dibayar: ");
                m.bayar(Double.parseDouble(sc.nextLine()));
                daftarMahasiswa.add(m);
            } else if (tipe == 2) {
                System.out.print("Minimal cicilan: ");
                double minCicilan = Double.parseDouble(sc.nextLine());
                MahasiswaCicilan m = new MahasiswaCicilan(nama, ukt, minCicilan);
                System.out.print("Jumlah yang sudah dibayar: ");
                m.bayar(Double.parseDouble(sc.nextLine()));
                daftarMahasiswa.add(m);
            } else if (tipe == 3) {
                System.out.print("Persen beasiswa (misal 0.5 untuk 50%): ");
                double persen = Double.parseDouble(sc.nextLine());
                System.out.print("Nama penyedia beasiswa: ");
                String namaPenyedia = sc.nextLine();
                PenyediaBeasiswa penyedia = penyediaMap.getOrDefault(namaPenyedia, new PenyediaBeasiswa(namaPenyedia));
                penyediaMap.put(namaPenyedia, penyedia);
                System.out.print("Apakah beasiswa sudah cair? (y/n): ");
                String cair = sc.nextLine();
                if (cair.equalsIgnoreCase("y")) penyedia.cairkanBeasiswa();
                System.out.print("Hari keterlambatan pencairan: ");
                penyedia.tambahHariTerlambat(Integer.parseInt(sc.nextLine()));
                MahasiswaBeasiswa m = new MahasiswaBeasiswa(nama, ukt, persen, penyedia);
                System.out.print("Jumlah yang sudah dibayar sendiri: ");
                m.bayar(Double.parseDouble(sc.nextLine()));
                daftarMahasiswa.add(m);
            } else if (tipe == 4) {
                System.out.print("Jumlah uang saku: ");
                double uangSaku = Double.parseDouble(sc.nextLine());
                System.out.print("Nama penyedia uang saku: ");
                String namaPenyedia = sc.nextLine();
                PenyediaBeasiswa penyedia = penyediaMap.getOrDefault(namaPenyedia, new PenyediaBeasiswa(namaPenyedia));
                penyediaMap.put(namaPenyedia, penyedia);
                System.out.print("Apakah uang saku sudah cair? (y/n): ");
                String cair = sc.nextLine();
                if (cair.equalsIgnoreCase("y")) penyedia.cairkanBeasiswa();
                System.out.print("Hari keterlambatan pencairan: ");
                penyedia.tambahHariTerlambat(Integer.parseInt(sc.nextLine()));
                MahasiswaUangSaku m = new MahasiswaUangSaku(nama, ukt, uangSaku, penyedia);
                System.out.print("Jumlah yang sudah dibayar sendiri: ");
                m.bayar(Double.parseDouble(sc.nextLine()));
                daftarMahasiswa.add(m);
            }
            System.out.println();
        }

        System.out.println("\n=== HASIL STATUS MAHASISWA ===");
        for (Mahasiswa m : daftarMahasiswa) {
            System.out.println("Nama: " + m.nama);
            System.out.println("Bisa Daftar: " + m.bisaDaftar());
            System.out.println("Bisa Daftar Akademik: " + m.bisaDaftarAkademik());
            System.out.println("Sisa Pembayaran: " + m.sisaPembayaran());
            System.out.println("Uang Saku: " + m.uangSaku);
            if (m instanceof MahasiswaBeasiswa) {
                MahasiswaBeasiswa mb = (MahasiswaBeasiswa) m;
                System.out.println("Penyedia: " + mb.penyedia.namaInstitusi);
                System.out.println("Beasiswa Cair: " + mb.penyedia.sudahCair);
                System.out.println("Hari Terlambat: " + mb.penyedia.hariTerlambat);
            }
            if (m instanceof MahasiswaUangSaku) {
                MahasiswaUangSaku mu = (MahasiswaUangSaku) m;
                System.out.println("Penyedia: " + mu.penyedia.namaInstitusi);
                System.out.println("Uang Saku Cair: " + mu.penyedia.sudahCair);
                System.out.println("Hari Terlambat: " + mu.penyedia.hariTerlambat);
            }
            System.out.println();
        }
    }
}