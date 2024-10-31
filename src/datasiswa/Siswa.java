package datasiswa;

import java.util.Date;

public class Siswa {
    private String nis;
    private String nama;
    private String tempLahir;
    private Date tglLahir;
    private String alamat;
    private String jenkel;
    private String telp;
    private String ibuKandung;
    private String ayahKandung;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
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

    public String getIbuKandung() {
        return ibuKandung;
    }

    public void setIbuKandung(String ibuKandung) {
        this.ibuKandung = ibuKandung;
    }

    public String getAyahKandung() {
        return ayahKandung;
    }

    public void setAyahKandung(String ayahKandung) {
        this.ayahKandung = ayahKandung;
    }
}
