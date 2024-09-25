package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.GiamGiaSanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGiaKhachHang;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GiamGiaSanPhamChiTietDao extends JpaRepository<GiamGiaSanPhamChiTiet,Integer> {
    public static final int DANG_HOAT_DONG=1;
    public static final int NGUNG_HOAT_DONG=0;
    @Query("select ggspct from GiamGiaSanPhamChiTiet ggspct where ggspct.sanPhamChiTietByIdSpct.id=:idSpct and ggspct.trangThai=1")
    public GiamGiaSanPhamChiTiet findBySpct(@Param("idSpct") int idSpct);

    @Query("select ggspct from  GiamGiaSanPhamChiTiet ggspct where ggspct.idGiamGia.id=:idGgsp")
    public Page<GiamGiaSanPhamChiTiet> getListGgspctByIdGgsp(@Param("idGgsp") int idPgg, Pageable pageable);

    /**
     Lấy list spct chưa add vô ggsp theo id ggsp
     */
    @Query("select spct from SanPhamChiTiet spct where spct.id not in (select spct.id from SanPhamChiTiet spct join GiamGiaSanPhamChiTiet ggspct on spct.id=ggspct.sanPhamChiTietByIdSpct.id where ggspct.idGiamGia.id = :idGg) and spct.sanPhamByIdSanPham.id in :ids")
    public Page<SanPhamChiTiet> getListSpctByIdGgspChuaAdd(@Param("ids") List<Integer> ids, @Param("idGg") int idGg, Pageable pageable);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.id not in (select spct.id from SanPhamChiTiet spct join GiamGiaSanPhamChiTiet ggspct on spct.id=ggspct.sanPhamChiTietByIdSpct.id where ggspct.idGiamGia.id = :idGg) and spct.sanPhamByIdSanPham.id in :ids and " +
            "(:mauSac IS NULL OR spct.mauSacByIdMauSac.id = :mauSac) AND " +
            "(:chatLieu IS NULL OR spct.chatLieuByIdChatLieu.id = :chatLieu) AND " +
            "(:kichCo IS NULL OR spct.kichThuocByIdKichCo.id = :kichCo) AND " +
            "(:deGiay IS NULL OR spct.deGiayByIdDeGiay.id = :deGiay) AND " +
            "(:thuongHieu IS NULL OR spct.thuongHieuByIdThuongHieu.id = :thuongHieu)"
    )
    public Page<SanPhamChiTiet> loc(@Param("idGg") int idGg,@Param("ids") List<Integer> ids,@Param("mauSac") Integer mauSac, @Param("chatLieu") Integer chatLieu, @Param("kichCo") Integer kichCo, @Param("deGiay") Integer deGiay, @Param("thuongHieu") Integer thuongHieu, Pageable pageable);

    @Query("select ggspct from GiamGiaSanPhamChiTiet ggspct where ggspct.sanPhamChiTietByIdSpct.id=:idSpct and ggspct.trangThai=:trangThai")
    public GiamGiaSanPhamChiTiet updateTtGgspct(@Param("idSpct") int idSpct,@Param("trangThai") int trangThai);

    @Query("select ggspct from GiamGiaSanPhamChiTiet ggspct where ggspct.sanPhamChiTietByIdSpct.id=:idSpct and ggspct.trangThai=:trangThai and ggspct.idGiamGia.id != :idGg")
    public GiamGiaSanPhamChiTiet updateTtGgspctDaCo(@Param("idSpct") int idSpct,@Param("trangThai") int trangThai, @Param("idGg") int idGg);

    @Modifying
    @Transactional
    @Query("update GiamGiaSanPhamChiTiet ggspct set ggspct.trangThai=:trangThai where ggspct.id=:idGgspct")
    public void updateTt(@Param("trangThai") int trangThai, @Param("idGgspct") int idGgspct);

    @Modifying
    @Transactional
    @Query("update GiamGiaSanPhamChiTiet ggspct set ggspct.trangThai=0 where ggspct.idGiamGia.trangThai=3")
    public void updateTtHetHan();

    @Modifying
    @Transactional
    @Query("update GiamGiaSanPhamChiTiet ggspct set ggspct.trangThai=0 where ggspct.idGiamGia.id=:idGiamGia")
    public void updateTtHetHanChoDoiTtNhanh(@Param("idGiamGia") int idGiamGia);

    @Query("SELECT gg FROM GiamGiaSanPhamChiTiet gg WHERE gg.sanPhamChiTietByIdSpct.id= ?1 and gg.trangThai = 1")
    GiamGiaSanPhamChiTiet findGiaGiamAndIdGiamGiaByIdScpt (int idSpct);

    @Query("SELECT gg.giaGiam FROM GiamGiaSanPhamChiTiet gg WHERE gg.sanPhamChiTietByIdSpct.id= ?1 and gg.trangThai = 1")
    BigDecimal findGiaGiamByIdSpct (int idSpct);

    @Query("select ggspct.sanPhamChiTietByIdSpct.id from GiamGiaSanPhamChiTiet ggspct where ggspct.idGiamGia.id=:idGgsp")
    public List<Integer> listIdSpctDaCo(@Param("idGgsp") int idGgsp);

    @Query("select ggspct from GiamGiaSanPhamChiTiet ggspct where ggspct.sanPhamChiTietByIdSpct.id=:idSpct and ggspct.idGiamGia.id=:idGiamGia")
    public GiamGiaSanPhamChiTiet getByIdSpctAndIdGgsp(@Param("idSpct") int idSpct, @Param("idGiamGia") int idGiamGia);

}
