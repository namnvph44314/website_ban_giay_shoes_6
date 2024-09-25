package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "kich_thuoc")
public class KichThuoc {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Vui lòng nhập tên kích thước")
    @Column(name = "ten_kich_thuoc")
    private Float tenKichThuoc;

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
