package DAO;

import java.sql.*;
import java.util.ArrayList;
import datasiswa.Siswa;

import javax.swing.*;

import static Koneksi.koneksi.getConnection;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public class SiswaDAO {
    Connection kon;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<Siswa> listsiswa;
    Siswa siswa;

    public SiswaDAO(){
        kon = getConnection();
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
    public Siswa carisiswa(String nis){
        siswa = new Siswa();
        try{
            ps = kon.prepareStatement("select * from siswa where nis = ?");
            ps.setString(1,nis);
            rs.beforeFirst();//kursor diarahkan ke sebelum data pertama
            if(rs.next()){//jika ditemukan data
                siswa.setNis(rs.getString("nis"));
            }
        }catch (SQLException e){
            System.out.println("Error : "+e);
        }
        return siswa;
    }
    public void tambahSiswa(String nis, String nama, String jenkel, String tmptlahir, Date tgllahir, String alamat,
                            String telpon,  String ayah, String ibu,String user){
        String qry = "insert into siswa (nis,nama,jenkel,tmptlahir,tgllahir," +
                "alamat,telpon,ayah,ibu,user) values (?,?,?,?,?,?,?,?,?,?)";
        try{
            ps= kon.prepareStatement(qry);
            ps.setString(1,nis);
            ps.setString(2,nama);
            ps.setString(3,jenkel);
            ps.setString(4,tmptlahir);
            ps.setDate(5,tgllahir);
            ps.setString(6,alamat);
            ps.setString(7,telpon);
            ps.setString(8,ayah);
            ps.setString(9,ibu);
            ps.setString(10,user);
            ps.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Error : "+e);
        }
    }
    public void inputData(){
        String nis = JOptionPane.showInputDialog("NIS");
        String nama = JOptionPane.showInputDialog("NAMA");
        String jenkel = JOptionPane.showInputDialog("JENIS KELAMIN (L/P)");
        String tmptlahir = JOptionPane.showInputDialog("TEMPAT LAHIR");
        String tgllahir = JOptionPane.showInputDialog("TANGGAL LAHIR (TAHUN-BULAN-HARI)");
        Date tgl = Date.valueOf(tgllahir);
        String alamat = JOptionPane.showInputDialog("ALAMAT");
        String telpon = JOptionPane.showInputDialog("TELP");
        String ayah = JOptionPane.showInputDialog("AYAH");
        String ibu = JOptionPane.showInputDialog("IBU");
        String user = JOptionPane.showInputDialog("USER");
        tambahSiswa(nis,nama,jenkel,tmptlahir,tgl,alamat,telpon,ayah,ibu,user);
    }
    public void tampilkanData(){
        System.out.println(getListsiswa());
        System.out.println("BIO DATA SISWA");
        for (int i=0;i<=getListsiswa().size()-1;i++) {
            System.out.println("--------------------------");
            System.out.println("Siswa Ke-"+(i+1));
            System.out.println("NIS : " + getListsiswa().get(i).getNis());
            System.out.println("Nama : " + getListsiswa().get(i).getNama());
            System.out.println("Jenkel : " + getListsiswa().get(i).getJenkel());
            System.out.println("Tempat Lahir : " + getListsiswa().get(i).getTempLahir());
            System.out.println("Tanggal Lahir : " + getListsiswa().get(i).getTglLahir());
            System.out.println("Alamat: " + getListsiswa().get(i).getAlamat());
            System.out.println("Telpon : " + getListsiswa().get(i).getTelp());
            System.out.println("Ayah : " + getListsiswa().get(i).getAyahKandung());
            System.out.println("Ibu : " + getListsiswa().get(i).getIbuKandung());
        }
    }

    public void hapusData(String nis){
        String qry = "delete from siswa where nis= ?";
       try{
           ps=kon.prepareStatement(qry);
           ps.setString(1,nis);
           ps.executeUpdate();
           System.out.println("Data Siswa Terhapus");
       }
       catch (SQLException e){
           System.out.println("Error Data Tidak Ditemukan: "+e);
       }
    }
    public void deleteData() {
        String nis = JOptionPane.showInputDialog("Masukan Nis Yang Ingin Dihapus");
        hapusData(nis);
    }

    public void updateSiswa(String nisLama, String nisBaru, String nama, String jenkel, String tmplahir,
                            Date tglLahir, String alamat, String telepon, String ayah, String ibu, String user) {
        String qry = "UPDATE siswa SET nis = ?, nama = ?, jenkel = ?, tmplahir = ?, tglLahir = ?, " +
                "alamat = ?, telepon = ?, ayah = ?, ibu = ?, user = ?  WHERE nis = ?";
        try {
            ps = kon.prepareStatement(qry);
            ps.setString(1, nisBaru);  // NIS baru
            ps.setString(2, nama);
            ps.setString(3, jenkel);
            ps.setString(4, tmplahir);
            ps.setDate(5, tglLahir);
            ps.setString(6, alamat);
            ps.setString(7, telepon);
            ps.setString(8, ayah);
            ps.setString(9, ibu);
            ps.setString(10, user);
            ps.setString(11, nisLama);  // NIS lama sebagai kondisi WHERE

            ps.executeUpdate();
            System.out.println("Data siswa dengan NIS: " + nisLama + " telah diupdate ke NIS baru: " + nisBaru);

        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
    }
    public void updateData() {
        String nisLama = JOptionPane.showInputDialog("Masukkan NIS siswa yang ingin diupdate datanya");
        Siswa datasiswa = carisiswa(nisLama);
        if (datasiswa.getNis() == null) {
            System.out.println("Tidak ada siswa dengan NIS: " + nisLama);
            return;
        }

        String nisBaru = JOptionPane.showInputDialog("Masukkan NIS baru (Sebelumnya: " + datasiswa.getNis() + "):");
        String nama = JOptionPane.showInputDialog("Masukkan nama baru (Sebelumnya: " + datasiswa.getNama() + "):");
        String jenkel = JOptionPane.showInputDialog("Jenis kelamin baru L/P (Sebelumnya: " + datasiswa.getJenkel() + "):");
        String tmpLahir = JOptionPane.showInputDialog("Tempat lahir baru (Sebelumnya: " + datasiswa.getTempLahir() + "):");
        String tglLahir = JOptionPane.showInputDialog("Tanggal lahir baru (YYYY-MM-DD) (Sebelumnya: " + datasiswa.getTglLahir() + "):");
        Date tgl = Date.valueOf(tglLahir);
        String alamat = JOptionPane.showInputDialog("Alamat baru (Sebelumnya: " + datasiswa.getAlamat() + "):");
        String telp = JOptionPane.showInputDialog("Nomor telepon baru (Sebelumnya: " + datasiswa.getTelp() + "):");
        String ayahKandung = JOptionPane.showInputDialog("Nama Ayah baru (Sebelumnya: " + datasiswa.getAyahKandung() + "):");
        String ibuKandung = JOptionPane.showInputDialog("Nama Ibu baru (Sebelumnya: " + datasiswa.getIbuKandung() + "):");
        String user = JOptionPane.showInputDialog("Nama User Baru (Sebelumnya: "+ datasiswa.getUser() + "):");

        updateSiswa(nisLama, nisBaru, nama, jenkel, tmpLahir, tgl, alamat, telp, ayahKandung, ibuKandung,user);
}

    public static void main(String[]args){
        SiswaDAO sis = new SiswaDAO();
        //sis.inputData();
        //sis.hapusData(JOptionPane.showInputDialog("Masukan Nis Yang Ingin Dihapus"));

       // sis.tampilkanData();
//        System.out.println("Nama : "+sis.carisiswa("54321").getNama());
        while (true) {
            String pilihan = JOptionPane.showInputDialog(
                    "PIlih operasi: \n" + "1.Tambah siswa\n" + "2.Tampilkan semua data siswa\n" +
                            "3.Update Data Siswa\n" + "4.Delete Data Siswa\n" + "5.Exit\n"
            );
            switch (pilihan) {
                case "1":
                    sis.inputData();
                    break;
                case "2":
                    sis.tampilkanData();
                    break;
                case "3":
                    sis.updateData();
                    break;
                case "4":
                    sis.hapusData(JOptionPane.showInputDialog("Masukan Nis Yang Ingin Dihapus"));
                    break;
                case "5":
                    System.exit(0);
                    break;
            }
        }
    }

}
