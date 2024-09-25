package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamResponse;
import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import com.group6.du_an_tot_nghiep.Entities.MauSac;
import com.group6.du_an_tot_nghiep.Entities.SanPham;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamChiTietDao extends JpaRepository<SanPhamChiTiet,Integer>, JpaSpecificationExecutor<SanPhamChiTiet> {
    public static final int DANG_BAN = 1;
    public static final int NGUNG_BAN = 0;
    public static final int DA_XOA = 2;

    @Query("select spct from SanPhamChiTiet spct where spct.id = ?1 and spct.trangThai = 1")
    SanPhamChiTiet findByIdAndTrangThaiDangBan (int idSpct);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.ma = ?1 AND (spct.trangThai =1 OR spct.trangThai =0)")
    Page<SanPhamChiTiet>  findByIdSanPhamChiTiet (String ma, Pageable pageable);

    @Query("SELECT SUM(spct.soLuong) FROM SanPhamChiTiet spct WHERE spct.sanPhamByIdSanPham.id = ?1")
    Optional<Integer> getSoLuongByIdSp (int idSp);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanPhamByIdSanPham.id = ?1 AND (spct.trangThai =1 OR spct.trangThai =0)")
    Page<SanPhamChiTiet> findAllByIdSanPham (int id, Pageable pageable);

    List<SanPhamChiTiet> findAllBySanPhamByIdSanPham (SanPham sanPham);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.gia between ?1 and ?2 and spct.chatLieuByIdChatLieu.id in ?3 and spct.deGiayByIdDeGiay.id in ?4 and spct.mauSacByIdMauSac.id in ?5 and spct.theLoaiByIdDanhMuc.id in ?6 and spct.thuongHieuByIdThuongHieu.id in ?7 and spct.trangThai in ?8 and spct.sanPhamByIdSanPham.id = ?9 and spct.kichThuocByIdKichCo.id in ?10 AND (spct.trangThai =1 OR spct.trangThai =0)")
    Page<SanPhamChiTiet> filter (BigDecimal start,
                                 BigDecimal end,
                                 List<Integer> idChatLieu,
                                 List<Integer> idDeGiay,
                                 List<Integer> idMauSac,
                                 List<Integer> idDanhMuc,
                                 List<Integer> idThuongHieu,
                                 List<Integer> trangThai,
                                 int idSp,
                                 List<Integer> idKichCo,
                                 Pageable pageable);

    @Query("SELECT distinct  spct.sanPhamByIdSanPham.id FROM SanPhamChiTiet spct WHERE spct.gia between ?1 and ?2 and spct.chatLieuByIdChatLieu.id in ?3 and spct.deGiayByIdDeGiay.id in ?4 and spct.mauSacByIdMauSac.id in ?5 and spct.theLoaiByIdDanhMuc.id in ?6 and spct.thuongHieuByIdThuongHieu.id in ?7 and spct.kichThuocByIdKichCo.id in ?8 and spct.trangThai = 1")
    List<Integer> findDistinctIdSpForSideBarFilter (BigDecimal start,
                                 BigDecimal end,
                                 List<Integer> idChatLieu,
                                 List<Integer> idDeGiay,
                                 List<Integer> idMauSac,
                                 List<Integer> idDanhMuc,
                                 List<Integer> idThuongHieu,
                                 List<Integer> idKichCo);

    @Query("SELECT spct.ma FROM SanPhamChiTiet spct order by spct.ngayTao desc limit 1")
    String getLastestMaSo ();

    @Query("select spct from SanPhamChiTiet spct where spct.sanPhamByIdSanPham.id in :ids")
    public Page<SanPhamChiTiet> getPageSpctByIdSp(@Param("ids") List<Integer> ids, Pageable pageable);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanPhamByIdSanPham.id in :ids and" +
            "(:mauSac IS NULL OR spct.mauSacByIdMauSac.id = :mauSac) AND " +
            "(:chatLieu IS NULL OR spct.chatLieuByIdChatLieu.id = :chatLieu) AND " +
            "(:kichCo IS NULL OR spct.kichThuocByIdKichCo.id = :kichCo) AND " +
            "(:deGiay IS NULL OR spct.deGiayByIdDeGiay.id = :deGiay) AND " +
            "(:thuongHieu IS NULL OR spct.thuongHieuByIdThuongHieu.id = :thuongHieu)"
    )
    public Page<SanPhamChiTiet> loc(@Param("ids") List<Integer> ids,@Param("mauSac") Integer mauSac, @Param("chatLieu") Integer chatLieu, @Param("kichCo") Integer kichCo, @Param("deGiay") Integer deGiay, @Param("thuongHieu") Integer thuongHieu, Pageable pageable);




    @Query("select spct from SanPhamChiTiet spct where spct.sanPhamByIdSanPham.id = ?1 and spct.mauSacByIdMauSac.id = ?2 and spct.trangThai = 1  order by spct.ngayTao desc limit 1")
    SanPhamChiTiet findAllSpDistinctMauSac (int idSp, int idMauSac);

    @Query("SELECT distinct spct.mauSacByIdMauSac.id FROM SanPhamChiTiet spct where spct.sanPhamByIdSanPham.id = ?1 and spct.trangThai=1")
    List<Integer> findDistinctIdMauSac (int idSp);

