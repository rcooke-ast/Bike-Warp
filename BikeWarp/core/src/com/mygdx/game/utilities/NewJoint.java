package com.mygdx.game.utilities;

import com.badlogic.gdx.math.Vector2;

public class NewJoint {
	String jointType;
	Vector2 anchorA;
	Vector2 anchorB;
	int bodyA;
	int bodyB;
	float length;

	public NewJoint(String jType, Vector2 ancA, Vector2 ancB, int bodA, int bodB, float len) {
		jointType = jType;
		anchorA = ancA;
		anchorB = ancB;
		bodyA = bodA;
		bodyB = bodB;
		length = len;
		return;
	}

}

