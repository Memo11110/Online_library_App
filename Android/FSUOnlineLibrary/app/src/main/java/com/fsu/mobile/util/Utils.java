package com.fsu.mobile.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.DatePicker;

import com.fsu.mobile.model.Book;
import com.fsu.mobile.model.Copy;
import com.fsu.mobile.model.Student;
import com.fus.mobile.R;
import com.google.gson.JsonObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class Utils {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    private static Font titleBoldBlue = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD,BaseColor.BLUE);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 13,
            Font.NORMAL);

    public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        //noinspection ResourceType
        calendar.set(year,month,day);
        return calendar.getTime();
    }

    //Create recipient
    public static String  generateRecepient(boolean rent,String subject,Student student,Book book,Copy copy,Date rentDate,Date returnDate,Context context){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/FSULibrarySystem");
        dir.mkdirs();
        dir.length();
        File FILE = new File(dir.getAbsolutePath()+"/recipient-"+dir.length()+".pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            Image logo = Image.getInstance(getImageFromDrawable(R.drawable.ic_launcher,context));
            logo.scaleToFit(50f, 50f);
            Paragraph logoText = new Paragraph();
            logo.setIndentationLeft(34);
            logoText.add(logo);
            logoText.add("FSU  Online Library\nSystem");
            logoText.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
            logoText.setIndentationLeft(400);
            document.add(logoText);

            Paragraph content = new Paragraph();

            // We add one empty line
            addEmptyLine(content, 1);
            // Lets write a big header
            content.add(new Paragraph("Recipient for action: "+subject, catFont));

            addEmptyLine(content, 3);

            content.add(
                    new Paragraph("Student Info: ",
                    titleBoldBlue));
            Paragraph studentInfo = new Paragraph("\nID: "+student.getStudentId()
                                        +"\nFull Name: "+student.getFirstName()+" "+student.getLastName()
                                        +"\nEmail: "+student.getEmail());

            studentInfo.setIndentationLeft(50);
            content.add(studentInfo);

            addEmptyLine(content, 1);

            content.add(
                    new Paragraph("Book Info: ",
                            titleBoldBlue));
            Paragraph bookInfo = new Paragraph("\nTitle: "+book.getTitle()
                    +"\nAutor: "+book.getAutor()
                    +"\nISBN: "+book.getIsbn()
                    +"\nCategory: "+book.getCategoryName());
            bookInfo.setIndentationLeft(50);
            content.add(bookInfo);


            addEmptyLine(content, 1);
            if(rent){
                content.add(
                        new Paragraph("Rent Info: ",
                                titleBoldBlue));
                Paragraph rentInfo  = new Paragraph("\nAccession number (borrowed copy): "+copy.getAccessionNumber()
                                +"\nDate of rent: "+rentDate.toLocaleString()
                                +"\nDate of return: "+returnDate.toLocaleString(),normalFont);
                rentInfo.setIndentationLeft(50);
                content.add(rentInfo);
            }
            else{
                content.add(
                        new Paragraph("Return Info: ",
                                titleBoldBlue));
                Paragraph returnInfo = new Paragraph(
                                "\nAccession number (returned copy): "+copy.getAccessionNumber()
                                +"\nDate of return: "+returnDate.toLocaleString(),normalFont);
                returnInfo.setIndentationLeft(50);
                content.add(returnInfo);
            }

            addEmptyLine(content, 8);
            Paragraph geneParagraph = new Paragraph("Recipient generated by: FSU Online Library System, " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    smallBold);
            geneParagraph.setAlignment(Element.ALIGN_RIGHT);
            content.add(geneParagraph);
            document.add(content);
            document.close();
        }
        catch (Exception e){}

        return FILE.getAbsolutePath();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static byte[] getImageFromDrawable(int drwable,Context context){
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(drwable);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        return bitMapData;
    }

    public static boolean checkConnection(){

        try {
            if(InetAddress.getByName(Constants.BASE_URL).isReachable(2000)){
                return true;
            }
        }catch (Exception e){}

        return false;
    }
}
