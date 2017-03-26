package com.m2m.yafun.view.pages.translate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.m2m.yafun.R;
import com.m2m.yafun.model.api.service.result.Languages;

class LanguagesAdapterWithAutoDetect extends LanguagesAdapter {

    LanguagesAdapterWithAutoDetect(@NonNull Context context, Languages languages) {
        super(context, languages);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        if (position == 0)
            return getContext().getString(R.string.detect_language);
        return super.getItem(position - 1);
    }

    @Override
    public String getLanguageForDirection(int position) {
        if (position == 0)
            return null;
        return super.getLanguageForDirection(position - 1);
    }
}
