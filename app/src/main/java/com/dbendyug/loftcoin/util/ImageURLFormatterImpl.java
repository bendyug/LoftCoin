package com.dbendyug.loftcoin.util;

import com.dbendyug.loftcoin.BuildConfig;

import java.util.Locale;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public class ImageURLFormatterImpl implements ImageURLFormatter {

    @Inject
    ImageURLFormatterImpl(){

    }

    @Override
    public String format(long id) {
        return String.format(Locale.US, "%scoins/64x64/%d.png", BuildConfig.COIN_IMAGE_URL, id);
    }
}
