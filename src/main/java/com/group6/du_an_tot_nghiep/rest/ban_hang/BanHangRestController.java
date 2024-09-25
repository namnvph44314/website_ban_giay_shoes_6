package com.group6.du_an_tot_nghiep.rest.ban_hang;

import com.group6.du_an_tot_nghiep.Dao.HoaDonChiTietDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.*;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.VoucherResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TkResponse;
import com.group6.du_an_tot_nghiep.Entities.*;
import com.group6.du_an_tot_nghiep.Service.QuanLy.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class BanHangRestController {

    private final HoaDonService service;

    private final HoaDonChiTietDao billDetailRepository;

    private final SanPhamChiTietService sanPhamChiTietService;

    private final HoaDonChiTietService billDetailService;

    private final PhieuGiamGiaService voucherService;

    private final TaiKhoanService taiKhoanService;

    private final HoaDonService hoaDonService;

    private final PdfService pdfService;

    private final SanPhamService sanPhamService;



    @Autowired
    public BanHangRestController(
            HoaDonService service,
            HoaDonChiTietDao billDetailRepository,
            SanPhamChiTietService sanPhamChiTietService,
            HoaDonChiTietService billDetailService,
            PhieuGiamGiaService voucherService,
            TaiKhoanService taiKhoanService,
            HoaDonService hoaDonService,
            PdfService pdfService,
            SanPhamService sanPhamService
    ) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.service = service;
        this.billDetailRepository = billDetailRepository;
        this.billDetailService = billDetailService;
        this.voucherService = voucherService;
        this.taiKhoanService = taiKhoanService;
        this.hoaDonService = hoaDonService;
        this.pdfService = pdfService;
        this.sanPhamService = sanPhamService;
    }

    @GetMapping("/bill-wait")
    public ResponseEntity<List<HoaDon>> getHoaDonCho(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getHoaDonCho());
    }

    @GetMapping("/bill/create-bill")
    public ResponseEntity<HoaDon> createBill(){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createEmptyBill());
    }

    @GetMapping("/products")
    public ResponseEntity<Page<SanPhamChiTiet>> getAllProduct(@RequestParam("page") Optional<Integer> page){
        Pageable pageProductList = PageRequest.of(page.orElse(0), 6);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sanPhamChiTietService.findAllByTrangThai(1, pageProductList));
    }

    @GetMapping("/bill/{id}")
    public ResponseEntity<List<HoaDonChiTiet>> getDetailBill(@PathVariable("id") Integer id){
        List<HoaDonChiTiet> list = billDetailRepository.getAllHdctById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @PostMapping("/bill/add-product")
    public ResponseEntity<HoaDonChiTiet> addProductToBill(@RequestBody AddProductToBillRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(billDetailService.addProductToBill(request));
    }

    @GetMapping("/bill/tong-tien/{id}")
    public ResponseEntity<Integer> getTongTien(@PathVariable("id") Integer billId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(billDetailService.getTongTien(billId));
    }

    @GetMapping("/voucher/auto/{id}")
    public ResponseEntity<ListPggGioHang> autoFillVoucher(@PathVariable("id") Integer billId, @RequestParam("tongTien") BigDecimal tongTien, @RequestParam("idCustomer") Optional<Integer> idCustomer){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voucherService.getTopVoucher(billId, tongTien, idCustomer));
    }

    @PostMapping("/bill/delete-product")
    public ResponseEntity<Integer> deleteProduct(@RequestBody DeleteProductRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.deleteProduct(request));
    }

    @GetMapping("/customers")
    public ResponseEntity<Page<TaiKhoan>> getListCustomer(@RequestParam("page") Optional<Integer> page){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taiKhoanService.getPage(page.orElse(0)));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<TkResponse> selectCustomer(@PathVariable("id") Integer id, @RequestParam("billId") Integer billId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taiKhoanService.getCustomerById(id, billId));
    }

    @GetMapping("/bill-info/{id}")
    public ResponseEntity<BillResponse> getBill(@PathVariable("id") Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.getBillInfo(id));
    }

    @GetMapping("/address/customer/{id}")
    public ResponseEntity<List<DiaChi>> getAddressCustomer(@PathVariable("id") Integer idCustomer){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.getAddressCustomer(idCustomer));
    }

    @GetMapping("/address/default/{id}")
    public ResponseEntity<DiaChi> getAddressDefault(@PathVariable("id") Integer idCustomer){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.getAddressDefault(idCustomer));
    }

    @PostMapping("/select-address/{id}")
    public ResponseEntity<DiaChi> selectAddress(@PathVariable("id") Integer idAddress, @RequestBody Integer billId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.selectAddress(idAddress, billId));
    }

    @PostMapping("/pay-history")
    public ResponseEntity<LichSuTraTien> savePayHistory(@RequestBody LichSuTraTien data){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.savePayHistory(data));
    }

    @GetMapping("/count-money/{billId}")
    public ResponseEntity<Integer> countMoneyPay(@PathVariable("billId") Integer billId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.sumMoney(billId));
    }

    @GetMapping("/history-pay-offline/{billId}")
    public ResponseEntity<List<LichSuTraTien>> getLichSuTraTienMat(@PathVariable("billId") Integer billId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.getLichSuTraTienMat(billId));
    }

    @GetMapping("/history-pay-online/{billId}")
    public ResponseEntity<List<LichSuTraTien>> getLichSuChuyenKhoan(@PathVariable("billId") Integer billId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hoaDonService.getLichSuChuyenKhoan(billId));
    }

    @GetMapping("/history-pay/{billId}")
    public ResponseEntity<List<LichSuTraTien>> getLichSuTraTien(@PathVariable("billId") Integer billId){
        return ResponseEntity.status(HttpStatus.OK).body(hoaDonService.getLichSuTraTien(billId));
    }

    @GetMapping("/customer-pay-money/{id}")
    public ResponseEntity<Integer> getTienKhachTra(@PathVariable("id") Integer billId){
        return ResponseEntity
                .ok(hoaDonService.getTienKhachTra(billId));
    }

    @PostMapping("/final-checkout/{id}")
    public ResponseEntity<Integer> finalCheckout(@PathVariable("id") Integer billId, @RequestBody FinalCheckoutRequest request){
        return ResponseEntity
                .ok(hoaDonService.finalCheckout(billId, request));
    }

    @GetMapping("/get-so-luong/{idSPCT}")
    public ResponseEntity<Integer> getSoLuongSanPham(@PathVariable("idSPCT") Integer idSPCT){
        return ResponseEntity.ok(hoaDonService.getSoLuong(idSPCT));
    }

    @GetMapping("/generate-pdf/{id}")
    public void generatePDF(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfService.orderCouter(response, id);
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<Page<SanPhamChiTiet>> findSPCTByName(@RequestParam("name") String name, @RequestParam("page") Optional<Integer> page){
        return ResponseEntity.ok(sanPhamChiTietService.findSPCTByName(name, page));
    }

    @PostMapping("/edit-amount/{id}")
    public ResponseEntity<Integer> editAmountProduct(@PathVariable("id") Integer billId, @RequestParam("productId") Integer productId, @RequestParam("amount") Integer amountProduct){
        return ResponseEntity.ok(billDetailRepository.updateAmountProduct(billId, amountProduct, productId));
    }

    @GetMapping("/count-bill-wait")
    public ResponseEntity<Integer> countBillWait(){
        return ResponseEntity.ok(hoaDonService.countBillWait());
    }

    @PostMapping("/delete-history-pay/{id}")
    public ResponseEntity<Integer> deleteHistoryPay(@PathVariable("id") Integer id){
        return ResponseEntity.ok(billDetailService.deleteHistoryPay(id));
    }

    @GetMapping("/check-product-name")
    public ResponseEntity<Boolean> checkProductMatch(@RequestParam("name") String productName){
        return ResponseEntity.ok(sanPhamService.checkProductNameMatch(productName));
    }

    @GetMapping("/get-so-luong-sp-gio-hang/{id}")
    public ResponseEntity<Integer> getSoLuongSPGioHang(@PathVariable("id") Integer billId, @RequestParam("productId") Integer productId){
        return ResponseEntity.ok(hoaDonService.getSoLuongSPTrongGioHang(billId, productId));
    }

    @GetMapping("/update-amount-product/{id}")
    public ResponseEntity<Integer> updateAmountProduct(@PathVariable("id") Integer billId, @RequestParam("productId") Integer productId, @RequestParam("amount") Integer amount){
        return ResponseEntity.ok(hoaDonService.updateAmountProduct(billId, productId, amount));
    }

    @GetMapping("/delete-bill/{id}")
    public ResponseEntity<Integer> deleteEmptyBill(@PathVariable("id") Integer billId){
        return ResponseEntity.ok(hoaDonService.deleteEmptyBill(billId));
    }

}
