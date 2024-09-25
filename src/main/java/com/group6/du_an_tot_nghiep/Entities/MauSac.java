package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "mau_sac")
public class MauSac {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Vui lòng nhập tên màu sắc")
    @Column(name = "ten_mau_sac")
    private String tenMauSac;

    @Column(name = "ma_mau_sac")
    private String maMauSac;

    @Column(name = "nguoi_tao")
    private String nguoiTao;

    @Column(name = "ngay_tao")
    private Timestamp ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = new Timestamp(System.currentTimeMillis());
    }

    @Column(name = "nguoi_sua")
    private String nguoiSua;

    @UpdateTimestamp
    @Column(name = "ngay_sua")
    private Timestamp ngaySua;

    @Column(name = "trang_thai")
    private int trangThai;

}
