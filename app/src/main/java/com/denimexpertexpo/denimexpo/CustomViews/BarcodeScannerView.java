package com.denimexpertexpo.denimexpo.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import com.denimexpertexpo.denimexpo.Interfaces.ViewResizer;
import com.google.zxing.BarcodeFormat;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by ratul on 7/10/2015.
 */
public class BarcodeScannerView extends ZXingScannerView {

    private ViewResizer resizer;

    public BarcodeScannerView(Context context) {
        super(context);
    }

    public BarcodeScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setFormats(List<BarcodeFormat> formats) {
        super.setFormats(formats);
    }

    public void setResultHandler(ZXingScannerView.ResultHandler resultHandler) {
        super.setResultHandler(resultHandler);
    }


    public void setViewResizer(ViewResizer vr) {
        this.resizer = vr;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int customHeight = MeasureSpec.getSize(heightMeasureSpec);
        int customWidth = MeasureSpec.getSize(widthMeasureSpec);

        this.setMeasuredDimension(customWidth, customHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (resizer != null) {
            resizer.heightWidthKnown(customHeight, customWidth);
        }
    }

}
