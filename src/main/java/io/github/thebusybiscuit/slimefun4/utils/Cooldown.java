package io.github.thebusybiscuit.slimefun4.utils;

import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Cooldown {
        private final Map<Block, Long> cooldowns = new HashMap<>();

        public boolean onCooldown(@Nonnull Block block, int cooldownSeconds) {
            long currentTime = System.nanoTime();
            long cooldownEnd = cooldowns.getOrDefault(block, currentTime);
            if (cooldownEnd > currentTime) return true;
            cooldowns.put(block, currentTime + TimeUnit.SECONDS.toNanos(cooldownSeconds));
            return false;
        }

        public long getTimeLeft(@Nonnull Block block) {
            long currentTime = System.nanoTime();
            long cooldownEnd = cooldowns.getOrDefault(block, currentTime);
            if (cooldownEnd > currentTime) return TimeUnit.NANOSECONDS.toSeconds(cooldownEnd - currentTime);
            return 0L;
        }
}
