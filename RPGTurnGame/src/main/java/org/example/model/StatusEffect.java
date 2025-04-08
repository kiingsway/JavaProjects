package org.example.model;

public class StatusEffect {
  private StatusEffectType type;
  private int duration; // em turnos

  public StatusEffect(StatusEffectType type, int duration) {
    this.type = type;
    this.duration = duration;
  }

  public StatusEffectType getType() {
    return type;
  }

  public int getDuration() {
    return duration;
  }

  public void reduceDuration() {
    duration = Math.max(0, duration - 1);
  }

  public boolean isExpired() {
    return duration <= 0;
  }
}
