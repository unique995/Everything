package com_everything_g2.core.intercreptor.impl;

import com_everything_g2.core.intercreptor.FileInterceptor;

import java.io.File;

public class FilePrintInterceptor implements FileInterceptor {
    @Override
    public void apply(File file){
        System.out.println(file.getAbsolutePath());
    }
}
