package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.DiaChiDao;
import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienDetail;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienFillDTO;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanRequest;
import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NhanVienService {


    @Autowired
    TaiKhoanDao taiKhoanDao;
    @Autowired
    DiaChiDao diaChiDao;
    @Autowired
    EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    public String findUrlAnhDaiDien(int id) {
        return taiKhoanDao.findUrlAnhDaiDien(id);
    }

    public Page<TaiKhoan> searchByNhanVien(NhanVienFillDTO nhanVienFillDTO) {
        Specification<TaiKhoan> specification = new Specification<TaiKhoan>() {
            @Override
            public Predicate toPredicate(Root<TaiKhoan> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                // Tìm kiếm theo tên khách hàng hoặc số điện thoại
                if (!ObjectUtils.isEmpty(nhanVienFillDTO.getSearch())) {
                    String search = "%" + nhanVienFillDTO.getSearch().trim().toUpperCase() + "%";
                    Predicate tenPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("hoVaTen")), search);
                    Predicate sdtPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("soDienThoai")), search);
                    predicates.add(criteriaBuilder.or(tenPredicate, sdtPredicate));
                }

                // Tìm kiếm theo giới tính
                if (nhanVienFillDTO.getGioiTinh() != null && nhanVienFillDTO.getGioiTinh() != 2) {
                    predicates.add(criteriaBuilder.equal(root.get("gioiTinh"), nhanVienFillDTO.getGioiTinh()));
                }

                // Tìm kiếm theo trạng thái
                if (nhanVienFillDTO.getTrangThai() != null && nhanVienFillDTO.getTrangThai() != 2) {
                    predicates.add(criteriaBuilder.equal(root.get("trangThai"), nhanVienFillDTO.getTrangThai()));
                }

                // Tìm kiếm theo quyền
                if ("Nhan_Vien".equals(nhanVienFillDTO.getQuyen())) {
                    predicates.add(criteriaBuilder.equal(root.get("quyen"), nhanVienFillDTO.getQuyen()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Sort sort = Sort.by(Sort.Order.desc("id")); // Default sort by id descending
        if (!ObjectUtils.isEmpty(nhanVienFillDTO.getSort())) {
            sort = Sort.by(Sort.Order.desc(nhanVienFillDTO.getSort()));
        }
        // Tạo Pageable với kích thước trang và sắp xếp
        Pageable pageable = PageRequest.of(nhanVienFillDTO.getPage(), nhanVienFillDTO.getSize(), sort);

        // Trả về trang kết quả
        return taiKhoanDao.findAll(specification, pageable);
    }

    public TaiKhoanRequest add(TaiKhoanRequest taiKhoanRequest) throws IOException {
        Instant now = Instant.now();
        Timestamp ngayTao = Timestamp.from(now);

        Optional<TaiKhoan> existingTaiKhoan = taiKhoanDao.findBySoCanCuoc(taiKhoanRequest.getSoCanCuoc());
        if (existingTaiKhoan.isPresent()) {
            throw new IllegalArgumentException("Số CCCD đã tồn tại trong hệ thống.");
        }
        int idTkMax = taiKhoanDao.findTopByOrderByIdDesc().getId();
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMa("NV" + idTkMax);
        System.out.println("taiKhoanRequest.getAnh()"+ taiKhoanRequest.getAnh());
        if (taiKhoanRequest.getAnh() == null) {
            taiKhoanRequest.setAnh("");
            taiKhoan.setAnhDaiDien(taiKhoanRequest.getAnh());
        } else {
            taiKhoan.setAnhDaiDien(taiKhoanRequest.getAnh());
        }
        taiKhoan.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        taiKhoan.setEmail(taiKhoanRequest.getEmail());
        taiKhoan.setSoCanCuoc(taiKhoanRequest.getSoCanCuoc());
        taiKhoan.setSoDienThoai(taiKhoanRequest.getSdt());
        taiKhoan.setNgaySinh(taiKhoanRequest.getNgaySinh());
        taiKhoan.setGioiTinh(taiKhoanRequest.getGioiTinh());
        taiKhoan.setQuyen("Nhan_Vien");
        taiKhoan.setTrangThai(taiKhoanRequest.getTrangThai());
        taiKhoan.setNgayTao(ngayTao);
        taiKhoan.setNguoiTao("admin");
        // Giả sử mật khẩu là một chuỗi ngẫu nhiên
        String generatedPassword = "yourGeneratedPassword";
        taiKhoan.setMatKhau(passwordEncoder.encode("123456"));
        taiKhoanDao.save(taiKhoan);

        TaiKhoan taiKhoanNew = taiKhoanDao.findTopByOrderByIdDesc();
        DiaChi diaChi = new DiaChi();
        diaChi.setTaiKhoanByIdTaiKhoan(taiKhoanNew);
        diaChi.setIdTinh(taiKhoanRequest.getIdTinh());
        diaChi.setIdHuyen(taiKhoanRequest.getIdHuyen());
        diaChi.setIdXa(taiKhoanRequest.getIdXa());
        diaChi.setDiaChiCuThe(taiKhoanRequest.getDiaChi());
        diaChi.setNgayTao(ngayTao);
        diaChi.setNguoiTao("admin");
        diaChi.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        diaChi.setMacDinh(true);
        diaChi.setSoDienThoai(taiKhoanRequest.getSdt());

        diaChiDao.save(diaChi);
        // Gửi email thông báo với mật khẩu
        String subject = "Thông tin tài khoản của bạn";
        String body = "Xin chào " + taiKhoan.getEmail() + ",\n\n" +
                "Tài khoản của bạn đã được tạo thành công.\n" +
                "Tên đăng nhập: " + taiKhoan.getEmail() + "\n" +
                "Mật khẩu: " + 123456 + "\n\n" +
                "Vui lòng đổi mật khẩu sau khi đăng nhập.";
        emailService.sendEmail(taiKhoan.getEmail(), subject, body);

        return taiKhoanRequest;
    }

    public TaiKhoanRequest update(Integer idTaiKhoan, TaiKhoanRequest taiKhoanRequest) throws IOException {
        // Tìm kiếm đối tượng TaiKhoan hiện có trong cơ sở dữ liệu
        TaiKhoan taiKhoan = taiKhoanDao.findById(idTaiKhoan)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại."));

        // Cập nhật thông tin của tài khoản
        taiKhoan.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        taiKhoan.setEmail(taiKhoanRequest.getEmail());
        taiKhoan.setSoCanCuoc(taiKhoanRequest.getSoCanCuoc());
        taiKhoan.setSoDienThoai(taiKhoanRequest.getSdt());
        taiKhoan.setNgaySinh(taiKhoanRequest.getNgaySinh());
        taiKhoan.setGioiTinh(taiKhoanRequest.getGioiTinh());
        taiKhoan.setTrangThai(taiKhoanRequest.getTrangThai());

        // Xử lý ảnh đại diện
        if (taiKhoanRequest.getAnh() != null) {
            taiKhoan.setAnhDaiDien(taiKhoanRequest.getAnh());
        }

        // Lưu đối tượng TaiKhoan đã cập nhật vào cơ sở dữ liệu
        taiKhoanDao.save(taiKhoan);

        // Tìm kiếm hoặc tạo mới địa chỉ liên quan đến tài khoản
        List<DiaChi> diaChiList = diaChiDao.findByTaiKhoanByIdTaiKhoan(taiKhoan);

        DiaChi diaChi;
        if (diaChiList.isEmpty()) {
            diaChi = new DiaChi();
        } else {
            diaChi = diaChiList.get(0);
        }

        diaChi.setTaiKhoanByIdTaiKhoan(taiKhoan);
        diaChi.setIdTinh(taiKhoanRequest.getIdTinh());
        diaChi.setIdHuyen(taiKhoanRequest.getIdHuyen());
        diaChi.setIdXa(taiKhoanRequest.getIdXa());
        diaChi.setDiaChiCuThe(taiKhoanRequest.getDiaChi());
        diaChi.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        diaChi.setSoDienThoai(taiKhoanRequest.getSdt());

        // Lưu đối tượng DiaChi đã cập nhật vào cơ sở dữ liệu
        diaChiDao.save(diaChi);

        // Nếu cần, có thể thêm logic gửi email hoặc các thao tác khác

        return taiKhoanRequest;
    }
//    public void updateTrangThai(Integer trangThai1, Integer trangThai2,Integer idTaiKhoan){
//        if(trangThai2==1){
//            trangThai1=0;
//        }
//        if(trangThai2==0){
//            trangThai1=1;
//        }
//        taiKhoanDao.doiTrangThai(trangThai1,trangThai2, idTaiKhoan);
//    }

    public TaiKhoan updateTrangThai(Integer id) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanDao.findById(id);

        if (optionalTaiKhoan.isPresent()) {
            TaiKhoan taiKhoan = optionalTaiKhoan.get();

            // Kiểm tra trạng thái hiện tại và tự động chuyển đổi
            int currentTrangThai = taiKhoan.getTrangThai();
            int newTrangThai = (currentTrangThai == 1) ? 0 : 1; // Chuyển trạng thái từ 1 -> 0 hoặc 0 -> 1

            taiKhoan.setTrangThai(newTrangThai);
            return taiKhoanDao.save(taiKhoan); // Lưu thay đổi vào database
        } else {
            throw new EntityNotFoundException("Không tìm thấy tài khoản với ID: " + id);
        }
    }

    public DiaChi getTkByIdKhAndMd(int id) {
        return taiKhoanDao.getDcByIdKhAndMd(id);
    }

    public NhanVienDetail getdetailNv(Integer idTaiKhoan) {
        return taiKhoanDao.getTkNhanVien(idTaiKhoan);
    }

    public TaiKhoan getSdt(String sdt) {
        return taiKhoanDao.getSdtNV(sdt);
    }

    public TaiKhoan getEmail(String email) {
        return taiKhoanDao.getEmailNV(email);
    }


    public TaiKhoan getSdtUpdate(String sdt,int id) {
        return taiKhoanDao.getSdtUpdateNv(sdt,id);
    }

    public TaiKhoan getEmailUpdate(String email,int id) {
        return taiKhoanDao.getEmailUpdateNV(email,id);
    }
    public TaiKhoan getSocccdUpdate(String soCanCuoc,int id) {
        return taiKhoanDao.getScccdUpdateNV(soCanCuoc,id);
    }


}
