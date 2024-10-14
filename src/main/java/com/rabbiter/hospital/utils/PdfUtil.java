package com.rabbiter.hospital.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.rabbiter.hospital.pojo.Orders;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PdfUtil {

    public static void ExportPdf(HttpServletRequest request, HttpServletResponse response, Orders order) throws Exception {
        response.setHeader("content-Type", "application/pdf");
        //attachment下载打印， inline预览
        response.setHeader("Content-Disposition", "inline;filename=病情报告单.pdf");

        // 使用品牌颜色
        BaseColor brandColor = WebColors.getRGBColor("#0072C6"); // 假设这是医院的品牌颜色

        // 创建字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font titleFont = new Font(bfChinese, 24, Font.BOLD, brandColor);
        Font subtitleFont = new Font(bfChinese, 16, Font.NORMAL, brandColor);
        Font contentFont = new Font(bfChinese, 12, Font.NORMAL, BaseColor.BLACK);
        Font smallContentFont = new Font(bfChinese, 10, Font.NORMAL, BaseColor.BLACK);

        // 创建文档
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // 添加logo
        PdfContentByte cb = writer.getDirectContent();
        Image logo = Image.getInstance("src/main/resources/static/images/logo.png");
        logo.scaleToFit(100, 100);
        logo.setAbsolutePosition(480, 760); // 右上角位置
        cb.addImage(logo);

        // 添加标题
        Paragraph titleParagraph = new Paragraph("XX医院病情报告单", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);

        // 添加打印时间
        Paragraph tipsParagraph = new Paragraph("打印时间：" + TodayUtil.getTodayYmd(), subtitleFont);
        tipsParagraph.setAlignment(Element.ALIGN_CENTER);
        tipsParagraph.setSpacingBefore(10);
        document.add(tipsParagraph);

        // 患者信息表格
        PdfPTable tableMessage = new PdfPTable(4);
        tableMessage.setSpacingBefore(20f);
        tableMessage.setSpacingAfter(10f);
        tableMessage.setWidths(new float[]{60, 100, 60, 100});
        tableMessage.getDefaultCell().setBorderWidth(1);
        tableMessage.getDefaultCell().setBackgroundColor(brandColor);
        tableMessage.getDefaultCell().setPadding(20);

        addCell(tableMessage, "姓名：", contentFont, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tableMessage, order.getPatient().getPName(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableMessage, "性别：", contentFont, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tableMessage, order.getPatient().getPGender(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);

        addCell(tableMessage, "年龄：", contentFont, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tableMessage, order.getPatient().getPAge() + " 岁", contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableMessage, "单号：", contentFont, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tableMessage, String.valueOf(order.getOId()), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);

        addCell(tableMessage, "诊断日期：", contentFont, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tableMessage, order.getOEnd(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableMessage, "联系电话：", contentFont, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tableMessage, order.getPatient().getPPhone(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);

        document.add(tableMessage);

        // 病情表格
        PdfPTable tableOrder = new PdfPTable(1);
        tableOrder.setSpacingBefore(30f);
        tableOrder.setSpacingAfter(20f);
        tableOrder.getDefaultCell().setBorderWidth(1);
        tableOrder.getDefaultCell().setBackgroundColor(brandColor);
        tableOrder.getDefaultCell().setPadding(5);

        addCell(tableOrder, "症状", subtitleFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableOrder, order.getORecord(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);

        addCell(tableOrder, "检查项目及价格", subtitleFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableOrder, order.getOCheck(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);

        addCell(tableOrder, "药物及价格", subtitleFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableOrder, order.getODrug(), contentFont, Element.ALIGN_RIGHT, BaseColor.WHITE);

        addCell(tableOrder, "诊断/医生意见", subtitleFont, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tableOrder, order.getOAdvice(), contentFont, Element.ALIGN_LEFT, BaseColor.WHITE);

        document.add(tableOrder);

        // 添加分隔线
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(brandColor);
        document.add(lineSeparator);

        // 设置底部版权说明
        cb.beginText();
        cb.setFontAndSize(bfChinese, 11);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "该报告单仅供参考", 300, 40, 0);
        cb.setFontAndSize(bfChinese, 13);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "版权医院所有", 300, 20, 0);
        cb.endText();

        // 关闭文档
        document.close();
    }

    private static void addCell(PdfPTable table, String text, Font font, int alignment, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(backgroundColor);
        BaseColor brandColor = WebColors.getRGBColor("#0072C6");
        cell.setBorderColor(brandColor); // 使用品牌颜色作为边框
        cell.setPadding(5);
        table.addCell(cell);
    }
}
