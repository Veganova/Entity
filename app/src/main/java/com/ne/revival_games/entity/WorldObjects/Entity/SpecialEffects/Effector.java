package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 7/12/2017.
 */

public interface Effector {

    boolean isEffect(Body effectBody);

    void addEffect(Effect effect);

    void removeEffect(Effect effect);

    void disableAllEffects();

    void enableAllEffects();
}
