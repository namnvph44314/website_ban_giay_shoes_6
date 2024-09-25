package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "the_loai")
public class TheLoai {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Vui lòng nhập tên thể loại")
    @Column(name = "ten_the_loai")
    private String tenTheLoai;

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

