package com.cht.network.monitoring.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    public static <T> Page<T> generatePageFromList(List<T> list, Pageable pageable) {
        if (list == null) {
            throw new IllegalArgumentException("To create a Page, the list mustn't be null!");
        }

        int startOfPage = pageable.getPageNumber() * pageable.getPageSize();
        if (startOfPage > list.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        int endOfPage = startOfPage + pageable.getPageSize();
        endOfPage = (endOfPage > list.size()) ? list.size() : endOfPage;
        return new PageImpl<>(list.subList(startOfPage, endOfPage), pageable, list.size());
    }
}
