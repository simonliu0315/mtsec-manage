// CHECKSTYLE:OFF

package com.cht.network.monitoring;

import com.cht.network.monitoring.statuscode.StatusCode;
import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;


/**
 * Generated localization support class.
 *
 */
@SuppressWarnings({
    "",
    "PMD",
    "all"
})
public class StatusCodes {

    /**
     * The resource bundle reference
     *
     */
    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(StatusCodes.class);

    /**
     * Key {@code APP-APP001I-0001-I}: {@code 查無資料。}.
     *
     * @return
     *     {@code 查無資料。}
     */
    public static StatusCode APP_APP001I_0001_I() {
        return new StatusCode(new Localizable(holder, "APP-APP001I-0001-I"));
    }

    /**
     * Key {@code APP-APP001I-0001-E}: {@code 抱歉，發生錯誤了。}.
     *
     * @return
     *     {@code 抱歉，發生錯誤了。}
     */
    public static StatusCode APP_APP001I_0001_E() {
        return new StatusCode(new Localizable(holder, "APP-APP001I-0001-E"));
    }

}
