package com.group6.du_an_tot_nghiep.Service.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.*;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThanhToanService {
    @Autowired
    LichSuHoaDonDao lichSuHoaDonDao;

    @Autowired
    GioHangDao gioHangDao;

    @Autowired
    GioHangChiTietDao gioHangChiTietDao;

    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;

    @Autowired
    PhieuGiamGiaDao phieuGiamGiaDao;

    @Autowired
    PhieuGiamGiaKhachHangDao phieuGiamGiaKhachHangDao;

    @Autowired
    DiaChiDao diaChiDao;

    @Autowired
    HoaDonDao hoaDonDao;

    @Autowired
    HoaDonChiTietDao hoaDonChiTietDao;

    public ThongTinThanhToan getThongTinThanhToan (GioHang gioHang) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietDao.fingGhctByGioHangAndTrangThaiSpct(gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
        List<ListGioHangChiTietResponse> responseList = new ArrayList<>();
        gioHangChiTietList.stream().forEach(gioHangChiTiet -> {
            if (gioHangChiTiet.getSelected()==true){
                ListGioHangChiTietResponse response = new ListGioHangChiTietResponse();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(gioHangChiTiet.getSanPhamChiTietByIdSpct().getId()).orElse(null);
                BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
                if (giaGiam == null) {
                    response.setGiaGiam(sanPhamChiTiet.getGia());
                }else {
                    response.setGiaGiam(sanPhamChiTiet.getGia().subtract(giaGiam));
                }
                response.setIdSpct(sanPhamChiTiet.getId());
                response.setKichThuoc(sanPhamChiTiet.getKichThuocByIdKichCo());
                response.setMauSac(sanPhamChiTiet.getMauSacByIdMauSac());
                response.setSoLuongMua(gioHangChiTiet.getSoLuong());
                response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
                response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
                response.setSelected(gioHangChiTiet.getSelected());
                response.setIdGhct(gioHangChiTiet.getId());
                responseList.add(response);
            }
        });

        List<BigDecimal> listThanhTien = new ArrayList<>();
        PhieuGiamGia phieuGiamGia = gioHang.getPhieuGiamGiaByIdPgg();
        BigDecimal tongTienTruocGiam = new BigDecimal(0);
        BigDecimal tienGiam = new BigDecimal(0);
        BigDecimal tongTienSauGiam = new BigDecimal(0);
        String maPgg;
        if (phieuGiamGia == null){
            maPgg = null;
        }else{
            maPgg = phieuGiamGia.getMaKhuyenMai();
        }


        responseList.stream().forEach(response -> {
            listThanhTien.add(response.getGiaGiam().multiply(BigDecimal.valueOf(response.getSoLuongMua())));
        });
        for (BigDecimal thanhTien: listThanhTien) {
            tongTienTruocGiam = tongTienTruocGiam.add(thanhTien) ;
        }

        ThongTinThanhToan thongTinThanhToan = new ThongTinThanhToan();
        if (gioHang.getPhuongThucThanhToan() == null) {
            thongTinThanhToan.setPhuongThucThanhToan(-1);
        }else {
            thongTinThanhToan.setPhuongThucThanhToan(gioHang.getPhuongThucThanhToan());
        }
        if (gioHang.getTienGiaoHang() == null) {
            thongTinThanhToan.setTienGiaoHang(new BigDecimal(0));
        }else {
            thongTinThanhToan.setTienGiaoHang(gioHang.getTienGiaoHang());
        }
        if (maPgg == null){
            thongTinThanhToan.setTienGiam(tienGiam);
            thongTinThanhToan.setMaPgg(null);
            thongTinThanhToan.setIdPgg(-1);
            thongTinThanhToan.setTongTienSauGiam(tongTienTruocGiam);
            thongTinThanhToan.setTongTienTruocGiam(tongTienTruocGiam);
        }else {
            if (phieuGiamGia.getKieuGiaTri()== PhieuGiamGiaDao.TIEN){
                tienGiam = phieuGiamGia.getGiaTri();
            }else{
                tienGiam = tongTienTruocGiam.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienGiam) == -1) {
                    tienGiam = phieuGiamGia.getGiaTriToiDa();
                }
            }
            tongTienSauGiam = tongTienTruocGiam.subtract(tienGiam);
            thongTinThanhToan.setTienGiam(tienGiam);
            thongTinThanhToan.setMaPgg(maPgg);
            thongTinThanhToan.setIdPgg(phieuGiamGia.getId());
            thongTinThanhToan.setTongTienSauGiam(tongTienSauGiam);
            thongTinThanhToan.setTongTienTruocGiam(tongTienTruocGiam);
        }
        return thongTinThanhToan;
    }

    public List<ListGioHangChiTietResponse> getListGioHangChiTiet (GioHang gioHang) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietDao.findGhctByGioHangAndTrangThaiSpctForThongTinGioHang(gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
        List<ListGioHangChiTietResponse> responseList = new ArrayList<>();
        gioHangChiTietList.stream().forEach(gioHangChiTiet -> {
            ListGioHangChiTietResponse response = new ListGioHangChiTietResponse();
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(gioHangChiTiet.getSanPhamChiTietByIdSpct().getId()).orElse(null);
            BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
            if (giaGiam == null) {
                response.setGiaGiam(sanPhamChiTiet.getGia());
            }else {
                response.setGiaGiam(sanPhamChiTiet.getGia().subtract(giaGiam));
            }
            response.setIdSpct(sanPhamChiTiet.getId());
            response.setKichThuoc(sanPhamChiTiet.getKichThuocByIdKichCo());
            response.setMauSac(sanPhamChiTiet.getMauSacByIdMauSac());
            response.setSoLuongMua(gioHangChiTiet.getSoLuong());
            response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
            response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
            response.setSelected(gioHangChiTiet.getSelected());
            response.setIdGhct(gioHangChiTiet.getId());
            responseList.add(response);
        });
        return responseList.stream().sorted(Comparator.comparingInt(ListGioHangChiTietResponse::getIdGhct)).collect(Collectors.toList());
    }

    public ThongTinKhachHang getThongTinKhachHang (TaiKhoan taiKhoan) {
        DiaChi diaChi = diaChiDao.findMacDinhByTaiKhoan(taiKhoan);

        System.out.println("============================");
        ThongTinKhachHang thongTinKhachHang = new ThongTinKhachHang();
        thongTinKhachHang.setGhiChu(gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).get().getGhiChu());
        if (diaChi != null) {
            System.out.println(diaChi.getId());
            thongTinKhachHang.setIdDiaChi(diaChi.getId());
            thongTinKhachHang.setHoVaTen(diaChi.getHoVaTen());
            thongTinKhachHang.setSoDienThoai(diaChi.getSoDienThoai());
            thongTinKhachHang.setIdTinh(diaChi.getIdTinh());
            thongTinKhachHang.setIdHuyen(diaChi.getIdHuyen());
            thongTinKhachHang.setIdXa(diaChi.getIdXa());
            thongTinKhachHang.setDiaChiCuThe(diaChi.getDiaChiCuThe());
        }
        return thongTinKhachHang;
    }

    public ThanhToanHoaDon getThanhToanHoaDon (GioHang gioHang, TaiKhoan taiKhoan) {
        return new ThanhToanHoaDon(getThongTinThanhToan(gioHang),getListGioHangChiTiet(gioHang),getThongTinKhachHang(taiKhoan));
    }

    public List<ListDiaChiResponse> getListDiaChi (TaiKhoan taiKhoan) {
        List<DiaChi> diaChiList = diaChiDao.findByTaiKhoanByIdTaiKhoan(taiKhoan);
        List<ListDiaChiResponse> listDiaChiResponses = new ArrayList<>();
        diaChiList.stream().forEach(diaChi -> {
            ListDiaChiResponse response = new ListDiaChiResponse();
            response.setId(diaChi.getId());
            response.setHoVaTen(diaChi.getHoVaTen());
            response.setSoDienThoai(diaChi.getSoDienThoai());
            response.setIdTinh(diaChi.getIdTinh());
            response.setIdHuyen(diaChi.getIdHuyen());
            response.setIdXa(diaChi.getIdXa());
            response.setDiaChiCuThe(diaChi.getDiaChiCuThe());
            response.setTenTinh("");
            response.setTenHuyen("");
            response.setTenXa("");
            listDiaChiResponses.add(response);
        });
        return listDiaChiResponses;
    }

    public Boolean updateDiaChiMacDinh (int idDiaChi, TaiKhoan taiKhoan) {
        try {
            diaChiDao.updateDiaChiMacDinh(taiKhoan);
            DiaChi diaChi = diaChiDao.findById(idDiaChi).orElse(null);
            diaChi.setMacDinh(true);
            diaChiDao.save(diaChi);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePhiShip (BigDecimal phiShip, TaiKhoan taiKhoan) {
        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
        if (gioHang == null) {
            return false;
        }else{
            gioHang.setTienGiaoHang(phiShip);
            gioHangDao.save(gioHang);
            return true;
        }
    }

    public boolean updateGhiChu (String ghiChu, TaiKhoan taiKhoan) {
        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
        if (gioHang == null) {
            return false;
        }else{
            gioHang.setGhiChu(ghiChu);
            gioHangDao.save(gioHang);
            return true;
        }
    }

    public boolean updateHinhThucThanhToan(int hinhThuc, TaiKhoan taiKhoan) {
        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
        if (gioHang == null) {
            return false;
        }else{
            gioHang.setPhuongThucThanhToan(hinhThuc);
            gioHangDao.save(gioHang);
            return true;
        }
    }

//    public ListGioHangChiTietResponse checkSoLuongSp(List<ListGioHangChiTietResponse> list, TaiKhoan taiKhoan) {
//        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
//        for (ListGioHangChiTietResponse response:list) {
//            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(response.getIdSpct()).orElse(null);
//            GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findBySanPhamChiTietByIdSpctAndGioHangByIdGioHangAndTrangThai(sanPhamChiTiet,gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
//            if (sanPhamChiTiet.getSoLuong() < 1 || gioHangChiTiet.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
////                gioHangChiTiet.setTrangThai(0);
////                gioHangChiTietDao.save(gioHangChiTiet);
//                return response;
//            }
//        }
//        return null;
//    }

    public ListGioHangChiTietResponse checkSoLuongSp(List<ListGioHangChiTietResponse> list, TaiKhoan taiKhoan) {
        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
        for (ListGioHangChiTietResponse response:list) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findByIdAndTrangThaiDangBan(response.getIdSpct());
            GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findByIdSpctAndGioHangAndTrangThai(response.getIdSpct(),gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
            if (sanPhamChiTiet == null) {
                gioHangChiTiet.setTrangThai(0);
                gioHangChiTietDao.save(gioHangChiTiet);
                response.setTenSanPham("sanPhamNayCanXoa");
                return response;
            }else {
                if (sanPhamChiTiet.getSoLuong() < 1 || gioHangChiTiet.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
                    return response;
                }
            }
        }
        return null;
    }

    public Integer checkPgg(int idPgg, TaiKhoan taiKhoan) {
        try {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findById(idPgg).orElse(null);
            GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
            if (phieuGiamGia.getTrangThai() == PhieuGiamGiaDao.NGUNG_HOAT_DONG || phieuGiamGia.getSoLuong() < 1 || phieuGiamGia.getNgayKetThuc().before(new Timestamp(System.currentTimeMillis()))){
                gioHang.setPhieuGiamGiaByIdPgg(null);
                gioHangDao.save(gioHang);
                return phieuGiamGia.getId();
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    public boolean luuDiaChi(ThongTinKhachHang thongTinKhachHang, TaiKhoan taiKhoan) {
        try {
            DiaChi diaChi2 = diaChiDao.findMacDinhByTaiKhoan(taiKhoan);
            DiaChi diaChi = new DiaChi();
            diaChi.setEmail(null);
            diaChi.setIdTinh(thongTinKhachHang.getIdTinh());
            diaChi.setIdHuyen(thongTinKhachHang.getIdHuyen());
            diaChi.setIdXa(thongTinKhachHang.getIdXa());
            diaChi.setSoDienThoai(thongTinKhachHang.getSoDienThoai());
            diaChi.setDiaChiCuThe(thongTinKhachHang.getDiaChiCuThe());
            diaChi.setLoai(1);
            diaChi.setTaiKhoanByIdTaiKhoan(taiKhoan);
            diaChi.setHoVaTen(thongTinKhachHang.getHoVaTen());
            diaChi.setNguoiTao(String.valueOf(taiKhoan.getId()));
            if (diaChi2 == null) {
                diaChi.setMacDinh(true);
            }else{
                diaChi.setMacDinh(false);
            }
            diaChiDao.save(diaChi);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

//    public boolean datHang(ThanhToanHoaDon thanhToanHoaDon, TaiKhoan taiKhoan) {
//        try {
//            GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
//            gioHang.setPhieuGiamGiaByIdPgg(null);
//            gioHang.setPhuongThucThanhToan(null);
//            gioHang.setTienGiaoHang(null);
//            gioHang.setGhiChu(null);
//            HoaDon hoaDon = new HoaDon();
//            PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findById(thanhToanHoaDon.getThongTinThanhToan().getIdPgg()).orElse(null);
//            if (phieuGiamGia == null) {
//                hoaDon.setIdPhieuGiamGia(null);
//            }else {
//                phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
//                if (phieuGiamGia.getKieu() == 0) {
//                    PhieuGiamGiaKhachHang phieuGiamGiaKhachHang = phieuGiamGiaKhachHangDao.findByIdPhieuGiamGia(phieuGiamGia);
//                    phieuGiamGiaKhachHang.setTrangThai((byte) PhieuGiamGiaKhachHangDao.DA_DUNG);
//                    phieuGiamGiaKhachHangDao.save(phieuGiamGiaKhachHang);
//                }
//                hoaDon.setIdPhieuGiamGia(phieuGiamGia);
//                phieuGiamGiaDao.save(phieuGiamGia);
//            }
//
//            hoaDon.setIdTaiKhoan(taiKhoan);
//
//            int maSo;
//            maSo  = Integer.parseInt(hoaDonDao.getLastestMaSo().substring(2)) + 1;
//            hoaDon.setMaHoaDon("HD" + maSo);
//            hoaDon.setEmail(taiKhoan.getEmail());
//            hoaDon.setHoVaTen(thanhToanHoaDon.getThongTinKhachHang().getHoVaTen());
//            hoaDon.setGhiChu(thanhToanHoaDon.getThongTinKhachHang().getGhiChu());
//            hoaDon.setDiaChi(thanhToanHoaDon.getThongTinKhachHang().getDiaChiCuThe());
//            hoaDon.setTienKhachTra(thanhToanHoaDon.getThongTinThanhToan().getTongTienSauGiam());
//            hoaDon.setTienSauGiam(thanhToanHoaDon.getThongTinThanhToan().getTongTienSauGiam());
//            hoaDon.setTienGiamGia(thanhToanHoaDon.getThongTinThanhToan().getTongTienTruocGiam().subtract(thanhToanHoaDon.getThongTinThanhToan().getTongTienSauGiam()));
//            hoaDon.setTienGiaoHang(thanhToanHoaDon.getThongTinThanhToan().getTienGiaoHang());
//            hoaDon.setLoaiDonHang(2);
//            hoaDon.setSoDienThoaiNhan(thanhToanHoaDon.getThongTinKhachHang().getSoDienThoai());
//            hoaDon.setTongTien(thanhToanHoaDon.getThongTinThanhToan().getTongTienTruocGiam());
//            hoaDon.setNgayNhanHangDuKien(null);
//            hoaDon.setTrangThai(1);
//            hoaDon.setPhuongthucthanhtoan(thanhToanHoaDon.getThongTinThanhToan().getPhuongThucThanhToan());
//            hoaDon.setNguoiTao(String.valueOf(taiKhoan.getId()));
//            hoaDonDao.save(hoaDon);
//
//            gioHangDao.save(gioHang);
//            thanhToanHoaDon.getListSanPham().stream().forEach(sp -> {
//                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
//                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(sp.getIdSpct()).orElse(null);
//                GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findBySanPhamChiTietByIdSpctAndGioHangByIdGioHangAndTrangThai(sanPhamChiTiet,gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
//                gioHangChiTiet.setTrangThai(GioHangChiTietDao.DA_THANH_TOAN);
//                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - sp.getSoLuongMua());
//                hoaDonChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
//                hoaDonChiTiet.setIdHoaDon(hoaDon);
//                hoaDonChiTiet.setSoLuongSanPham(sp.getSoLuongMua());
//                hoaDonChiTiet.setGia(sp.getGiaGiam());
//                hoaDonChiTiet.setTrangThai(1);
//                hoaDonChiTiet.setNguoiTao(String.valueOf(taiKhoan.getId()));
//                hoaDonChiTietDao.save(hoaDonChiTiet);
//                sanPhamChiTietDao.save(sanPhamChiTiet);
//                gioHangChiTietDao.save(gioHangChiTiet);
//            });
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//
//    }

    public boolean datHang(ThanhToanHoaDon thanhToanHoaDon, TaiKhoan taiKhoan) {
        try {
            GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
            gioHang.setPhieuGiamGiaByIdPgg(null);
            gioHang.setPhuongThucThanhToan(null);
            gioHang.setTienGiaoHang(null);
            gioHang.setGhiChu(null);
            HoaDon hoaDon = new HoaDon();
            PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findById(thanhToanHoaDon.getThongTinThanhToan().getIdPgg()).orElse(null);
            if (phieuGiamGia == null) {
                hoaDon.setIdPhieuGiamGia(null);
            }else {
                hoaDon.setIdPhieuGiamGia(phieuGiamGia);
            }
            hoaDon.setIdTaiKhoan(taiKhoan);
//            int maSo;
//            maSo  = Integer.parseInt(hoaDonDao.getLastestMaSo().substring(2)) + 1;
//            hoaDon.setMaHoaDon("HD" + maSo);
            hoaDon.setMaHoaDon("HD01");
            hoaDon.setEmail(taiKhoan.getEmail());
            hoaDon.setHoVaTen(thanhToanHoaDon.getThongTinKhachHang().getHoVaTen());
            hoaDon.setGhiChu(thanhToanHoaDon.getThongTinKhachHang().getGhiChu());
            hoaDon.setDiaChi(thanhToanHoaDon.getThongTinKhachHang().getDiaChiCuThe());
            if (thanhToanHoaDon.getThongTinThanhToan().getPhuongThucThanhToan() == GioHangDao.COD) {
                hoaDon.setTienKhachTra(null);
            }else {
                hoaDon.setTienKhachTra(thanhToanHoaDon.getThongTinThanhToan().getTongTienSauGiam().add(thanhToanHoaDon.getThongTinThanhToan().getTienGiaoHang()));
            }
            hoaDon.setTienSauGiam(thanhToanHoaDon.getThongTinThanhToan().getTongTienSauGiam());
            hoaDon.setTienGiamGia(thanhToanHoaDon.getThongTinThanhToan().getTienGiam());
            hoaDon.setTienGiaoHang(thanhToanHoaDon.getThongTinThanhToan().getTienGiaoHang());
            hoaDon.setLoaiDonHang(2);
            hoaDon.setSoDienThoaiNhan(thanhToanHoaDon.getThongTinKhachHang().getSoDienThoai());
            hoaDon.setTongTien(thanhToanHoaDon.getThongTinThanhToan().getTongTienSauGiam().add(thanhToanHoaDon.getThongTinThanhToan().getTienGiaoHang()));
            hoaDon.setNgayNhanHangDuKien(null);
            hoaDon.setTrangThai(1);
            hoaDon.setPhuongthucthanhtoan(thanhToanHoaDon.getThongTinThanhToan().getPhuongThucThanhToan());
            hoaDon.setNguoiTao("online");
            hoaDon.setIdTinh(thanhToanHoaDon.getThongTinKhachHang().getIdTinh());
            hoaDon.setIdHuyen(thanhToanHoaDon.getThongTinKhachHang().getIdHuyen());
            hoaDon.setIdXa(thanhToanHoaDon.getThongTinKhachHang().getIdXa());
            HoaDon hoaDonSaved = hoaDonDao.save(hoaDon);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setTaiKhoanByIdTaiKhoan(taiKhoan);
            lichSuHoaDon.setHoaDonByIdHoaDon(hoaDonSaved);
            lichSuHoaDon.setTrangThai(hoaDonSaved.getTrangThai());
            lichSuHoaDon.setGhiChu("chờ xác nhận");
            lichSuHoaDon.setNguoiTao(String.valueOf(taiKhoan.getHoVaTen()));
            lichSuHoaDonDao.save(lichSuHoaDon);
            gioHangDao.save(gioHang);
            thanhToanHoaDon.getListSanPham().stream().forEach(sp -> {
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(sp.getIdSpct()).orElse(null);
                GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findBySanPhamChiTietByIdSpctAndGioHangByIdGioHangAndTrangThai(sanPhamChiTiet,gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
                gioHangChiTiet.setTrangThai(GioHangChiTietDao.DA_THANH_TOAN);
                hoaDonChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
                hoaDonChiTiet.setIdHoaDon(hoaDon);
                hoaDonChiTiet.setSoLuongSanPham(sp.getSoLuongMua());
                hoaDonChiTiet.setGia(sp.getGiaGiam());
                hoaDonChiTiet.setTrangThai(1);
                hoaDonChiTiet.setNguoiTao(String.valueOf(taiKhoan.getId()));
                hoaDonChiTietDao.save(hoaDonChiTiet);
                gioHangChiTietDao.save(gioHangChiTiet);
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
