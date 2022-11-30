package com.max.restaurant.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

import static com.max.restaurant.utils.UtilsCommandNames.LANG_ATTR;
import static com.max.restaurant.utils.UtilsCommandNames.LOGGED_USER_ATTR;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This class fills the PDF {@link Document} object with current order information. It needs an object of
 * {@link HttpServletRequest} interface to get different data from it (locale, app name, manager`s name, etc.)
 */
public class UtilsPDFReportCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsPDFReportCreator.class);
    private static final String FONT_I_PTH = "/pdffonts/pt_sans_italic.ttf";
    private static final String FONT_Z_PTH = "/pdffonts/pt_sans_bold_italic.ttf";
    private static final Font FONT_I = FontFactory.getFont(FONT_I_PTH, BaseFont.IDENTITY_H, true);
    private static final Font FONT_Z = FontFactory.getFont(FONT_Z_PTH, BaseFont.IDENTITY_H, true);
    private static ResourceBundle bundle;
    private final static String bundleFileName = "messages";
    private static List<String> headers;
    private static final float TAB_INTERVAL = 50f;
    private static final float tableWidth = 80f;
    private final OrderData orderData;
    private final HttpServletRequest request;
    private final Locale locale;
    private final String imageHeaderSourcePath = "/views/images/report_header.jpg";
    private final Document document;


    public UtilsPDFReportCreator(OrderData orderData, HttpServletRequest request) {
        LOGGER.info(CONSTRUCTOR);
        document = new Document();
        this.orderData = orderData;
        this.request = request;
        Locale locale = new Locale(Optional
                .ofNullable((String) request.getSession().getAttribute(LANG_ATTR))
                .orElse((String) request.getServletContext().getAttribute(LANG_ATTR)));
        this.locale = locale;
        bundle = ResourceBundle.getBundle(bundleFileName, locale);
        initTableHeaderNames();
    }

    public void fillPDFReportDocument() {
        LOGGER.info(METHOD_STARTS_MSG, "fillPDFReportDocument", "true");
        try {
            setPDFAttributes(document);
            setPageHeader(document);
            document.add(new Paragraph("\n"));
            setBriefOrderInfo(document);
            document.add(new Paragraph("\n\n\n"));
            setTable(document);
            document.add(new Paragraph("\n\n"));
            setFooter(document);
        } catch (Exception e) {
            LOGGER.error(METHOD_FAILED, document);
            throw new RuntimeException("pdf creation error");
        }
    }

    public ByteArrayOutputStream getPDFStream() throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        fillPDFReportDocument();
        document.close();
        return outputStream;
    }

    private void setPDFAttributes(Document document) {
        LOGGER.info(METHOD_STARTS_MSG, "setPDFAttributes", "true");
        document.addAuthor(getManager());
        document.addCreationDate();
        String appName = request.getContextPath().replaceAll("/", "");   // /mywebapp
        document.addCreator(appName);
        document.addTitle("Order #" + orderData.getCustom().getId());
        document.addSubject("Order report");
    }

    private static void initTableHeaderNames() {
        LOGGER.info(METHOD_STARTS_MSG, "initTableHeaderNames", "true");
        headers = new ArrayList<>();
        headers.add(bundle.getString("order.num"));
        headers.add(bundle.getString("order.dish.name"));
        headers.add(bundle.getString("order.price"));
        headers.add(bundle.getString("order.quantity"));
        LOGGER.info(METHOD_STARTS_MSG, "initTableHeaderNames", headers);
    }

    private void setPageHeader(Document document) throws DocumentException, IOException {
        LOGGER.info(METHOD_STARTS_MSG, "setPageHeader", "true");
        String imagePath = getURL() + imageHeaderSourcePath;
        Image image1 = Image.getInstance(new URL(imagePath));
        float scaler = (document.getPageSize().getWidth() / image1.getWidth()) * 100;
        image1.setPaddingTop(0);
        image1.scalePercent(scaler);
        image1.setAbsolutePosition(0, document.getPageSize().getTop() - image1.getScaledHeight());
        document.add(image1);
    }

    private void setBriefOrderInfo(Document document) throws DocumentException {
        LOGGER.info(METHOD_STARTS_MSG, "setBriefOrderInfo", "true");
        FONT_Z.setSize(14);
        FONT_I.setSize(14);
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(FONT_Z);
        paragraph.setTabSettings(new TabSettings(TAB_INTERVAL));
        paragraph.add(Chunk.TABBING);
        paragraph.setAlignment(Element.IMGTEMPLATE);

        paragraph.add(bundle.getString("order") + orderData.getCustom().getId());
        paragraph.add("\n\n");
        paragraph.add(Chunk.TABBING);
        paragraph.add(bundle.getString("order.customer") + ": ");
        paragraph.setFont(FONT_I);
        paragraph.add(getUserString(orderData.getUser()));
        paragraph.add("\n\n");
        paragraph.add(Chunk.TABBING);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale);
        paragraph.setFont(FONT_Z);
        paragraph.add(bundle.getString("order.created") + " ");
        paragraph.setFont(FONT_I);
        paragraph.add(dateFormat.format(orderData.getCustom().getCreateTime()));
        document.add(paragraph);
    }

    private void setTable(Document document) throws DocumentException {
        LOGGER.info(METHOD_STARTS_MSG, "setTable", "true");
        float[] columnWidths = {0.7f, 3f, 1.5f, 1f};
        PdfPTable table = new PdfPTable(columnWidths.length); // 3 columns.
        table.setWidthPercentage(tableWidth); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        table.setWidths(columnWidths);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//set table header row
        FONT_Z.setSize(12f);
        fillTableRow(table, headers, FONT_Z, new BaseColor(135, 206, 235));
        List<List<String>> remappedDishes = remapDishInStringList();
        int i = 0;
        BaseColor baseColor;
//set table data rows
        for (List<String> mappedDish : remappedDishes) {
            baseColor = i++ % 2 == 0 ? BaseColor.WHITE : new BaseColor(241, 241, 241);//BaseColor.LIGHT_GRAY;
            fillTableRow(table, mappedDish, FONT_I, baseColor);
        }
        document.add(table);
    }

    private static void fillTableRow(PdfPTable table, List<String> cellData, Font font, BaseColor backColor) {
        LOGGER.trace(METHOD_STARTS_MSG, "fillTableRow", "true");
        PdfPCell cell;
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            cell = new PdfPCell(new Phrase(cellData.get(i), font));
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(5);
            cell.setPaddingBottom(5);
            if (i == 1) {
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            } else {
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(backColor);
            table.addCell(cell);
        }
    }

    private void setFooter(Document document) throws DocumentException {
        LOGGER.info(METHOD_STARTS_MSG, "setFooter", "true");
        FONT_Z.setSize(12);
        FONT_I.setSize(12);
        float[] columnWidths = {1f, 1f};
        PdfPTable table = new PdfPTable(columnWidths.length);
        table.setWidthPercentage(tableWidth);
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        table.setWidths(columnWidths);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setFixedHeight(30f);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
        String[] strings = {
                (bundle.getString("order.total") + ":"),
                getFormatedHrivna(orderData.getCustom().getCost()),
                (bundle.getString("report.manager") + ":"),
                getManager(),
                (bundle.getString("report.date") + ":"),
                dateFormat.format(new Timestamp(new Date().getTime()))
        };
        for (int i = 0; i < strings.length; i++) {
            table.addCell(new Paragraph(strings[i], (i % 2 == 0 ? FONT_Z : FONT_I)));
        }
        document.add(table);
    }

    private String getManager() {
        LOGGER.info(METHOD_STARTS_MSG, "getManager", "true");
        HttpSession session = request.getSession(false);
        User currentManager = (User) session.getAttribute(LOGGED_USER_ATTR);
        return getUserString(currentManager);
    }

    private static String getUserString(User user) {
        return user.getName() + " " + user.getLastName();
    }

    private String getURL() {
        LOGGER.info(METHOD_STARTS_MSG, "getURL", "true");
        String scheme = request.getScheme();             // http
        String serverName = request.getServerName();     // hostname.com
        int serverPort = request.getServerPort();        // 80
        String contextPath = request.getContextPath();   // /mywebapp
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        return url.toString();
    }

    private List<List<String>> remapDishInStringList() {
        LOGGER.info(METHOD_STARTS_MSG, "remapDishInStringList", "true");
        List<List<String>> result = new ArrayList<>();
        int counter = 1;
        TreeMap<Dish, Integer> dishes = new TreeMap<>(Comparator.comparing(Dish::getName));
        dishes.putAll(orderData.getDishes());
        for (Map.Entry<Dish, Integer> entry : dishes.entrySet()) {
            List<String> mapped = new ArrayList<>();
            mapped.add(String.valueOf(counter++));
            mapped.add(entry.getKey().getName());
            mapped.add(getFormatedHrivna(entry.getKey().getPrice()));
            mapped.add(String.valueOf(entry.getValue()));
            result.add(mapped);
        }
        return result;
    }
    private String getFormatedHrivna(double currency){
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedPrice = currencyFormat.format(currency);
        formattedPrice = formattedPrice.replaceAll(currencyFormat.getCurrency().getSymbol(), bundle.getString("currency"));
        return formattedPrice;
    }
}
