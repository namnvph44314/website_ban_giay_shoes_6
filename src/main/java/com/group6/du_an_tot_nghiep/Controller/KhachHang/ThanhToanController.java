package com.group6.du_an_tot_nghiep.Controller.KhachHang;

import com.group6.du_an_tot_nghiep.Config.Config;
import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.*;
import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.KhachHang.GioHangService;
import com.group6.du_an_tot_nghiep.Service.KhachHang.ThanhToanService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ThanhToanController {

    @Autowired
    TaiKhoanService _taiKhoanService;

    TaiKhoan _taiKhoan;

    GioHang _gioHang;
    @Autowired
    ThanhToanService thanhToanService;

    @Autowired
    GioHangService gioHangService;

    ThanhToanHoaDon _thanhToanHoaDon = new ThanhToanHoaDon();

    @Autowired
    HttpSession session;

    Boolean _luuDiaChi = false;
    @GetMapping("/khach-hang/thanh-toan-index")
    public String index (Model model) {
        model.addAttribute("searchRequest",new KhachHangSanPhamSearchRequest());
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        return "/khach_hang/thanh_toan";
    }

    @GetMapping("/khach-hang/thanh-toan-dat-hang-thanh-cong")
    public String datHangThanhCong () {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");

        return "/khach_hang/dat_hang_thanh_cong";
    }

    @GetMapping("/api/get-gio-hang")
    @ResponseBody
    public void getGioHang() {
        try {
            _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @GetMapping("/api/luu-dia-chi")
    @ResponseBody
    public void luuDiaChi() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            if (_luuDiaChi == true) {
                boolean result = thanhToanService.luuDiaChi(_thanhToanHoaDon.getThongTinKhachHang(),_taiKhoan);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/api/get-tai-khoan")
    @ResponseBody
    public ResponseEntity<TaiKhoan> getTaiKhoan() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            return ResponseEntity.ok()
                    .body(_taiKhoan);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/api/get-thanh-toan-hoa-don")
    @ResponseBody
    public ResponseEntity<ThanhToanHoaDon> getThanhToanHoaDon() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = gioHangService.getGioHangByTaiKhoan(_taiKhoan);
        ThanhToanHoaDon thanhToanHoaDon = thanhToanService.getThanhToanHoaDon(_gioHang,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(thanhToanHoaDon);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/get-list-dia-chi")
    @ResponseBody
    public ResponseEntity<List<ListDiaChiResponse>> getListDiaChi() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        List<ListDiaChiResponse> listDiaChiResponses = thanhToanService.getListDiaChi(_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(listDiaChiResponses);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/update-dia-chi-mac-dinh/{idDiaChi}")
    @ResponseBody
    public ResponseEntity<Boolean> updateDiaChiMacDinh(@PathVariable int idDiaChi) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        Boolean result = thanhToanService.updateDiaChiMacDinh(idDiaChi,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/update-phi-ship/{tienShip}")
    @ResponseBody
    public ResponseEntity<Boolean> updatePhiShip(@PathVariable BigDecimal tienShip) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        Boolean result = thanhToanService.updatePhiShip(tienShip,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/update-ghi-chu/{ghiChu}")
    @ResponseBody
    public ResponseEntity<Boolean> updateGhiChu(@PathVariable String ghiChu) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        Boolean result = thanhToanService.updateGhiChu(ghiChu,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/update-hinh-thuc-thanh-toan/{hinhThuc}")
    @ResponseBody
    public ResponseEntity<Boolean> updateHinhThucThanhToan(@PathVariable int hinhThuc) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        Boolean result = thanhToanService.updateHinhThucThanhToan(hinhThuc,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/api/check-so-luong-san-pham")
    @ResponseBody
    public ResponseEntity<ListGioHangChiTietResponse> checkSoLuongSp(@RequestBody List<ListGioHangChiTietResponse> listGioHangChiTietResponse) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        ListGioHangChiTietResponse response = thanhToanService.checkSoLuongSp(listGioHangChiTietResponse, _taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(response);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/check-pgg/{idPgg}")
    @ResponseBody
    public ResponseEntity<Integer> checkSoLuongSp(@PathVariable int idPgg) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        Integer idPggResult = thanhToanService.checkPgg(idPgg,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(idPggResult);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/api/dat-hang")
    @ResponseBody
    public ResponseEntity<Boolean> datHang() {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        boolean result = thanhToanService.datHang(_thanhToanHoaDon,_taiKhoan);
        try {
            return ResponseEntity.ok()
                    .body(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/api/luu-thanh-toan-hoa-don")
    @ResponseBody
    public void luuThanhToanHoaDon(@RequestBody ThanhToanHoaDon thanhToanHoaDon, @RequestParam Optional<Boolean> luuDiaChi) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _thanhToanHoaDon = thanhToanHoaDon;
        _luuDiaChi = luuDiaChi.orElse(false);
    }

    @GetMapping("/api/thanh-toan-vnp/{tongTien}")
    @ResponseBody
    public ResponseEntity<PaymentUrl> checkSoLuongSp(HttpServletRequest request,@PathVariable BigDecimal tongTien) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            BigDecimal amount = tongTien.setScale(0,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
            String bankCode = "VNBANK";

            String vnp_TxnRef = Config.getRandomNumber(8);
            String vnp_IpAddr = Config.getIpAddress(request);

            String vnp_TmnCode = Config.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", bankCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
            return ResponseEntity.ok()
                    .body(new PaymentUrl(200,paymentUrl));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/khach-hang/thanh-toan/check-thanh-toan-vnp")
    public String checkThanhToanVnp(Model model, @RequestParam String vnp_ResponseCode) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        PaymentUrl paymentUrl = new PaymentUrl();
        try {
                if ("00".equals(vnp_ResponseCode)) {
                    return "/khach_hang/dat_hang_thanh_cong";
                } else {
                    return "redirect:/khach-hang/thanh-toan-index";
                }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
