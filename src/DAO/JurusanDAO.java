package DAO;

import datasiswa.Siswa;
import model.Jurusan;
import model.nilai;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

import static Koneksi.koneksi.getConnection;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public class JurusanDAO {
    Connection kon;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<Jurusan> listJurusan;
    Jurusan jurusan;
    public JurusanDAO(){
        kon = getConnection();
    }
    public ArrayList<Jurusan> getListjurusan(){
        try{
            listJurusan = new ArrayList<>();
            String query = "SELECT jurusan.kdJurusan, jurusan.nmJurusan, guru.nama " +
                    "FROM jurusan " +
                    "INNER JOIN guru ON jurusan.kdKajur = guru.nik;";
            ps=kon.prepareStatement(query,TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();

            while (rs.next()){
                jurusan =  new Jurusan();
                jurusan.setKdJurusan(rs.getString("kdJurusan"));
                jurusan.setNmJurusan(rs.getString("nmJurusan"));
                jurusan.setKdKajur(rs.getString("nama"));

                listJurusan.add(jurusan);
            }
        }
        catch (SQLException se){
            System.out.println("Error: "+ se);
        }
        return listJurusan;
    }
    public void tampilkanJurusan(){
        ArrayList<Jurusan> listJurusan = getListjurusan();
        System.out.println("JURUSAN :");
        System.out.println("Kode Jurusan    Nama Jurusan    Kaprog");
        System.out.println("------------    -------------    -----------");
        for (Jurusan jurusan : listJurusan) {
            System.out.printf("%-15s %-20s %-15s\n",
                    jurusan.getKdJurusan(),
                    jurusan.getNmJurusan(),
                    jurusan.getKdKajur());
        }
    }
    public void hapusJurusan(String kdJurusan){
        String qry = "delete from jurusan where kdJurusan= ?";
        try{
            ps=kon.prepareStatement(qry);
            ps.setString(1,kdJurusan);
            ps.executeUpdate();
            System.out.println("Data Nilai Terhapus");
        }
        catch (SQLException e){
            System.out.println("Error Data Tidak Ditemukan: "+e);
        }
    }
    public void deleteJurusan(){
        String kdJurusan = JOptionPane.showInputDialog("Masukan kdJurusan Yang Ingin Dihapus");
        hapusJurusan(kdJurusan);
    }

    public void tambahJurusan(String kdJurusan, String nmJurusan, String kdKajur){
        String qry = "insert into jurusan (kdJurusan,nmJurusan,kdKajur) values (?,?,?)";
        try{
            ps= kon.prepareStatement(qry);
            ps.setString(1,kdJurusan);
            ps.setString(2,nmJurusan);
            ps.setString(3,kdKajur);
            ps.executeUpdate();
            System.out.println("Data baru berhasil ditambahkan");
        }
        catch (SQLException e){
            System.out.println("Error : "+e);
        }
    }
    public void inputData(){
        String kdJurusan = JOptionPane.showInputDialog("kdJurusan");
        String nmJurusan = JOptionPane.showInputDialog("nmJurusan");
        String kdKajur = JOptionPane.showInputDialog("kdKajur");
        tambahJurusan(kdJurusan,nmJurusan,kdKajur);
    }
    public void updateJurusan(String kdJurusanlama, String kdJurusanbaru, String nmJurusan, String kdKajur) {
        String qry = "UPDATE jurusan SET kdJurusan = ?, nmJurusan = ?, kdKajur = ? WHERE kdJurusan = ?";
        try {
            ps = kon.prepareStatement(qry);
            ps.setString(1, kdJurusanbaru);  // kdJurusan baru
            ps.setString(2, nmJurusan);
            ps.setString(3, kdKajur);
            ps.setString(4, kdJurusanlama);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data jurusan dengan kdJurusan: " + kdJurusanlama + " telah diupdate ke kdJurusan baru: " + kdJurusanbaru);
            } else {
                System.out.println("Tidak ada data yang diupdate. Kode jurusan mungkin tidak ditemukan.");
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Find Jurusan by kdJurusan
    public Jurusan cariJurusan(String kdJurusan) {
        jurusan = new Jurusan();
        try {
            ps = kon.prepareStatement("SELECT * FROM jurusan WHERE kdJurusan = ?");
            ps.setString(1, kdJurusan);
            rs = ps.executeQuery();  // Execute the query

            if (rs.next()) {  // If data is found
                jurusan.setKdJurusan(rs.getString("kdJurusan"));
                jurusan.setNmJurusan(rs.getString("nmJurusan"));
                jurusan.setKdKajur(rs.getString("kdKajur"));
            } else {
                System.out.println("Jurusan dengan kode " + kdJurusan + " tidak ditemukan.");
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return jurusan;
    }

    // Update data using input dialogs
    public void updateData() {
        String kdJurusanlama = JOptionPane.showInputDialog("Masukkan kdJurusan yang ingin diupdate datanya");
        Jurusan model = cariJurusan(kdJurusanlama);

        if (model.getKdJurusan() == null) {
            System.out.println("Tidak ada kode jurusan dengan kode: " + kdJurusanlama);
            return;
        }

        String kdJurusanbaru = JOptionPane.showInputDialog("Masukkan kdJurusan baru (Sebelumnya: " + model.getKdJurusan() + "):");
        String nmJurusan = JOptionPane.showInputDialog("Masukkan nama jurusan baru (Sebelumnya: " + model.getNmJurusan() + "):");
        String kdKajur = JOptionPane.showInputDialog("Masukan kdKajur (Sebelumnya: " + model.getKdKajur() + "):");

        updateJurusan(kdJurusanlama, kdJurusanbaru, nmJurusan, kdKajur);
    }

    public static void main(String []args){
        JurusanDAO jr = new JurusanDAO();
//        jr.tampilkanJurusan();
//        jr.deleteJurusan();
//        jr.inputData();
//        jr.updateData();
        while (true) {
            String pilihan = JOptionPane.showInputDialog(
                    "PIlih operasi: \n" + "1.Tambah Jurusan\n" + "2.Tampilkan semua data Jurusan\n" +
                            "3.Update Data Jurusan\n" + "4.Delete Data Jurusan\n" + "5.Exit\n"
            );
            switch (pilihan) {
                case "1":
                    jr.inputData();
                    break;
                case "2":
                    jr.tampilkanJurusan();
                    break;
                case "3":
                    jr.updateData();
                    break;
                case "4":
                    jr.deleteJurusan();
                    break;
                case "5":
                    System.exit(0);
                    break;
    }
        }
    }
}
