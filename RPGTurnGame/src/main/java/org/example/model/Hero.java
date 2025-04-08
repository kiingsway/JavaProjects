package org.example.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Hero {

  // Atributos principais
  private String name;
  private int level;
  private int maxHP;
  private int currentHP;
  private int maxMP;
  private int currentMP;

  private int atk;  // Ataque físico
  private int def;  // Defesa física
  private int mag;  // Magia ofensiva
  private int res;  // Resistência mágica
  private int spd;  // Velocidade
  private int acc;  // Precisão
  private int eva;  // Esquiva
  private int crt;  // Chance de crítico
  private int con;  // Constituição
  private int lck;  // Sorte

  private List<StatusEffect> statusEffects = new ArrayList<>();

  // Construtor
  public Hero(String name, int level, int maxHP, int maxMP, int atk, int def, int mag, int res, int spd, int acc, int eva, int crt, int con, int lck) {
    this.name = name;
    this.level = level;
    this.maxHP = maxHP;
    this.currentHP = maxHP;
    this.maxMP = maxMP;
    this.currentMP = maxMP;

    this.atk = atk;
    this.def = def;
    this.mag = mag;
    this.res = res;
    this.spd = spd;
    this.acc = acc;
    this.eva = eva;
    this.crt = crt;
    this.con = con;
    this.lck = lck;
  }

  // Métodos de exemplo
  public void receiveDamage(int damage) {
    int finalDamage = Math.max(damage - def, 0);
    currentHP = Math.max(currentHP - finalDamage, 0);
    System.out.println(name + " recebeu " + finalDamage + " de dano.");
  }

  public boolean isAlive() {
    return currentHP > 0;
  }

  public void heal(int amount) {
    currentHP = Math.min(currentHP + amount, maxHP);
    System.out.println(name + " recuperou " + amount + " de HP.");
  }

  public void useMP(int amount) {
    currentMP = Math.max(currentMP - amount, 0);
  }

  public void regenerateMP(int amount) {
    currentMP = Math.min(currentMP + amount, maxMP);
  }

  public void addStatusEffect(StatusEffect effect) {
    statusEffects.add(effect);
    System.out.println(name + " foi afetado por " + effect.getType() + " por " + effect.getDuration() + " turnos.");
  }

  public void updateStatusEffects() {
    Iterator<StatusEffect> iterator = statusEffects.iterator();
    while (iterator.hasNext()) {
      StatusEffect effect = iterator.next();
      effect.reduceDuration();
      if (effect.isExpired()) {
        System.out.println(name + " se livrou do efeito: " + effect.getType());
        iterator.remove();
      }
    }
  }

  public boolean hasEffect(StatusEffectType type) {
    return statusEffects.stream().anyMatch(e -> e.getType() == type && !e.isExpired());
  }


  // Getters e Setters (pode gerar automaticamente com IDE)
  public String getName() {return name;}

  public String getHPText() {return "HP: " + currentHP + "/" + maxHP;}

  public String getMPText() {return "MP: " + currentMP + "/" + maxMP;}

  // toString() para debug
  @Override
  public String toString() {
    return name + " [HP: " + currentHP + "/" + maxHP + ", MP: " + currentMP + "/" + maxMP + "]";
  }

  public int getLevel() {return level;}
}
