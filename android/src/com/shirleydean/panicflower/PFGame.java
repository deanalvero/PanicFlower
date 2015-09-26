package com.shirleydean.panicflower;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;

/*******************************************************************************
 * Copyright 2015 Dean Alvero (deanalvero@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
public class PFGame implements ApplicationListener {
    public PerspectiveCamera cam;
    ModelInstance modelInstance;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;

    public static final String modelName = "flower.g3db";
    
    SpriteBatch batch;
    BitmapFont font;

    String upperText;
    String lowerText;
    
    int upperTextX;
    int lowerTextX;

//    static final int centerPoint = 100;
    static final int multiplier = 3;
    float rotationModelInstance;
    
    public int width;
    public int height;
    
    public void setUpperText(String text){
    	this.upperText = text;
    	upperTextX = width / 2 - multiplier*this.upperText.length();
    }
    
    public void setLowerText(String text){
    	this.lowerText = text;
    	lowerTextX = width / 2 - multiplier*this.lowerText.length();
    }

    @Override
    public void create () {
//    	setUpperText("To my deanest Shirley");
//    	setUpperText("To whom it may concern!");
//    	setLowerText("Happy Valentines Day!");

        batch = new SpriteBatch();    
        font = new BitmapFont();
        font.setColor(Color.RED);

        modelBatch = new ModelBatch();
        environment = new Environment();

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f,1f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
         
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0.0001f,2f,0.0001f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();
        assets = new AssetManager();
        assets.load(modelName, Model.class);
        loading = true;
    }
 

    private void doneLoading() {
        Model model = assets.get(modelName, Model.class);
        modelInstance = new ModelInstance(model);
        
        this.rotationModelInstance = 0f;
        modelInstance.transform.rotate(0f, 1f, 0, 45f);
        
        instances.add(modelInstance);
        loading = false;
    }

    public void setRotationModelInstance(float pRotation){
        modelInstance.transform.rotate(0f, 1f, 0, pRotation - this.rotationModelInstance);
    	this.rotationModelInstance = pRotation;
    }

    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
        
        batch.begin();
        font.draw(batch, upperText, upperTextX, 120);	//700);
        font.draw(batch, lowerText, lowerTextX, 100);

        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        font.dispose();
    	
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}



