package com.example.helloworld_en_jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@FacesConverter("localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return LocalDate.parse(value, FORMATTER);
        }
        catch (DateTimeParseException e) {
            StudentListBean.showMessage("Format de date accepté : dd-mm-yyyy");
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        return value.format(FORMATTER);
    }
}