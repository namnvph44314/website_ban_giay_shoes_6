package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SanPhamRequest {
    private Integer id;

    private String ten;

    private int danhMuc;

    private int thuongHieu;

    private int deGiay;

    private int chatLieu;

    private String moTa;

    private List<Integer> mauSac;

    private int idMauSac;

    private List<Integer> kichCo;

    private int idKichCo;

    private double canNang;

    private int soLuong;

    private List<String> images;

    private BigDecimal gia;

    public SanPhamRequest() {
    }

    public SanPhamRequest(String ten, int danhMuc, int thuongHieu, int deGiay, int chatLieu, String moTa, int idMauSac, int idKichCo) {
        this.ten = ten;
        this.danhMuc = danhMuc;
        this.thuongHieu = thuongHieu;
        this.deGiay = deGiay;
        this.chatLieu = chatLieu;
        this.moTa = moTa;
        this.idMauSac = idMauSac;
        this.idKichCo = idKichCo;
    }
}
