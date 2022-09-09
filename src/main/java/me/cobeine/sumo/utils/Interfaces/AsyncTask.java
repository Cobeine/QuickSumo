package me.cobeine.sumo.utils.Interfaces;

import me.cobeine.sumo.Core;
import org.bukkit.scheduler.BukkitRunnable;

public interface AsyncTask {

    void run();
    static void run(AsyncTask task) {
        new BukkitRunnable() {
            public void run() {
                task.run();
            }
        }.runTaskAsynchronously(Core.getInstance());
    }


}
