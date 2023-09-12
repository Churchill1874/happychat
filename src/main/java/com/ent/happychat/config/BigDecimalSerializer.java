package com.ent.happychat.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 处理小数保留两位配置
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    public BigDecimalSerializer() {
    }

    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            BigDecimal number = value.setScale(2, BigDecimal.ROUND_HALF_UP);
            gen.writeNumber(number);
        } else {
            gen.writeNumber(value);
        }

    }
}