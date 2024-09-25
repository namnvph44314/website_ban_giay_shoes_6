package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.HinhAnh;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import com.group6.du_an_tot_nghiep.Entities.SanPham;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

@Repository
public interface HinhAnhDao extends JpaRepository<HinhAnh,Integer> {
    public static final int ACTIVE = 1;
    public static final int DISABLED = 0;

    @Query("SELECT h.url FROM HinhAnh h WHERE h.sanPhamChiTietByIdSpct.id = ?1 AND h.anhMacDinh = true")
    String findUrlAnhMacDinh (int idSpct);

    @Query("SELECT h.id FROM HinhAnh h order by h.id desc limit 1")
    Integer getLastestid ();

    @Query("SELECT ha.url from HinhAnh ha where ha.sanPhamChiTietByIdSpct.id = :id")
    List<String> getAllHinhAnhByIDSPCT(@Param("id") Integer spctId);

    @Transactional
    @Modifying
    @Query("delete from HinhAnh ha where ha.sanPhamChiTietByIdSpct.id = :id")
    Integer deleteImage(@Param("id") Integer productId);

    @Query("SELECT h FROM HinhAnh h where h.sanPhamChiTietByIdSpct.id = ?1")
    List<HinhAnh> findAllByIdSpct (int id);

    @Query("SELECT h.url FROM HinhAnh h where h.id = ?1")
    String findUrlById (int id);

    @Query("select ha from HinhAnh ha where ha.anhMacDinh = true \n" +
            "and ha.sanPhamChiTietByIdSpct in (select spct from SanPhamChiTiet spct where spct.sanPhamByIdSanPham = ?1 and spct.mauSacByIdMauSac = ?2)\n" +
            " order by ha.ngayTao desc limit 1")
    HinhAnh findAllAnhMacDinhSp (SanPham sanPham, MauSac mauSac);
}
