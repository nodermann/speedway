package com.example.speedway;


public class VerticalAutoParallaxBackground extends VerticalParallaxBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mParallaxChangePerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VerticalAutoParallaxBackground(final float pRed, final float pGreen, final float pBlue, final float pParallaxChangePerSecond) {
		super(pRed, pGreen, pBlue);
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);

		this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
