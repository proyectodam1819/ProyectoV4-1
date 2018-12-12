package org.izv.aad.proyecto.Adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.widget.ImageView;
import android.widget.TextView;

import org.izv.aad.proyecto.Objects.Book;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyPrintDocumentAdapter extends PrintDocumentAdapter {

    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 1;
    private TextView author, state, resume;
    private String dates, dateStart, dateFinish;
    private Book book;
    private ImageView photo;

    public MyPrintDocumentAdapter(Context context, TextView author, String dateStart, String dateFinish, TextView state, TextView resume, Book book) {
        this.context = context;
        this.author = author;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.state = state;

        this.resume = resume;
        this.book = book;
    }


    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight =
                newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        pageWidth =
                newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pages, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pages);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }


    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {
        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText(
                "Title of the book: " + book.getTitle().toString(),
                leftMargin,
                titleBaseLine,
                paint);
        paint.setTextSize(14);


        canvas.drawText("Author : " + author.getText().toString() + "    State : " + state.getText().toString(), leftMargin, titleBaseLine + 35, paint);

        if (dateStart.compareTo("") == 0) {
            dates = "";
        } else {
            dates = "Date Start : " + dateStart;
        }

        if (dateFinish.compareTo("") == 0) {
            dates = dates + "";
        } else {
            dates = dates+"           Date Finish : " + dateFinish;
        }

        if(dates.compareTo("")!=0){
            //canvas.drawText(dates, leftMargin, titleBaseLine + 70, paint);
            //canvas.drawText("Resume : " + resume.getText().toString(), leftMargin, titleBaseLine + 105, paint);
            canvas.drawText(dates, leftMargin, titleBaseLine + 70, paint);
            canvas.drawText("Resume : " + resume.getText().toString(), leftMargin, titleBaseLine + 105, paint);
            canvas.drawText("Rating : " + book.getAssessment(), leftMargin, titleBaseLine + 140, paint);}
        else {
            canvas.drawText("Resume : " + resume.getText().toString(), leftMargin, titleBaseLine + 70, paint);
            canvas.drawText("Rating : " + book.getAssessment(), leftMargin, titleBaseLine + 105, paint);
        }

        // canvas.drawBitmap(photo.getDrawingCache(), leftMargin, titleBaseLine + 140, paint);

    }



}
