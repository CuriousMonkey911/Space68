package com.example.space68.data;

import com.example.space68.model.CrewMember;

import java.util.Random;

// handles XP gambling — 50/50 double or lose half
public class CasinoManager {

    private static final Random rand = new Random();

    public enum Result { WIN, LOSE, NO_XP }

    // gambles the crew member's XP, returns the result
    public static Result gamble(CrewMember member) {
        if (member.getExperience() <= 0) {
            return Result.NO_XP;
        }
        if (rand.nextBoolean()) {
            // win — double XP
            int currentXp = member.getExperience();
            member.gainExperience(currentXp); // adds another full xp = double
            return Result.WIN;
        } else {
            // lose — halve XP
            int currentXp = member.getExperience();
            member.resetExperience();
            member.gainExperience(currentXp / 2);
            return Result.LOSE;
        }
    }
}