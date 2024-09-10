package com.cht.network.monitoring.commons.base;

import java.io.Serializable;

/**
 * 狀態碼物件，一般系統對外應該要有狀態碼對照表，把錯誤或是其他資訊的代碼及錯誤訊息列出來。
 * <p>
 * 狀態碼應該要有統一的編碼規則，例如 {@code SYSCP001E} 之類。
 */
public interface StatusCode extends Serializable {

    /**
     * 錯誤等級，包含 {@link #SUCCESS}, {@link #INFO}, {@link #WARN} 及 {@link #ERROR} 四種級別。
     */
    static enum Level {

        /** 作業成功。 */
        SUCCESS,

        /** 作業訊息，提示系統或使用者。 */
        INFO,

        /** 警告等級，代表部分完成或是需要使用者介入。 */
        WARN,

        /** 錯誤，無法完成作業，請排除障礙後再執行。 */
        ERROR;
    }

    /**
     * 取得狀態碼。
     *
     * @return 狀態碼。
     */
    String getName();

    /**
     * 取得代表該狀態碼的訊息。
     *
     * @return 訊息。
     */
    String getMessage();

    /**
     * 取得 {@link Level} 錯誤等級。
     *
     * @return 錯誤等級。
     */
    Level getLevel();
}
