import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class implementasiUKTTest {

  @BeforeAll
  static void setupLocale() {
    Locale.setDefault(Locale.US);
  }

  @Test
  void testPenyediaBeasiswaBasic() {
    PenyediaBeasiswa p = new PenyediaBeasiswa("LPDP");
    assertEquals("LPDP", p.namaInstitusi);
    assertFalse(p.sudahCair);
    assertEquals(0, p.hariTerlambat);

    p.cairkanBeasiswa();
    assertTrue(p.sudahCair);

    p.tambahHariTerlambat(3);
    assertEquals(3, p.hariTerlambat);

    p.tambahHariTerlambat(2);
    assertEquals(5, p.hariTerlambat);
  }

  @Test
  void testMahasiswaFullBayar() {
    MahasiswaFullBayar m = new MahasiswaFullBayar("Faiz", 10_000_000);
    assertEquals(10_000_000, m.ukt);
    assertEquals(0, m.sudahBayar);
    assertEquals(0, m.uangSaku);

    m.bayar(4_000_000);
    assertFalse(m.bisaDaftar());
    assertEquals(6_000_000, m.sisaPembayaran());
    assertTrue(m.bisaDaftarAkademik());

    m.bayar(6_000_000);
    assertTrue(m.bisaDaftar());
    assertEquals(0, m.sisaPembayaran());

    m.tambahUangSaku(500_000);
    assertEquals(0, m.sisaPembayaran());
    assertEquals(500_000, m.uangSaku);
  }

  @Test
  void testMahasiswaCicilan() {
    MahasiswaCicilan m = new MahasiswaCicilan("Ani", 8_000_000, 2_000_000);
    assertFalse(m.bisaDaftar());
    m.bayar(1_500_000);
    assertFalse(m.bisaDaftar());
    assertEquals(6_500_000, m.sisaPembayaran());

    m.bayar(600_000);
    assertTrue(m.bisaDaftar());
    assertEquals(5_900_000, m.sisaPembayaran());
  }

  @Test
  void testMahasiswaBeasiswa_beforeCair() {
    PenyediaBeasiswa p = new PenyediaBeasiswa("DonaturX");
    MahasiswaBeasiswa m = new MahasiswaBeasiswa("Budi", 12_000_000, 0.5, p);
    assertFalse(m.bisaDaftar());
    m.bayar(5_000_000);
    assertFalse(m.bisaDaftar());
    m.bayar(1_000_000);
    assertTrue(m.bisaDaftar());

    assertTrue(m.bisaDaftarAkademik());
    assertEquals(12_000_000 - 6_000_000, m.sisaPembayaran());
  }

  @Test
  void testMahasiswaBeasiswa_afterCair() {
    PenyediaBeasiswa p = new PenyediaBeasiswa("DonaturY");
    p.cairkanBeasiswa();
    p.tambahHariTerlambat(7);

    MahasiswaBeasiswa m = new MahasiswaBeasiswa("Cici", 10_000_000, 0.3, p);
    assertTrue(m.bisaDaftar());
    assertEquals(10_000_000, m.sisaPembayaran());
    assertEquals(7, m.penyedia.hariTerlambat);
  }

  @Test
  void testMahasiswaUangSaku_notCair() {
    PenyediaBeasiswa p = new PenyediaBeasiswa("SponsorA");
    MahasiswaUangSaku m = new MahasiswaUangSaku("Dian", 5_000_000, 1_000_000, p);
    assertFalse(m.bisaDaftar());
    m.bayar(4_000_000);
    assertFalse(m.bisaDaftar());
    assertEquals(1_000_000, m.sisaPembayaran());
  }

  @Test
  void testMahasiswaUangSaku_cair() {
    PenyediaBeasiswa p = new PenyediaBeasiswa("SponsorB");
    p.cairkanBeasiswa();
    MahasiswaUangSaku m = new MahasiswaUangSaku("Eka", 5_000_000, 1_500_000, p);

    assertFalse(m.bisaDaftar());
    m.bayar(3_600_000);
    assertTrue(m.bisaDaftar());
    assertEquals(5_000_000 - 3_600_000 - 1_500_000, m.sisaPembayaran());
  }

  @Test
  void testSisaPembayaranLowerBound() {
    MahasiswaFullBayar m = new MahasiswaFullBayar("Fajar", 2_000_000);
    m.bayar(2_500_000);
    m.tambahUangSaku(1_000_000);
    assertEquals(0, m.sisaPembayaran());
  }

  @Test
  void testMain_allTypesPaths_withoutExternalLibs() {
    String input =
        String.join(
            "\n",
            "4",
            "Andi",
            "1000000",
            "1",
            "1000000",
            "Beni",
            "2000000",
            "2",
            "500000",
            "600000",
            "Clara",
            "4000000",
            "3",
            "0.5",
            "DonaturZ",
            "y",
            "3",
            "2000000",
            "Dodi",
            "3000000",
            "4",
            "1000000",
            "SponsorZ",
            "n",
            "5",
            "3000000")
        + "\n";

    ByteArrayInputStream in =
        new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    java.io.InputStream originalIn = System.in;

    try {
      System.setIn(in);
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      implementasiUKT.main(new String[] {});

      String output = out.toString(StandardCharsets.UTF_8);
      assertTrue(output.contains("=== HASIL STATUS MAHASISWA ==="));
      assertTrue(output.contains("Nama: Andi"));
      assertTrue(output.contains("Nama: Beni"));
      assertTrue(output.contains("Nama: Clara"));
      assertTrue(output.contains("Nama: Dodi"));

      assertTrue(output.contains("Penyedia: DonaturZ"));
      assertTrue(output.contains("Beasiswa Cair: true"));
      assertTrue(output.contains("Hari Terlambat: 3"));

      assertTrue(output.contains("Penyedia: SponsorZ"));
      assertTrue(output.contains("Uang Saku Cair: false"));
      assertTrue(output.contains("Hari Terlambat: 5"));

      assertTrue(output.contains("Bisa Daftar:"));
      assertTrue(output.contains("Bisa Daftar Akademik:"));
    } catch (Exception e) {
      fail("Main method threw exception: " + e.getMessage());
    } finally {
      System.setOut(originalOut);
      System.setIn(originalIn);
    }
  }
}