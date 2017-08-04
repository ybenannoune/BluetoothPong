package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entities.Ball;
import com.mygdx.game.Entities.Paddle;

public class BluetoothPong extends ApplicationAdapter implements GestureDetector.GestureListener {

    private static final String TAG = "BluetoothPong";

    //Renderer
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

    //Entites
	private Paddle playerPaddle;
    private Paddle opponentPaddle;
	private Ball ball;

    //Bluetooth Interface for Communications
	private IBluetooth bluetoothCom;

	@Override
	public void create () {

        shapeRenderer = new ShapeRenderer();

        //init camera
		camera = new OrthographicCamera(Constants.GAME_WIDTH,Constants.GAME_HEIGHT);
		camera.position.set(Constants.GAME_WIDTH /2 , Constants.GAME_HEIGHT /2 , 0.0F);

        //init entities
		playerPaddle = new Paddle(0,20);
		opponentPaddle = new Paddle(0,Constants.GAME_HEIGHT - 20);
		ball = new Ball();

        //init touch input
		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
	}

	public void onDisconnect()
	{
		ball.set(Constants.GAME_WIDTH /2 , Constants.GAME_HEIGHT /2,0,0);
	}

	public void onConnected(boolean isHost)
	{
		if( isHost )
		{
			ball.set(Constants.GAME_WIDTH /2 , Constants.GAME_HEIGHT /2,0,-300);
		}
		else
		{
			ball.set(Constants.GAME_WIDTH /2 , Constants.GAME_HEIGHT /2,0,300);
		}
	}

	public void setBluetoothInterface(IBluetooth interfaceBluetooth)
	{
		bluetoothCom = interfaceBluetooth;
	}

	private float getFloatFromStr(String str)
	{
		try
		{
			return Float.parseFloat(str);
		}
		catch (NumberFormatException ex)
		{
			return 0.0f;
		}
	}

	public void incomingMessage(String str)
	{
        int index,end;
        // Get Opponent Paddle Position from String
        index = str.indexOf("[");
        if(index != -1)
        {
            end = str.indexOf("]");
            if( end > index)
            {
                String pos = str.substring(index + 1,end);
                Gdx.app.log(TAG, pos);
                opponentPaddle.set(getFloatFromStr(pos));
            }
        }
        else //Handle Ball Position Update
        {
            index = str.indexOf("{");
            if(index != -1)
            {
                end = str.indexOf("}");
                if( end > index)
                {
                    String content = str.substring(index + 1,end);
                    Gdx.app.log(TAG, content);

                    String[] splited = content.split(":");
                    ball.set(getFloatFromStr(splited[0]),
                            getFloatFromStr(splited[1]),
                            getFloatFromStr(splited[2]),
                            getFloatFromStr(splited[3]));
                }
            }
        }
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

		camera.update();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(this.camera.combined);

        //Collision behavior
        Vector2 intersectVector = new Vector2();
        if(Intersector.intersectSegments(ball.getX(),ball.getY(),(ball.getX() + (ball.getVelocityX() * deltaTime)),( ball.getY() +  (ball.getVelocityY() * deltaTime)),
                playerPaddle.getX(),playerPaddle.getY() + Paddle.HEIGHT,playerPaddle.getX() + Paddle.WIDTH, playerPaddle.getY() + Paddle.HEIGHT,intersectVector))
        {
            float newVelY = ( ball.getVelocityY() + ( 20 * Math.signum( ball.getVelocityY() ) ) ) *-1;
            float newVelX =  (3.0f * (ball.getX() - (playerPaddle.getX()+ Paddle.WIDTH / 2)));

            //apply paddle bounce
            ball.set(intersectVector.x,intersectVector.y,newVelX,newVelY);
            String updatePositionMessage = "{" + intersectVector.x + ":" + (Constants.GAME_HEIGHT - intersectVector.y) + ":" + newVelX + ":" + -newVelY + "}";
            bluetoothCom.write(updatePositionMessage);
        }

		ball.update(deltaTime);

		playerPaddle.draw(shapeRenderer);
		opponentPaddle.draw(shapeRenderer);
		ball.draw(shapeRenderer);

		shapeRenderer.end();
		limitFrameRate(30);
	}

	private long diff, start = System.currentTimeMillis();
	public void limitFrameRate(int fps) {
		if (fps > 0)
		{
			diff = System.currentTimeMillis() - start;
			long targetDelay = 1000 / fps;
			if (diff < targetDelay) {
				try {
					Thread.sleep(targetDelay - diff);
				} catch (InterruptedException e) {
				}
			}
			start = System.currentTimeMillis();
		}
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		playerPaddle.move(deltaX);
		bluetoothCom.write("[" + playerPaddle.getX() + "]");
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}
}
