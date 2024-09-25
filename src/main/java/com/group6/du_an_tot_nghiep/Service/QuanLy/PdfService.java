package com.group6.du_an_tot_nghiep.Service.QuanLy;


import com.group6.du_an_tot_nghiep.Dao.HoaDonChiTietDao;
import com.group6.du_an_tot_nghiep.Dao.HoaDonDao;
import com.group6.du_an_tot_nghiep.Entities.HoaDon;
import com.group6.du_an_tot_nghiep.Entities.HoaDonChiTiet;
import com.group6.du_an_tot_nghiep.Util.FormatNumber;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PdfService {


private static final String FONT_ARIAL="D:\\du_an_tot_nghiep\\lib\\font\\oki.TTF";
    @Autowired
    private HoaDonDao hoaDonDao;

    @Autowired
    private HoaDonChiTietDao hoaDonChiTietDao;

    public void orderCouter(HttpServletResponse response, Integer idHoaDon) throws IOException {

        Optional<HoaDon> hoaDon = hoaDonDao.findById(idHoaDon);
        if (!hoaDon.isPresent()) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon);
        }

        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            BaseFont bf = BaseFont.createFont(FONT_ARIAL, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 18, Font.BOLD);
            Font fontParagraph = new Font(bf, 12, Font.BOLD);


            // Thêm logo
