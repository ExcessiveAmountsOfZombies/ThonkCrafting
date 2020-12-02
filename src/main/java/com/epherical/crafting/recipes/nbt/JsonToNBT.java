package com.epherical.crafting.recipes.nbt;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import java.util.HashMap;
import java.util.Map;

public class JsonToNBT extends JsonOps {
    public static final JsonToNBT INSTANCE = new JsonToNBT(false);

    private final Map<Class<?>, Integer> NUMBER_CLASSES;

    protected JsonToNBT(boolean compressed) {
        super(compressed);
        NUMBER_CLASSES = new HashMap<>();
        NUMBER_CLASSES.put(Byte.class, 1);
        NUMBER_CLASSES.put(Short.class, 2);
        NUMBER_CLASSES.put(Integer.class, 3);
        NUMBER_CLASSES.put(Long.class, 4);
        NUMBER_CLASSES.put(Float.class, 5);
        NUMBER_CLASSES.put(Double.class, 6);
    }


    @Override
    public <U> U convertTo(DynamicOps<U> outOps, JsonElement input) {
        if (input.isJsonPrimitive() && input.getAsJsonPrimitive().isNumber()) {
            Number num = input.getAsNumber();

            if (NUMBER_CLASSES.containsKey(num.getClass())) {
                int numType = NUMBER_CLASSES.get(num.getClass());
                switch (numType) {
                    case 1:
                        return outOps.createByte(num.byteValue());
                    case 2:
                        return outOps.createShort(num.shortValue());
                    case 3:
                        return outOps.createInt(num.intValue());
                    case 4:
                        return outOps.createLong(num.longValue());
                    case 5:
                        return outOps.createFloat(num.floatValue());
                    case 6:
                        return outOps.createDouble(num.doubleValue());
                }
            }
        }
        return super.convertTo(outOps, input);
    }
}
