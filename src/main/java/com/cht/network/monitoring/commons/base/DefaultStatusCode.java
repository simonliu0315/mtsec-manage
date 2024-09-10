package com.cht.network.monitoring.commons.base;

import com.google.common.collect.ImmutableMap;
import org.jvnet.localizer.Localizable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;


/**
 * 預設狀態碼物件。
 * <p>
 * 如果想要客製化編碼規則，可以覆寫 {@link #getPattern()} 及 {@link #resolveLevel(String)}。
 */
public class DefaultStatusCode extends LocalizableStatusCode {

    private static Map<String, Level> LEVELS = ImmutableMap.of("S", Level.SUCCESS, "I", Level.INFO,
            "W", Level.WARN, "E", Level.ERROR);

    private static final Pattern CODE_PATTERN = Pattern
            .compile("(?<applicationId>[A-Z0-9]{3})(?<moduleId>[A-Z0-9]{3})(?<serial>[0-9]{3})(?<level>[SIWE])");

    private String applicationId;
    private String moduleId;
    private String serial;
    private Level level;

    public DefaultStatusCode(Localizable localizable) {
        super(localizable);

        parseCode(getName());
    }

    private void parseCode(String code) {
        Matcher matcher = getPattern().matcher(code);
        if (matcher.matches()) {
            applicationId = matcher.group("applicationId");
            moduleId = matcher.group("moduleId");
            serial = matcher.group("serial");
            level = resolveLevel(matcher.group("level"));

        } else {
            String message = format("%1$s doesn't match pattern: %2$s", code, CODE_PATTERN);
            throw new IllegalArgumentException(message);
        }
    }

    protected Pattern getPattern() {
        return CODE_PATTERN;
    }

    protected Level resolveLevel(String levelString) {
        Level level = LEVELS.get(levelString);
        if (level != null) {
            return level;
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
     * 取得功能模組識別碼。
     *
     * @return 功能模組識別碼; 例如 {@code CMP}。
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * 取得流水序號。
     *
     * @return 序號; 例如 {@code 001}。
     */
    public String getSerial() {
        return serial;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((serial == null) ? 0 : serial.hashCode());
        result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultStatusCode other = (DefaultStatusCode) obj;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        if (level != other.level)
            return false;
        if (serial == null) {
            if (other.serial != null)
                return false;
        } else if (!serial.equals(other.serial))
            return false;
        if (applicationId == null) {
            if (other.applicationId != null)
                return false;
        } else if (!applicationId.equals(other.applicationId))
            return false;
        return true;
    }
}
