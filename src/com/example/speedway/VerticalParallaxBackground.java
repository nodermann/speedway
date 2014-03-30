package com.example.speedway;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;

public class VerticalParallaxBackground extends ColorBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ParallaxEntity> mParallaxEntities = new ArrayList<ParallaxEntity>();
	private int mParallaxEntityCount;

	protected float mParallaxValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VerticalParallaxBackground(final float pRed, final float pGreen, final float pBlue) {
		super(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxValue(final float pParallaxValue) {
		this.mParallaxValue = pParallaxValue;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GL10 pGL, final Camera pCamera) {
		super.onDraw(pGL, pCamera);

		final float parallaxValue = this.mParallaxValue;
		final ArrayList<ParallaxEntity> parallaxEntities = this.mParallaxEntities;

		for(int i = 0; i < this.mParallaxEntityCount; i++) {
			parallaxEntities.get(i).onDraw(pGL, parallaxValue, pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void attachParallaxEntity(final ParallaxEntity pParallaxEntity) {
		this.mParallaxEntities.add(pParallaxEntity);
		this.mParallaxEntityCount++;
	}

	public boolean detachParallaxEntity(final ParallaxEntity pParallaxEntity) {
		this.mParallaxEntityCount--;
		final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
		if(!success) {
			this.mParallaxEntityCount++;
		}
		return success;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ParallaxEntity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		final float mParallaxFactor;
		final Shape mShape;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntity(final float pParallaxFactor, final Shape pShape) {
			this.mParallaxFactor = pParallaxFactor;
			this.mShape = pShape;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public void onDraw(final GL10 pGL, final float pParallaxValue, final Camera pCamera) {
			pGL.glPushMatrix();
			{
				final float cameraHeight = pCamera.getHeight();
				final float shapeHeightScaled = this.mShape.getHeightScaled();
				float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeHeightScaled;

				while(baseOffset > 0) {
					baseOffset -= shapeHeightScaled;
				}
				pGL.glTranslatef(0, baseOffset, 0);

				float currentMaxX = baseOffset;

				do {
					this.mShape.onDraw(pGL, pCamera);
					pGL.glTranslatef(0, shapeHeightScaled, 0);
					currentMaxX += shapeHeightScaled;
				} while(currentMaxX < cameraHeight);
			}
			pGL.glPopMatrix();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