//    @Query("SELECT distinct spct.mauSacByIdMauSac.id FROM SanPhamChiTiet spct where spct.sanPhamByIdSanPham.id = ?1 and spct.mauSacByIdMauSac.id in ?2 and spct.trangThai=1")
//    List<Integer> findDistinctIdMauSacSideBarFilter (int idSp, List<Integer> idMauSac);

    @Query("SELECT distinct spct.mauSacByIdMauSac.id FROM SanPhamChiTiet spct where spct.gia between ?1 and ?2 and spct.chatLieuByIdChatLieu.id in ?3 and spct.deGiayByIdDeGiay.id in ?4 and spct.mauSacByIdMauSac.id in ?5 and spct.theLoaiByIdDanhMuc.id in ?6 and spct.thuongHieuByIdThuongHieu.id in ?7 and spct.kichThuocByIdKichCo.id in ?8 and spct.trangThai = 1 and spct.sanPhamByIdSanPham.id = ?9")
    List<Integer> findDistinctIdMauSacSideBarFilter (BigDecimal start,
                                                     BigDecimal end,
                                                     List<Integer> idChatLieu,
                                                     List<Integer> idDeGiay,
                                                     List<Integer> idMauSac,
                                                     List<Integer> idDanhMuc,
                                                     List<Integer> idThuongHieu,
                                                     List<Integer> idKichCo,
                                                     int idSp);

    @Query("SELECT distinct scpt.sanPhamByIdSanPham.id FROM SanPhamChiTiet scpt WHERE scpt.sanPhamByIdSanPham.tenSanPham like %?1% and scpt.trangThai = 1")
    List<Integer>  findDistinctIdMauSacSearch (String ten);

    @Query("select distinct spct.mauSacByIdMauSac from SanPhamChiTiet spct where spct.trangThai =1 and spct.sanPhamByIdSanPham = (select spct2.sanPhamByIdSanPham from SanPhamChiTiet spct2 where spct2.id = ?1)")
    List<MauSac> findAllMauSacSanPhamKhachHang (int idSpct);

    @Query("select spct from SanPhamChiTiet spct where spct.mauSacByIdMauSac.id = ?1 and spct.sanPhamByIdSanPham = (select spct2.sanPhamByIdSanPham from SanPhamChiTiet spct2 where spct2.id = ?2) and spct.kichThuocByIdKichCo.id = ?3 order by spct.ngayTao desc limit 1")
    SanPhamChiTiet findSanPhamByMauSacAndKichThuoc (int idMauSac, int idSpct, int idKichThuoc);

    @Query("select spct from SanPhamChiTiet spct where spct.trangThai = 1 and spct.mauSacByIdMauSac.id = ?1 and spct.sanPhamByIdSanPham = (select spct2.sanPhamByIdSanPham from SanPhamChiTiet spct2 where spct2.id = ?2) order by spct.ngayTao desc limit 1")
    SanPhamChiTiet findSanPhamByMauSac (int idMauSac, int idSpct);

    @Query("SELECT distinct spct.sanPhamByIdSanPham.id FROM SanPhamChiTiet spct where spct.theLoaiByIdDanhMuc.id = ?1 and spct.sanPhamByIdSanPham.id != ?2")
    List<Integer> findIdSanPhamLienQuan (int idDanhMuc, int idSpct);

    @Query("SELECT spct.sanPhamByIdSanPham.id FROM SanPhamChiTiet spct where spct.id = ?1")
    Integer findIdSanPhamByIdspct (int idSpct);

    @Query("SELECT spct.sanPhamByIdSanPham.tenSanPham FROM SanPhamChiTiet spct where spct.id = ?1")
    String findTenSanPhamByIdspct (int idSpct);

    @Query("SELECT distinct spct.mauSacByIdMauSac.id FROM SanPhamChiTiet spct where spct.sanPhamByIdSanPham.id = ?1 and spct.theLoaiByIdDanhMuc.id = ?2 and spct.trangThai=1")
    List<Integer> findDistinctIdMauSacForSanPhamLienQuan (int idSp, int idDanhMuc);

    @Query("SELECT spct.kichThuocByIdKichCo FROM SanPhamChiTiet spct where spct.mauSacByIdMauSac.id = ?1 and spct.trangThai = 1 and spct.sanPhamByIdSanPham = (select spct2.sanPhamByIdSanPham from SanPhamChiTiet spct2 where spct2.id = ?2)")
    List<KichThuoc> getSizesByIdspctAndIdMau (int idMauSac, int idSpct);

    @Query("SELECT spct FROM SanPhamChiTiet spct where spct.mauSacByIdMauSac.id = ?1 and spct.sanPhamByIdSanPham = (select spct2.sanPhamByIdSanPham from SanPhamChiTiet spct2 where spct2.id = ?2) and spct.kichThuocByIdKichCo.id = ?3")
    SanPhamChiTiet findSpctToAddGioHang (int idMauSac, int idSpct, int idKichCo);

    @Query("SELECT spct from SanPhamChiTiet spct where spct.trangThai = :trangThai")
    Page<SanPhamChiTiet> findAllByTrangThai(@Param("trangThai") int trangThai, Pageable pageable);

    @Query("SELECT spct.id from SanPhamChiTiet spct where spct.sanPhamByIdSanPham.id = :productId")
    List<Integer> getAllIdSPCT(@Param("productId") Integer productId);

    @Transactional
    @Modifying
    @Query("UPDATE SanPhamChiTiet spct set spct.chatLieuByIdChatLieu.id = :chatLieu, spct.deGiayByIdDeGiay.id = :deGiay, spct.kichThuocByIdKichCo.id = :kichCo, spct.mauSacByIdMauSac.id = :mauSac, spct.theLoaiByIdDanhMuc.id = :theLoai, spct.thuongHieuByIdThuongHieu.id = :thuongHieu, spct.canNang = :canNang, spct.soLuong = :soLuong, spct.gia = :donGia, spct.moTa = :mota where spct.id = :productId")
    Integer updateDetailProduct(@Param("productId") Integer productId, @Param("chatLieu") Integer chatLieu, @Param("deGiay") Integer deGiay, @Param("kichCo") Integer kichCo, @Param("mauSac") Integer mauSac, @Param("theLoai") Integer theLoai, @Param("thuongHieu") Integer thuongHieu, @Param("canNang") Integer canNang, @Param("soLuong") Integer soLuong, @Param("donGia") Integer donGia, @Param("mota") String moTa);

    @Query("SELECT spct.soLuong from SanPhamChiTiet spct where spct.id = :idSPCT")
    Integer getSoLuong(@Param("idSPCT") Integer idSPCT);

    @Query("SELECT spct from SanPhamChiTiet spct where spct.sanPhamByIdSanPham.tenSanPham = :name or spct.sanPhamByIdSanPham.tenSanPham LIKE %:name%")
    Page<SanPhamChiTiet> findByName(@Param("name") String name, Pageable pageable);


}



