package model;

import java.util.Date;
public class guru {
    private String nik;
    private String nama;
    private String tempLahir;
    private Date tglLahir;
    private String alamat;
    private String jenkel;
    private String telp;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempLahir() {
        return tempLahir;
    }

    public void setTempLahir(String tempLahir) {
        this.tempLahir = tempLahir;
    }

    public Date getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(Date tglLahir) {
        try { //exception
            this.tglLahir = tglLahir;
        }
        catch (Exception e){
            System.out.println("Error: "+e);
            this.tglLahir = null;
        }
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenkel() {
        return jenkel;
    }

    public void setJenkel(String jenkel) {
        this.jenkel = jenkel;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
