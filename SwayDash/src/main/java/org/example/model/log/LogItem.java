package org.example.model.log;

import java.util.Date;

public record LogItem(
        LogItemLevel level,
        String title,
        String message,
        Date date
) {}
