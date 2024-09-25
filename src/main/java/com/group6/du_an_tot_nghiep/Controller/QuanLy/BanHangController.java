package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.HoaDonChiTietDao;
import com.group6.du_an_tot_nghiep.Service.QuanLy.HoaDonService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BanHangController {



    private final HoaDonService service;

    private final HoaDonChiTietDao billDetailRepository;

    private final SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    public BanHangController(
            HoaDonService service,
            HoaDonChiTietDao billDetailRepository,
            SanPhamChiTietService sanPhamChiTietService
    ) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.service = service;
        this.billDetailRepository = billDetailRepository;
    }

    @GetMapping("/ban-hang-tai-quay")
    public String banHang(){
        return "quan-ly/ban_hang_tai_quay/index";
    }



//    @GetMapping("/admin/sell")
//    public String index(Model model, @RequestParam("pageProduct") Optional<Integer> pageProduct) {
//        List<HoaDon> billsWait = repository.findByTrangThai(0);
//        Pageable pageProductList = PageRequest.of(pageProduct.orElse(0), 5);
//        Page<SanPhamChiTiet> productDetail = sanPhamChiTietService.findAllByTrangThai(1, pageProductList);
//
//        model.addAttribute("billWait", billsWait);
//        model.addAttribute("pageData", productDetail);
//
//        return "quan-ly/ban_hang_tai_quay/index";
//    }
//
//    @GetMapping("/admin/create-bill")
//    public String createBill() {
//        Random random = new Random();
//        Integer number1 = random.nextInt(character.size() - 1);
//        Integer number2 = random.nextInt(character.size() - 1);
//        Integer number3 = random.nextInt(character.size() - 1);
//        Integer number4 = random.nextInt(character.size() - 1);
//        Integer number5 = random.nextInt(character.size() - 1);
//
//        HoaDon hoaDon = new HoaDon();
//        hoaDon.setMaHoaDon(character.get(number1) + character.get(number2) + character.get(number3) + character.get(number4) + character.get(number5));
//        hoaDon.setTrangThai(0);
//        hoaDon.setNgayTao(new Timestamp(System.currentTimeMillis()));
//
//        repository.save(hoaDon);
//        return "redirect:/admin/sell";
//    }
//
//    @GetMapping("/admin/bill/{billCode}")
//    public String detailBill(@PathVariable("billCode") String billCode, Model model, @RequestParam("pageInCart") Optional<Integer> pageInCart) {
//        Optional<HoaDon> billOp = repository.findByMaHoaDon(billCode);
//        Integer billId = billOp.get().getId();
//        Pageable pageProductInCart = PageRequest.of(pageInCart.orElse(0), 5);
//        Page<HoaDonChiTiet> billDetails = billDetailRepository.findByIdHoaDon(billId, pageProductInCart);
//
//        model.addAttribute("bill", billOp.get());
//        model.addAttribute("billDetail", billDetails);
//
//        return "quan-ly/ban_hang_tai_quay/index";
//
//    }

}
