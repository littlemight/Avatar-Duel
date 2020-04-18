package com.avatarduel.model;

/**
 * Phase enum for the game phase.
 * These phase also include "sub-phases" such as when summoning a skill card and picking a character, or discarding an excess card
 */
public enum Phase {
    DRAW,
    MAIN,
    SKILLPICK,
    BATTLE,
    END,
    DISCARD
}
