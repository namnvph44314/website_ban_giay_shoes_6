package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Service.QuanLy.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Optional;

@Controller

public class ThongKeController {
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    TaiKhoanDao taiKhoanDao;

    boolean checkNgay=true;

    @GetMapping("/admin/thong-ke/index")
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("pageSp") Optional<Integer> pageSpParam,
            @RequestParam(value = "soLuong",required = false) Optional<Integer> soLuongParam
    ) {
        checkNgay=true;
        int soLuong=soLuongParam.orElse(50);
        int page= pageParam.orElse(0);
        int pageSp= pageSpParam.orElse(0);
        addAttributeThuocTinh(model);
        BieuDoMacDinh(model);
        tkTuyChinh(model);
        tocDoTangTruong(model);
        model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChay(page));
        model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
        model.addAttribute("soLuong",soLuong);
        return "quan_ly/thong_ke/index";
    }

    @GetMapping("/admin/thong-ke/loc-ngay")
    public String locNgay(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("pageSp") Optional<Integer> pageSpParam,
            @RequestParam(value = "soLuong",required = false) Optional<Integer> soLuongParam
    ) {
        checkNgay=true;
        int soLuong=soLuongParam.orElse(50);
        int page= pageParam.orElse(0);
        int pageSp= pageSpParam.orElse(0);
        addAttributeThuocTinh(model);
        BieuDoNgay(model);
        tkTuyChinh(model);
        tocDoTangTruong(model);
        model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChayByNgay(page));
        model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
        model.addAttribute("soLuong",soLuong);
        return "quan_ly/thong_ke/index";
    }

    @GetMapping("/admin/thong-ke/loc-tuan")
    public String locTuan(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("pageSp") Optional<Integer> pageSpParam,
            @RequestParam(value = "soLuong",required = false) Optional<Integer> soLuongParam
    ) {
        checkNgay=true;
        int soLuong=soLuongParam.orElse(50);
        int page= pageParam.orElse(0);
        int pageSp= pageSpParam.orElse(0);
        addAttributeThuocTinh(model);
        BieuDoTuan(model);
        tkTuyChinh(model);
        tocDoTangTruong(model);
        model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChayByTuan(page));
        model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
        model.addAttribute("soLuong",soLuong);
        return "quan_ly/thong_ke/index";
    }

    @GetMapping("/admin/thong-ke/loc-thang")
    public String locThang(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("pageSp") Optional<Integer> pageSpParam,
            @RequestParam(value = "soLuong",required = false) Optional<Integer> soLuongParam
    ) {
        checkNgay=true;
        int soLuong=soLuongParam.orElse(50);
        int page= pageParam.orElse(0);
        int pageSp= pageSpParam.orElse(0);
        addAttributeThuocTinh(model);
        BieuDoThang(model);
        tkTuyChinh(model);
        tocDoTangTruong(model);
        model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChayByThang(page));
        model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
        model.addAttribute("soLuong",soLuong);
        return "quan_ly/thong_ke/index";
    }

    @GetMapping("/admin/thong-ke/loc-nam")
    public String locNam(
            Model model,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("pageSp") Optional<Integer> pageSpParam,
            @RequestParam(value = "soLuong",required = false) Optional<Integer> soLuongParam
    ) {
        checkNgay=true;
        int soLuong=soLuongParam.orElse(50);
        int page= pageParam.orElse(0);
        int pageSp= pageSpParam.orElse(0);
        addAttributeThuocTinh(model);
        BieuDoNam(model);
        tkTuyChinh(model);
        tocDoTangTruong(model);
        model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChayByNam(page));
        model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
        model.addAttribute("soLuong",soLuong);
        return "quan_ly/thong_ke/index";
    }

    @GetMapping("/admin/thong-ke/loc")
    public String loc(
            Model model,
            @RequestParam(value = "tuNgay",required = false) String tuNgay,
            @RequestParam(value = "denNgay",required = false) String denNgay,
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("pageSp") Optional<Integer> pageSpParam,
            @RequestParam(value = "soLuong",required = false) Optional<Integer> soLuongParam
    ){
        Timestamp startDate=null;
        Timestamp endDate=null;
        if(!tuNgay.trim().equals("") && !denNgay.trim().equals("")){
            startDate = Timestamp.valueOf(tuNgay+" 00:00:00");
            endDate = Timestamp.valueOf(denNgay+" 00:00:00");
        }
        if(tuNgay.trim().equals("") && denNgay.trim().equals("")){
            checkNgay=true;
            int soLuong=soLuongParam.orElse(50);
            int page= pageParam.orElse(0);
            int pageSp= pageSpParam.orElse(0);
            addAttributeThuocTinh(model);
            BieuDoMacDinh(model);
            tkTuyChinh(model);
            tocDoTangTruong(model);
            model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChay(page));
            model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
            model.addAttribute("soLuong",soLuong);
            return "quan_ly/thong_ke/index";
        }
        if(tuNgay.trim().equals("") && !denNgay.trim().equals("")){
            checkNgay=false;
            model.addAttribute("checkNgay",checkNgay);
            model.addAttribute("message","Vui lòng chọn ngày kết thúc");
            int soLuong=soLuongParam.orElse(50);
            int page= pageParam.orElse(0);
            int pageSp= pageSpParam.orElse(0);
            addAttributeThuocTinh(model);
            BieuDoMacDinh(model);
            tkTuyChinh(model);
            tocDoTangTruong(model);
            model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChay(page));
            model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
            model.addAttribute("soLuong",soLuong);
            return "quan_ly/thong_ke/index";
        }
        if(!tuNgay.trim().equals("") && denNgay.trim().equals("")){
            checkNgay=false;
            model.addAttribute("checkNgay",checkNgay);
            model.addAttribute("message","Vui lòng chọn ngày bắt đầu");
            int soLuong=soLuongParam.orElse(50);
            int page= pageParam.orElse(0);
            int pageSp= pageSpParam.orElse(0);
            addAttributeThuocTinh(model);
            BieuDoMacDinh(model);
            tkTuyChinh(model);
            tocDoTangTruong(model);
            model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChay(page));
            model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
            model.addAttribute("soLuong",soLuong);
            return "quan_ly/thong_ke/index";
        }
        if(startDate.after(endDate)){
            checkNgay=false;
            model.addAttribute("checkNgay",checkNgay);
            model.addAttribute("message","Ngày bắt đầu phải sau ngày kết thúc");
            int soLuong=soLuongParam.orElse(50);
            int page= pageParam.orElse(0);
            int pageSp= pageSpParam.orElse(0);
            addAttributeThuocTinh(model);
            BieuDoMacDinh(model);
            tkTuyChinh(model);
            tocDoTangTruong(model);
            model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChay(page));
            model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
            model.addAttribute("soLuong",soLuong);
            return "quan_ly/thong_ke/index";
        }
        checkNgay=true;
        model.addAttribute("tuNgay",tuNgay);
        model.addAttribute("denNgay",denNgay);
        int soLuong=soLuongParam.orElse(50);
        int page= pageParam.orElse(0);
        int pageSp= pageSpParam.orElse(0);
        addAttributeThuocTinh(model);
        BieuDoTuyChinh(model,tuNgay,denNgay);
        tkTuyChinhByNgay(model,tuNgay,denNgay);
        tocDoTangTruong(model);
        model.addAttribute("spBanChay",hoaDonService.getPageSanPhamBanChayTuyChinh(page,tuNgay,denNgay));
        model.addAttribute("spSapHet",hoaDonService.getPageSanPhamSapHet(soLuong,pageSp));
        model.addAttribute("soLuong",soLuong);
        return "quan_ly/thong_ke/index";
    }

    @GetMapping("/api/read-file-thong-ke/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile (@PathVariable("id") int id){
        try {
            String photo = taiKhoanDao.findUrlAnhDaiDien(id);
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void addAttributeThuocTinh(Model model) {
        model.addAttribute("tienNgay", hoaDonService.getTongTienByNgay());
        model.addAttribute("tienTuan", hoaDonService.getTongTienByTuan());
        model.addAttribute("tienThang", hoaDonService.getTongTienByThang());
        model.addAttribute("tienNam", hoaDonService.getTongTienByNam());
        model.addAttribute("donTCNgay", hoaDonService.getDonTCByNgay());
        model.addAttribute("donTCTuan", hoaDonService.getDonTCByTuan());
        model.addAttribute("donTCThang", hoaDonService.getDonTCByThang());
        model.addAttribute("donTCNam", hoaDonService.getDonTCByNam());
        model.addAttribute("donHuyNgay", hoaDonService.getDonHuyByNgay());
        model.addAttribute("donHuyTuan", hoaDonService.getDonHuyByTuan());
        model.addAttribute("donHuyThang", hoaDonService.getDonHuyByThang());
        model.addAttribute("donHuyNam", hoaDonService.getDonHuyByNam());
//        model.addAttribute("donTraNgay", hoaDonService.getDonTraByNgay());
//        model.addAttribute("donTraTuan", hoaDonService.getDonTraByTuan());
//        model.addAttribute("donTraThang", hoaDonService.getDonTraByThang());
//        model.addAttribute("donTraNam", hoaDonService.getDonTraByNam());
        model.addAttribute("spNgay", hoaDonService.getSpByNgay());
        model.addAttribute("spTuan", hoaDonService.getSpByTuan());
        model.addAttribute("spThang", hoaDonService.getSpByThang());
        model.addAttribute("spNam", hoaDonService.getSpByNam());
    }

    public void BieuDoMacDinh(Model model){
        model.addAttribute("hdHuy", hoaDonService.tongHoaDonHuyChoTtChoGh(6));
        model.addAttribute("hdChoXacNhan", hoaDonService.tongHoaDonHuyChoTtChoGh(1));
        model.addAttribute("hdXacNhan", hoaDonService.getSoLuongHoaDonXacNhan(2));
        model.addAttribute("hdChoGiaoHang", hoaDonService.tongHoaDonHuyChoTtChoGh(3));
        model.addAttribute("hdGiaoHang", hoaDonService.getSoLuongHoaDonGiaoHang(4));
        model.addAttribute("hdHoanThanh", hoaDonService.getSoLuongHoaDon(5));
    }
    public void BieuDoNgay(Model model){
        model.addAttribute("hdHuy", hoaDonService.getSoLuongHoaDonHuyChoChoGiaoNgay(6));
        model.addAttribute("hdChoXacNhan", hoaDonService.getSoLuongHoaDonHuyChoChoGiaoNgay(1));
        model.addAttribute("hdXacNhan", hoaDonService.getSoLuongHoaDonXacNhanNgay(2));
        model.addAttribute("hdChoGiaoHang", hoaDonService.getSoLuongHoaDonHuyChoChoGiaoNgay(3));
        model.addAttribute("hdGiaoHang", hoaDonService.getSoLuongHoaDonDangGiaoNgay(4));
        model.addAttribute("hdHoanThanh", hoaDonService.getSoLuongHoaDonHoanThanhNgay(5));
    }
    public void BieuDoTuan(Model model){
        model.addAttribute("hdHuy", hoaDonService.getDonHuyByTuan());
        model.addAttribute("hdChoXacNhan", hoaDonService.getDonChoXacNhanByTuan());
        model.addAttribute("hdXacNhan", hoaDonService.getSoLuongHoaDonXacNhanTuan(2));
        model.addAttribute("hdChoGiaoHang", hoaDonService.getDonChoGiaoHangByTuan());
        model.addAttribute("hdGiaoHang", hoaDonService.getSoLuongHoaDonGiaoHangTuan(4));
        model.addAttribute("hdHoanThanh", hoaDonService.getSoLuongHoaDonTuan(5));
    }
    public void BieuDoThang(Model model){
        model.addAttribute("hdHuy", hoaDonService.getDonHuyByThang());
        model.addAttribute("hdChoXacNhan", hoaDonService.getDonChoXacNhanByThang());
        model.addAttribute("hdXacNhan", hoaDonService.getSoLuongHoaDonXacNhanThang(2));
        model.addAttribute("hdChoGiaoHang", hoaDonService.getDonChoGiaoHangByThang());
        model.addAttribute("hdGiaoHang", hoaDonService.getSoLuongHoaDonGiaoHangThang(4));
        model.addAttribute("hdHoanThanh", hoaDonService.getSoLuongHoaDonThang(5));
    }
    public void BieuDoNam(Model model){
        model.addAttribute("hdHuy", hoaDonService.getDonHuyByNam());
        model.addAttribute("hdChoXacNhan", hoaDonService.getDonChoXacNhanByNam());
        model.addAttribute("hdXacNhan", hoaDonService.getSoLuongHoaDonXacNhanNam(2));
        model.addAttribute("hdChoGiaoHang", hoaDonService.getDonChoGiaoHangByNam());
        model.addAttribute("hdGiaoHang", hoaDonService.getSoLuongHoaDonGiaoHangNam(4));
        model.addAttribute("hdHoanThanh", hoaDonService.getSoLuongHoaDonNam(5));
    }

    public void tkTuyChinh(Model model){
        model.addAttribute("tien", hoaDonService.getTongTien());
        model.addAttribute("donTC", hoaDonService.getDonTC());
        model.addAttribute("donHuy", hoaDonService.getDonHuy());
        model.addAttribute("sp", hoaDonService.getSp());
    }

    public void tkTuyChinhByNgay(Model model, String tuNgay, String denNgay){
        model.addAttribute("tien", hoaDonService.getTongTienTuyChinh(tuNgay, denNgay));
        model.addAttribute("donTC", hoaDonService.getDonTCTuyChinh(tuNgay, denNgay));
        model.addAttribute("donHuy", hoaDonService.getDonHuyTuyChinh(tuNgay, denNgay));
        model.addAttribute("sp", hoaDonService.getSpTuyChinh(tuNgay, denNgay));
    }
    public void BieuDoTuyChinh(Model model,String tuNgay, String denNgay){
        model.addAttribute("hdHuy", hoaDonService.getSoLuongHoaDonHuyChoTtChoGhTuyChinh(6,tuNgay,denNgay));
        model.addAttribute("hdChoXacNhan", hoaDonService.getSoLuongHoaDonHuyChoTtChoGhTuyChinh(1,tuNgay,denNgay));
        model.addAttribute("hdXacNhan", hoaDonService.getSoLuongHoaDonXacNhanTuyChinh(2,tuNgay,denNgay));
        model.addAttribute("hdChoGiaoHang", hoaDonService.getSoLuongHoaDonHuyChoTtChoGhTuyChinh(3,tuNgay,denNgay));
        model.addAttribute("hdGiaoHang", hoaDonService.getSoLuongHoaDonGiaoHangTuyChinh(4,tuNgay,denNgay));
        model.addAttribute("hdHoanThanh", hoaDonService.getSoLuongHoaDonHoanThanhTuyChinh(5,tuNgay,denNgay));
    }

    public void tocDoTangTruong(Model model){
        BigDecimal doanhThuNgayHomQua= hoaDonService.getTongTienByNgayHomQua();
        BigDecimal doanhThuTuanTruoc= hoaDonService.getTongTienByTuanTruoc();
        BigDecimal doanhThuThangTruoc= hoaDonService.getTongTienByThangTruoc();
        BigDecimal doanhThuNamTruoc= hoaDonService.getTongTienByNamTruoc();
        BigDecimal donNgayHomQua=hoaDonService.getDonTCByNgayHomQua();
        BigDecimal donTuanTruoc=hoaDonService.getDonTCByTuanTruoc();
        BigDecimal donThangTruoc=hoaDonService.getDonTCByThangTruoc();
        BigDecimal donNamTruoc=hoaDonService.getDonTCByNamTruoc();
        BigDecimal spNgayHomQua=hoaDonService.getSpByNgayHomQua();
        BigDecimal spTuanTruoc=hoaDonService.getSpByTuanTruoc();
        BigDecimal spThangTruoc=hoaDonService.getSpByThangTruoc();
        BigDecimal spNamTruoc=hoaDonService.getSpByNamTruoc();

        BigDecimal doanhThuNgay= hoaDonService.getTongTienByNgay();
        BigDecimal doanhThuTuan= hoaDonService.getTongTienByTuan();
        BigDecimal doanhThuThang= hoaDonService.getTongTienByThang();
        BigDecimal doanhThuNam= hoaDonService.getTongTienByNam();
        BigDecimal donNgay=hoaDonService.getDonTCByNgay();
        BigDecimal donTuan=hoaDonService.getDonTCByTuan();
        BigDecimal donThang=hoaDonService.getDonTCByThang();
        BigDecimal donNam=hoaDonService.getDonTCByNam();
        BigDecimal spNgay=hoaDonService.getSpByNgay();
        BigDecimal spTuan=hoaDonService.getSpByTuan();
        BigDecimal spThang=hoaDonService.getSpByThang();
        BigDecimal spNam=hoaDonService.getSpByNam();

        model.addAttribute("checkDTNgay",getCheck(doanhThuNgay,doanhThuNgayHomQua));
        model.addAttribute("checkDTTuan",getCheck(doanhThuTuan,doanhThuTuanTruoc));
        model.addAttribute("checkDTThang",getCheck(doanhThuThang,doanhThuThangTruoc));
        model.addAttribute("checkDTNam",getCheck(doanhThuNam,doanhThuNamTruoc));
        model.addAttribute("checkDonNgay",getCheck(donNgay,donNgayHomQua));
        model.addAttribute("checkDonTuan",getCheck(donTuan,donTuanTruoc));
        model.addAttribute("checkDonThang",getCheck(donThang,donThangTruoc));
        model.addAttribute("checkDonNam",getCheck(donNam,donNamTruoc));
        model.addAttribute("checkSpNgay",getCheck(spNgay,spNgayHomQua));
        model.addAttribute("checkSpTuan",getCheck(spTuan,spTuanTruoc));
        model.addAttribute("checkSpThang",getCheck(spThang,spThangTruoc));
        model.addAttribute("checkSpNam",getCheck(spNam,spNamTruoc));

        model.addAttribute("phanTramDoanhThuNgay",getPhanTram(doanhThuNgay,doanhThuNgayHomQua));
        model.addAttribute("phanTramDoanhThuTuan",getPhanTram(doanhThuTuan,doanhThuTuanTruoc));
        model.addAttribute("phanTramDoanhThuThang",getPhanTram(doanhThuThang,doanhThuThangTruoc));
        model.addAttribute("phanTramDoanhThuNam",getPhanTram(doanhThuNam,doanhThuNamTruoc));
        model.addAttribute("phanTramDonNgay",getPhanTram(donNgay,donNgayHomQua));
        model.addAttribute("phanTramDonTuan",getPhanTram(donTuan,donTuanTruoc));
        model.addAttribute("phanTramDonThang",getPhanTram(donThang,donThangTruoc));
        model.addAttribute("phanTramDonNam",getPhanTram(donNam,donNamTruoc));
        model.addAttribute("phanTramSpNgay",getPhanTram(spNgay,spNgayHomQua));
        model.addAttribute("phanTramSpTuan",getPhanTram(spTuan,spTuanTruoc));
        model.addAttribute("phanTramSpThang",getPhanTram(spThang,spThangTruoc));
        model.addAttribute("phanTramSpNam",getPhanTram(spNam,spNamTruoc));

    }

    public BigDecimal getPhanTram(BigDecimal pt1, BigDecimal pt2){
        if(pt1.compareTo(new BigDecimal(0))==0 && pt2.compareTo(new BigDecimal(0))>0){
            return new BigDecimal(100);
        } else if(pt1.compareTo(new BigDecimal(0))>0 && pt2.compareTo(new BigDecimal(0))==0){
            return new BigDecimal(100);
        }else if(pt1.compareTo(new BigDecimal(0))==0 && pt2.compareTo(new BigDecimal(0))==0){
            return new BigDecimal(0);
        }else{
            return pt1.subtract(pt2).divide(pt2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).abs();
        }
    }

    public boolean getCheck(BigDecimal pt1, BigDecimal pt2){
        if(pt1.compareTo(new BigDecimal(0))==0 && pt2.compareTo(new BigDecimal(0))>0){
            return false;
        } else if(pt1.compareTo(new BigDecimal(0))>0 && pt2.compareTo(new BigDecimal(0))==0){
            return true;
        }else if(pt1.compareTo(new BigDecimal(0))==0 && pt2.compareTo(new BigDecimal(0))==0){
            return true;
        } else if(pt1.compareTo(pt2)<0){
            return false;
        }else if(pt1.compareTo(pt2)>0){
            return true;
        }
        return true;
    }
}
