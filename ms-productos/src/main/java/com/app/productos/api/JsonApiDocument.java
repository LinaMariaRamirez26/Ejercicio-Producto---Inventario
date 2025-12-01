package com.app.productos.api;

import java.util.List;
import java.util.Map;

public class JsonApiDocument {
    public Object data;
    public List<ErrorObject> errors;
    public Map<String, Object> meta;

    public static JsonApiDocument of(ResourceObject data) {
        JsonApiDocument doc = new JsonApiDocument();
        doc.data = data;
        return doc;
    }

    public static JsonApiDocument ofList(List<ResourceObject> data) {
        JsonApiDocument doc = new JsonApiDocument();
        doc.data = data;
        return doc;
    }

    public static JsonApiDocument error(List<ErrorObject> errors) {
        JsonApiDocument doc = new JsonApiDocument();
        doc.errors = errors;
        return doc;
    }
}

