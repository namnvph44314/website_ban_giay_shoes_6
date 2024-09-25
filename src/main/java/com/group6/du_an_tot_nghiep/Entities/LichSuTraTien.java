package com.group6.du_an_tot_nghiep.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lich_su_tra_tien")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LichSuTraTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_giao_dich")
    private String maGiaoDich;

    @Column(name = "phuong_thuc")
    private Integer phuongThuc;

    @Column(name = "so_tien")
    private Integer soTien;

    @Column(name = "id_hoa_don")
    private Integer idHoaDon;
}

