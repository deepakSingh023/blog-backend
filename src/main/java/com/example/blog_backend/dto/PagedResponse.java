package com.example.blog_backend.dto;

import java.time.Instant;
import java.util.List;

public record PagedResponse<T>(
        List<T> items,
        Instant nextCursor
) {}
