package com.group6.du_an_tot_nghiep.Controller.Chung;

import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.EmailService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
public class DangNhapController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    EmailService emailService;

    private String _email = "";

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String displayLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "register", required = false) String register,
            Model model
    ){
        String errorMessage = "";

        if (error != null) {
            errorMessage = "Tài khoản hoặc mật khẩu không đúng";
        }

        if (logout != null) {
            errorMessage = "Bạn đã đăng xuất thành công";
        }

        if (register != null) {
            errorMessage = "Bạn đã đăng ký thành công";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "/chung/dang_nhap";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }


    @PostMapping("/register-user")
    @ResponseBody
    public TaiKhoan register(@RequestBody TaiKhoan request){
        return taiKhoanService.registerUser(request);
    }

    @PostMapping("/api/send-code-verify")
    @ResponseBody
    public Integer sendCodeVerify(@RequestBody String email){
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);

        String subject = "Mã xác nhận quên mật khẩu của bạn";
        String body = "Xin chào " + email + ",\n\n" +
                "Đây là mã xác nhận lấy lại mật khẩu của bạn.\n" +
                "Vui lòng không cung cấp mã này cho người lạ \n" +
                "Mã của bạn là: " + number + "\n\n" +
                "Vui lòng sử dụng để lấy lại mật khẩu";
        emailService.sendEmail(email, subject, body);
        return number;
    }

    @GetMapping("/dang-ky")
    public String dangKy (Model model) {
        return "/chung/dang_ky";
    }
    @GetMapping("/quen-mat-khau")
    public String quanPass (Model model) {
        return "/chung/quen_mat_khau";
    }
    @GetMapping("/dat-lai-mat-khau")
    public String datLai (Model model, @RequestParam("email") String email) {
        _email = email;
        return "/chung/dat_lai_mat_khau";
    }

    @GetMapping("/api/re-change-password")
    @ResponseBody
    public void rechangePassword(@RequestParam("password") String password){
        taiKhoanService.reChangePassword(password, _email);
    }

    @GetMapping("/api/test/addUser")
    public String addUser () {
        return "/userAddTest";
    }

    @GetMapping("/api/test/qlsp")
    public String QLSP () {
        return "/QLSPTest";
    }

    @GetMapping("/api/test/trang-mau")
    public String banOff () {
        return "/trang_mau";
    }

    @GetMapping("/api/test/test")
    public String test () {
        return "/test";
    }
}
