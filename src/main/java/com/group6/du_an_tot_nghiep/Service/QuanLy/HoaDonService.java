package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.*;

import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.HoaDonChiTietResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.HoaDonFillRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.BillResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.DeleteProductRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.FinalCheckoutRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.HoaDonResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.ThongKe.ThongKeSanPhamBanChay;
import com.group6.du_an_tot_nghiep.Entities.*;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;


@Service
@Slf4j
public class HoaDonService {

    @Autowired
    private HoaDonDao hoaDonDao;

    @Autowired
    private SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    private PhieuGiamGiaKhachHangDao phieuGiamGiaKhachHangDao;

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    @Autowired
    private HttpSession session;

    @Autowired
    private LichSuHoaDonDao lichSuHoaDonDao;

    public Page<HoaDonResponse> search(HoaDonFillRequest hoaDonFillRequest) {
        Specification<HoaDon> specification = new Specification<HoaDon>() {
            @Override
            public Predicate toPredicate(Root<HoaDon> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (!ObjectUtils.isEmpty(hoaDonFillRequest.getSearch())) {
                    String search = "%" + hoaDonFillRequest.getSearch().trim().toUpperCase() + "%";
                    Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("hoVaTen")), search);
                    Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("soDienThoaiNhan")), search);
                    predicates.add(criteriaBuilder.or(codePredicate, namePredicate));
                }

                if (hoaDonFillRequest.getLoaiDonHang() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("loaiDonHang"), hoaDonFillRequest.getLoaiDonHang()));
                }
                if (hoaDonFillRequest.getTrangThai() != null && hoaDonFillRequest.getTrangThai() != -1) {
                    predicates.add(criteriaBuilder.equal(root.get("trangThai"), hoaDonFillRequest.getTrangThai()));
                }
                if (hoaDonFillRequest.getNgaybatdau() != null && hoaDonFillRequest.getNgayketthuc() != null) {
                    predicates.add(criteriaBuilder.between(root.get("ngayTao"), hoaDonFillRequest.getNgaybatdau(), hoaDonFillRequest.getNgayketthuc()));
                }
                if (hoaDonFillRequest.getNgaybatdau() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("ngayTao"), hoaDonFillRequest.getNgaybatdau()));
                }
                if (hoaDonFillRequest.getNgayketthuc() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("ngayTao"), hoaDonFillRequest.getNgayketthuc()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        // Determine sorting direction and property
        Sort.Direction sortDirection = Sort.Direction.DESC; // Default direction
        String sortProperty = "id"; // Default property

        if (hoaDonFillRequest.getSort() != null) {
            String[] sortParts = hoaDonFillRequest.getSort().split(",");
            if (sortParts.length > 1) {
                sortProperty = sortParts[0];
                sortDirection = Sort.Direction.fromString(sortParts[1].toUpperCase());
            } else {
                sortProperty = sortParts[0];
            }
        }

        Pageable pageable = PageRequest.of(
                hoaDonFillRequest.getPage(),
                hoaDonFillRequest.getSize(),
                Sort.by(sortDirection, sortProperty)
        );

        Page<HoaDon> hoaDonPage = hoaDonDao.findAll(specification, pageable);

        // Convert Page<HoaDon> to Page<HoaDonResponse>
        Page<HoaDonResponse> hoaDonResponses = hoaDonPage.map(hoaDon -> {
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
        });

        return hoaDonResponses;
    }

    public BigDecimal getTongTienByNgay() {
        if (hoaDonDao.getTongTienByNgay() != null) {
            return hoaDonDao.getTongTienByNgay();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienByNgayHomQua() {
        if (hoaDonDao.getTongTienByNgayHomQua() != null) {
            return hoaDonDao.getTongTienByNgayHomQua();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienByTuan() {
        if (hoaDonDao.getTongTienByTuan() != null) {
            return hoaDonDao.getTongTienByTuan();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienByTuanTruoc() {
        if (hoaDonDao.getTongTienByTuanTruoc() != null) {
            return hoaDonDao.getTongTienByTuanTruoc();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienByThang() {
        if (hoaDonDao.getTongTienByThang() != null) {
            return hoaDonDao.getTongTienByThang();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienByThangTruoc() {
        if (hoaDonDao.getTongTienByThangTruoc() != null) {
            return hoaDonDao.getTongTienByThangTruoc();
        }
        return new BigDecimal(0);
    }


    public BigDecimal getTongTienByNam() {
        if (hoaDonDao.getTongTienByNam() != null) {
            return hoaDonDao.getTongTienByNam();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienByNamTruoc() {
        if (hoaDonDao.getTongTienByNamTruoc() != null) {
            return hoaDonDao.getTongTienByNamTruoc();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTien() {
        if (hoaDonDao.getTongTien() != null) {
            return hoaDonDao.getTongTien();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getTongTienTuyChinh(String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        if (hoaDonDao.getTongTienTuyChinh(startDate, endDate) != null) {
            return hoaDonDao.getTongTienTuyChinh(startDate, endDate);
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByNgay() {
        if (hoaDonDao.getDonTCByNgay() != null) {
            return hoaDonDao.getDonTCByNgay();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByNgayHomQua() {
        if (hoaDonDao.getDonTCByNgayHomQua() != null) {
            return hoaDonDao.getDonTCByNgayHomQua();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByTuan() {
        if (hoaDonDao.getDonTCByTuan() != null) {
            return hoaDonDao.getDonTCByTuan();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByTuanTruoc() {
        if (hoaDonDao.getDonTCByTuanTruoc() != null) {
            return hoaDonDao.getDonTCByTuanTruoc();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByThang() {
        if (hoaDonDao.getDonTCByThang() != null) {
            return hoaDonDao.getDonTCByThang();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByThangTruoc() {
        if (hoaDonDao.getDonTCByThangTruoc() != null) {
            return hoaDonDao.getDonTCByThangTruoc();
        }
        return new BigDecimal(0);
    }


    public BigDecimal getDonTCByNam() {
        if (hoaDonDao.getDonTCByNam() != null) {
            return hoaDonDao.getDonTCByNam();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCByNamTruoc() {
        if (hoaDonDao.getDonTCByNamTruoc() != null) {
            return hoaDonDao.getDonTCByNamTruoc();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTC() {
        if (hoaDonDao.getDonTC() != null) {
            return hoaDonDao.getDonTC();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTCTuyChinh(String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        if (hoaDonDao.getDonTCTuyChinh(startDate, endDate) != null) {
            return hoaDonDao.getDonTCTuyChinh(startDate, endDate);
        }
        return new BigDecimal(0);
    }

    public long getDonHuyByNgay() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonHuyByNgay(today);
    }

    public BigDecimal getDonHuyByTuan() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonHuyByTuan(today);
    }

    public BigDecimal getDonChoXacNhanByTuan() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonChoXacNhanByTuan(today);
    }

    public BigDecimal getDonChoGiaoHangByTuan() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonChoGiaoHangByTuan(today);
    }

    public BigDecimal getDonHuyByThang() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonHuyByThang(today);
    }

    public BigDecimal getDonChoXacNhanByThang() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonChoXacNhanByThang(today);
    }

    public BigDecimal getDonChoGiaoHangByThang() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonChoGiaoHangByThang(today);
    }

    public BigDecimal getDonHuyByNam() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonHuyByNam(today);

    }

    public BigDecimal getDonChoXacNhanByNam() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonChoXacNhanByNam(today);

    }

    public BigDecimal getDonChoGiaoHangByNam() {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getDonChoGiaoHangByNam(today);

    }

    public BigDecimal getDonHuy() {
        if (hoaDonDao.getDonHuy() != null) {
            return hoaDonDao.getDonHuy();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonHuyTuyChinh(String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        if (hoaDonDao.getDonHuyTuyChinh(startDate, endDate) != null) {
            return hoaDonDao.getDonHuyTuyChinh(startDate, endDate);
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTraByNgay() {
        if (hoaDonDao.getDonTraByNgay() != null) {
            return hoaDonDao.getDonTraByNgay();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTraByTuan() {
        if (hoaDonDao.getDonTraByTuan() != null) {
            return hoaDonDao.getDonTraByTuan();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTraByThang() {
        if (hoaDonDao.getDonTraByThang() != null) {
            return hoaDonDao.getDonTraByThang();
        }
        return new BigDecimal(0);
    }


    public BigDecimal getDonTraByNam() {
        if (hoaDonDao.getDonTraByNam() != null) {
            return hoaDonDao.getDonTraByNam();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTra() {
        if (hoaDonDao.getDonTra() != null) {
            return hoaDonDao.getDonTra();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getDonTraTuyChinh(String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        if (hoaDonDao.getDonTraTuyChinh(startDate, endDate) != null) {
            return hoaDonDao.getDonTraTuyChinh(startDate, endDate);
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByNgay() {
        if (hoaDonDao.getSpByNgay() != null) {
            return hoaDonDao.getSpByNgay();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByNgayHomQua() {
        if (hoaDonDao.getSpByNgayHomQua() != null) {
            return hoaDonDao.getSpByNgayHomQua();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByTuan() {
        if (hoaDonDao.getSpByTuan() != null) {
            return hoaDonDao.getSpByTuan();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByTuanTruoc() {
        if (hoaDonDao.getSpByTuanTruoc() != null) {
            return hoaDonDao.getSpByTuanTruoc();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByThang() {
        if (hoaDonDao.getSpByThang() != null) {
            return hoaDonDao.getSpByThang();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByThangTruoc() {
        if (hoaDonDao.getSpByThangTruoc() != null) {
            return hoaDonDao.getSpByThangTruoc();
        }
        return new BigDecimal(0);
    }


    public BigDecimal getSpByNam() {
        if (hoaDonDao.getSpByNam() != null) {
            return hoaDonDao.getSpByNam();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpByNamTruoc() {
        if (hoaDonDao.getSpByNamTruoc() != null) {
            return hoaDonDao.getSpByNamTruoc();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSp() {
        if (hoaDonDao.getSp() != null) {
            return hoaDonDao.getSp();
        }
        return new BigDecimal(0);
    }

    public BigDecimal getSpTuyChinh(String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        if (hoaDonDao.getSpTuyChinh(startDate, endDate) != null) {
            return hoaDonDao.getSpTuyChinh(startDate, endDate);
        }
        return new BigDecimal(0);
    }

    public Integer tongHoaDonHuyChoTtChoGh(int trangThai){
        return hoaDonDao.tongHdHuyChoTtChoXn(trangThai);
    }

    public Integer getSoLuongHoaDon(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDon(trangThai);
    }
    public Integer getSoLuongHoaDonGiaoHang(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonGiaoHang(trangThai) ;
    }
    public Integer getSoLuongHoaDonXacNhan(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonXacNhan(trangThai) ;
    }

    public Integer getSoLuongHoaDonNgay(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonNgay(trangThai);
    }

    public Integer getSoLuongHoaDonHuyChoChoGiaoNgay(Integer trangThai) {
        Date today = new Date(System.currentTimeMillis());
        return hoaDonDao.getSoLuongHoaDonHuyChoChoGiaoNgay(trangThai,today);
    }

    public Integer getSoLuongHoaDonXacNhanNgay(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonXacNhanNgay(trangThai);
    }

    public Integer getSoLuongHoaDonDangGiaoNgay(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonGiaoHangNgay(trangThai);
    }

    public Integer getSoLuongHoaDonHoanThanhNgay(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonHoanThanhNgay(trangThai);
    }

    public Integer getSoLuongHoaDonTuan(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonTuan(trangThai);
    }

    public Integer getSoLuongHoaDonGiaoHangTuan(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonGiaoHangTuan(trangThai);
    }
    public Integer getSoLuongHoaDonGiaoHangThang(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonGiaoHangThang(trangThai);
    }
    public Integer getSoLuongHoaDonGiaoHangNam(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonGiaoHangNam(trangThai);
    }

    public Integer getSoLuongHoaDonXacNhanTuan(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonXacNhanTuan(trangThai);
    }

    public Integer getSoLuongHoaDonXacNhanThang(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonXacNhanThang(trangThai);
    }

    public Integer getSoLuongHoaDonXacNhanNam(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonXacNhanNam(trangThai);
    }

    public Integer getSoLuongHoaDonThang(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonThang(trangThai);
    }

    public Integer getSoLuongHoaDonNam(Integer trangThai) {
        return hoaDonDao.getSoLuongHoaDonNam(trangThai);
    }

    public Integer getSoLuongHoaDonHoanThanhTuyChinh(Integer trangThai, String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        return hoaDonDao.getSoLuongHoaDonHoanThanhTuyChinh(trangThai, startDate, endDate);
    }
    public Integer getSoLuongHoaDonGiaoHangTuyChinh(Integer trangThai, String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        return hoaDonDao.getSoLuongHoaDonGiaoHangTuyChinh(trangThai, startDate, endDate);
    }
    public Integer getSoLuongHoaDonXacNhanTuyChinh(Integer trangThai, String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        return hoaDonDao.getSoLuongHoaDonXacNhanTuyChinh(trangThai, startDate, endDate);
    }

    public Integer getSoLuongHoaDonHuyChoTtChoGhTuyChinh(Integer trangThai, String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        return hoaDonDao.getSoLuongHoaDonHuyChoTtChoGh(trangThai, startDate, endDate);
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamBanChay(int page) {
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamBanChay(p);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamBanChayTuyChinh(int page, String tuNgay, String denNgay) {
        Timestamp startDate = Timestamp.valueOf(tuNgay + " 00:00:00");
        Timestamp endDate = Timestamp.valueOf(denNgay + " 00:00:00");
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamBanChayTuyChinh(p, startDate, endDate);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }
    private List<String> character = new ArrayList<>();

    @Autowired
    private DiaChiDao diaChiDao;

    @Autowired
    private LichSuTraTienDao lichSuTraTienDao;

    @Autowired
    private HoaDonChiTietDao hoaDonChiTietDao;

    @Autowired
    private PdfService pdfService;

    public HoaDonService() {
        character = List.of("a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "q", "e", "r", "y", "t", "u", "i", "o", "p");
    }

    // public List<HoaDon> getALL(){
//      return hoaDonDao.findAll();
//  }
    public Page<HoaDonResponse> getAllHoaDon(Pageable pageable) {
        return hoaDonDao.findAll(pageable).map(hoaDon -> new HoaDonResponse(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                hoaDon.getEmail(),
                hoaDon.getHoVaTen(),
                hoaDon.getDiaChi(),
                hoaDon.getLoaiDonHang(),
                hoaDon.getSoDienThoaiNhan(),
                hoaDon.getTongTien(),
                hoaDon.getTrangThai(),
                hoaDon.getNgayTao()
        ));
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamBanChayByNgay(int page) {
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamBanChayByNgay(p);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamBanChayByTuan(int page) {
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamBanChayByTuan(p);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamBanChayByThang(int page) {
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamBanChayByThang(p);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamBanChayByNam(int page) {
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamBanChayByNam(p);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }

    public Page<ThongKeSanPhamBanChay> getPageSanPhamSapHet(Integer soLuong, int page) {
        Pageable p = PageRequest.of(page, 4);
        Page<Object[]> results = hoaDonDao.getPageSanPhamSapHet(soLuong, p);
        return results.map(record -> new ThongKeSanPhamBanChay(
                (int) ((Number) record[0]).longValue(),
                (String) record[1],
                (int) ((Number) record[2]).longValue(),
                (BigDecimal) record[3]
        ));
    }

    public List<HoaDon> getHoaDonCho() {
        try {
            List<HoaDon> hoaDons = hoaDonDao.getHoaDonCho();
            return hoaDons;
        } catch (Exception e) {
            log.error("[ERROR] getHoaDonCho {} " + e.getMessage());
            throw new RuntimeException("null");
        }
    }

    public HoaDon createEmptyBill() {
        try {
            int maSo;
            TaiKhoan taiKhoanHienTai = (TaiKhoan) session.getAttribute("username");
            maSo  = Integer.parseInt(hoaDonDao.getLastestMaSo().substring(2)) + 1;
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHoaDon("HD" + maSo);
            hoaDon.setTrangThai(10);
            hoaDon.setNgayTao(new Timestamp(System.currentTimeMillis()));
            hoaDon.setLoaiDonHang(0);
            hoaDon.setHoVaTen("Khách lẻ");
            hoaDon.setIdTinh(0);
            hoaDon.setIdHuyen(0);
            hoaDon.setIdXa("0");
            hoaDon.setNguoiTao(taiKhoanHienTai.getHoVaTen());

            HoaDon isSaved = hoaDonDao.save(hoaDon);

            if (Objects.isNull(isSaved)) {
                throw new RuntimeException("Create bill failed");
            } else {
                return isSaved;
            }
        } catch (Exception exception) {
            log.error("[ERROR] createEmptyBill {} " + exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public BillResponse getBillInfo(Integer billId) {
        try {
            HoaDon hoaDon = hoaDonDao.findById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill do not exists or have been delete"));
            BillResponse bill = new BillResponse();
            bill.setEmail(hoaDon.getEmail());
            bill.setDiaChi(hoaDon.getDiaChi());
            bill.setHoVaTen(hoaDon.getHoVaTen());
            bill.setSoDienThoai(hoaDon.getSoDienThoaiNhan());
            if (hoaDon.getIdTaiKhoan() != null) {
                bill.setIdTaiKhoan(hoaDon.getIdTaiKhoan().getId());
            }
            return bill;
        } catch (Exception e) {
            log.error("[ERROR] getBillInfo {} " + e.getMessage());
            return new BillResponse();
        }
    }

    public List<DiaChi> getAddressCustomer(Integer idCustomer) {
        try {
            List<DiaChi> address = new ArrayList<>();
            address = diaChiDao.getDiaChiByTaiKhoanByIdTaiKhoan(idCustomer);
            if (address == null){
                return new ArrayList<>();
            }
            return address;
        } catch (Exception exception) {
            log.error("[ERROR] getAddressCustomer {} " + exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public DiaChi getAddressDefault(Integer idCustomer) {
        try {
            DiaChi address = diaChiDao.getAddressDefault(idCustomer).orElse(null);
            return address;
        } catch (Exception e) {
            log.error("[ERROR] getAddressDefault {} " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public DiaChi selectAddress(Integer idAddress, Integer idBill){
        try {
            DiaChi diaChi = diaChiDao.findById(idAddress)
                    .orElseThrow(() -> new RuntimeException("Address not exists"));
            HoaDon hoaDon = hoaDonDao.findById(idBill)
                    .orElseThrow(() -> new RuntimeException("Bill not exists"));
            hoaDon.setDiaChi(diaChi.getDiaChiCuThe());
            diaChi.setMacDinh(true);

            Optional<DiaChi> defaultAddress = diaChiDao.getAddressDefault(hoaDon.getIdTaiKhoan().getId());
            if (defaultAddress.isPresent()){
                defaultAddress.get().setMacDinh(false);
                diaChiDao.save(defaultAddress.get());
            }

            diaChiDao.save(diaChi);
            hoaDonDao.save(hoaDon);
            return diaChi;
        }catch (Exception e){
            log.error("[ERROR] selectAddress {} " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public LichSuTraTien savePayHistory(LichSuTraTien data){
        try {
            LichSuTraTien lichSuTraTien = data;
            LichSuTraTien isSave = lichSuTraTienDao.save(data);
            BigDecimal tienKhachTraNew = hoaDonDao.getTienKhachTra(data.getIdHoaDon());
            hoaDonDao.addMoneyCustomerPay(data.getIdHoaDon(), tienKhachTraNew);
            return isSave;
        }catch (Exception e){
            log.error("[ERROR] savePayHistory {} " + e.getMessage());
            throw new RuntimeException("System Error");
        }
    }

    public Integer sumMoney(Integer billId){
        return lichSuTraTienDao.sumSoTienByIdHoaDon(billId);
    }

    public List<LichSuTraTien> getLichSuTraTienMat(Integer billId){
        return lichSuTraTienDao.getLichSuTraTienMat(billId);
    }

    public List<LichSuTraTien> getLichSuChuyenKhoan(Integer billId){
        return lichSuTraTienDao.getLichSuChuyenKhoan(billId);
    }

    public List<LichSuTraTien> getLichSuTraTien(Integer billId){
        return lichSuTraTienDao.getLichSuTraTien(billId);
    }

    public Integer deleteProduct(DeleteProductRequest request){
        return hoaDonChiTietDao.deleteProduct(request.getBillId(), request.getProductId());
    }

    public Integer getTienKhachTra(Integer billId){
        BigDecimal data = hoaDonDao.getTienKhachTra(billId);
        if (data == null){
            return 0;
        }else{
            return data.intValue();
        }
    }

    public Integer finalCheckout(Integer billId, FinalCheckoutRequest request){
        FinalCheckoutRequest finalReq = request;
        TaiKhoan taiKhoanHienTai = (TaiKhoan) session.getAttribute("username");
        Integer isCheckout = 0;
        if (request.getTenKhachHang().equals("") || request.getTenKhachHang() == null){
            isCheckout = hoaDonDao.finalCheckoutNotCusName(billId, request.getTienSauGiam(), request.getTienGiamGia(), request.getTienGiaoHang(), request.getSoDienThoai(), request.getTrangThai(), request.getPhuongThucThanhToan(), request.getDiaChi(), request.getTongTien(), request.getIdTinh(), request.getIdXa(), request.getIdHuyen());
        }else{

            isCheckout = hoaDonDao.finalCheckout(billId, request.getTienSauGiam(), request.getTienGiamGia(), request.getTienGiaoHang(), request.getSoDienThoai(), request.getTrangThai(), request.getPhuongThucThanhToan(), request.getDiaChi(), request.getTongTien(), request.getTenKhachHang(), request.getIdTinh(), request.getIdXa(), request.getIdHuyen());
        }

        Integer trangThaiLichSuHoaDon = 1;
        HoaDon hoaDon = hoaDonDao.findById(billId).orElse(null);
        if(request.getPhuongThucThanhToan().intValue() == 2){
            List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietDao.findAllByIdHoaDon(billId);
            for (HoaDonChiTiet hdct : hoaDonChiTiets){
                Integer soLuongHienTai = hoaDonChiTietDao.getSoLuongByIdSPCT(hdct.getSanPhamChiTietByIdSpct().getId());
                Integer soLuongSauMua = soLuongHienTai - hdct.getSoLuongSanPham();
                hoaDonChiTietDao.updateSoLuongSP(hdct.getSanPhamChiTietByIdSpct().getId(), soLuongSauMua);
            }

            hoaDon.setNgayHoanThanh(new Timestamp(System.currentTimeMillis()));
            hoaDonDao.save(hoaDon);
            hoaDonChiTietDao.updateStatusBillDetail(billId);

            PhieuGiamGia idPGG = hoaDonChiTietDao.getIdPGGByIdHD(billId);
            if (idPGG.getKieu() == 0){
                PhieuGiamGiaKhachHang phieuGiamGiaKhachHang = new PhieuGiamGiaKhachHang();
                phieuGiamGiaKhachHang.setIdPhieuGiamGia(idPGG);
                phieuGiamGiaKhachHang.setIdTaiKhoan(taiKhoanDao.findById(request.getIdKhachHang()).orElse(null));
                phieuGiamGiaKhachHang.setNgayTao(new Timestamp(System.currentTimeMillis()));
                phieuGiamGiaKhachHang.setNguoiTao(taiKhoanHienTai.getHoVaTen());
                phieuGiamGiaKhachHangDao.updateTrangThaiPGGKH(phieuGiamGiaKhachHang.getNgayTao(), phieuGiamGiaKhachHang.getNguoiTao(), idPGG.getId(), phieuGiamGiaKhachHang.getIdTaiKhoan().getId());
            }else{
                hoaDonChiTietDao.updateSoLuongPGG(idPGG.getId());
            }
            trangThaiLichSuHoaDon = 5;
        }else{
            trangThaiLichSuHoaDon = 1;
            hoaDon.setNgayXacNhan(new Timestamp(System.currentTimeMillis()));
            hoaDonDao.save(hoaDon);
            hoaDonChiTietDao.updateStatusBillDetailShip(billId);
        }

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDonByIdHoaDon(hoaDonDao.findById(billId).orElse(null));
        lichSuHoaDon.setTaiKhoanByIdTaiKhoan(taiKhoanHienTai);
        lichSuHoaDon.setTrangThai(trangThaiLichSuHoaDon);
        lichSuHoaDon.setNgayTao(new Timestamp(System.currentTimeMillis()));
        lichSuHoaDon.setNguoiTao(taiKhoanHienTai.getHoVaTen());
        lichSuHoaDon.setGhiChu("");
        lichSuHoaDonDao.save(lichSuHoaDon);


        if (request.getDiaChiNew() != null && request.getIdKhachHang() != null){
            DiaChi diaChi = request.getDiaChiNew();
            diaChiDao.save(diaChi);
        }
        return isCheckout;
    }

    public Integer getSoLuong(Integer idSPCT){
        Integer soLuong = sanPhamChiTietDao.getSoLuong(idSPCT);
        return soLuong;
    }

    public void editAmountProduct(Integer billId, Integer amountProduct){

    }

    public Integer countBillWait(){
        return hoaDonDao.countBillWait();
    }


    public Integer getSoLuongSPTrongGioHang(Integer billId, Integer productId){
        return hoaDonChiTietDao.getSoLuongSPTrongGio(billId, productId);
    }

    public Integer updateAmountProduct(Integer billId, Integer productId, Integer amount){
        return hoaDonChiTietDao.changeAmountProduct(billId, productId, amount);
    }

    public Integer deleteEmptyBill(Integer billId){
        Integer isDeleted = hoaDonDao.deleteBill(billId);
        return isDeleted;
    }

}




