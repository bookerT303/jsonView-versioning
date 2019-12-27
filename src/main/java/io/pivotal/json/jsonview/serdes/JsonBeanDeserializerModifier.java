package io.pivotal.json.jsonview.serdes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import io.pivotal.json.jsonview.model.Item;

import java.io.IOException;
import java.util.Iterator;

public class JsonBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc,
                                                 BeanDeserializerBuilder builder) {
        if (beanDesc.getBeanClass().isAssignableFrom(Item.class) == false) {
            return builder;
        }

        // For the Item we want to force the ownerName to upper case
        Iterator<SettableBeanProperty> beanPropertyIterator = builder.getProperties();
        while (beanPropertyIterator.hasNext()) {
            SettableBeanProperty settableBeanProperty = beanPropertyIterator.next();
            if ("ownerName".equals(settableBeanProperty.getName())) {
                SettableBeanProperty newSettableBeanProperty = settableBeanProperty.withValueDeserializer(new UpperCaseDeserializer());
                builder.addOrReplaceProperty(newSettableBeanProperty, true);
                break;
            }
        }
        return builder;
    }

    static class UpperCaseDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            // serialized value, 'customValue'
            String value = jp.getText();
            return (value == null) ? "" : value.toUpperCase();
        }
    }
}
