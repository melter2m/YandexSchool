package com.m2m.yafun.view;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class YaLicenseInitializer {

    public static void initClickableTextView(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
