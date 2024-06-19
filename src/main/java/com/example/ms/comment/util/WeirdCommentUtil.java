package com.example.ms.comment.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@Component
public class WeirdCommentUtil {

    List<String> weirdWords;

    {
        try {
            weirdWords = Files.readAllLines(Paths.get("src/main/java/com/example/ms/comment/blocked_comments.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String regexPattern = "(?i)\\b(?:" + String.join("|", weirdWords) + ")\\b";

}