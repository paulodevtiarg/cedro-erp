package br.com.dpsistemas.cedroerp.utils;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Locale;

@Converter
public class UppercaseTrimConverter
        implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String valor) {

        if (valor == null) {
            return null;
        }

        return valor
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    @Override
    public String convertToEntityAttribute(String valor) {
        return valor;
    }
}
