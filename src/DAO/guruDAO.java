package DAO;

import java.sql.*;
import java.util.ArrayList;
import model.guru;

import javax.swing.*;

import static Koneksi.koneksi.getConnection;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
public class guruDAO {
    Connection kon;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<guru> listguru;
    guru guru;

    public guruDAO(){
        kon = getConnection();
    }

    public ArrayList<guru> getListguru(){
        try{
            listguru = new ArrayList<>();
            ps=kon.prepareStatement("SELECT * FROM guru",TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()){
                guru = new guru();
                guru.setNik(rs.getString("nik"));
                guru.setNama(rs.getString("nama"));
                guru.setJenkel(rs.getString("jenkel"));
                guru.setTempLahir(rs.getString("tmptlahir"));
                guru.setTglLahir(rs.getDate("tgllahir"));
                guru.setAlamat(rs.getString("alamat"));
                guru.setTelp(rs.getString("telpon"));
                listguru.add(guru);
            }
        }
        catch (SQLException se){
            System.out.println("Error: "+ se);
        }
        return listguru;
    }
    public void tampilkanData(){
        System.out.println(getListguru());
        System.out.println("BIO DATA GURU");
        for (int i=0;i<=getListguru().size()-1;i++) {
            System.out.println("--------------------------");
            System.out.println("Guru Ke-"+(i+1));
            System.out.println("NIK : " + getListguru().get(i).getNik());
            System.out.println("Nama : " + getListguru().get(i).getNama());
            System.out.println("Jenkel : " + getListguru().get(i).getJenkel());
            System.out.println("Tempat Lahir : " + getListguru().get(i).getTempLahir());
            System.out.println("Tanggal Lahir : " + getListguru().get(i).getTglLahir());
            System.out.println("Alamat: " + getListguru().get(i).getAlamat());
            System.out.println("Telpon : " + getListguru().get(i).getTelp());
        }
    }
    public void hapusData(String nik){
        String qry = "delete from guru where nik= ?";
        try{
            ps=kon.prepareStatement(qry);
            ps.setString(1,nik);
            ps.executeUpdate();
            System.out.println("Data Guru Terhapus");
        }
        catch (SQLException e){
            System.out.println("Error Data Tidak Ditemukan: "+e);
        }
    }
    public void deleteData() {
        String nis = JOptionPane.showInputDialog("Masukan Nik Yang Ingin Dihapus");
        hapusData(nis);
    }
    public void tambahGuru(String nik, String nama, String jenkel, String tmptlahir, Date tgllahir, String alamat,
                           String telpon,String user){
        String qry = "insert into guru (nik,nama,jenkel,tmptlahir,tgllahir," +
                "alamat,telpon,user) values (?,?,?,?,?,?,?,?)";
        try{
            ps= kon.prepareStatement(qry);
            ps.setString(1,nik);
            ps.setString(2,nama);
            ps.setString(3,jenkel);
            ps.setString(4,tmptlahir);
            ps.setDate(5,tgllahir);
            ps.setString(6,alamat);
            ps.setString(7,telpon);
            ps.setString(8,user);
            ps.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Error : "+e);
        }
    }
    public void inputData(){
        String nik = JOptionPane.showInputDialog("NIK");
        String nama = JOptionPane.showInputDialog("NAMA");
        String jenkel = JOptionPane.showInputDialog("JENIS KELAMIN (L/P)");
        String tmptlahir = JOptionPane.showInputDialog("TEMPAT LAHIR");
        String tgllahir = JOptionPane.showInputDialog("TANGGAL LAHIR (TAHUN-BULAN-HARI)");
        Date tgl = Date.valueOf(tgllahir);
        String alamat = JOptionPane.showInputDialog("ALAMAT");
        String telpon = JOptionPane.showInputDialog("TELP");
        String user = JOptionPane.showInputDialog("USER");
        tambahGuru(nik,nama,jenkel,tmptlahir,tgl,alamat,telpon,user);
    }
    public guru cariguru(String nik) {
        guru = new guru();
        try {
            ps = kon.prepareStatement("select * from guru where nik = ?");
            ps.setString(1, nik);
            rs.beforeFirst();//kursor diarahkan ke sebelum data pertama
            if (rs.next()) {//jika ditemukan data
                guru.setNik(rs.getString("nik"));
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return guru;
    }
    public void updateGuru(String nikLama, String nikBaru, String nama, String jenkel, String tmplahir,
                           Date tglLahir, String alamat, String telepon, String user) {
        String qry = "UPDATE guru SET nik = ?, nama = ?, jenkel = ?, tmplahir = ?, tglLahir = ?, " +
                "alamat = ?, telepon = ?, user = ?  WHERE nik = ?";
        try {
            ps = kon.prepareStatement(qry);
            ps.setString(1, nikBaru);  // NIS baru
            ps.setString(2, nama);
            ps.setString(3, jenkel);
            ps.setString(4, tmplahir);
            ps.setDate(5, tglLahir);
            ps.setString(6, alamat);
            ps.setString(7, telepon);
            ps.setString(8, user);
            ps.setString(9, nikLama);  // NIS lama sebagai kondisi WHERE

            ps.executeUpdate();
            System.out.println("Data Guru dengan NIK: " + nikLama + " telah diupdate ke NIK baru: " + nikBaru);

        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
    }
    public void updateData() {
        String nikLama = JOptionPane.showInputDialog("Masukkan NIK Guru yang ingin diupdate datanya");
        guru dataguru = cariguru(nikLama);
        if (dataguru.getNik() == null) {
            System.out.println("Tidak ada siswa dengan NIK: " + nikLama);
            return;
        }

        String nikBaru = JOptionPane.showInputDialog("Masukkan NIK baru (Sebelumnya: " + dataguru.getNik() + "):");
        String nama = JOptionPane.showInputDialog("Masukkan nama baru (Sebelumnya: " + dataguru.getNama() + "):");
        String jenkel = JOptionPane.showInputDialog("Jenis kelamin baru L/P (Sebelumnya: " + dataguru.getJenkel() + "):");
        String tmpLahir = JOptionPane.showInputDialog("Tempat lahir baru (Sebelumnya: " + dataguru.getTempLahir() + "):");
        String tglLahir = JOptionPane.showInputDialog("Tanggal lahir baru (YYYY-MM-DD) (Sebelumnya: " + dataguru.getTglLahir() + "):");
        Date tgl = Date.valueOf(tglLahir);
        String alamat = JOptionPane.showInputDialog("Alamat baru (Sebelumnya: " + dataguru.getAlamat() + "):");
        String telp = JOptionPane.showInputDialog("Nomor telepon baru (Sebelumnya: " + dataguru.getTelp() + "):");
        String user = JOptionPane.showInputDialog("Nama User Baru (Sebelumnya: "+ dataguru.getUser() + "):");

        updateGuru(nikLama, nikBaru, nama, jenkel, tmpLahir, tgl, alamat, telp,user);
    }

    public static void main(String[]args){
        guruDAO gr =new guruDAO();
        while (true) {
            String pilihan = JOptionPane.showInputDialog(
                    "PIlih operasi: \n" + "1.Tambah Guru\n" + "2.Tampilkan semua data Guru\n" +
                            "3.Update Data Guru\n" + "4.Delete Data Guru\n" + "5.Exit\n"
            );
            switch (pilihan) {
                case "1":
                    gr.inputData();
                    break;
                case "2":
                    gr.tampilkanData();
                    break;
                case "3":
                    gr.updateData();
                    break;
                case "4":
                    gr.hapusData(JOptionPane.showInputDialog("Masukan Nis Yang Ingin Dihapus"));
                    break;
                case "5":
                    System.exit(0);
                    break;
            }
        }
    }
}
