package com.group6.du_an_tot_nghiep.Dao;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface HoaDonChiTietDao extends JpaRepository<HoaDonChiTiet, Integer> {

    HoaDonChiTiet findByIdHoaDon_Id(Integer idHoaDon);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.ThongTinDonHang (hd.maHoaDon, hd.email, hd.hoVaTen, hd.diaChi, hd.loaiDonHang, hd.soDienThoaiNhan, hd.trangThai, tk.id, hd.idTinh, hd.idHuyen, hd.idXa, hd.ghiChu, hd.phuongthucthanhtoan) FROM HoaDon hd JOIN hd.idTaiKhoan tk WHERE hd.id = :idHoaDon" )
    ThongTinDonHang getThongTinDonHang(@Param("idHoaDon") Integer idHoaDon);
    HoaDonChiTiet findByIdHoaDon_Id(int idHoaDon);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.LichSuHoaDonResponse(hd.ngayHoanThanh,hd.tienKhachTra,hd.phuongthucthanhtoan, hd.ghiChu, tk.hoVaTen) FROM HoaDon hd JOIN hd.idTaiKhoan tk where hd.id = :idHoaDon")
    List<LichSuHoaDonResponse> getLichSuThanhToan(@Param("idHoaDon") Integer idHoaDon);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.TrangThaiHoaDonRepose(tt.trangThai, tt.ngayTao, tk.hoVaTen, tt.ghiChu, tt.nguoiTao) FROM HoaDon hd JOIN hd.trangThaiHoaDonList tt left Join hd.idTaiKhoan tk where hd.id = :idHoaDon order by tt.ngayTao desc limit 4")
    List<TrangThaiHoaDonRepose> getTrangThaiHoaDon(@Param("idHoaDon") Integer idHoaDon);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.TrangThaiHoaDonRepose(tt.trangThai, tt.ngayTao, tk.hoVaTen, tt.ghiChu, tt.nguoiTao) FROM HoaDon hd JOIN hd.trangThaiHoaDonList tt left Join hd.idTaiKhoan tk where hd.id = :idHoaDon order by tt.ngayTao desc ")
    List<TrangThaiHoaDonRepose> getAllTrangThaiHoaDon(@Param("idHoaDon") Integer idHoaDon);

    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.TrangThaiHoaDonRepose(tt.trangThai, tt.ngayTao, tk.hoVaTen, tt.ghiChu, tt.nguoiTao) " +
            "FROM HoaDon hd " +
            "JOIN hd.trangThaiHoaDonList tt " +
            "LEFT JOIN hd.idTaiKhoan tk " +
            "WHERE tk = :taiKhoan " +
            "ORDER BY tt.ngayTao ASC")
    List<TrangThaiHoaDonRepose> getTrangThaiHoaDonKh(@Param("taiKhoan") TaiKhoan taiKhoan);


    @Query("SELECT hdct from HoaDonChiTiet hdct where hdct.idHoaDon.id = :idHoaDon")
    Page<HoaDonChiTiet> findByIdHoaDon(@Param("idHoaDon") Integer idHoaDon, Pageable pageable);

    @Query("SELECT hdct from HoaDonChiTiet hdct where hdct.idHoaDon.id = :idHoaDon")
    List<HoaDonChiTiet> getAllHdctById(@Param("idHoaDon") Integer idHoaDon);

    @Query("SELECT hdct from HoaDonChiTiet hdct where hdct.sanPhamChiTietByIdSpct.id = :productDetailId and hdct.idHoaDon.id = :billId")
    Optional<HoaDonChiTiet> findByIdSPCT(@Param("productDetailId") Integer idSPCT, @Param("billId") Integer billId);

    @Query("SELECT sum(hdct.gia * hdct.soLuongSanPham) from HoaDonChiTiet hdct where hdct.idHoaDon.id = :billId")
    Integer getTongTien(@Param("billId") Integer billId);

    @Transactional
    @Modifying
    @Query("DELETE from HoaDonChiTiet h where h.idHoaDon.id = :idHoaDon and h.sanPhamChiTietByIdSpct.id = :idSP")
    Integer deleteProduct(@Param("idHoaDon") Integer billId, @Param("idSP") Integer productId);

//    @Query("SELECT sp, hdct FROM SanPhamChiTiet sp " +
//            "join HoaDonChiTiet  hdct on hdct.sanPhamChiTietByIdSpct.id = sp.id join" +
//            " HoaDon hd on hd.id = hdct.idHoaDon.id where hd.id =:idHoaDon")
//    List<SanPhamChiTiet> findSanPhamByIdHoaDon(@Param("idHoaDon") Integer idHoaDon);

    @Query("select sp from SanPhamChiTiet sp where  sp .trangThai=1")
    Page<SanPhamChiTiet> getAllSpct(Pageable pageable);

    @Query("select spct from SanPhamChiTiet spct join spct.sanPhamByIdSanPham sp where spct.trangThai = 1 and (sp.tenSanPham like %?1% or ?1 is null)")
    Page<SanPhamChiTiet> getAllSpct(String tenSanPham, Pageable pageable);

@Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.SanPhamChiTietDTO(sp, hdct.soLuongSanPham, hdct.gia, hdct.id) " +
        "FROM SanPhamChiTiet sp " +
        "JOIN HoaDonChiTiet hdct ON hdct.sanPhamChiTietByIdSpct.id = sp.id " +
        "JOIN HoaDon hd ON hd.id = hdct.idHoaDon.id " +
        "WHERE hd.id = :idHoaDon")
    List<SanPhamChiTietDTO> findSanPhamByIdHoaDon(@Param("idHoaDon") Integer idHoaDon);


    @Query("select anh from HinhAnh anh where anh.anhMacDinh = true AND anh.sanPhamChiTietByIdSpct.id = :idSanPhamCt ")
    List<HinhAnh> getAllAnhByIdSp(@Param("idSanPhamCt")Integer idSanPhamCt);

//    @Query("SELECT new com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.MoneyReponse(hd.tienGiamGia, hd.tienGiaoHang, hd.tongTien, hd.tienSauGiam) FROM HoaDon hd where hd.id = :idHoaDon")
//    MoneyReponse getAllMoneyByIdHoaDon(@Param("idHoaDon") Integer idHoaDon);

//    HoaDon getAllMoneyByIdHoaDon(@Param("idHoaDon") Integer idHoaDon);

    Optional<HoaDonChiTiet> findByIdHoaDonAndSanPhamChiTietByIdSpct(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet);

    List<HoaDonChiTiet> findAllByIdHoaDon(HoaDon hoaDon);

    @Query("select hdct from HoaDonChiTiet hdct where hdct.idHoaDon.id = :idHoaDon")
    List<HoaDonChiTiet> findAllByIdHoaDon(@Param("idHoaDon") Integer idHoaDon);

    @Query("select spct.soLuong from SanPhamChiTiet spct where spct.id = :idSpct")
    Integer getSoLuongByIdSPCT(@Param("idSpct") Integer idSPCT);

    @Transactional
    @Modifying
    @Query("UPDATE SanPhamChiTiet spct set spct.soLuong = :soLuong where spct.id = :idSPCT")
    Integer updateSoLuongSP(@Param("idSPCT") Integer idSPCT, @Param("soLuong") Integer soLuong);

    @Transactional
    @Modifying
    @Query("UPDATE PhieuGiamGia pgg set pgg.soLuong = (pgg.soLuong - 1) where pgg.id = :idPGG")
    Integer updateSoLuongPGG(@Param("idPGG") Integer idPGG);


    @Query("SELECT hd.idPhieuGiamGia from HoaDon hd where hd.id = :id")
    PhieuGiamGia getIdPGGByIdHD(@Param("id") Integer idHD);

    @Transactional
    @Modifying
    @Query("UPDATE HoaDonChiTiet hdct set hdct.soLuongSanPham = (hdct.soLuongSanPham + :amountProduct) where hdct.idHoaDon.id = :billId and hdct.sanPhamChiTietByIdSpct.id = :productId")
    Integer updateAmountProduct(@Param("billId") Integer billId, @Param("amountProduct") Integer amountProduct, @Param("productId") Integer productId);

    @Query("select hdct.soLuongSanPham from HoaDonChiTiet hdct where hdct.idHoaDon.id = :billId and hdct.sanPhamChiTietByIdSpct.id = :productId")
    Integer getSoLuongSPTrongGio(@Param("billId") Integer billId, @Param("productId") Integer productId);

    @Transactional
    @Modifying
    @Query("update HoaDonChiTiet hdct set hdct.soLuongSanPham = :soLuong where hdct.idHoaDon.id = :billId and hdct.sanPhamChiTietByIdSpct.id = :productId")
    Integer changeAmountProduct(@Param("billId") Integer billId, @Param("productId") Integer productId, @Param("soLuong") Integer amount);

    @Transactional
    @Modifying
    @Query("update HoaDonChiTiet hdct set hdct.trangThai = 5 where hdct.idHoaDon.id = :billId")
    Integer updateStatusBillDetail(@Param("billId") Integer billId);

    @Transactional
    @Modifying
    @Query("update HoaDonChiTiet hdct set hdct.trangThai = 1 where hdct.idHoaDon.id = :billId")
    Integer updateStatusBillDetailShip(@Param("billId") Integer billId);
}
