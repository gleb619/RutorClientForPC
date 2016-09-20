package org.test.service.http;

import org.apache.commons.beanutils.BeanUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Map;

/**
 * Created by BORIS on 19.09.2016.
 */
public class DefaultJsonConverter implements JsonConverter {

    @Override
    public <T> T read(String text, Class<T> clazz) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(text);
        T item = clazz.newInstance();
        BeanUtils.populate(item, json);

        return item;
    }

    @Override
    public String write(Object object) throws Exception {
        Map<String, String> objectAsMap = BeanUtils.describe(object);
        JSONObject jsonObject = new JSONObject(objectAsMap);

        return jsonObject.toString();
    }

}
