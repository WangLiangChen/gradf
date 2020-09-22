package liangchen.wang.gradf.framework.web.result;

import com.google.gson.*;
import liangchen.wang.gradf.framework.commons.exception.*;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiangChen.Wang
 */
public class ResponseUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
    private static final String JSON = "json";
    private static final String XML = "xml";
    private final HttpServletResponse httpServletResponse;
    private final Map<String, Object> mapResult = new HashMap<>();
    private Response response = new Response(true);

    private String format = JSON;
    private String[] excludeProperties;

    private ResponseUtil(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public static ResponseUtil createResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return new ResponseUtil(response);
    }

    public static ResponseUtil createResponse(HttpServletResponse httpServletResponse) {
        return new ResponseUtil(httpServletResponse);
    }

    public ResponseUtil setFormat(String format) {
        Assert.INSTANCE.isTrue((JSON.equals(format) || XML.equals(format)), "数据格式必须是{}或{}", JSON, XML);
        this.format = format;
        return this;
    }

    public ResponseUtil setExcludeProperties(String... excludeProperties) {
        this.excludeProperties = excludeProperties;
        return this;
    }

    public ResponseUtil error(ExceptionMessage exceptionMessage) {
        this.response = new ResponseError(exceptionMessage);
        return this;
    }

    public ResponseUtil error(String errorCode, String errorText, String debugText) {
        this.response = new ResponseError(errorCode, errorText, debugText);
        return this;
    }

    public ResponseUtil error(String errorCode, String errorText) {
        this.response = new ResponseError(errorCode, errorText);
        return this;
    }

    public ResponseUtil error(String errorText) {
        this.response = new ResponseError(errorText);
        return this;
    }

    public ResponseUtil error(Throwable throwable) {
        String message = ExceptionUtil.INSTANCE.parseMessage(throwable);
        // GradfException之外的其它异常
        if (!(throwable instanceof GradfException)) {
            logger.error(message, throwable);
            ExceptionMessage exceptionMessage = new ExceptionMessage();
            exceptionMessage.setMessage("系统错误，请稍后重试或联系管理员");
            exceptionMessage.setDebug(message);
            return error(exceptionMessage);
        }
        //以下是GradfException
        GradfException gradfException = (GradfException) throwable;
        ExceptionMessage exceptionMessage = gradfException.getExceptionMessage();

        String code = exceptionMessage.getCode();
        StringBuilder loggerText = new StringBuilder();
        if (StringUtil.INSTANCE.isNotBlank(code)) {
            loggerText.append(code).append(":");
        }
        loggerText.append(message);

        if (throwable instanceof PromptException) {
            logger.debug(loggerText.toString(), throwable);
            return error(exceptionMessage);
        }
        if (throwable instanceof InfoException) {
            logger.info(loggerText.toString(), throwable);
            return error(exceptionMessage);
        }
        if (throwable instanceof ErrorException) {
            logger.error(loggerText.toString(), throwable);
            return error(exceptionMessage);
        }
        return error(exceptionMessage);
    }

    public ResponseUtil data(Object data) {
        this.response = new ResponseSuccess(data);
        return this;
    }

    public ResponseUtil putDatas(String key, Object value) {
        mapResult.put(key, value);
        return this;
    }

    public void flush() {
        if (mapResult.size() > 0) {
            this.response = new ResponseSuccess(mapResult);
        }
        switch (format) {
            case JSON:
                flushJson();
                break;
            case XML:
                flushXml();
                break;
            default:
                break;
        }
    }

    public void flushString(String string) {
        if (StringUtil.INSTANCE.isBlank(string)) {
            return;
        }
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-type", "text/plain;charset=UTF-8");
        try {
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(string.getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Response ExceptionData", e);
        }
    }

    private void flushJson() {
        Gson gson = gsonBuilder(excludeProperties).create();
        String gsonString = gson.toJson(response);
        flushString(gsonString);
    }

    private GsonBuilder gsonBuilder(String... excludeProperties) {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonDeserializer()).registerTypeAdapter(LocalDate.class, new LocalDateJsonDeserializer())
                .registerTypeAdapter(Long.class, new LongJsonDeserializer()).disableHtmlEscaping().setDateFormat(DateTimeUtil.YYYY_MM_DD_HH_MM_SS).setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes field) {
                        return ArrayUtils.contains(excludeProperties, field.getName());
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                });
        return gsonBuilder;
    }

    private void flushXml() {
        String xmlString = ClassBeanUtil.INSTANCE.bean2xml(response, "root");
        flushString(xmlString);
    }

}

class LongJsonDeserializer implements JsonSerializer<Long>, JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return json.getAsLong();
    }

    @Override
    public JsonElement serialize(Long longParam, Type type, JsonSerializationContext jsonDeserializationContext) {
        String json = Long.toString(longParam);
        return new JsonPrimitive(json);
    }

}

class LocalDateTimeJsonDeserializer implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        //return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(),formatter).toLocalDateTime();
        String string = json.getAsJsonPrimitive().getAsString();
        return LocalDateTime.parse(string, formatter);
    }

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        String json = localDateTime.format(formatter);
        return new JsonPrimitive(json);
    }
}

class LocalDateJsonDeserializer implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtil.YYYY_MM_DD);

    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(), formatter).toLocalDate();
    }

    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        String json = localDate.format(formatter);
        return new JsonPrimitive(json);
    }

}
