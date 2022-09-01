package me.cobeine.qSumo.utils.metrics;

import me.cobeine.qSumo.Core;

public class MetricsImpl {
    private final Metrics metrics;
    final int pluginID = 16326;

    public MetricsImpl(Core core) {
        this.metrics = new Metrics(core,pluginID);
    }
}
