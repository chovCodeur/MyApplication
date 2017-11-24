package com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.application.inventaire.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;


import static java.lang.String.format;

public class ReadPDF extends AppCompatActivity implements OnPageChangeListener {
    //public static final String SAMPLE_FILE = "test.pdf";
    public static final String ABOUT_FILE = "about.pdf";
    String pdfName;// = SAMPLE_FILE;
    Integer pageNumber = 1;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pdf);

        pdfView = (PDFView) findViewById(R.id.pdfview);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            pdfName = extras.getString("nomPDF");
        }

        display(pdfName, true);
    }

    void afterViews() {
        display(pdfName, false);
    }

    public void about() {
        if (!displaying(ABOUT_FILE))
            display(ABOUT_FILE, true);
    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        //setTitle(pdfName = assetFileName);

        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //setTitle(format("%s %s / %s", pdfName, page, pageCount));
    }

    @Override
    public void onBackPressed() {
        if (ABOUT_FILE.equals(pdfName)) {
            display(pdfName, true);
        } else {
            super.onBackPressed();
        }
    }

    private boolean displaying(String fileName) {
        return fileName.equals(pdfName);
    }
}