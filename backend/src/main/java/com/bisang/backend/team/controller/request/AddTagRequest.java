package com.bisang.backend.team.controller.request;

import java.util.List;

public record AddTagRequest(
    List<String> tags
) {
}
