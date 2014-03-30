package com.example.speedway;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;


import com.example.speedway.VerticalParallaxBackground.ParallaxEntity;

public class SpeedwayLauncher extends BaseGameActivity  implements IOnSceneTouchListener, IAccelerometerListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BitmapTextureAtlas mCarTexture;
	private TextureRegion mCarTextureRegion;

	private BitmapTextureAtlas mBackgroundTexture;
	private TextureRegion mBackgroundTextureRegion;
	
	private BitmapTextureAtlas mBandTexture;
	private TextureRegion mBandTextureRegion;
	
	private float mSpeedY = 10;
	private float mSpeedX = 200;
	
	private Sprite mCar;
	
	private PhysicsHandler mPhysicsHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mCarTexture = new BitmapTextureAtlas(32, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mCarTexture, this, "gfx/car.png", 0, 0);

		this.mBackgroundTexture = new BitmapTextureAtlas(1024, 512, TextureOptions.DEFAULT);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "gfx/bg.png", 0, 0);
		
		this.mBandTexture = new BitmapTextureAtlas(64, 512, TextureOptions.DEFAULT);
		this.mBandTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBandTexture, this, "gfx/band.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTextures(this.mCarTexture, this.mBackgroundTexture, this.mBandTexture);
	}
	private VerticalAutoParallaxBackground mAutoParallaxBackground;
	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene();
		scene.setOnSceneTouchListener(this);
		mAutoParallaxBackground = new VerticalAutoParallaxBackground(0, 0, 0, 5);
		mAutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(10.0f, new Sprite(0, CAMERA_HEIGHT - this.mBackgroundTextureRegion.getHeight(), this.mBackgroundTextureRegion)));
		mAutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(10.0f, new Sprite(0, CAMERA_HEIGHT - this.mBandTextureRegion.getHeight(), this.mBandTextureRegion.clone())));
		mAutoParallaxBackground.attachParallaxEntity(new ParallaxEntity(10.0f, new Sprite(CAMERA_WIDTH - this.mBandTextureRegion.getWidth(), CAMERA_HEIGHT - this.mBandTextureRegion.getHeight(), this.mBandTextureRegion.clone())));
		scene.setBackground(mAutoParallaxBackground);
		
		this.mCar = new Sprite (CAMERA_WIDTH * 0.5f - this.mCarTextureRegion.getWidth() * 0.5f, CAMERA_HEIGHT - this.mCarTextureRegion.getHeight() * 1.2f, this.mCarTextureRegion);
		scene.attachChild(this.mCar);
		
		this.mPhysicsHandler = new PhysicsHandler(mCar);
		mCar.registerUpdateHandler(this.mPhysicsHandler);
		
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			mSpeedY = mSpeedY + 5;
			mAutoParallaxBackground.setParallaxChangePerSecond(mSpeedY);
			return true;
		} 
		return false;
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		if (pAccelerometerData.getX() > 0.3f) {
			this.mPhysicsHandler.setVelocity(pAccelerometerData.getX()*mSpeedX, 0);
		} else if (pAccelerometerData.getX() < -0.3f) {
			this.mPhysicsHandler.setVelocity(pAccelerometerData.getX()*mSpeedX, 0);
		} else this.mPhysicsHandler.setVelocity(0, 0); 
		
		if (this.mCar.getX() + this.mCar.getWidth() < 0) {
			this.mCar.setPosition(CAMERA_WIDTH, this.mCar.getY());
		} else if (this.mCar.getX() - this.mCar.getWidth() > CAMERA_WIDTH) {
			this.mCar.setPosition(0 - this.mCar.getWidth(), this.mCar.getY());
		}
	}
	
	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerometerSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerometerSensor();
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}