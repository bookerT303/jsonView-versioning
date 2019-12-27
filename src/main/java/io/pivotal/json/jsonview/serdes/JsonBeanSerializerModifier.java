package io.pivotal.json.jsonview.serdes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import io.pivotal.json.jsonview.model.User;

import java.util.List;

public class JsonBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(final SerializationConfig config, final BeanDescription beanDesc, final List<BeanPropertyWriter> beanProperties) {
        if (beanDesc.getBeanClass().isAssignableFrom(User.class) == false) {
            return beanProperties;
        }
        // We are forcing the User name to upper case when we serialize
        for (int i = 0; i < beanProperties.size(); i++) {
            final BeanPropertyWriter beanPropertyWriter = beanProperties.get(i);
            if ("name".equals(beanPropertyWriter.getName())) {
                beanProperties.set(i, new UpperCasingWriter(beanPropertyWriter));
            }
        }
        return beanProperties;
    }

    static class UpperCasingWriter extends BeanPropertyWriter {
        final BeanPropertyWriter _writer;

        public UpperCasingWriter(final BeanPropertyWriter w) {
            super(w);
            _writer = w;
        }

        @Override
        public void serializeAsField(final Object bean, final JsonGenerator gen, final SerializerProvider prov) throws Exception {
            String value = ((User) bean).name;
            value = (value == null) ? "" : value.toUpperCase();
            gen.writeStringField("name", value);
        }
    }
}
