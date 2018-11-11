package com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Converter for Retrofit that supports JSON marshalling using Jackson.
 */
public class JacksonConverter implements Converter {

    private final ObjectMapper mapper;

    public JacksonConverter(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        try {
            return mapper.readValue(typedInput.in(), TypeFactory.rawClass(type));
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object o) {
        try {
            return new TypedByteArray("application/json; charset=UTF-8", mapper.writeValueAsBytes(o));
        } catch (JsonProcessingException e) {
            throw new JacksonConverterException(e);
        }
    }
}
