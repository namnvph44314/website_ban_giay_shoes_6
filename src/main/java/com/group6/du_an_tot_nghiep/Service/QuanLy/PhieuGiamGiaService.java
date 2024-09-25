package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaDao;
import com.group6.du_an_tot_nghiep.Dao.PhieuGiamGiaKhachHangDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaFilter;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaUpdate;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.VoucherResponse;
import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGiaKhachHang;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PhieuGiamGiaService {
    @Autowired
    PhieuGiamGiaDao phieuGiamGiaDao;
    @Autowired
    PhieuGiamGiaKhachHangDao phieuGiamGiaKhachHangDao;

    @Autowired
    private HoaDonDao hoaDonDao;

    public Page<PhieuGiamGia> getAll(int page) {
        Pageable p = PageRequest.of(page, 10);
        Page<PhieuGiamGia> pageData = phieuGiamGiaDao.hienThi(p);
        return pageData;
    }

    public Page<PhieuGiamGia> loc(PhieuGiamGiaFilter phieuGiamGiaFilter, int page) {
        Pageable p = PageRequest.of(page, 10);
        if (phieuGiamGiaFilter.getNgayBatDau() != null && phieuGiamGiaFilter.getNgayKetThuc() != null) {
            Date sqlDateNBD = Date.valueOf(phieuGiamGiaFilter.getNgayBatDau());
            Date sqlDateNKT = Date.valueOf(phieuGiamGiaFilter.getNgayKetThuc());
            Timestamp ngayBatDau = new Timestamp(sqlDateNBD.getTime());
            Timestamp ngayKetThuc = new Timestamp(sqlDateNKT.getTime());
            System.out.println(ngayBatDau);
            System.out.println(ngayKetThuc);
            Page<PhieuGiamGia> pageData = phieuGiamGiaDao.loc(ngayBatDau, ngayKetThuc, phieuGiamGiaFilter.getKieu(), phieuGiamGiaFilter.getKieuGiaTri(), phieuGiamGiaFilter.getTrangThai(), p);
            return pageData;
        }
        Page<PhieuGiamGia> pageData = phieuGiamGiaDao.loc(null, null, phieuGiamGiaFilter.getKieu(), phieuGiamGiaFilter.getKieuGiaTri(), phieuGiamGiaFilter.getTrangThai(), p);
        return pageData;
    }

    public PhieuGiamGia search(String ma) {
        return phieuGiamGiaDao.search(ma);
    }

    public PhieuGiamGiaRequest add(PhieuGiamGiaRequest pggRequest, String nguoiTao) {
        // Lấy thời gian hiện tại
//        LocalDateTime ngayTao = LocalDateTime.now();
        Instant now = Instant.now();
        Timestamp ngayTao = Timestamp.from(now);

        // Lấy thời gian hiện tại dưới dạng LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();

        // Chuyển đổi LocalDateTime sang ZonedDateTime
        ZonedDateTime zonedDateTimeNBD = pggRequest.getNgayBatDau().atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeNKT = pggRequest.getNgayKetThuc().atZone(ZoneId.systemDefault());
        // Chuyển đổi ZonedDateTime sang Instant
        Instant instantNBD = zonedDateTimeNBD.toInstant();
        Instant instantNKT = zonedDateTimeNKT.toInstant();
        // Chuyển đổi Instant sang Timestamp
        Timestamp ngayBatDau = Timestamp.from(instantNBD);
        Timestamp ngayKetThuc = Timestamp.from(instantNKT);

        PhieuGiamGia pgg = new PhieuGiamGia();
        pgg.setMaKhuyenMai(pggRequest.getMaKhuyenMai());
        pgg.setTen(pggRequest.getTen());
        pgg.setGiaTriToiDa(pggRequest.getGiaTriToiDa());
        pgg.setGiaTriNhoNhat(pggRequest.getGiaTriNhoNhat());
        pgg.setSoLuong(pggRequest.getSoLuong());
        if (ngayTao.before(ngayBatDau)) {
            pgg.setTrangThai((byte) phieuGiamGiaDao.CHUA_DIEN_RA);
        } else if (ngayKetThuc.before(ngayTao)) {
            pgg.setTrangThai((byte) phieuGiamGiaDao.NGUNG_HOAT_DONG);
        } else {
            pgg.setTrangThai((byte) phieuGiamGiaDao.DANG_DIEN_RA);
        }
        pgg.setKieu(pggRequest.getKieu());
        pgg.setKieuGiaTri(pggRequest.getKieuGiaTri());
        pgg.setGiaTri(pggRequest.getGiaTri());
        pgg.setNgayBatDau(ngayBatDau);
        pgg.setNgayKetThuc(ngayKetThuc);
        pgg.setNguoiTao(nguoiTao);
        pgg.setNgayTao(ngayTao);
        pgg.setNguoiSua(null);
        pgg.setNgaySua(null);
        phieuGiamGiaDao.save(pgg);
        return pggRequest;
    }

    public PhieuGiamGia getOnePgg() {
        return phieuGiamGiaDao.findTopByOrderByIdDesc();
    }

    public Optional<PhieuGiamGia> getOneById(int id) {
        return phieuGiamGiaDao.findById(id);
    }

    public void updateTTNHD(Timestamp ngayHienTai) {
        phieuGiamGiaDao.updateTTNHD(ngayHienTai);
    }

    public void updateTTDDR(Timestamp ngayHienTai) {
        phieuGiamGiaDao.updateTTDDR(ngayHienTai);
    }

    public PhieuGiamGiaUpdate detail(int id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findById(id).get();
        PhieuGiamGiaUpdate phieuGiamGiaUpdate = new PhieuGiamGiaUpdate();
        phieuGiamGiaUpdate.setMaKhuyenMai(phieuGiamGia.getMaKhuyenMai());
        phieuGiamGiaUpdate.setTen(phieuGiamGia.getTen());
        phieuGiamGiaUpdate.setGiaTriToiDa(phieuGiamGia.getGiaTriToiDa());
        phieuGiamGiaUpdate.setGiaTriNhoNhat(phieuGiamGia.getGiaTriNhoNhat());
        phieuGiamGiaUpdate.setSoLuong(phieuGiamGia.getSoLuong());
        phieuGiamGiaUpdate.setKieu(phieuGiamGia.getKieu());
        phieuGiamGiaUpdate.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
        phieuGiamGiaUpdate.setGiaTri(phieuGiamGia.getGiaTri());
        phieuGiamGiaUpdate.setNgayBatDau(phieuGiamGia.getNgayBatDau().toLocalDateTime());
        phieuGiamGiaUpdate.setNgayKetThuc(phieuGiamGia.getNgayKetThuc().toLocalDateTime());
        return phieuGiamGiaUpdate;
    }

    public PhieuGiamGiaRequest update(String maPgg, PhieuGiamGiaRequest phieuGiamGiaRequest, String nguoiSua) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findByMaKhuyenMai(maPgg);

        if (phieuGiamGia.getKieu() == 0 && phieuGiamGiaRequest.getKieu() == 1) {
            phieuGiamGiaKhachHangDao.deleteByPgg(phieuGiamGia.getMaKhuyenMai());
        }

        Instant now = Instant.now();
        Timestamp ngaySua = Timestamp.from(now);

        // Lấy thời gian hiện tại dưới dạng LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();

        ZonedDateTime zonedDateTimeNBD = phieuGiamGiaRequest.getNgayBatDau().atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeNKT = phieuGiamGiaRequest.getNgayKetThuc().atZone(ZoneId.systemDefault());
        // Chuyển đổi ZonedDateTime sang Instant
        Instant instantNBD = zonedDateTimeNBD.toInstant();
        Instant instantNKT = zonedDateTimeNKT.toInstant();
        // Chuyển đổi Instant sang Timestamp
        Timestamp ngayBatDau = Timestamp.from(instantNBD);
        Timestamp ngayKetThuc = Timestamp.from(instantNKT);

        phieuGiamGia.setTen(phieuGiamGiaRequest.getTen());
        phieuGiamGia.setGiaTriToiDa(phieuGiamGiaRequest.getGiaTriToiDa());
        phieuGiamGia.setGiaTriNhoNhat(phieuGiamGiaRequest.getGiaTriNhoNhat());
        phieuGiamGia.setSoLuong(phieuGiamGiaRequest.getSoLuong());
        phieuGiamGia.setKieu(phieuGiamGiaRequest.getKieu());
        phieuGiamGia.setKieuGiaTri(phieuGiamGiaRequest.getKieuGiaTri());
        phieuGiamGia.setGiaTri(phieuGiamGiaRequest.getGiaTri());
        phieuGiamGia.setNgayBatDau(ngayBatDau);
        phieuGiamGia.setNgayKetThuc(ngayKetThuc);
        if (ngaySua.before(ngayBatDau)) {
            phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.CHUA_DIEN_RA);
        } else if (ngayKetThuc.before(ngaySua)) {
            phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.NGUNG_HOAT_DONG);
        } else {
            phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.DANG_DIEN_RA);
        }
        phieuGiamGia.setNguoiSua(nguoiSua);
        phieuGiamGia.setNgaySua(ngaySua);

        phieuGiamGiaDao.save(phieuGiamGia);

        return phieuGiamGiaRequest;
    }

    public PhieuGiamGia getOneByMa(String ma) {
        return phieuGiamGiaDao.findByMaKhuyenMai(ma);
    }

    public void updatePggNhd(int idPgg) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaDao.findById(idPgg).get();
        if (phieuGiamGia.getTrangThai() != 4) {
            phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.DA_DUNG);
        } else {
            if (phieuGiamGia.getNgayTao().before(phieuGiamGia.getNgayBatDau())) {
                phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.CHUA_DIEN_RA);
            } else if (phieuGiamGia.getNgayKetThuc().before(phieuGiamGia.getNgayTao())) {
                phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.NGUNG_HOAT_DONG);
            } else {
                phieuGiamGia.setTrangThai((byte) phieuGiamGiaDao.DANG_DIEN_RA);
            }
        }

        phieuGiamGiaDao.save(phieuGiamGia);
    }

    public ListPggGioHang getTopVoucher(Integer billId, BigDecimal tongTien, Optional<Integer> idTaiKhoan) {
        try {
                List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
                List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaDao.findPggCongKhai(tongTien, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
                if (idTaiKhoan.isPresent()){
                    phieuGiamGiaList.addAll(phieuGiamGiaDao.findPggCaNhan(tongTien, idTaiKhoan.get(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
                }
                phieuGiamGiaList.stream().forEach(phieuGiamGia -> {
                    ListPggGioHang pggGioHang = new ListPggGioHang();
                    if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                        pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTri());
                    } else {
                        BigDecimal tienDuocGiamTheoDon = tongTien.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                        if (phieuGiamGia.getGiaTriToiDa().compareTo(tienDuocGiamTheoDon) == -1) {
                            pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTriToiDa());
                        } else {
                            pggGioHang.setTienDuocGiamTheoDon(tienDuocGiamTheoDon);
                        }
                    }
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

                listPggGioHangs.sort(new Comparator<ListPggGioHang>() {
                    @Override
                    public int compare(ListPggGioHang o1, ListPggGioHang o2) {
                        return o2.getTienDuocGiamTheoDon().compareTo(o1.getTienDuocGiamTheoDon());
                    }
                });
                hoaDonDao.updatePhieuGiamGia(billId, listPggGioHangs.get(0).getIdPgg());
                return listPggGioHangs.get(0);
        } catch (Exception exception) {
            log.error("[ERROR] getTopVoucher {} " + exception.getMessage());
            return new ListPggGioHang();
        }
    }


    public List<PhieuGiamGia> getAllPgg() {
        return phieuGiamGiaDao.getAllPhieuGiamGia();
    }


}
