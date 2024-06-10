package com.pab.android_client;


public class Users {
    private int id;
    private String name;
    private String email;
    private String nim;
    private String kelas;

    // Konstruktor lengkap
    public Users(int id, String name, String email, String nim, String kelas) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nim = nim;
        this.kelas = kelas;
    }

    // Konstruktor untuk menambahkan user baru (tanpa ID)
    public Users(String name, String email, String nim, String kelas) {
        this.name = name;
        this.email = email;
        this.nim = nim;
        this.kelas = kelas;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
}
