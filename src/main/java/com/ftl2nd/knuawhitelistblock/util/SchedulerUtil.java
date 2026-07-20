package com.ftl2nd.knuawhitelistblock.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public final class SchedulerUtil {

    private static final boolean IS_FOLIA;

    static {
        boolean isFolia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException ignored) {}
        IS_FOLIA = isFolia;
    }

    private SchedulerUtil() {}

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    /**
     * Executes a task asynchronously immediately.
     */
    public static TaskWrapper runAsync(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getAsyncScheduler().runNow(plugin, task -> runnable.run()));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
        }
    }

    /**
     * Executes a task asynchronously after a delay.
     */
    public static TaskWrapper runAsyncDelayed(Plugin plugin, Runnable runnable, long delay, TimeUnit unit) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getAsyncScheduler().runDelayed(plugin, task -> runnable.run(), delay, unit));
        } else {
            long ticks = unit.toMillis(delay) / 50L;
            return new TaskWrapper(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, Math.max(1, ticks)));
        }
    }

    /**
     * Executes a repeating task asynchronously.
     */
    public static TaskWrapper runAsyncRepeating(Plugin plugin, Runnable runnable, long delay, long period, TimeUnit unit) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task -> runnable.run(), delay, period, unit));
        } else {
            long delayTicks = unit.toMillis(delay) / 50L;
            long periodTicks = unit.toMillis(period) / 50L;
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimerAsynchronously(
                    plugin, 
                    runnable, 
                    Math.max(1, delayTicks), 
                    Math.max(1, periodTicks)
            ));
        }
    }

    /**
     * Executes a task on the global region (main thread) immediately.
     */
    public static TaskWrapper runSync(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getGlobalRegionScheduler().run(plugin, task -> runnable.run()));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, runnable));
        }
    }

    /**
     * Executes a task on the global region after a delay in ticks.
     */
    public static TaskWrapper runSyncDelayed(Plugin plugin, Runnable runnable, long delayTicks) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> runnable.run(), Math.max(1, delayTicks)));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, runnable, Math.max(1, delayTicks)));
        }
    }

    /**
     * Executes a repeating task on the global region.
     */
    public static TaskWrapper runSyncRepeating(Plugin plugin, Runnable runnable, long delayTicks, long periodTicks) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                    plugin, 
                    task -> runnable.run(), 
                    Math.max(1, delayTicks), 
                    Math.max(1, periodTicks)
            ));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimer(
                    plugin, 
                    runnable, 
                    Math.max(1, delayTicks), 
                    Math.max(1, periodTicks)
            ));
        }
    }

    /**
     * Executes a task at a specific location region thread.
     */
    public static TaskWrapper runAtLocation(Plugin plugin, Location location, Runnable runnable) {
        if (IS_FOLIA) {
            return new TaskWrapper(Bukkit.getRegionScheduler().run(plugin, location, task -> runnable.run()));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, runnable));
        }
    }

    /**
     * Executes a task at an entity's region thread.
     */
    public static TaskWrapper runAtEntity(Plugin plugin, Entity entity, Runnable runnable) {
        if (IS_FOLIA) {
            return new TaskWrapper(entity.getScheduler().run(plugin, task -> runnable.run(), null));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, runnable));
        }
    }

    public static TaskWrapper runAtEntityLater(Plugin plugin, Entity entity, Runnable runnable, Runnable retiredRunnable, long delayTicks) {
        if (IS_FOLIA) {
            return new TaskWrapper(entity.getScheduler().runDelayed(
                    plugin, 
                    task -> runnable.run(), 
                    retiredRunnable, 
                    Math.max(1, delayTicks)
            ));
        } else {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, runnable, Math.max(1, delayTicks)));
        }
    }

    /**
     * A unified wrapper class for both BukkitTask and Folia's ScheduledTask.
     */
    public static final class TaskWrapper {
        private final BukkitTask bukkitTask;
        private final ScheduledTask scheduledTask;

        public TaskWrapper(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
            this.scheduledTask = null;
        }

        public TaskWrapper(ScheduledTask scheduledTask) {
            this.bukkitTask = null;
            this.scheduledTask = scheduledTask;
        }

        /**
         * Cancels the scheduled task.
         */
        public void cancel() {
            if (scheduledTask != null) {
                scheduledTask.cancel();
            } else if (bukkitTask != null) {
                bukkitTask.cancel();
            }
        }

        /**
         * Checks if the task is currently cancelled.
         */
        public boolean isCancelled() {
            if (scheduledTask != null) {
                return scheduledTask.isCancelled();
            } else if (bukkitTask != null) {
                return bukkitTask.isCancelled();
            }
            return true;
        }

        /**
         * Returns the underlying BukkitTask, if available.
         */
        public BukkitTask getBukkitTask() {
            return bukkitTask;
        }

        /**
         * Returns the underlying ScheduledTask, if available.
         */
        public ScheduledTask getScheduledTask() {
            return scheduledTask;
        }
    }
}
