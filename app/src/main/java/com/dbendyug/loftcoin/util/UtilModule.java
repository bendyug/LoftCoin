package com.dbendyug.loftcoin.util;

import dagger.Binds;
import dagger.Module;

@Module
public interface UtilModule {

    @Binds
    Change24hFormatter change24hFormatter(Change24hFormatterImpl change24hFormatter);

    @Binds
    ImageURLFormatter imageURLFormatter(ImageURLFormatterImpl imageURLFormatter);

    @Binds
    PriceFormatter priceFormatter(PriceFormatterImpl priceFormatter);
}