//            String logoPath = "/path/to/logo.png"; // Đường dẫn tới tệp logo
//            Image logo = Image.getInstance(logoPath);
//            logo.setAlignment(Image.ALIGN_CENTER);
//            logo.scaleToFit(100, 100); // Thay đổi kích thước logo nếu cần
//            document.add(logo);

            PdfPTable lineTable = new PdfPTable(1);
            lineTable.setWidthPercentage(100);
            PdfPCell lineCell = new PdfPCell(new Phrase(""));
            lineCell.setBorder(Rectangle.BOTTOM);
            lineCell.setBorderWidth(1);
            lineCell.setPaddingBottom(5);
            lineCell.setBorderColor(BaseColor.BLACK);
            lineTable.addCell(lineCell);

            Paragraph paragraph = new Paragraph("SHOES6", fontTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(new Paragraph("\n"));
            document.add(lineTable);

            Paragraph paragraph2 = new Paragraph("Số điện thoại: 0979583823", fontParagraph);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph2);

            Paragraph paragraph3 = new Paragraph("Email: shoes6@gmail.com", fontParagraph);
            paragraph3.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph3);

            Paragraph paragraph4 = new Paragraph("Địa chỉ: Cổng số 1, Tòa nhà FPT Polytechnic, 13 phố Trịnh Văn Bô, phường Phương Canh, quận Nam Từ Liêm, TP Hà Nội", fontParagraph);
            paragraph4.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph4);

            Paragraph paragraph5 = new Paragraph("Hóa đơn ", fontTitle);
            paragraph5.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph5);

            Paragraph paragraph12 = new Paragraph(hoaDon.get().getMaHoaDon(), fontParagraph);
            paragraph12.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph12);

            Paragraph paragraph6 = new Paragraph("Ngày mua: " + hoaDon.get().getNgayTao(), fontParagraph);
            paragraph6.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph6);

            Paragraph paragraph7 = new Paragraph("Khách hàng: " + hoaDon.get().getHoVaTen(), fontParagraph);
            paragraph7.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph7);

            if (hoaDon.get().getDiaChi() != null) {
                Paragraph paragraph8 = new Paragraph("Địa chỉ: " + hoaDon.get().getDiaChi(), fontParagraph);
                paragraph8.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph8);
            }

            if (hoaDon.get().getSoDienThoaiNhan() != null) {
                Paragraph paragraph9 = new Paragraph("Số điện thoại: " + hoaDon.get().getDiaChi(), fontParagraph);
                paragraph9.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph9);
            }

            if(hoaDon.get().getLoaiDonHang() == 0){
                Paragraph paragraph10 = new Paragraph("Nhân viên bán hàng: " + hoaDon.get().getNguoiTao(), fontParagraph);
                paragraph10.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph10);
            }

            Paragraph paragraph11 = new Paragraph("DANH SÁCH SẢN PHẨM KHÁCH HÀNG MUA", fontParagraph);
            paragraph11.setSpacingBefore(15.0f);
            paragraph11.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph11);
            document.add(new Paragraph("\n")); // Thêm một dòng trống

            Font fontTableHeader = new Font(bf, 12, Font.BOLD);
            String[] tableHeaders = {"STT", "Sản phẩm", "Số lượng", "Giá tiền", "Tổng tiền"};
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 4, 1, 2, 2});


            for (String header : tableHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontTableHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            BigDecimal tienGiaoHang = hoaDon.get().getTienGiaoHang() != null ? hoaDon.get().getTienGiaoHang() : BigDecimal.ZERO;
            BigDecimal tienGiamGia = hoaDon.get().getTienGiamGia() != null ? hoaDon.get().getTienGiamGia() : BigDecimal.ZERO;
            BigDecimal tongTienPhaiThanhToan;

            int stt = 1;
            BigDecimal tongTienSanPham = BigDecimal.ZERO;
            for (HoaDonChiTiet hoaDonChiTiet : hoaDon.get().getHoaDonChiTiets()) {
                if (hoaDonChiTiet.getTrangThai() != 7) {
                    table.addCell(Integer.toString(stt)); // STT
                    table.addCell(hoaDonChiTiet.getSanPhamChiTietByIdSpct().getSanPhamByIdSanPham().getTenSanPham());
                    // Tên sản phẩm
                    table.addCell(Integer.toString(hoaDonChiTiet.getSoLuongSanPham())); // Số lượng


                    table.addCell(FormatNumber.formatBigDecimal(hoaDonChiTiet.getGia()));


                    BigDecimal tongTien = hoaDonChiTiet.getGia().multiply(new BigDecimal(hoaDonChiTiet.getSoLuongSanPham()));
                    table.addCell(FormatNumber.formatBigDecimal(tongTien) + "VND");

                    tongTienSanPham = tongTienSanPham.add(tongTien);
                    stt++;
                }

            }

            tongTienPhaiThanhToan = tongTienSanPham.add(tienGiaoHang).subtract(tienGiamGia);
            Font fontTotal = new Font(bf, 12, Font.BOLDITALIC);
            PdfPCell cellTotalLabel = new PdfPCell(new Phrase("Tổng tiền sản phẩm: " + FormatNumber.formatBigDecimal(tongTienSanPham) + " VND", fontTotal));
            cellTotalLabel.setColspan(5);
            cellTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cellTotalLabel);

            document.add(table);
            document.add(new Paragraph("\n")); // Thêm một dòng trống

            if (hoaDon.get().getTienGiaoHang() != null) {
                Paragraph paragraph20 = new Paragraph("Tiền giao hàng: " + FormatNumber.formatBigDecimal(hoaDon.get().getTienGiaoHang()) + " VND", fontParagraph);
                paragraph20.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph20);
            }

            Paragraph paragraph19;
            if (hoaDon.get().getTienGiamGia() == null) {
                paragraph19 = new Paragraph("Tiền giảm giá: 0 VND", fontParagraph);
            } else {
                paragraph19 = new Paragraph("Tiền giảm giá: " + FormatNumber.formatBigDecimal(hoaDon.get().getTienGiamGia()) + " VND", fontParagraph);
            }
            paragraph19.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph19);

            Paragraph paragraph18 = new Paragraph("Tổng tiền phải thanh toán: " + FormatNumber.formatBigDecimal(tongTienPhaiThanhToan) + " VND", fontParagraph);
            paragraph18.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph18);



            document.add(new Paragraph("\n")); // Thêm một dòng trống

            Paragraph paragraph17 = new Paragraph("----Cảm ơn quý khách !----", fontParagraph);
            paragraph17.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph17);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                if (document.isOpen()) {
                    document.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
  }

}
