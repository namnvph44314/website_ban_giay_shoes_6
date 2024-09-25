package com.group6.du_an_tot_nghiep.Service.QuanLy;


import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.MessageResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.TrangThaiHoaDonRequest;
import com.group6.du_an_tot_nghiep.Entities.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class TrangThaiHoaDonService {

    @Autowired
    private HoaDonDao hoaDonDao;
    @Autowired
    private HoaDonChiTietDao hoaDonChiTietDao;
    @Autowired
    private LichSuHoaDonDao lichSuHoaDonDao;

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private HttpSession session;

    @Autowired
    private PhieuGiamGiaKhachHangDao phieuGiamGiaKhachHangDao;

    @Autowired
    PhieuGiamGiaDao phieuGiamGiaDao;


    public MessageResponse confirmOrder(Integer hoadonId, TrangThaiHoaDonRequest request) {
        TaiKhoan currentTaiKhoan = (TaiKhoan) session.getAttribute("username");
        request.setIdTaiKhoan(currentTaiKhoan.getId());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Tìm kiếm hóa đơn theo ID
        HoaDon hoaDon = hoaDonDao.findById(hoadonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        if (hoaDon.getTrangThai() == request.getNewTrangThai()) {

            return MessageResponse.builder().message("Trạng thái đã tồn tại").build();
        }

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietDao.findAllByIdHoaDon(hoaDon);

        // Tạo bản ghi lịch sử hóa đơn mới
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDonByIdHoaDon(hoaDon);
        lichSuHoaDon.setNgayTao(timestamp);
        lichSuHoaDon.setTrangThai(request.getNewTrangThai());
        lichSuHoaDon.setGhiChu(request.getGhiChu());
        lichSuHoaDon.setNguoiTao(currentTaiKhoan.getHoVaTen());
//
//        TaiKhoan taiKhoan = taiKhoanDao.findById(request.getIdTaiKhoan())
//                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        lichSuHoaDon.setTaiKhoanByIdTaiKhoan(currentTaiKhoan);


        hoaDon.setTrangThai(request.getNewTrangThai());
        hoaDon.setNgaySua(timestamp);
        hoaDonChiTietList.stream()
                .filter(hoaDonChiTiet -> hoaDonChiTiet.getTrangThai() != 7)
                .forEach(hoaDonChiTiet -> {
                    hoaDonChiTiet.setTrangThai(request.getNewTrangThai());
                    hoaDonChiTietDao.save(hoaDonChiTiet);
                });
        if (request.getNewTrangThai() == 2) {
            BigDecimal tienHang = hoaDon.getTienSauGiam().add(hoaDon.getTienGiamGia());

//            ListPggGioHang bestVoucher = hoaDonChiTietService.getBestPgg(hoadonId, tienHang, request.getIdTaiKhoan());

            PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findById(hoaDon.getIdPhieuGiamGia().getId()).get();
            // Kiểm tra loại phiếu giảm giá
            if (phieuGiamGia.getKieu() == 0) { // pgg cá nhân
                PhieuGiamGiaKhachHang pggKhachHang = phieuGiamGiaKhachHangDao.findByIdPhieuGiamGiaAndIdTaiKhoan(
                        phieuGiamGia, hoaDon.getIdTaiKhoan()).get();

                // Cập nhật trạng thái phiếu giảm giá cá nhân
                pggKhachHang.setTrangThai((byte) 1);

                phieuGiamGiaKhachHangDao.save(pggKhachHang);

            } else { // pgg công khai
                // Trừ số lượng phiếu giảm giá công khai
                phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
                phieuGiamGiaDao.save(phieuGiamGia);
            }

            // Trừ số lượng sản phẩm trong kho khi trạng thái là 2
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTietByIdSpct();
                // Kiểm tra nếu số lượng tồn kho đủ
                if (sanPhamChiTiet.getSoLuong() < hoaDonChiTiet.getSoLuongSanPham()) {
                    throw new RuntimeException("Số lượng sản phẩm không đủ trong kho cho sản phẩm: "
                            + sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
                }
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuongSanPham());
                sanPhamChiTietDao.save(sanPhamChiTiet);
            }
            hoaDon.setNgayXacNhan(timestamp);

            if (hoaDon.getPhuongthucthanhtoan() == 2) {
                BigDecimal tienKhachTra = hoaDon.getTienSauGiam().add(hoaDon.getTienGiaoHang());
                hoaDon.setTienKhachTra(tienKhachTra);
            }
        }

        if (request.getNewTrangThai() == 4) {
            hoaDon.setNgayGiaoHang(timestamp);
        }
        if (request.getNewTrangThai() == 5) {
            hoaDon.setPhuongthucthanhtoan(1); // Ví dụ: 1 có thể là mã cho "Khách thanh toán"
            hoaDon.setNgayNhan(new Timestamp(System.currentTimeMillis()));
            hoaDon.setNgayHoanThanh(new Timestamp(System.currentTimeMillis()));
//            hoaDon.setTongTien(hoaDon.getTienKhachTra().subtract(hoaDon.getTienGiamGia()).add(hoaDon.getTienGiaoHang()));
            if (hoaDon.getPhuongthucthanhtoan() == 1) {
                BigDecimal tienKhachTra = hoaDon.getTienSauGiam().add(hoaDon.getTienGiaoHang());
                hoaDon.setTienKhachTra(tienKhachTra);
            }
            hoaDon.setGhiChu(request.getGhiChu());
        }
        // Lưu hóa đơn và lịch sử hóa đơn
        hoaDonDao.save(hoaDon);
        lichSuHoaDonDao.save(lichSuHoaDon);

        return MessageResponse.builder().message("Cập nhật thành công").build();
    }

    public MessageResponse cancelOrder(Integer hoadonId, TrangThaiHoaDonRequest request) {
        TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("username");
        request.setIdTaiKhoan(taiKhoan.getId());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Kiểm tra nếu trạng thái mới không phải là 6 hoặc 7, trả về thông báo lỗi
        Integer newTrangThai = request.getNewTrangThai();
        if (newTrangThai != 6 && newTrangThai != 7) {
            return MessageResponse.builder().message("Trạng thái không hợp lệ cho hành động hủy đơn.").build();
        }

        // Tìm kiếm hóa đơn theo ID
        HoaDon hoaDon = hoaDonDao.findById(hoadonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        // Kiểm tra trạng thái hiện tại của hóa đơn
        if (hoaDon.getTrangThai() == 5) {
            return MessageResponse.builder().message("Đơn hàng đã hoàn thành, không thể hủy.").build();
        }

        // Kiểm tra nếu trạng thái hiện tại là 4 thì không cho phép hủy
        if (hoaDon.getTrangThai() == 4) {
            return MessageResponse.builder().message("Đơn hàng đang giao, không thể hủy.").build();
        }

        // Kiểm tra trạng thái hiện tại của hóa đơn
        if (hoaDon.getTrangThai() == 6 || hoaDon.getTrangThai() == 7) {
            return MessageResponse.builder().message("Đơn hàng đã hủy hoặc đã trả hàng.").build();
        }

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietDao.findAllByIdHoaDon(hoaDon);

        // Tạo bản ghi lịch sử hóa đơn mới
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDonByIdHoaDon(hoaDon);
        lichSuHoaDon.setNgayTao(timestamp);
        lichSuHoaDon.setTrangThai(newTrangThai);
        lichSuHoaDon.setGhiChu(request.getGhiChu());
        lichSuHoaDon.setNguoiTao(taiKhoan.getHoVaTen());
        lichSuHoaDon.setTaiKhoanByIdTaiKhoan(taiKhoan);

        // Cập nhật trạng thái của hóa đơn
        hoaDon.setTrangThai(newTrangThai);
//        hoaDon.setIdTaiKhoan(taiKhoan);
        hoaDon.setNgaySua(timestamp);

        // Cập nhật trạng thái của các chi tiết hóa đơn
        hoaDonChiTietList.stream()
                .filter(hoaDonChiTiet -> hoaDonChiTiet.getTrangThai() != 7)
                .forEach(hoaDonChiTiet -> {
                    hoaDonChiTiet.setTrangThai(newTrangThai);
                    hoaDonChiTietDao.save(hoaDonChiTiet);
                });



        hoaDonDao.save(hoaDon);
        lichSuHoaDonDao.save(lichSuHoaDon);

        return MessageResponse.builder().message("Hủy đơn thành công").build();
    }



    //rollbacktrangthai
    public MessageResponse rollbackOrderStatus(Integer hoadonId, TrangThaiHoaDonRequest request) {

        TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("username");
        request.setIdTaiKhoan(taiKhoan.getId());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Tìm hóa đơn theo ID
        HoaDon hoaDon = hoaDonDao.findById(hoadonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        // Tìm bản ghi lịch sử gần nhất của hóa đơn
        List<LichSuHoaDon> historyList = lichSuHoaDonDao.findAllByHoaDonByIdHoaDonOrderByNgayTaoDesc(hoaDon);
        if (historyList.size() < 2) {
            throw new RuntimeException("không thể rollback với  trạng thái là chờ xác nhận ");
        }

        // Lấy trạng thái hiện tại và trạng thái trước đó từ lịch sử
        Integer currentStatus = hoaDon.getTrangThai();
        Integer previousStatus = historyList.get(1).getTrangThai();

        // Kiểm tra trạng thái trước đó không null và không giống với trạng thái hiện tại
        if (previousStatus == null) {
            throw new RuntimeException("Trạng thái trước đó không tìm thấy.");
        }

        if (currentStatus.equals(previousStatus)) {
            throw new RuntimeException("Trạng thái hiện tại đã là trạng thái trước đó. Không cần rollback.");
        }

        if (previousStatus == 1) {
            hoaDonChiTietDao.findAllByIdHoaDon(hoaDon).forEach(hoaDonChiTiet -> {
                if (hoaDonChiTiet.getTrangThai() == 2) {
                    SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTietByIdSpct();
                    sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuongSanPham());
                    sanPhamChiTietDao.save(sanPhamChiTiet);

                }
            });
            PhieuGiamGia phieuGiamGia = hoaDon.getIdPhieuGiamGia();

            if (phieuGiamGia.getKieu() == 0) { // pgg cá nhân
                PhieuGiamGiaKhachHang pggKhachHang = phieuGiamGiaKhachHangDao.findByIdPhieuGiamGiaAndIdTaiKhoan(
                        phieuGiamGia, hoaDon.getIdTaiKhoan()).get();

                // Cập nhật trạng thái phiếu giảm giá cá nhân
                pggKhachHang.setTrangThai((byte) 0);

                phieuGiamGiaKhachHangDao.save(pggKhachHang);

            } else { // pgg công khai
                // Trừ số lượng phiếu giảm giá công khai
                phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() + 1);
                phieuGiamGiaDao.save(phieuGiamGia);
            }
        }

        hoaDon.setTrangThai(previousStatus);
        hoaDon.setNgaySua(timestamp);

        // Cập nhật trạng thái của các chi tiết hóa đơn tương ứng
        hoaDonChiTietDao.findAllByIdHoaDon(hoaDon).forEach(hoaDonChiTiet -> {
            if (hoaDonChiTiet.getTrangThai() != 7) {
                hoaDonChiTiet.setTrangThai(previousStatus);
                hoaDonChiTietDao.save(hoaDonChiTiet);
            }
        });

        // Lưu lại hóa đơn với trạng thái mới
        hoaDonDao.save(hoaDon);

        // Tạo bản ghi lịch sử mới với trạng thái trước đó và ghi chú
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDonByIdHoaDon(hoaDon);
        lichSuHoaDon.setNgayTao(timestamp);
        lichSuHoaDon.setTrangThai(previousStatus);
        lichSuHoaDon.setGhiChu(request.getGhiChu()); // Sử dụng ghi chú từ request
        lichSuHoaDon.setNguoiTao(taiKhoan.getHoVaTen()); // Thay thế giá trị này bằng tên người tạo hợp lệ
        lichSuHoaDon.setTaiKhoanByIdTaiKhoan(taiKhoan); // Lấy tài khoản từ lịch sử gần nhất
        lichSuHoaDonDao.save(lichSuHoaDon);

        return MessageResponse.builder().message("Rollback trạng thái thành công. Ghi chú: " + request.getGhiChu()).build();
    }







}
