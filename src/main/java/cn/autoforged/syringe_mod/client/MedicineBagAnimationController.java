package cn.autoforged.syringe_mod.client;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Client-only render state for the medicine bag lid and the retracting ampoule rack.
 * Closing uses the same timeline in reverse, so it cannot drift from the opening curve.
 */
public final class MedicineBagAnimationController {
    public static final float DURATION_SECONDS = 0.6F;

    private static final float MAX_FRAME_DELTA_SECONDS = 0.1F;
    private static final float[] KEY_TIMES = {0.0F, 0.1F, 0.25F, 0.45F, 0.6F};
    private static final float[] LID_ANGLES = {0.0F, -5.0F, -28.0F, -62.0F, -70.0F};
    private static final float[] AMPOULE_OFFSETS = {-1.75F, -1.75F, -1.15F, -0.25F, 0.0F};
    private static final Map<LivingEntity, TimelineState> ENTITY_STATES = new WeakHashMap<>();
    private static final Pose CLOSED_POSE = samplePose(0.0F);

    public static Pose sample(LivingEntity owner, boolean opening) {
        long now = System.nanoTime();
        TimelineState state = ENTITY_STATES.computeIfAbsent(owner, ignored -> new TimelineState(now));

        float elapsed = Math.min(
                (now - state.lastUpdateNanos) / 1_000_000_000.0F,
                MAX_FRAME_DELTA_SECONDS);
        state.lastUpdateNanos = now;
        state.timelineSeconds = Mth.clamp(
                state.timelineSeconds + (opening ? elapsed : -elapsed),
                0.0F,
                DURATION_SECONDS);
        return samplePose(state.timelineSeconds);
    }

    public static Pose closedPose() {
        return CLOSED_POSE;
    }

    static Pose samplePose(float timelineSeconds) {
        float time = Mth.clamp(timelineSeconds, 0.0F, DURATION_SECONDS);
        return new Pose(
                sampleMonotoneCurve(KEY_TIMES, LID_ANGLES, time),
                sampleMonotoneCurve(KEY_TIMES, AMPOULE_OFFSETS, time));
    }

    private static float sampleMonotoneCurve(float[] times, float[] values, float time) {
        if (time <= times[0]) {
            return values[0];
        }
        int last = times.length - 1;
        if (time >= times[last]) {
            return values[last];
        }

        int segment = 0;
        while (time > times[segment + 1]) {
            segment++;
        }

        float duration = times[segment + 1] - times[segment];
        float position = (time - times[segment]) / duration;
        float slope = (values[segment + 1] - values[segment]) / duration;
        float startTangent = tangentAt(times, values, segment);
        float endTangent = tangentAt(times, values, segment + 1);

        if (slope == 0.0F) {
            startTangent = 0.0F;
            endTangent = 0.0F;
        } else {
            if (startTangent * slope <= 0.0F) {
                startTangent = 0.0F;
            }
            if (endTangent * slope <= 0.0F) {
                endTangent = 0.0F;
            }
            float startRatio = startTangent / slope;
            float endRatio = endTangent / slope;
            float magnitude = startRatio * startRatio + endRatio * endRatio;
            if (magnitude > 9.0F) {
                float scale = 3.0F / Mth.sqrt(magnitude);
                startTangent = scale * startRatio * slope;
                endTangent = scale * endRatio * slope;
            }
        }

        float positionSquared = position * position;
        float positionCubed = positionSquared * position;
        float startBasis = 2.0F * positionCubed - 3.0F * positionSquared + 1.0F;
        float startTangentBasis = positionCubed - 2.0F * positionSquared + position;
        float endBasis = -2.0F * positionCubed + 3.0F * positionSquared;
        float endTangentBasis = positionCubed - positionSquared;
        return startBasis * values[segment]
                + startTangentBasis * duration * startTangent
                + endBasis * values[segment + 1]
                + endTangentBasis * duration * endTangent;
    }

    private static float tangentAt(float[] times, float[] values, int index) {
        if (index == 0) {
            return (values[1] - values[0]) / (times[1] - times[0]);
        }
        int last = times.length - 1;
        if (index == last) {
            return (values[last] - values[last - 1]) / (times[last] - times[last - 1]);
        }
        return (values[index + 1] - values[index - 1])
                / (times[index + 1] - times[index - 1]);
    }

    public record Pose(float lidAngleDegrees, float ampouleOffsetY) {
    }

    private static final class TimelineState {
        private long lastUpdateNanos;
        private float timelineSeconds;

        private TimelineState(long now) {
            this.lastUpdateNanos = now;
        }
    }

    private MedicineBagAnimationController() {
    }
}
