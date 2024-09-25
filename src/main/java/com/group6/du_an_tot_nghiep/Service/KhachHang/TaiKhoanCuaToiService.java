package com.group6.du_an_tot_nghiep.Service.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.HoaDonChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Dao.LichSuHoaDonDao;
import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Entities.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaiKhoanCuaToiService {

    @Autowired
    PhieuGiamGiaDao phieuGiamGiaDao;

    @Autowired
    private HoaDonChiTietDao hoaDonChiTietDao;

    @Autowired
    HoaDonDao hoaDonDao;

    @Autowired
    LichSuHoaDonDao lichSuHoaDonDao;

    @Autowired
    private HttpSession session;


    public List<ListPggGioHang> getListPggPrivate(TaiKhoan taiKhoan) {
        List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaDao.findPggCaNhanWithoutTongTien(taiKhoan.getId(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaList.stream().forEach(phieuGiamGia -> {
            ListPggGioHang pggGioHang = new ListPggGioHang();
            pggGioHang.setIdPgg(phieuGiamGia.getId());
            pggGioHang.setMaPgg(phieuGiamGia.getMaKhuyenMai());
            pggGioHang.setGiaTriGiam(phieuGiamGia.getGiaTri());
            pggGioHang.setGiamToiDa(phieuGiamGia.getGiaTriToiDa());
            pggGioHang.setDonToiThieu(phieuGiamGia.getGiaTriNhoNhat());
            pggGioHang.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
            pggGioHang.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            pggGioHang.setSoLuong(phieuGiamGia.getSoLuong());
            listPggGioHangs.add(pggGioHang);
        });
        return listPggGioHangs;
    }

    public List<HoaDonResponse> searchKh(HoaDonFillRequest hoaDonFillRequest, TaiKhoan taiKhoan) {
        Specification<HoaDon> specification = new Specification<HoaDon>() {
            @Override
            public Predicate toPredicate(Root<HoaDon> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.equal(root.get("idTaiKhoan").get("id") ,taiKhoan.getId()));

                if (hoaDonFillRequest.getTrangThai() != null && hoaDonFillRequest.getTrangThai() != -1) {
                    predicates.add(criteriaBuilder.equal(root.get("trangThai"), hoaDonFillRequest.getTrangThai()));
                }
                query.orderBy(criteriaBuilder.desc(root.get("ngayTao")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        List<HoaDon> hoaDonList = hoaDonDao.findAll(specification);

        // Convert List<HoaDon> to List<HoaDonResponse>
        List<HoaDonResponse> hoaDonResponses = hoaDonList.stream().map(hoaDon -> {
            HoaDonResponse response = new HoaDonResponse();
            response.setId(hoaDon.getId());
            response.setMaHoaDon(hoaDon.getMaHoaDon());
            response.setHoVaTen(hoaDon.getHoVaTen());
            response.setSoDienThoaiNhan(hoaDon.getSoDienThoaiNhan());
            response.setEmail(hoaDon.getEmail());
            response.setLoaiDonHang(hoaDon.getLoaiDonHang());
            response.setDiaChi(hoaDon.getDiaChi());
            response.setNgayTao(hoaDon.getNgayTao());
            response.setTrangThai(hoaDon.getTrangThai());
            response.setTongTien(hoaDon.getTongTien());
            return response;
        }).toList();

        return hoaDonResponses;
    }

    public MessageResponse cancelOrderkh(Integer hoadonId, TrangThaiHoaDonRequest request) {
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


}
