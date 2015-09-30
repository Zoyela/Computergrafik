package simple;

import jrtr.*;
import jrtr.glrenderer.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.vecmath.*;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class simple{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static Shader normalShader;
	static Shader diffuseShader;
	static Material material;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float currentstep, basicstep;
	static VertexData vertexData;
	static String whatShape;
	

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. Here we construct
	 * a simple 3D scene and start a timer task to generate an animation.
	 */ 
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			simple.renderContext = r;
			simple.vertexData = make(simple.whatShape);
			
								
			// Make a scene manager and add the object
			simple.sceneManager = new SimpleSceneManager();
			simple.shape = new Shape(simple.vertexData);
			simple.sceneManager.addShape(simple.shape);

			// Add the scene to the renderer
			simple.renderContext.setSceneManager(simple.sceneManager);
			
			// Load some more shaders
		    simple.normalShader = simple.renderContext.makeShader();
		    try {
		    	simple.normalShader.load("../jrtr/shaders/normal.vert", "../jrtr/shaders/normal.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }
	
		    simple.diffuseShader = simple.renderContext.makeShader();
		    try {
		    	simple.diffuseShader.load("../jrtr/shaders/diffuse.vert", "../jrtr/shaders/diffuse.frag");
		    } catch(Exception e) {
		    	System.out.print("Problem with shader:\n");
		    	System.out.print(e.getMessage());
		    }

		    // Make a material that can be used for shading
			simple.material = new Material();
			simple.material.shader = simple.diffuseShader;
			simple.material.diffuseMap = simple.renderContext.makeTexture();
			try {
				simple.material.diffuseMap.load("../textures/plant.jpg");
			} catch(Exception e) {				
				System.out.print("Could not load texture.\n");
				System.out.print(e.getMessage());
			}

			// Register a timer task
		    Timer timer = new Timer();
		    simple.basicstep = 0.01f;
		    simple.currentstep = simple.basicstep;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}

		

	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public class AnimationTask extends TimerTask
	{
		public void run()
		{
			// Update transformation by rotating with angle "currentstep"
    		Matrix4f t = simple.shape.getTransformation();
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(simple.currentstep);
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(simple.currentstep);
    		t.mul(rotX);
    		t.mul(rotY);
    		simple.shape.setTransformation(t);
    		
    		// Trigger redrawing of the render window
    		simple.renderPanel.getCanvas().repaint(); 
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
    	public void mousePressed(MouseEvent e) {}
    	public void mouseReleased(MouseEvent e) {}
    	public void mouseEntered(MouseEvent e) {}
    	public void mouseExited(MouseEvent e) {}
    	public void mouseClicked(MouseEvent e) {}
	}
	
	/**
	 * A key listener for the main window. Use this to process key events.
	 * Currently this provides the following controls:
	 * 's': stop animation
	 * 'p': play animation
	 * '+': accelerate rotation
	 * '-': slow down rotation
	 * 'd': default shader
	 * 'n': shader using surface normals
	 * 'm': use a material for shading
	 */
	public static class SimpleKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyChar())
			{
				case 's': {
					// Stop animation
					simple.currentstep = 0;
					break;
				}
				case 'p': {
					// Resume animation
					simple.currentstep = simple.basicstep;
					break;
				}
				case '+': {
					// Accelerate roation
					simple.currentstep += simple.basicstep;
					break;
				}
				case '-': {
					// Slow down rotation
					simple.currentstep -= simple.basicstep;
					break;
				}
				case 'n': {
					// Remove material from shape, and set "normal" shader
					simple.shape.setMaterial(null);
					simple.renderContext.useShader(simple.normalShader);
					break;
				}
				case 'd': {
					// Remove material from shape, and set "default" shader
					simple.shape.setMaterial(null);
					simple.renderContext.useDefaultShader();
					break;
				}
				case 'm': {
					// Set a material for more complex shading of the shape
					if(simple.shape.getMaterial() == null) {
						simple.shape.setMaterial(simple.material);
					} else
					{
						simple.shape.setMaterial(null);
						simple.renderContext.useDefaultShader();
					}
					break;
				}
			}
			
			// Trigger redrawing
			simple.renderPanel.getCanvas().repaint();
		}
		
		public void keyReleased(KeyEvent e)
		{
		}

		public void keyTyped(KeyEvent e)
        {
        }

	}
	
	/**
	 * The main function opens a 3D rendering window, implemented by the class
	 * {@link SimpleRenderPanel}. {@link SimpleRenderPanel} is then called backed 
	 * for initialization automatically. It then constructs a simple 3D scene, 
	 * and starts a timer task to generate an animation.
	 */
	public static void main(String[] args){		
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		simple.renderPanel = new SimpleRenderPanel();
		simple.whatShape = args[0];
	
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(simple.renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse and key listener
	    simple.renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
	    simple.renderPanel.getCanvas().addKeyListener(new SimpleKeyListener());
		simple.renderPanel.getCanvas().setFocusable(true);   	    	    
	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}

	private VertexData make(String string) {
		
		
		String theShape = string;
	
		switch(theShape){
			case "cube": return makeCube();
			case "cylinder": return makeCylinder();
		}
		
		return null;
	}

	private VertexData makeCylinder() {
		// TODO Auto-generated method stub
		return null;
	}

	public VertexData makeCube() {
		// Make a simple geometric object: a cube

		// The vertex positions of the cube
		float v[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1,		// front face
			         -1,-1,-1, -1,-1,1, -1,1,1, -1,1,-1,	// left face
				  	 1,-1,-1,-1,-1,-1, -1,1,-1, 1,1,-1,		// back face
					 1,-1,1, 1,-1,-1, 1,1,-1, 1,1,1,		// right face
					 1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		// top face
					-1,-1,1, -1,-1,-1, 1,-1,-1, 1,-1,1};	// bottom face

		// The vertex normals 
		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1,			// front face
			         -1,0,0, -1,0,0, -1,0,0, -1,0,0,		// left face
				  	 0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,		// back face
					 1,0,0, 1,0,0, 1,0,0, 1,0,0,			// right face
					 0,1,0, 0,1,0, 0,1,0, 0,1,0,			// top face
					 0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0};		// bottom face

		// The vertex colors
		float c[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1};

		// Texture coordinates 
		float uv[] = {0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1};

		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = simple.renderContext.makeVertexData(24);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(uv, VertexData.Semantic.TEXCOORD, 2);
		
		// The triangles (three vertex indices for each triangle)
		int indices[] = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22};	// bottom face

		vertexData.addIndices(indices);
		
		return vertexData;
		
	}
	}
}
