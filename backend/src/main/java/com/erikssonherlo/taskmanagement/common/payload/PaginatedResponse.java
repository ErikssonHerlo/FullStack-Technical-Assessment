package com.erikssonherlo.taskmanagement.common.payload;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class PaginatedResponse<T> {
    private Integer code;
    private String message;
    private HttpStatus status;
    private T data;
    private Pageable pageable;
    private boolean isLast;
    private boolean isFirst;
    private boolean hasNext;
    private boolean hasPrevious;
    private Integer totalPages;
    private Integer totalElements;

    public PaginatedResponse(Integer code, String message, HttpStatus status, T data, Pageable pageable, boolean isLast, boolean isFirst, boolean hasNext, boolean hasPrevious, Integer totalPages, Integer totalElements) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.data = data;
        this.pageable = pageable;
        this.isLast = isLast;
        this.isFirst = isFirst;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}

