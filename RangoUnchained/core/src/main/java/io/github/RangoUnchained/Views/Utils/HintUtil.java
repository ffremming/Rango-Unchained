package io.github.RangoUnchained.Views.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PowerUpEntity;
import io.github.RangoUnchained.Model.level.GameLevel;

public class HintUtil {

    public static String hint;

    public static Map<Integer, ArrayList<String>> hints = new HashMap<>();

    public static void initializeHints() {
        hints = new HashMap<>();

        // Scenario 1: Early death with low score
        ArrayList<String> earlyDeathHints = new ArrayList<>();
        earlyDeathHints.add("HINT: Click shoot to shoot the balls.\nHitting them will make them smaller and eventually destroy them.");
        earlyDeathHints.add("HINT: Try moving side to side early on to avoid getting trapped.");
        earlyDeathHints.add("HINT: Don’t rush into the middle—observe the ball patterns first!");
        hints.put(1, earlyDeathHints);

        // Scenario 2: Decent score but sudden death
        ArrayList<String> decentScoreHints = new ArrayList<>();
        decentScoreHints.add("HINT: Watch your corners—don’t get trapped when balls split.");
        decentScoreHints.add("HINT: You're getting the hang of it! Now focus on dodging after ball splits.");
        decentScoreHints.add("HINT: Try clearing one side before engaging the other to avoid being overwhelmed.");
        hints.put(2, decentScoreHints);

        // Scenario 3: Death after collecting a debuff
        ArrayList<String> debuffDeathHints = new ArrayList<>();
        debuffDeathHints.add("HINT: Not all pickups are helpful - some are debuffs!");
        debuffDeathHints.add("HINT: If you pick up a debuff, prioritize dodging until the effect wears off.");
        hints.put(3, debuffDeathHints);

        // Scenario 4: Power-up spawned but wasnt picked up
        ArrayList<String> missedPowerupHints = new ArrayList<>();
        missedPowerupHints.add("HINT: Grab power-ups when it's safe—they can make a huge difference!");
        missedPowerupHints.add("HINT: A shield or speed boost might’ve saved you — don’t leave them behind.");
        hints.put(4, missedPowerupHints);

        // Scenario 6: Long survival but chaotic death
        ArrayList<String> longSurvivalHints = new ArrayList<>();
        longSurvivalHints.add("HINT: Great endurance! Try to keep the screen clear before grabbing items.");
        longSurvivalHints.add("HINT: Too many balls? Split them slowly to stay in control. Patience is key.");
        longSurvivalHints.add("HINT: The longer you last, the more careful you need to be with spacing and power-up timing.");
        hints.put(5, longSurvivalHints);

        // Scenario 7: General tips or motivational hints
        ArrayList<String> generalTips = new ArrayList<>();
        generalTips.add("HINT: Every run teaches you something — keep practicing!");
        generalTips.add("HINT: Getting better? Try improving your split timing.");
        generalTips.add("HINT: Watch for patterns in the way balls bounce and split.");
        generalTips.add("HINT: Split balls when there's enough space to react to the new ones.");
        generalTips.add("HINT: The smallest balls bounce low and fast—stay sharp!");
        hints.put(6, generalTips);
    }



    public static String getHint() {
        return hint;
    }

    public static void setHint(GameLevel level) {

        HintUtil.initializeHints();
        Random random = new Random();
        double timer = level.getTimer().getTime();
        int score = level.getScore();

        // If powerups spawned but were not yet picked up
        for (Entity entity : level.getEntities()) {
            if (entity instanceof PowerUpEntity) {
                hint = hints.get(4).get(random.nextInt(hints.get(4).size()));
                return;
            }
        }
        // Early death with low points
        if (timer < 15 && score < 5) {
            hint = hints.get(1).get(random.nextInt(hints.get(1).size()));
        }
        // Decent score but sudden death
        else if (timer < 20 && score < 10) {
            hint = hints.get(2).get(random.nextInt(hints.get(2).size()));
        }
        // TODO: Death after collecting debuff
        else if (timer == 0) {
            hint = hints.get(3).get(random.nextInt(hints.get(3).size()));
        }
        // Death after long time
        else if (timer > 20) {
            hint = hints.get(5).get(random.nextInt(hints.get(5).size()));
        } else {
            hint = hints.get(6).get(random.nextInt(hints.get(6).size()));
        }
    }

}
