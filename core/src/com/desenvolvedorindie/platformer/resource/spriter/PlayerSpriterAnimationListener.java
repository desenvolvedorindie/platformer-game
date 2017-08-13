package com.desenvolvedorindie.platformer.resource.spriter;

import com.badlogic.gdx.Gdx;

import net.spookygames.gdx.spriter.SpriterAnimationAdapter;
import net.spookygames.gdx.spriter.SpriterAnimator;
import net.spookygames.gdx.spriter.data.SpriterAnimation;

public class PlayerSpriterAnimationListener extends SpriterAnimationAdapter {

    @Override
    public void onAnimationFinished(SpriterAnimator animator, SpriterAnimation animation) {
        if (animation.name.equals("jump_start")) {
            animator.play("jump_loop");
        }
    }

}
