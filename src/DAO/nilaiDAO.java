package DAO;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import datasiswa.Siswa;
import model.nilai;
import model.mapel;

import javax.swing.*;

import static Koneksi.koneksi.getConnection;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
public class nilaiDAO {
    Connection kon;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<nilai> listnilai;
    ArrayList<Siswa> listsiswa;
    ArrayList<mapel> mapel;
    Siswa siswa;
    nilai nilai;
    public nilaiDAO(){
        kon = getConnection();
    }

    public ArrayList<nilai> getListnilai(){
        try{
            listnilai = new ArrayList<>();
            ps=kon.prepareStatement("SELECT * FROM nilai",TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()){
                nilai = new nilai();
                nilai.setNis(rs.getString("nis"));
                nilai.setSemester(rs.getString("Semester"));
                nilai.setKodemapel(rs.getString("KodeMapel"));
                nilai.setNilai(rs.getBigDecimal("nilai"));
                listnilai.add(nilai);
            }
        }
        catch (SQLException se){
            System.out.println("Error: "+ se);
        }
        return listnilai;
    }
    public ArrayList<Siswa> getListsiswa(){
        try{
            listsiswa = new ArrayList<>();
            ps=kon.prepareStatement("SELECT * FROM siswa",TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()){
                siswa = new Siswa();
                siswa.setNis(rs.getString("nis"));
                siswa.setNama(rs.getString("nama"));
                siswa.setJenkel(rs.getString("jenkel"));
                siswa.setTempLahir(rs.getString("tmptlahir"));
                siswa.setTglLahir(rs.getDate("tgllahir"));
                siswa.setAlamat(rs.getString("alamat"));
                siswa.setAyahKandung(rs.getString("ayah"));
                siswa.setIbuKandung(rs.getString("ibu"));
                siswa.setTelp(rs.getString("telpon"));
                listsiswa.add(siswa);
            }
        }
        catch (SQLException se){
            System.out.println("Error: "+ se);
        }
        return listsiswa;
    }
    public ArrayList<mapel> getListmapel() {
        ArrayList<mapel> listmapel = new ArrayList<>();
        try {
            // Query untuk mengambil semua data dari tabel matapelajaran
            ps = kon.prepareStatement("SELECT * FROM matapelajaran", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();

            // Memastikan result set dimulai dari awal
            rs.beforeFirst();

            // Looping untuk mengambil setiap record dan menyimpannya ke dalam listmapel
            while (rs.next()) {
                mapel mapel = new mapel();
                mapel.setKode(rs.getString("kode"));  // Mengambil kode mapel dari kolom 'kode'
                mapel.setMapel(rs.getString("mapel"));  // Mengambil nama mapel dari kolom 'nama_mapel'
                listmapel.add(mapel);  // Menambahkan objek mapel ke dalam ArrayList
            }
        } catch (SQLException se) {
            System.out.println("Error: " + se);
        }
        return listmapel;  // Mengembalikan list mapel
    }
    public void tampilkanData() {
        ArrayList<nilai> listNilai = getListnilai();
        ArrayList<Siswa> listSiswa = getListsiswa();
        ArrayList<mapel> listMapel = getListmapel();

        System.out.println("DATA NILAI SISWA");

        // Looping untuk setiap siswa
        for (Siswa siswa : listSiswa) {
            System.out.println("--------------------------");
            System.out.println("Nama: " + siswa.getNama());
            System.out.println("NIS: " + siswa.getNis());

            // Looping untuk mencari nilai berdasarkan NIS siswa tersebut
            System.out.print("Semester: ");
            boolean nilaiDitemukan = false;
            int no = 1;

            for (nilai nilaiSiswa : listNilai) {
                if (siswa.getNis().equals(nilaiSiswa.getNis())) {
                    if (!nilaiDitemukan) {
                        System.out.println(nilaiSiswa.getSemester());
                        nilaiDitemukan = true;
                        System.out.println("No.\tKode Mapel\tNama Mapel\tNilai");
                    }

                    // Cari nama mapel berdasarkan kode mapel dari nilai siswa
                    String namaMapel = "";
                    for (mapel m : listMapel) {
                        if (m.getKode().equals(nilaiSiswa.getKodemapel())) {
                            namaMapel = m.getMapel();
                            break;
                        }
                    }

                    // Menampilkan nomor, kode mapel, nama mapel, dan nilai
                    System.out.println(no + ".\t" + nilaiSiswa.getKodemapel() + "\t" + namaMapel + "\t" + nilaiSiswa.getNilai());
                    no++;
                }
            }

            // Jika siswa tidak memiliki nilai
            if (!nilaiDitemukan) {
                System.out.println("Siswa ini belum memiliki nilai.");
            }
        }
    }
    public void tambahNilai(String nis, String semester, String kodemapel, BigDecimal nilai){
        String qry = "insert into nilai (nis,semester,kodemapel,nilai) values (?,?,?,?)";
        try{
            ps= kon.prepareStatement(qry);
            ps.setString(1,nis);
            ps.setString(2,semester);
            ps.setString(3,kodemapel);
            ps.setBigDecimal(4,nilai);
            ps.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Error : "+e);
        }
    }
    public void inputData(){
        String nis = JOptionPane.showInputDialog("NIS");
        String semester = JOptionPane.showInputDialog("Semester");
        String kodemapel = JOptionPane.showInputDialog("Kode Mapel").toUpperCase();
        String nilaiStr = JOptionPane.showInputDialog("Nilai");
        BigDecimal nilai = null;

        try {
            nilai = new BigDecimal(nilaiStr);  // Konversi String ke BigDecimal
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nilai yang dimasukkan tidak valid. Harap masukkan angka.");
            return;  // Menghentikan eksekusi jika input nilai tidak valid
        }
        tambahNilai(nis,semester,kodemapel,nilai);
    }
    public void editDataNilai(String nis, String kodeMapel, String semester, BigDecimal nilaiBaru) {
        try {
            // Query untuk mencari data nilai berdasarkan NIS, KodeMapel, dan Semester
            ps = kon.prepareStatement(
                    "SELECT * FROM nilai WHERE nis = ? AND KodeMapel = ? AND Semester = ?");
            ps.setString(1, nis);
            ps.setString(2, kodeMapel);
            ps.setString(3, semester);
            rs = ps.executeQuery();

            // Jika data nilai ditemukan
            if (rs.next()) {
                // Update nilai baru
                ps = kon.prepareStatement(
                        "UPDATE nilai SET nilai = ? WHERE nis = ? AND KodeMapel = ? AND Semester = ?");
                ps.setBigDecimal(1, nilaiBaru);
                ps.setString(2, nis);
                ps.setString(3, kodeMapel);
                ps.setString(4, semester);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Data nilai berhasil diupdate.");
                }
            } else {
                System.out.println("Data nilai tidak ditemukan.");
            }
        } catch (SQLException se) {
            System.out.println("Error: " + se);
        }
    }
    public void hapusData(String nis,String semester,String kodemapel){
        String qry = "delete from nilai where nis= ? and semester=? and kodemapel=?";
        try{
            ps=kon.prepareStatement(qry);
            ps.setString(1,nis);
            ps.setString(2,semester);
            ps.setString(3,kodemapel);
            ps.executeUpdate();
            System.out.println("Data Nilai Terhapus");
        }
        catch (SQLException e){
            System.out.println("Error Data Tidak Ditemukan: "+e);
        }
    }
    public void deleteData() {
        String nis = JOptionPane.showInputDialog("Masukan Nis Yang Ingin Dihapus");
        String semester = JOptionPane.showInputDialog("Masukan Semester berapa Yang Ingin Dihapus");
        String kodemapel = JOptionPane.showInputDialog("Masukan Kode Mapel Yang Ingin Dihapus");
        hapusData(nis,semester,kodemapel);
    }
    public nilai carinilai(String nis,String semester,String kodemapel){
        nilai = new nilai();
        try{
            ps = kon.prepareStatement("select * from guru where nis = ? and semester = ? and kodemapel = ?");
            ps.setString(1,nis);
            ps.setString(2,semester);
            ps.setString(3,kodemapel);
            rs.beforeFirst();//kursor diarahkan ke sebelum data pertama
            if(rs.next()){//jika ditemukan data
                nilai.setNis(rs.getString("nis"));
                nilai.setSemester(rs.getString("semester"));
                nilai.setKodemapel(rs.getString("kodemapel"));
            }
        }catch (SQLException e){
            System.out.println("Error : "+e);
        }
        return nilai;
    }
    public void updateNilai(String nis, String semester, String kodemapel, BigDecimal nilaiBaru) {
        String qry = "UPDATE nilai SET nilai = ? WHERE nis = ? AND semester = ? AND kodemapel = ?";
        try {
            ps = kon.prepareStatement(qry);
            ps.setBigDecimal(1, nilaiBaru);
            ps.setString(2, nis);
            ps.setString(3, semester);
            ps.setString(4, kodemapel);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data nilai berhasil diupdate.");
            } else {
                System.out.println("Data nilai tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
    public void updateData() {
        String nis = JOptionPane.showInputDialog("Masukkan NIS siswa");
        String semester = JOptionPane.showInputDialog("Masukkan Semester");
        String kodemapel = JOptionPane.showInputDialog("Masukkan Kode Mapel");
        String nilaiBaruStr = JOptionPane.showInputDialog("Masukkan Nilai Baru");
        BigDecimal nilaiBaru = new BigDecimal(nilaiBaruStr);

        updateNilai(nis, semester, kodemapel, nilaiBaru);
    }
    public void cariNilai(String nis, String semester, String kodemapel) {
        String qry = "SELECT * FROM nilai WHERE nis = ? AND semester = ? AND kodemapel = ?";
        try {
            ps = kon.prepareStatement(qry);
            ps.setString(1, nis);
            ps.setString(2, semester);
            ps.setString(3, kodemapel);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("NIS: " + rs.getString("nis"));
                System.out.println("Semester: " + rs.getString("semester"));
                System.out.println("Kode Mapel: " + rs.getString("kodemapel"));
                System.out.println("Nilai: " + rs.getBigDecimal("nilai"));
            } else {
                System.out.println("Data nilai tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public void searchData() {
        String nis = JOptionPane.showInputDialog("Masukkan NIS siswa");
        String semester = JOptionPane.showInputDialog("Masukkan Semester");
        String kodemapel = JOptionPane.showInputDialog("Masukkan Kode Mapel");

        cariNilai(nis, semester, kodemapel);
    }
    public static void main(String[]args){
        nilaiDAO nl = new nilaiDAO();
        while (true) {
            String pilihan = JOptionPane.showInputDialog(
                    "Pilih operasi: \n" +
                            "1. Tambah Nilai\n" +
                            "2. Tampilkan Semua Data Nilai\n" +
                            "3. Update Data Nilai\n" +
                            "4. Hapus Data Nilai\n" +
                            "5. Cari Data Nilai\n" +
                            "6. Exit\n"
            );
            switch (pilihan) {
                case "1":
                    nl.inputData();
                    break;
                case "2":
                    nl.tampilkanData();
                    break;
                case "3":
                    nl.updateData();
                    break;
                case "4":
                    nl.deleteData();
                    break;
                case "5":
                    nl.searchData();
                    break;
                case "6":
                    System.exit(0);
                    break;
            }
        }
    }
}
