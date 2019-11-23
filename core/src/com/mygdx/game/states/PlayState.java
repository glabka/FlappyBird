package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;

import javax.xml.soap.Text;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;

    private Bird bird;
    private Texture bg;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 100);
        bg = new Texture("background.png");
        cam.setToOrtho(false, MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2);

        tubes = new Array<Tube>();
        for (int i = 1; i <= TUBE_COUNT; i++){
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80; // 80 == just offset

        for(int i = 0; i < tubes.size; i++){
            Tube tube = tubes.get(i);

            if(cam.position.x - (cam.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTubeTexture().getWidth()){
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }
            if(tube.collides(bird.getBounds())){
                gsm.set(new PlayState(gsm));
            }
        }

        cam.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
//        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0); // original line from video
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), cam.position.y - (cam.viewportHeight /2));
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTubeTexture(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTubeTexture(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        for(Tube tube : tubes){
            tube.dispose();
        }
        System.out.println("PlayState disposed");
    }
}
