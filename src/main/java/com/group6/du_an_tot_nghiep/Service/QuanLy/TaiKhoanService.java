package com.group6.du_an_tot_nghiep.Service.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dao.DiaChiDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.DiaChi.DiaChiRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.*;
import com.group6.du_an_tot_nghiep.Entities.*;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse;
import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaiKhoanService {
    @Autowired
    private TaiKhoanDao taiKhoanDao;
    @Autowired
    private DiaChiDao diaChiDao;
    @Autowired
    private HoaDonDao hoaDonDao;

    @Autowired
    GioHangDao gioHangDao;


    @Autowired
    public TaiKhoanService(TaiKhoanDao taiKhoanDao, DiaChiDao diaChiDao, HoaDonDao hoaDonDao) {
        this.taiKhoanDao = taiKhoanDao;
        this.diaChiDao = diaChiDao;
        this.hoaDonDao = hoaDonDao;
    }

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;

    private static String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    public String findUrlAnhDaiDien(int id) {
        return taiKhoanDao.findUrlAnhDaiDien(id);
    }


    public List<TaiKhoan> getAll() {
        return taiKhoanDao.findAll();
    }

    public Page<TaiKhoan> getPage(int page) {
        Pageable p = PageRequest.of(page, 6);
        Page<TaiKhoan> pageData = taiKhoanDao.getPage(p);
        return pageData;
    }

    public List<Integer> getIdKHTT1() {
        List<Integer> lst = new ArrayList<>();
        for (TaiKhoan kh : taiKhoanDao.findAllByTrangThaiAndQuyen(taiKhoanDao.DANG_HOAT_DONG, "Khach_Hang")) {
            lst.add(kh.getId());
        }
        return lst;
    }

    public TaiKhoan getById(int id) {
        return taiKhoanDao.findById(id).orElse(null);
    }

    /**
     * Tìm tài khoản với sđt
     */
    public Page<TaiKhoan> getTkBySdt(String soDienThoai, int page) {
        if (soDienThoai.trim().equals("")) {
            soDienThoai = null;
        }
        Pageable p = PageRequest.of(page, 6);
        Page<TaiKhoan> pageData = taiKhoanDao.getTKBySdt(soDienThoai, p);
        return pageData;
    }

    public Page<TaiKhoanResponse> getLocTK(Integer gioiTinh, Integer trangThai, int page) {
        if (gioiTinh == 2) {
            gioiTinh = null;
        }
        if (trangThai == 2) {
            trangThai = null;
        }
        Pageable p = PageRequest.of(page, 8);
        Page<TaiKhoanResponse> pageData = taiKhoanDao.loc(gioiTinh, trangThai, p);
        return pageData;
    }

    public Page<TaiKhoanResponse> getPageTK(int page) {
        Pageable p = PageRequest.of(page, 10);
        Page<TaiKhoanResponse> pageData = taiKhoanDao.getPageTk(p);
        return pageData;
    }

    public Page<TaiKhoanResponse> timKiem(String timKiem, int page) {
        Pageable p = PageRequest.of(page, 10);
        Page<TaiKhoanResponse> pageData = taiKhoanDao.timKiem(timKiem, p);
        return pageData;
    }


    public TaiKhoanRequest add(TaiKhoanRequest taiKhoanRequest) throws IOException {
        Instant now = Instant.now();
        Timestamp ngayTao = Timestamp.from(now);
        int idTkMax = taiKhoanDao.findTopByOrderByIdDesc().getId();
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMa("KH" + idTkMax+1);
        if(taiKhoanRequest.getAnh()==null){
            taiKhoanRequest.setAnh("");
            taiKhoan.setAnhDaiDien(taiKhoanRequest.getAnh());
        }else{
            taiKhoan.setAnhDaiDien(taiKhoanRequest.getAnh());
        }
        taiKhoan.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        taiKhoan.setMatKhau(passwordEncoder.encode("123456"));
        taiKhoan.setEmail(taiKhoanRequest.getEmail());
        taiKhoan.setSoCanCuoc(taiKhoanRequest.getSoCanCuoc());
        taiKhoan.setSoDienThoai(taiKhoanRequest.getSdt());
        taiKhoan.setNgaySinh(taiKhoanRequest.getNgaySinh());
        taiKhoan.setGioiTinh(taiKhoanRequest.getGioiTinh());
        taiKhoan.setQuyen("Khach_Hang");
        taiKhoan.setTrangThai(taiKhoanRequest.getTrangThai());
        taiKhoan.setNgayTao(ngayTao);
        taiKhoan.setNguoiTao("namnv");

        taiKhoanDao.save(taiKhoan);

        TaiKhoan taiKhoanNew = taiKhoanDao.findTopByOrderByIdDesc();
        DiaChi diaChi = new DiaChi();
        diaChi.setTaiKhoanByIdTaiKhoan(taiKhoanNew);
        diaChi.setIdTinh(taiKhoanRequest.getIdTinh());
        diaChi.setIdHuyen(taiKhoanRequest.getIdHuyen());
        diaChi.setIdXa(taiKhoanRequest.getIdXa());
        diaChi.setDiaChiCuThe(taiKhoanRequest.getDiaChi());
        diaChi.setNgayTao(ngayTao);
        diaChi.setNguoiTao("namnv");
        diaChi.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        diaChi.setMacDinh(true);
        diaChi.setSoDienThoai(taiKhoanRequest.getSdt());

        diaChiDao.save(diaChi);

        GioHang gioHang=new GioHang();
        gioHang.setTaiKhoanByIdTaiKhoan(taiKhoanNew);
        gioHang.setNguoiTao("namnv");
        gioHang.setNgayTao(ngayTao);
        gioHang.setTrangThai(0);

        gioHangDao.save(gioHang);

        String subject = "Thông tin tài khoản của bạn";
        String body = "Xin chào " + taiKhoan.getEmail() + ",\n\n" +
                "Tài khoản của bạn đã được tạo thành công.\n" +
                "Tên đăng nhập: " + taiKhoan.getEmail() + "\n" +
                "Mật khẩu: " + 123456 + "\n\n" +
                "Vui lòng đổi mật khẩu sau khi đăng nhập.";
        emailService.sendEmail(taiKhoan.getEmail(), subject, body);

        return taiKhoanRequest;
    }

    public TaiKhoan getSdt(String sdt) {
        return taiKhoanDao.getSdt(sdt);
    }

    public TaiKhoan getEmail(String email) {
        return taiKhoanDao.getEmail(email);
    }

    public TaiKhoan getSdtUpdate(String sdt,int id) {
        return taiKhoanDao.getSdtUpdate(sdt,id);
    }

    public TaiKhoan getEmailUpdate(String email,int id) {
        return taiKhoanDao.getEmailUpdate(email,id);
    }

    public TaiKhoan getTkById(int id){
        return taiKhoanDao.findById(id).get();
    }

    public void updateTrangThai(Integer trangThai1, Integer trangThai2, int id){
        if(trangThai2==1){
            trangThai1=0;
        }
        if(trangThai2==0){
            trangThai1=1;
        }
        taiKhoanDao.doiTrangThai(trangThai1,trangThai2, id);
    }

    public TaiKhoanRequest updateTaiKhoan(int id, TaiKhoanRequest taiKhoanRequest){
        Instant now = Instant.now();
        Timestamp ngaySua = Timestamp.from(now);
        int idTkMax = taiKhoanDao.findTopByOrderByIdDesc().getId();
        TaiKhoan taiKhoan = taiKhoanDao.findById(id).get();
        taiKhoan.setMa("KH" + idTkMax);
        taiKhoan.setHoVaTen(taiKhoanRequest.getTenKhachHang());
        taiKhoan.setEmail(taiKhoanRequest.getEmail());
        taiKhoan.setSoDienThoai(taiKhoanRequest.getSdt());
        taiKhoan.setNgaySinh(taiKhoanRequest.getNgaySinh());
        taiKhoan.setGioiTinh(taiKhoanRequest.getGioiTinh());
        taiKhoan.setQuyen("Khach_Hang");
        taiKhoan.setTrangThai(taiKhoanRequest.getTrangThai());
        taiKhoan.setNgaySua(ngaySua);
        taiKhoan.setNguoiSua("namnv");

        taiKhoanDao.save(taiKhoan);
        return taiKhoanRequest;
    }

    public DiaChi getTkByIdKhAndMd(int id){
        return taiKhoanDao.getDcByIdKhAndMd(id);
    }

    public DiaChiRequest updateDiaChi(int id, DiaChiRequest diaChiRequest){
        Instant now = Instant.now();
        Timestamp ngaySua = Timestamp.from(now);
        DiaChi diaChi=diaChiDao.findById(id).get();
        diaChi.setHoVaTen(diaChiRequest.getHoVaTen());
        diaChi.setSoDienThoai(diaChiRequest.getSoDienThoai());
        diaChi.setIdTinh(diaChiRequest.getIdTinh());
        diaChi.setIdXa(diaChiRequest.getIdXa());
        diaChi.setIdHuyen(diaChiRequest.getIdHuyen());
        diaChi.setDiaChiCuThe(diaChiRequest.getDiaChiCuThe());
        diaChi.setNgaySua(ngaySua);
        diaChi.setNguoiSua("namnv");
        diaChiDao.save(diaChi);
        return diaChiRequest;
    }

    public DiaChiRequest addDiaChi(DiaChiRequest diaChiRequest, int id){
        Instant now = Instant.now();
        Timestamp ngayTao= Timestamp.from(now);
        DiaChi diaChi=new DiaChi();
        TaiKhoan taiKhoan=taiKhoanDao.findById(id).get();
        diaChi.setTaiKhoanByIdTaiKhoan(taiKhoan);
        diaChi.setHoVaTen(diaChiRequest.getHoVaTen());
        diaChi.setSoDienThoai(diaChiRequest.getSoDienThoai());
        diaChi.setIdTinh(diaChiRequest.getIdTinh());
        diaChi.setIdXa(diaChiRequest.getIdXa());
        diaChi.setIdHuyen(diaChiRequest.getIdHuyen());
        diaChi.setIdXa(diaChiRequest.getIdXa());
        diaChi.setDiaChiCuThe(diaChiRequest.getDiaChiCuThe());
        diaChi.setEmail("");
        diaChi.setMacDinh(false);
        diaChi.setNguoiTao("namnv");
        diaChi.setNgayTao(ngayTao);
        diaChi.setLoai(1);
        diaChiDao.save(diaChi);
        return diaChiRequest;
    }

    public Page<DiaChi> getPageDiaChi(int id, int page) {
        Pageable p = PageRequest.of(page, 2);
        Page<DiaChi> pageData = taiKhoanDao.getPageDiaChi(id, p);
        return pageData;
    }

    public void deleteDc(int idDc){
        taiKhoanDao.delete(idDc);
        System.out.println("delete òiiiiiiiiiiiiiiiiiii");
    }

    public void updateMacDinhTrue(int idDc){
        taiKhoanDao.updateMacDinhTrue(idDc);
    }
    public void  updateMacDinhFalse(int idDc, int idTaiKhoan){
        taiKhoanDao.updateMacDinhFalse(idDc,idTaiKhoan);
    }

    public TaiKhoan registerUser(TaiKhoan request){
        request.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        request.setNgayTao(new Timestamp(System.currentTimeMillis()));
        request.setNguoiTao("DBA");
        request.setTrangThai(1);
        request.setQuyen("Khach_Hang");
        request.setSoCanCuoc("");
        if (request.getGioiTinh() == 1) {
            request.setAnhDaiDien("avatar-nam.jpg");
        }else {
            request.setAnhDaiDien("avatar-nu.jpg");
        }
        TaiKhoan taiKhoan = taiKhoanDao.save(request);
        GioHang gioHang = new GioHang();
        gioHang.setTaiKhoanByIdTaiKhoan(taiKhoan);
        gioHang.setTrangThai(1);
        gioHangDao.save(gioHang);
        return taiKhoan;
    }

    public String reChangePassword(String password, String email){
        String newPassword = passwordEncoder.encode(password);
        int isUpdate = taiKhoanDao.reChangePassword(newPassword, email);
        if (isUpdate >= 1){
            return "Đổi mật khẩu thành công";
        }else{
            return "Đổi mật khẩu thất bại, vui lòng thử lại";
        }
    }


    public TkResponse getCustomerById(Integer idCustomer, Integer billId){
        try{
             TaiKhoan account = taiKhoanDao.getCustomerById(idCustomer)
                     .orElseThrow(() -> new RuntimeException("Account not exists"));
             HoaDon hoaDon = hoaDonDao.findById(billId).orElse(null);
             if (hoaDon != null){
                 TaiKhoan idAccount = new TaiKhoan();
                 idAccount.setId(idCustomer);
                 hoaDon.setIdTaiKhoan(idAccount);
                 hoaDon.setHoVaTen(account.getHoVaTen());
                 hoaDon.setEmail(account.getEmail());
                 hoaDonDao.save(hoaDon);
             }else{
                 throw new RuntimeException("Bill not exists or have been deleted");
             }

             List<DiaChi> address = diaChiDao.getDiaChiByTaiKhoanByIdTaiKhoan(idCustomer);
             String phoneNumber = diaChiDao.findMacDinhByTaiKhoan(account).getSoDienThoai();
             return new TkResponse()
                     .setName(account.getHoVaTen())
                     .setSoDienThoai(phoneNumber)
                     .setAddress(address);
        }catch (Exception exception){
            log.error("[ERROR] getCustomerById {} " + exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public Boolean doiMatKhau(DoiMatKhauDto request){
        TaiKhoan taiKhoan = taiKhoanDao.findById(request.getId()).orElse(null);
        if (taiKhoan == null) {
            return false;
        }else {
            taiKhoan.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
            taiKhoanDao.save(taiKhoan);
            return true;
        }
    }
}
