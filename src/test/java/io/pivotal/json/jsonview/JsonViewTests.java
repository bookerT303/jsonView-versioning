package io.pivotal.json.jsonview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import io.pivotal.json.jsonview.model.Item;
import io.pivotal.json.jsonview.model.User;
import io.pivotal.json.jsonview.model.Versions;
import io.pivotal.json.jsonview.serdes.JsonBeanDeserializerModifier;
import io.pivotal.json.jsonview.serdes.JsonBeanSerializerModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JsonViewTests {

    private static final String NAME = "John Doe";
    private static final String NAME_UPPER = NAME.toUpperCase();
    private static final String ITEM_NAME = "Item Name";
    private static final String CREDIT_CARD = "4147000100020003";
    private static final int USER_ID = 1;
    private static final String USER_ID_AS_STRING = String.valueOf(USER_ID);
    private static final int ITEM_ID = 2;
    private static final String ITEM_ID_AS_STRING = String.valueOf(ITEM_ID);

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        final SerializerFactory serializerFactory = BeanSerializerFactory.instance.withSerializerModifier(new JsonBeanSerializerModifier());
        mapper.setSerializerFactory(serializerFactory);

        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new JsonBeanDeserializerModifier());
        mapper.registerModule(module);

    }

    @Test
    void whenUseJsonViewToSerialize_thenCorrect() throws JsonProcessingException {
        final User user = createUser();

        final String result = mapper.writerWithView(Versions.V1.class)
                .writeValueAsString(user);

        assertThat(result, containsString(NAME_UPPER));
        assertThat(result, not(containsString(USER_ID_AS_STRING)));
    }

    @Test
    void whenUsePublicView_thenOnlyPublicSerialized() throws JsonProcessingException {
        final Item item = createItem();

        final String result = mapper.writerWithView(Versions.V1.class)
                .writeValueAsString(item);

        assertThat(result, containsString(ITEM_ID_AS_STRING));
        assertThat(result, containsString(ITEM_NAME));

        assertThat(result, not(containsString(NAME)));
        assertThat(result, not(containsString(CREDIT_CARD)));
    }

    @Test
    void whenUseAdminView_thenOnlyPublicAdminSerialized() throws JsonProcessingException {
        final Item item = createItem();

        final String result = mapper.writerWithView(Versions.V2.class)
                .writeValueAsString(item);

        assertThat(result, containsString(ITEM_ID_AS_STRING));
        assertThat(result, containsString(ITEM_NAME));

        assertThat(result, containsString(NAME));
        assertThat(result, not(containsString(CREDIT_CARD)));
    }

    @Test
    void whenUsePrivilegedView_thenAllSerialized() throws JsonProcessingException {
        final Item item = createItem();

        // This could be done programmatically using the Accepts Header to determine
        // the version of the payload
        final String result = mapper.writerWithView(Versions.V3.class)
                .writeValueAsString(item);

        assertThat(result, containsString(ITEM_ID_AS_STRING));
        assertThat(result, containsString(ITEM_NAME));

        assertThat(result, containsString(NAME));
        assertThat(result, containsString(CREDIT_CARD));
    }

    @Test
    void whenUseJsonViewToDeserializeItem_thenCorrect() throws IOException {
        final String json = "{\"id\":2,\"itemName\":\"Item Name\", \"ownerName\":\"John Doe\", \"ownerCreditCard\":\"4147000100020003\"}";

        final Item item = mapper.readerWithView(Versions.V1.class)
                .forType(Item.class)
                .readValue(json);
        assertEquals(ITEM_ID, item.getId());
        assertNull(item.getOwnerName());

        final Item v3Item = mapper.readerWithView(Versions.V3.class)
                .forType(Item.class)
                .readValue(json);
        assertEquals(ITEM_ID, v3Item.getId());
        assertEquals(ITEM_NAME, v3Item.getItemName());
        assertEquals(NAME_UPPER, v3Item.getOwnerName());
        assertEquals(CREDIT_CARD, v3Item.getOwnerCreditCard());
    }

    @Test
    void whenUseJsonViewToDeserialize_thenCorrect() throws IOException {
        final String json = "{\"id\":1,\"name\":\"John Doe\"}";

        final User user = mapper.readerWithView(Versions.V1.class)
                .forType(User.class)
                .readValue(json);
        assertEquals(0, user.getId());
        assertEquals(NAME, user.getName());
    }

    @Test
    void whenUseCustomJsonViewToSerialize_thenCorrect() throws JsonProcessingException {
        final User user = createUser();

        final String result = mapper.writerWithView(Versions.V1.class)
                .writeValueAsString(user);
        assertThat(result, containsString(NAME_UPPER));
        assertThat(result, not(containsString("\"id\":")));
    }

    @Test
    void withoutDefaultJsonView() throws JsonProcessingException {
        final User user = createUser();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        final String result = objectMapper.writerWithView(Versions.V1.class)
                .writeValueAsString(user);
        assertThat(result, containsString(NAME));
        assertThat(result, not(containsString("\"id\":")));
    }

    @Test
    void withoutCustomSerializer_thenCorrect() throws JsonProcessingException {
        final User user = createUser();

        // If we do not use the correct ObjectMapper with all the customization
        // then we just get regular serialization
        ObjectMapper objectMapper = new ObjectMapper();

        final String result = objectMapper.writerWithView(Versions.V1.class)
                .writeValueAsString(user);
        assertThat(result, containsString(NAME));
        assertThat(result, containsString("\"id\":"));
        assertThat(result, containsString(USER_ID_AS_STRING));
    }

    private User createUser() {
        return new User(USER_ID, NAME);
    }

    private Item createItem() {
        return new Item(2, ITEM_NAME, NAME, CREDIT_CARD);
    }
}
