package com.rojas.dev.XCampo.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCartListSerializer extends StdSerializer<Set<CartItem>> {
    protected ShoppingCartListSerializer(Class<Set<CartItem>> t) {
        super(t);
    }

    protected ShoppingCartListSerializer(JavaType type) {
        super(type);
    }

    protected ShoppingCartListSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected ShoppingCartListSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(Set<CartItem> cartItems, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        Set<Integer> ids = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            ids.add(Math.toIntExact(cartItem.getId_cart_item()));
        }
        jsonGenerator.writeObject(ids);
    }
}
