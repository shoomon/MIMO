package com.bisang.backend.team.controller.dto;

import java.util.List;

public record TagDto(
        Integer size,
        List<String> tags
) {
}
