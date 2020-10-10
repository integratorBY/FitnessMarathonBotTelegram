package com.example.fitnessMarathonBot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum Emojis {
    ARROW_DOWN(EmojiParser.parseToUnicode(":arrow_down:")),
    ARROW_RIGHT(EmojiParser.parseToUnicode(":arrow_right:")),
    HEAVY_CHECK_MARK(EmojiParser.parseToUnicode(":heavy_check_mark:")),
    WARNING(EmojiParser.parseToUnicode(":warning:")),
    NO_ENTRY_SIGN(EmojiParser.parseToUnicode(":no_entry_sign:")),
    POINT_RIGHT(EmojiParser.parseToUnicode(":point_right:")),
    POINT_DOWN(EmojiParser.parseToUnicode(":point_down:")),
    POINT_UP(EmojiParser.parseToUnicode(":point_up_2:")),
    MEMO(EmojiParser.parseToUnicode(":memo:")),
    HEART(EmojiParser.parseToUnicode(":heart:")),
    TADA(EmojiParser.parseToUnicode(":tada:")),
    GRIN(EmojiParser.parseToUnicode(":grin:")),
    BLUSH(EmojiParser.parseToUnicode(":blush:")),
    SUNNY(EmojiParser.parseToUnicode(":sunny:")),
    WHITE_CHECK_MARK(EmojiParser.parseToUnicode(":white_check_mark:")),
    NEGATIVE_MARK(EmojiParser.parseToUnicode(":negative_squared_cross_mark:"));


    private String emojiName;
    @Override
    public String toString() {
        return emojiName;
    }
}
