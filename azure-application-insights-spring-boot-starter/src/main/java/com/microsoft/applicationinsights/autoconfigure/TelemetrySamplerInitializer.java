package com.microsoft.applicationinsights.autoconfigure;

import com.microsoft.applicationinsights.channel.TelemetrySampler;
import com.microsoft.applicationinsights.internal.channel.sampling.AdaptiveTelemetrySampler;
import com.microsoft.applicationinsights.internal.channel.sampling.FixedRateTelemetrySampler;

final class TelemetrySamplerInitializer {
    /**
     * Sets the configuration data of Context Initializers in configuration class.
     *
     * @param sampler The configuration data.
     */
    public TelemetrySampler getSampler(ApplicationInsightsProperties.TelemetryProcessor.Sampling sampler) {
        if (sampler == null) {
            return null;
        }

        TelemetrySampler telemetrySampler;
        if (sampler.getAdaptive().isEnabled()) {
            ApplicationInsightsProperties.TelemetryProcessor.Sampling.Adaptive adaptiveSettings = sampler.getAdaptive();

            AdaptiveTelemetrySampler adaptiveTelemetrySampler = new AdaptiveTelemetrySampler();

            adaptiveTelemetrySampler.setIncludeTypes(String.join(",", sampler.getInclude()));
            adaptiveTelemetrySampler.setExcludeTypes(String.join(",", sampler.getExclude()));

            adaptiveTelemetrySampler.initialize(
                    String.valueOf(adaptiveSettings.getMaxTelemetryItemsPerSecond()),
                    String.valueOf(adaptiveSettings.getEvaluationIntervalInSec()),
                    String.valueOf(adaptiveSettings.getSamplingPercentageDecreaseTimeoutInSec()),
                    String.valueOf(adaptiveSettings.getSamplingPercentageIncreaseTimeoutInSec()),
                    String.valueOf(adaptiveSettings.getMinSamplingPercentage()),
                    String.valueOf(adaptiveSettings.getMaxSamplingPercentage()),
                    String.valueOf(adaptiveSettings.getInitialSamplingPercentage()),
                    String.valueOf(adaptiveSettings.getMovingAverageRatio())
            );

            telemetrySampler = adaptiveTelemetrySampler;

        } else {
            telemetrySampler = new FixedRateTelemetrySampler();
            telemetrySampler.setIncludeTypes(String.join(",", sampler.getInclude()));
            telemetrySampler.setExcludeTypes(String.join(",", sampler.getExclude()));
            telemetrySampler.setSamplingPercentage(sampler.getPercentage());
        }

        return telemetrySampler;
    }
}
