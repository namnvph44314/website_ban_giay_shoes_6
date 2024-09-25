package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Entities.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.group6.du_an_tot_nghiep.Dao.HoaDonChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Dao.SanPhamChiTietDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.AddProductToBillRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.HoaDonChiTietResponse;
import com.group6.du_an_tot_nghiep.Entities.HinhAnh;
import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import com.group6.du_an_tot_nghiep.Entities.HoaDonChiTiet;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import com.opencsv.exceptions.CsvValidationException;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class HoaDonChiTietService {
    public static final String PRODUCT_NOT_FOUND = "Sản phẩm không tìm thấy trong hệ thống hoặc đã bị xoá";
    @Autowired
    private HoaDonChiTietDao hoaDonChiTietDao;

    @Autowired
    private SanPhamChiTietDao sanPhamChiTietDao;

    private final HoaDonDao hoaDonRepository;

    @Autowired
    public HoaDonChiTietService(HoaDonDao hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }
    @Autowired
    private HoaDonDao hoaDonDao;

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    @Autowired
    private PhieuGiamGiaDao phieuGiamGiaDao;

    @Autowired
    PhieuGiamGiaKhachHangDao phieuGiamGiaKhachHangDao;

    @Autowired
    private LichSuHoaDonDao lichSuHoaDonDao;

    @Autowired
    private LichSuTraTienDao lichSuTraTien;

@Autowired
    HttpSession session;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(HoaDonChiTietService.class);


    public ThongTinDonHang getThongTinDonHang(Integer idHoaDon) {
    ThongTinDonHang  ttdh =  hoaDonChiTietDao.getThongTinDonHang(idHoaDon);
    return ttdh;
//        return hoaDonChiTietDao.getThongTinDonHang(idHoaDon);
    }

    public List<LichSuHoaDonResponse> getLichSuHoaDon(Integer idHoaDon) {
        return hoaDonChiTietDao.getLichSuThanhToan(idHoaDon);
    }

    public List<TrangThaiHoaDonRepose> getlimitTrangThaiHoaDon(Integer idHoaDon) {
        List<TrangThaiHoaDonRepose> reposeList = hoaDonChiTietDao.getTrangThaiHoaDon(idHoaDon);
        reposeList.sort(Comparator.comparing(TrangThaiHoaDonRepose::getNgayTao));
        return reposeList;
    }

    public List<TrangThaiHoaDonRepose> getAllTrangThaiHoaDon(Integer idHoaDon) {
        List<TrangThaiHoaDonRepose> reposeList = hoaDonChiTietDao.getAllTrangThaiHoaDon(idHoaDon);
        return reposeList;
    }

    public List<HinhAnh> getAnh(Integer idSanPhamCt) {
        return this.hoaDonChiTietDao.getAllAnhByIdSp(idSanPhamCt);
    }

    @Transactional
    public ThongTinDonHang updateThongTinDonHang(Integer idHoaDon, ThongTinDonHang thongTinDonHang) {
        HoaDon existingHoaDon = hoaDonDao.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("HoaDon not found with id: " + idHoaDon));

        // Only update specified fields
        existingHoaDon.setEmail(thongTinDonHang.getEmail());
        existingHoaDon.setHoVaTen(thongTinDonHang.getHoVaTen());
        existingHoaDon.setDiaChi(thongTinDonHang.getDiaChi());
        existingHoaDon.setSoDienThoaiNhan(thongTinDonHang.getSoDienThoaiNhan());

        existingHoaDon.setIdTinh(thongTinDonHang.getIdTinh());
        existingHoaDon.setIdHuyen(thongTinDonHang.getIdHuyen());
        existingHoaDon.setIdXa(thongTinDonHang.getIdXa());

        hoaDonDao.save(existingHoaDon);

        // Create the response object
        ThongTinDonHang updatedThongTinDonHang = new ThongTinDonHang();
        updatedThongTinDonHang.setMaHoaDon(existingHoaDon.getMaHoaDon());
        updatedThongTinDonHang.setEmail(existingHoaDon.getEmail());
        updatedThongTinDonHang.setHoVaTen(existingHoaDon.getHoVaTen());
        updatedThongTinDonHang.setDiaChi(existingHoaDon.getDiaChi());
        updatedThongTinDonHang.setLoaiDonHang(existingHoaDon.getLoaiDonHang());
        updatedThongTinDonHang.setSoDienThoaiNhan(existingHoaDon.getSoDienThoaiNhan());
        updatedThongTinDonHang.setTrangThai(existingHoaDon.getTrangThai());
        updatedThongTinDonHang.setIdTaiKhoan(existingHoaDon.getIdTaiKhoan().getId());
        updatedThongTinDonHang.setIdTinh(existingHoaDon.getIdTinh());
        updatedThongTinDonHang.setIdHuyen(existingHoaDon.getIdHuyen());
        updatedThongTinDonHang.setIdXa(existingHoaDon.getIdXa());
        return updatedThongTinDonHang;
    }

    public MoneyReponse getAllMonyByIdHoaDon(Integer idHoaDon) {
        HoaDon hoaDon = hoaDonDao.findById(idHoaDon).get();
        MoneyReponse moneyReponse = new MoneyReponse();
        moneyReponse.setTienGiaoHang(hoaDon.getTienGiaoHang());
        moneyReponse.setTienGiamGia(hoaDon.getTienGiamGia());
        moneyReponse.setTienSauGiam(hoaDon.getTienSauGiam());
        moneyReponse.setTongTien(hoaDon.getTongTien());
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietDao.findAllByIdHoaDon(hoaDon);

        BigDecimal tienHang = new BigDecimal(0);
        for (HoaDonChiTiet hoaDonchiTiet: hoaDonChiTietList
             ) {
            tienHang = tienHang.add(hoaDonchiTiet.getGia().multiply(new BigDecimal(hoaDonchiTiet.getSoLuongSanPham()) ));
        }
        moneyReponse.setTienHang(tienHang);

        return moneyReponse;
    }

    public MessageResponse themSanPhamVaoHoaDonChiTiet(Integer idHoaDon, Integer idSanPhamChiTiet, int soLuong, String username) throws IOException, CsvValidationException {
        TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("username");
        taiKhoan.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Optional<HoaDon> optionalHoaDon = hoaDonDao.findById(idHoaDon);
//        TaiKhoan taiKhoan = taiKhoanDao.findByHoVaTen(username).orElse(null);

        HoaDon hoaDon = optionalHoaDon.get();
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(idSanPhamChiTiet)
                .orElseThrow(() -> new EntityNotFoundException("SanPhamChiTiet not found"));

        if (sanPhamChiTiet.getSoLuong() < soLuong) {
            return MessageResponse.builder().message("Số lượng yêu cầu vượt quá số lượng có sẵn trong kho").build();
        }

        Optional<HoaDonChiTiet> hoaDonChiTietOptional = hoaDonChiTietDao.findByIdHoaDonAndSanPhamChiTietByIdSpct(hoaDon, sanPhamChiTiet);

        HoaDonChiTiet hoaDonChiTiet;
        if (hoaDonChiTietOptional.isPresent()) {
            // Nếu sản phẩm đã tồn tại trong hóa đơn, tăng số lượng
            hoaDonChiTiet = hoaDonChiTietOptional.get();
            hoaDonChiTiet.setSoLuongSanPham(hoaDonChiTiet.getSoLuongSanPham() + soLuong);
            hoaDonChiTiet.setGia(sanPhamChiTiet.getGia());
            hoaDonChiTiet.setTrangThai(1);
        } else {
            // Nếu sản phẩm chưa có trong hóa đơn, tạo mới
            hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
            hoaDonChiTiet.setIdHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuongSanPham(soLuong);
            hoaDonChiTiet.setGia(sanPhamChiTiet.getGia());
            hoaDonChiTiet.setTrangThai(1);
            hoaDonChiTiet.setNguoiTao(username);
            hoaDonChiTiet.setNgayTao(timestamp);
        }

        // Cập nhật số lượng sản phẩm trong kho chỉ khi trạng thái hóa đơn là 2
        if (hoaDon.getTrangThai() == 2) {
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
            sanPhamChiTietDao.save(sanPhamChiTiet);
        }

        hoaDonChiTietDao.save(hoaDonChiTiet);

        // tiền hàng
        BigDecimal tongTienDonGia = hoaDon.getHoaDonChiTiets().stream()
                .map(hdct -> hdct.getGia().multiply(new BigDecimal(hdct.getSoLuongSanPham())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        hoaDon.setTongTien(tongTienDonGia);

        BigDecimal tienSauGiam = tongTienDonGia.subtract(hoaDon.getTienGiamGia());
        hoaDon.setTienSauGiam(tienSauGiam);

        BigDecimal tongTien = hoaDon.getTienGiaoHang().add(tienSauGiam);
        hoaDon.setTongTien(tongTien);

        hoaDonDao.save(hoaDon);

        return MessageResponse.builder().message("Thêm thành công").build();
    }


    public Page<SanPhamChiTiet> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonChiTietDao.getAllSpct(pageable);
    }


    public HoaDon updateHoaDon(Integer idHoaDon, Integer soLuong, Integer idHoaDonChiTiet) {
        // Tìm HoaDonChiTiet và HoaDon từ cơ sở dữ liệu
        HoaDonChiTiet hoaDonChiTiet1 = this.hoaDonChiTietDao.findById(idHoaDonChiTiet).orElse(null);
        HoaDon hoaDon = hoaDonDao.findById(idHoaDon).orElse(null);

        // Kiểm tra xem HoaDonChiTiet và HoaDon có tồn tại
        if (hoaDonChiTiet1 == null || hoaDon == null) {
            throw new RuntimeException("HoaDonChiTiet hoặc HoaDon không tồn tại");
        }

        // Lấy SanPhamChiTiet từ HoaDonChiTiet
        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet1.getSanPhamChiTietByIdSpct();

        // Kiểm tra số lượng sản phẩm mới có vượt quá số lượng tồn kho không
        Integer soLuongCu = hoaDonChiTiet1.getSoLuongSanPham();
        Integer soLuongMoi = soLuong - soLuongCu;
        if (soLuongMoi > sanPhamChiTiet.getSoLuong()) {
            throw new RuntimeException("Số lượng sản phẩm mới vượt quá số lượng tồn kho");
        }

        // Cập nhật số lượng sản phẩm trong kho
        if (hoaDon.getTrangThai() == 2) {


            if (soLuongCu < soLuong) {
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuongMoi);
            } else {
                soLuongMoi = soLuongCu - soLuong;
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + soLuongMoi);
            }

            sanPhamChiTietDao.save(sanPhamChiTiet);
        }

        // Cập nhật số lượng sản phẩm trong HoaDonChiTiet
        hoaDonChiTiet1.setSoLuongSanPham(soLuong);
        hoaDonChiTietDao.save(hoaDonChiTiet1);

        //tiền hàng
        BigDecimal tongTienDonGia = hoaDon.getHoaDonChiTiets().stream()
                .map(hdct -> hdct.getGia().multiply(new BigDecimal(hdct.getSoLuongSanPham())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        hoaDon.setTongTien(tongTienDonGia);

        BigDecimal tienSauGiam = tongTienDonGia.subtract(hoaDon.getTienGiamGia());
        hoaDon.setTienSauGiam(tienSauGiam);

        BigDecimal tongTien = hoaDon.getTienGiaoHang().add(tienSauGiam);
        hoaDon.setTongTien(tongTien);

        // Lưu thay đổi HoaDon
        return this.hoaDonDao.save(hoaDon);
    }


    public MessageResponse xoaSanPhamKhoiHoaDonChiTiet(Integer idHoaDon, Integer idSanPhamChiTiet) {
        // Tìm hóa đơn
        Optional<HoaDon> optionalHoaDon = hoaDonDao.findById(idHoaDon);
        if (optionalHoaDon.isEmpty()) {
            return MessageResponse.builder().message("Hóa Đơn không tồn tại").build();
        }

        HoaDon hoaDon = optionalHoaDon.get();

        // Tìm sản phẩm chi tiết
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(idSanPhamChiTiet)
                .orElseThrow(() -> new EntityNotFoundException("SanPhamChiTiet not found"));

        // Tìm sản phẩm trong hóa đơn chi tiết
        Optional<HoaDonChiTiet> hoaDonChiTietOptional = hoaDonChiTietDao.findByIdHoaDonAndSanPhamChiTietByIdSpct(hoaDon, sanPhamChiTiet);
        if (hoaDonChiTietOptional.isEmpty()) {
            return MessageResponse.builder().message("Sản phẩm không tồn tại trong hóa đơn").build();
        }

        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietOptional.get();

        // Xóa sản phẩm khỏi hóa đơn chi tiết
        hoaDonChiTietDao.delete(hoaDonChiTiet);

        // Cập nhật tổng số tiền trong hóa đơn
        BigDecimal tongTienDonGia = hoaDon.getHoaDonChiTiets().stream()
                .map(hdct -> hdct.getGia().multiply(new BigDecimal(hdct.getSoLuongSanPham())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        hoaDon.setTongTien(tongTienDonGia);
        hoaDonDao.save(hoaDon);

        return MessageResponse.builder().message("Xóa sản phẩm thành công").build();
    }

    public Page<SanPhamChiTiet> searchBySanPham(SanPhamChiTietFillReq sanPhamChiTietFillReq) {

        // Tạo Specification cho tìm kiếm
        Specification<SanPhamChiTiet> specification = new Specification<SanPhamChiTiet>() {
            @Override
            public Predicate toPredicate(Root<SanPhamChiTiet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                // Điều kiện trạng thái luôn luôn bằng 1
                predicates.add(criteriaBuilder.equal(root.get("trangThai"), 1));

                // Tìm kiếm theo tên sản phẩm nếu có
                if (sanPhamChiTietFillReq.getTenSanPham() != null && !sanPhamChiTietFillReq.getTenSanPham().isEmpty()) {
                    predicates.add(criteriaBuilder.like(root.get("sanPhamByIdSanPham").get("tenSanPham"), "%" + sanPhamChiTietFillReq.getTenSanPham() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        // Tạo Pageable từ thông tin phân trang
        Pageable pageable = PageRequest.of(sanPhamChiTietFillReq.getPage(), sanPhamChiTietFillReq.getSize());

        // Tìm kiếm với các tiêu chí và phân trang
        return sanPhamChiTietDao.findAll(specification, pageable);
    }



    public ListPggGioHang getBestPgg(Integer idHoaDon, BigDecimal tongTien, Integer idTaiKhoan) {

        // Lấy danh sách phiếu giảm giá phù hợp
        List<PhieuGiamGia> phieuGiamGiaList;

        phieuGiamGiaList = phieuGiamGiaDao.findPggCongKhai(tongTien, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaList.addAll( phieuGiamGiaDao.findPggCaNhan(tongTien, idTaiKhoan, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));


        // Xử lý phiếu giảm giá
        List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
        for (PhieuGiamGia phieuGiamGia : phieuGiamGiaList) {
            ListPggGioHang pggGioHang = new ListPggGioHang();
            BigDecimal tienDuocGiamTheoDon;

            if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                tienDuocGiamTheoDon = phieuGiamGia.getGiaTri();
            } else {
                tienDuocGiamTheoDon = tongTien.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienDuocGiamTheoDon) < 0) {
                    tienDuocGiamTheoDon = phieuGiamGia.getGiaTriToiDa();
                }
            }

            pggGioHang.setTienDuocGiamTheoDon(tienDuocGiamTheoDon);
            pggGioHang.setIdPgg(phieuGiamGia.getId());
            pggGioHang.setMaPgg(phieuGiamGia.getMaKhuyenMai());
            pggGioHang.setGiaTriGiam(phieuGiamGia.getGiaTri());
            pggGioHang.setGiamToiDa(phieuGiamGia.getGiaTriToiDa());
            pggGioHang.setDonToiThieu(phieuGiamGia.getGiaTriNhoNhat());
            pggGioHang.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
            pggGioHang.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            pggGioHang.setSoLuong(phieuGiamGia.getSoLuong());
            listPggGioHangs.add(pggGioHang);
        }

        // Sắp xếp danh sách theo số tiền được giảm từ cao đến thấp và lấy phần tử đầu tiên
        listPggGioHangs.sort((o1, o2) -> o2.getTienDuocGiamTheoDon().compareTo(o1.getTienDuocGiamTheoDon()));
        ListPggGioHang bestVoucher = listPggGioHangs.isEmpty() ? null : listPggGioHangs.get(0);


        if (bestVoucher != null) {
            HoaDon hoaDon = hoaDonDao.findById(idHoaDon).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
            hoaDon.setTienGiamGia(bestVoucher.getTienDuocGiamTheoDon());

            // Cập nhật tổng tiền sau giảm giá

            BigDecimal tienSauGiamGia = tongTien.subtract(bestVoucher.getTienDuocGiamTheoDon());
            hoaDon.setTienSauGiam(tienSauGiamGia);

            BigDecimal tongTienHd = tienSauGiamGia.add(hoaDon.getTienGiaoHang());
            hoaDon.setTongTien(tongTienHd);

       hoaDon.setIdPhieuGiamGia(phieuGiamGiaDao.findById(bestVoucher.getIdPgg()).get());
            // Chỉ trừ số lượng và cập nhật trạng thái phiếu giảm giá khi trạng thái của hóa đơn là 2
            if (hoaDon.getTrangThai() == 2) {

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
            }

            hoaDonDao.save(hoaDon);
        }


        return bestVoucher;
    }

    public List<SanPhamChiTietDTO> findSanPhamChiTietByIdHoaDon(Integer idHoaDon) {
        return this.hoaDonChiTietDao.findSanPhamByIdHoaDon(idHoaDon);
    }

    public List<HinhAnh> getAnhYeu(Integer id) {
        return this.hoaDonChiTietDao.getAllAnhByIdSp(id);
    }

    public HoaDonChiTiet addProductToBill(AddProductToBillRequest request) {
        try {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));

            HoaDon hoaDon = hoaDonRepository.findById(request.getBillId())
                    .orElseThrow(() -> new RuntimeException("Hoá đơn không tồn tại hoặc đã bị xoá khỏi hệ thống"));

            Optional<HoaDonChiTiet> hoaDonChiTietOp = hoaDonChiTietDao.findByIdSPCT(sanPhamChiTiet.getId(), hoaDon.getId());

            if (hoaDonChiTietOp.isPresent()){
                HoaDonChiTiet detailBillExists = hoaDonChiTietOp.get();
                detailBillExists.setSoLuongSanPham(detailBillExists.getSoLuongSanPham() + request.getAmountProduct());
                HoaDonChiTiet saved = hoaDonChiTietDao.save(detailBillExists);
                return saved;
            }else{
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setGia(sanPhamChiTiet.getGia());
                hoaDonChiTiet.setSoLuongSanPham(request.getAmountProduct());
                hoaDonChiTiet.setIdHoaDon(hoaDon);
                hoaDonChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
                hoaDonChiTiet.setNguoiTao("admin");
                hoaDonChiTiet.setNgayTao(new Timestamp(System.currentTimeMillis()));

                HoaDonChiTiet objectSaved = hoaDonChiTietDao.save(hoaDonChiTiet);

                if (objectSaved == null){
                    throw new RuntimeException("Thêm sản phẩm vào giỏ hàng thất bại");
                }

                return objectSaved;
            }
        } catch (Exception exception) {
            log.error("[ERROR] addProductToBill {} " + exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public Integer getTongTien(Integer billId){
        Integer tongTien = hoaDonChiTietDao.getTongTien(billId);
        return tongTien;
    }


    public Integer deleteHistoryPay(Integer id){
        lichSuTraTien.deleteById(id);
        Optional<LichSuTraTien> isDelete = lichSuTraTien.findById(id);
        if (isDelete.isPresent()){
            return 0;
        }else{
            return 1;
        }
    }

}



