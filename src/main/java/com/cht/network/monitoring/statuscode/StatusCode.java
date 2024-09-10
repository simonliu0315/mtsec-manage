package com.cht.network.monitoring.statuscode;

import com.cht.network.monitoring.commons.base.LocalizableStatusCode;
import com.google.common.collect.ImmutableMap;
import org.jvnet.localizer.Localizable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class StatusCode extends LocalizableStatusCode {

    private static final Map<String, Level> LEVELS = ImmutableMap.of(
            "S",
            Level.SUCCESS,
            "I",
            Level.INFO,
            "W",
            Level.WARN,
            "E",
            Level.ERROR
    );
    private static final Pattern CODE_PATTERN = Pattern.compile(
            "(?<applicationId>[A-Z]{3})-(?<componentId>[A-Z0-9]{2,7})-(?<serial>[0-9]{4,5})-(?<level>[SIWE])"
    );
    private String applicationId;
    private String componentId;
    private int serial;
    private Level level;
    private String[] args;

    public StatusCode(Localizable localizable) {
        super(localizable);
        parseCode(getName());
        parseArgs(localizable);
    }

    private void parseCode(String code) {
        Matcher matcher = CODE_PATTERN.matcher(code);
        if (matcher.matches()) {
            applicationId = matcher.group("applicationId");
            componentId = matcher.group("componentId");
            serial = Integer.parseInt(matcher.group("serial"));
            level = resolveLevel(matcher.group("level"));
        } else {
            String message = format("%1$s doesn't match pattern: %2$s", code, CODE_PATTERN);
            throw new IllegalArgumentException(message);
        }
    }

    private void parseArgs(Localizable localizable) {
        try {
            Field field = Localizable.class.getDeclaredField("args");
            field.setAccessible(true);
            Serializable[] args = (Serializable[]) field.get(localizable);
            this.args = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
        } catch (Exception ignore) {
            this.args = new String[]{};
        }
    }

    private Level resolveLevel(String levelString) {
        Level levelResolved = LEVELS.get(levelString);
        if (levelResolved != null) {
            return levelResolved;
        } else {
            throw new IllegalArgumentException("Unknown level string \"" + levelString + "\", allowed values are: " + LEVELS.keySet());
        }
    }

    /**
     * 取得應用系統識別碼。
     *
     * @return 系統識別碼; 例如 {@code SYS}。
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * 取得功能代號或模組功識別碼。
     *
     * @return 功能代號或模組功識別碼。
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * 取得流水序號。
     *
     * @return 序號; 例如 {@code 001}。
     */
    public int getSerial() {
        return serial;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    /**
     * 取得輸入參數
     *
     * @return 參數;
     */
    public String[] getArgs() {
        return args;
    }
}
