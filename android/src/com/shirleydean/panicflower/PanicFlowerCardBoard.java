package com.shirleydean.panicflower;

import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

public class PanicFlowerCardBoard extends CardBoardAndroidApplication implements CardBoardApplicationListener {

    private CardboardCamera cam;
    private Environment environment;
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000.0f;
    private static final float CAMERA_Z = 0.01f;

    private ModelBatch modelBatch;
    AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();

    boolean loading;
    private ModelInstance modelInstance;

    public static final String modelName = "flower.g3db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(this, config);
    }

    @Override
    public void create() {
        cam = new CardboardCamera();
        cam.position.set(0f, 0f, CAMERA_Z);
        cam.lookAt(0, 0, 0);
        cam.near = Z_NEAR;
        cam.far = Z_FAR;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();
        assets = new AssetManager();
        assets.load(modelName, Model.class);
        loading = true;
    }
    
    private void doneLoading() {
        Model model = assets.get(modelName, Model.class);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(0, -0.75f, -1);
        modelInstance.transform.scale(0.5f, 0.5f, 0.5f);
        instances.add(modelInstance);
        loading = false;
    }
    
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }

    @Override
    public void onNewFrame(HeadTransform paramHeadTransform) {
        if (modelInstance != null){
        	modelInstance.transform.rotate(0, 1, 0, Gdx.graphics.getDeltaTime() * 5);
        }
    }

    @Override
    public void onDrawEye(Eye eye) {
        if (loading && assets.update())
            doneLoading();
    	
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        cam.setEyeViewAdjustMatrix(new Matrix4(eye.getEyeView()));
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
        cam.setEyeProjection(new Matrix4(perspective));
		cam.update();

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    @Override
    public void onFinishFrame(Viewport paramViewport) {

    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    public void onCardboardTrigger() {

    }
}
