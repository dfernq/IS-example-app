package model;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class QueryLoader {
    private static final Configuration configuration;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setClassLoaderForTemplateLoading(QueryLoader.class.getClassLoader(), "queries");
    }

    public static String getQuery(String templateName, Map<String, Object> params) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Unable to get query", e);
        }
    }
}
