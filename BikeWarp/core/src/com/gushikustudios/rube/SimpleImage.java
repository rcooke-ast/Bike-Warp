package com.gushikustudios.rube;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SimpleImage {
	private Sprite mSprite;
	private final Vector2 mCenter = new Vector2();
	private final Vector2 mHalfSize = new Vector2();
	private static final Vector2 mTmp = new Vector2();

	public SimpleImage(Texture texture, boolean flip, Color color, Vector2 size,
			Vector2 center, float rotationInDegrees) {
		mSprite = new Sprite(texture);
		defineImage(flip,color,size,center,rotationInDegrees);
	}

	public SimpleImage(TextureRegion region, boolean flip, Color color,
			Vector2 size, Vector2 center, float rotationInDegrees) {
		mSprite = new Sprite(region);
		defineImage(flip,color,size,center,rotationInDegrees);
	}
	
	public void defineImage(boolean flip, Color color, Vector2 size,
			Vector2 center, float rotationInDegrees)
	{
		mSprite.flip(flip, false);
		mSprite.setColor(color);
		mSprite.setSize(size.x, size.y);
		mSprite.setOrigin(size.x / 2, size.y / 2);
		mHalfSize.set(size.x / 2, size.y / 2);
		mCenter.set(center);
		mTmp.set(center.x - size.x / 2, center.y - size.y / 2);
		mSprite.setRotation(rotationInDegrees);
		mSprite.setPosition(mTmp.x, mTmp.y);
	}

	public void render(SpriteBatch batch) {
		mSprite.draw(batch);
	}
}
