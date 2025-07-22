package com.weyland.bishop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class Command {
    @Setter
    @Getter
    @NotBlank(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description is too long(max 1000 chars)")
    private String description;
    @Setter
    @Getter
    @NotNull(message = "COMMON or CRITICAL only")
    private Priority priority;
    @Setter
    @Getter
    @NotBlank(message = "Author cannot be empty")
    @Size(max = 100, message = "Author name too long (max 100 chars)")
    private String author;
    @Setter
    @Getter
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$",
            message = "Invalid time format. Use ISO8601 (e.g. 2023-01-01T12:00:00Z)")
    private String time;

}
