package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;

public class StateComponent<T extends State<Entity>> extends Component {

    public StateMachine<Entity, T> state;

}
