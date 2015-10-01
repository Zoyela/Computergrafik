/*
 * 		Name: 				Linus Schwab
 * 		Matrikelnummer: 	09-125-592
 */

package main;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import jrtr.RenderContext;
import jrtr.Shape;
import jrtr.VertexData;

public class ShapeFactory {
	private static RenderContext renderContext;
	
	public ShapeFactory(RenderContext r) {
		renderContext = r;
	}

	public Shape createCube() {
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
//		float c[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
//				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
//					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
//					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
//					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
//					 0,0,1, 0,0,1, 0,0,1, 0,0,1};

		float c[] = {0,0,0.9f, 0,0,0.9f, 0,0,0.9f, 0,0,0.9f,	// back
					 0,0,0.8f, 0,0,0.8f, 0,0,0.8f, 0,0,0.8f,	// sides
					 0,0,0.9f, 0,0,0.9f, 0,0,0.9f, 0,0,0.9f, 	// front
					 0,0,0.8f, 0,0,0.8f, 0,0,0.8f, 0,0,0.8f,	// sides
					 0,0,0.7f, 0,0,0.7f, 0,0,0.7f, 0,0,0.7f,	// top
					 0,0,0.7f, 0,0,0.7f, 0,0,0.7f, 0,0,0.7f};	// bottom

		// Texture coordinates 
		float uv[] = {0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1,
				  0,0, 1,0, 1,1, 0,1};

		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = renderContext.makeVertexData(24);
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
		
		return new Shape(vertexData);
	}
	
	public Shape createCylinder(int segments, double height, double size, double red, double green, double blue) {		
		// Number of vertex used
		final int VERTEX = 2*segments+2;
		
		// Calculate vertex positions
		double angle = (2*Math.PI / segments);
		ArrayList<Float> vertex = new ArrayList<Float>();
		
		// Lower circle
		vertex.add(0.0f);
		vertex.add(0.0f);
		vertex.add(0.0f);
		
		for (int i=1; i <= segments; i++) {
			vertex.add((float) (size*Math.cos(angle*i)));	// x
			vertex.add((float) (size*Math.sin(angle*i)));	// y
			vertex.add((float) 0.0);			 			// z
		}
		
		// Upper circle
		vertex.add(0.0f);
		vertex.add(0.0f);
		vertex.add((float) height);
		
		for (int i=1; i <= segments; i++) {
			vertex.add((float) (size*Math.cos(angle*i)));	// x
			vertex.add((float) (size*Math.sin(angle*i)));	// y
			vertex.add((float) height);			 			// z
		}

		// Convert ArrayList to array
		float v[] = new float[vertex.size()];
		int j = 0;
		for (Float f : vertex) {
			v[j++] = f;
		}
		
		// The texture coordinates		
		int counter = 0;
		float[] t = new float[4 * segments + 4];
		t[counter++] = 1 / 2;
		t[counter++] = 1 / 2;
		for (int i = 0; i < segments; i++) {
			t[counter++] = i / (float) (segments - 1);
			t[counter++] = 0;
		}
		t[counter++] = 1 / 2;
		t[counter++] = 1 / 2;
		for (int i = 0; i < segments; i++) {
			t[counter++] = i / (float) (segments - 1);
			t[counter++] = 1;
		}

		// The vertex colors
		float c[] = new float[VERTEX*3];
		for (int i=0; i < VERTEX*3; i+=3) {
			if (i<=segments*3) {
				if (i%2==0) {
					c[i] = (float) red;
					c[i+1] = (float) green;
					c[i+2] = (float) blue;
				} else {
					c[i] = (float) 1;
					c[i+1] = (float) 1;
					c[i+2] = (float) 1;
				}
			}
			else {
				if (i%2==0){
					c[i] = (float) 1;
					c[i+1] = (float) 1;
					c[i+2] = (float) 1;
				} else {
					c[i] = (float) red;
					c[i+1] = (float) green;
					c[i+2] = (float) blue;
				}
			}
		}
		
		// The normals
		ArrayList<Float> normals = new ArrayList<Float>();
		
		normals = calculateNormalsCylinder(v, segments);
		
		// Convert ArrayList to array
		float n[] = new float[normals.size()];
		int l = 0;
		for (Float f : normals) {
			n[l++] = f;
		}		
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = renderContext.makeVertexData(VERTEX);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		
		// The triangles (three vertex indices for each triangle)
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		// Lower circle
		for (int i=1; i <= segments; i++) {
			index.add(0); // center
			index.add(i);
			if (i != segments)
				index.add(i+1);
			// last triangle
			else
				index.add(1);
		}
		
		// Upper circle
		for (int i=segments+2; i <= VERTEX-1; i++) {
			index.add(segments+1); // center
			index.add(i);
			if (i != VERTEX-1)
				index.add(i+1);
			// last triangle
			else
				index.add(segments+2);
		}
		
		// Mantle (lower triangles)
		for (int i=1; i <= segments; i++) {
			index.add(i);
			if (i != segments) {
				index.add(i+1);
				index.add(i+segments+2);
			// last triangle
			} else {
				index.add(1);
				index.add(segments+2);
			}
		}
		
		// Mantle (upper triangles)
		for (int i=1; i <= segments; i++) {
			index.add(i);
			if (i != segments)
				index.add(i+segments+2);
			// last triangle
			else
				index.add(segments+2);
			index.add(i+segments+1);
		}
		
		// Convert ArrayList to array
		int indices[] = new int[index.size()];
		int k = 0;
		for (Integer i : index) {
			indices[k++] = i;
		}
		
		vertexData.addIndices(indices);
		
		return new Shape(vertexData);
	}
	
	public Shape createTorus(int segments, double R, double r, int stripes, double red, double green, double blue) {
		// Number of vertex used
		final int VERTEX = segments*segments;
		
		// Calculate vertex positions
		double angle = (2*Math.PI / segments);
		ArrayList<Float> vertex = new ArrayList<Float>();
		
		for (int i=1; i <= segments; i++) {
			for (int j=1; j <= segments; j++) {
				vertex.add((float) ((R+r*Math.cos(angle*i))*Math.cos(angle*j)));	// x
				vertex.add((float) ((R+r*Math.cos(angle*i))*Math.sin(angle*j)));	// y
				vertex.add((float) (r*Math.sin(angle*i)));			 				// z
			}
		}
		
		// Convert ArrayList to array
		float v[] = new float[vertex.size()];
		int k = 0;
		for (Float f : vertex) {
			v[k++] = f;
		}
		
		// The vertex colors
		float c[] = new float[VERTEX*3];
		for (int i=0; i < VERTEX*3; i+=3) {
			if (stripes==0) {
				c[i] = (float) Math.random();
				c[i+1] = (float) Math.random();
				c[i+2] = (float) Math.random();
			}
			else {
				if (i%stripes==0) {
					c[i] = (float) red;
					c[i+1] = (float) green;
					c[i+2] = (float) blue;
				} else {
					c[i] = (float) 1;
					c[i+1] = (float) 1;
					c[i+2] = (float) 1;
				}
			}
		}

		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = renderContext.makeVertexData(VERTEX);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		
		// The triangles (three vertex indices for each triangle)
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		// Lower triangles
		for (int i=0; i < segments; i++) {
			for (int j=0; j < segments; j++) {
				if (j != segments-1) {
					index.add(segments*i+j);
					if (i != segments-1) {
						index.add(segments*(i+1)+j);
						index.add(segments*(i+1)+1+j);
					// last triangle in segment
					} else {
						index.add(0+j);
						index.add(1+j);
					}
				}
				// last segment
				else {
					index.add(segments*(i+1)-1);
					if (i != segments-1) {
						index.add(segments*(i+2)-1);
						index.add(segments*(i+1));
					// last triangle in segment
					} else {
						index.add(segments-1);
						index.add(0);
					}
				}

			}
		}
		
		// Upper triangles
		for (int i=0; i < segments; i++) {
			for (int j=0; j < segments; j++) {
				if (j != segments-1) {
					if (i != segments-1) {
						index.add(segments*(i+1)+1+j);
					// last triangle in segment
					} else {
						index.add(1+j);
					}
					index.add(segments*i+1+j);
					index.add(segments*i+j);
				}
				// last segment
				else {
					if (i != segments-1) {
						index.add(segments*(i+1));
					// last triangle in segment
					} else {
						index.add(0);
					}
					index.add(segments*i);
					index.add(segments*(i+1)-1);
				}
			}
		}
		
		// Convert ArrayList to array
		int indices[] = new int[index.size()];
		int l = 0;
		for (Integer i : index) {
			indices[l++] = i;
		}
		
		vertexData.addIndices(indices);
		
		return new Shape(vertexData);
	}
	
	public Shape createHouse()
	{
		// A house
		float vertices[] = {-4,-4,4, 4,-4,4, 4,4,4, -4,4,4,		// front face
							-4,-4,-4, -4,-4,4, -4,4,4, -4,4,-4, // left face
							4,-4,-4,-4,-4,-4, -4,4,-4, 4,4,-4,  // back face
							4,-4,4, 4,-4,-4, 4,4,-4, 4,4,4,		// right face
							4,4,4, 4,4,-4, -4,4,-4, -4,4,4,		// top face
							-4,-4,4, -4,-4,-4, 4,-4,-4, 4,-4,4, // bottom face
	
							-20,-4,20, 20,-4,20, 20,-4,-20, -20,-4,-20, // ground floor
							-4,4,4, 4,4,4, 0,8,4,				// the roof
							4,4,4, 4,4,-4, 0,8,-4, 0,8,4,
							-4,4,4, 0,8,4, 0,8,-4, -4,4,-4,
							4,4,-4, -4,4,-4, 0,8,-4};
		
		float texcoord[] = {0,1, 1,1, 1,0, 0,0,		// front face
							0,1, 1,1, 1,0, 0,0, 	// left face
							0,1, 1,1, 1,0, 0,0,  	// back face
							0,1, 1,1, 1,0, 0,0,		// right face
							0,0, 0,1, 1,1, 1,0,		// top face
							0,0, 0,1, 1,1, 1,0, 	// bottom face

							0,0, 0,1, 1,1, 1,0, 	// ground floor
							0,1, 1,1, 1,0,			// the roof
							0,1, 1,1, 1,0, 0,0,
							1,1, 1,0, 0,0, 0,1,
							0,1, 1,1, 1,0};
	
		float normals[] = {0,0,1,  0,0,1,  0,0,1,  0,0,1,		// front face
						   -1,0,0, -1,0,0, -1,0,0, -1,0,0,		// left face
						   0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,		// back face
						   1,0,0,  1,0,0,  1,0,0,  1,0,0,		// right face
						   0,1,0,  0,1,0,  0,1,0,  0,1,0,		// top face
						   0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0,		// bottom face
	
						   0,1,0,  0,1,0,  0,1,0,  0,1,0,		// ground floor
						   0,0,1,  0,0,1,  0,0,1,				// front roof
						   0.707f,0.707f,0, 0.707f,0.707f,0, 0.707f,0.707f,0, 0.707f,0.707f,0, // right roof
						   -0.707f,0.707f,0, -0.707f,0.707f,0, -0.707f,0.707f,0, -0.707f,0.707f,0, // left roof
						   0,0,-1, 0,0,-1, 0,0,-1};				// back roof
						   
		float colors[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
						  0,1,0, 0,1,0, 0,1,0, 0,1,0,
						  1,0,0, 1,0,0, 1,0,0, 1,0,0,
						  0,1,0, 0,1,0, 0,1,0, 0,1,0,
						  0,0,1, 0,0,1, 0,0,1, 0,0,1,
						  0,0,1, 0,0,1, 0,0,1, 0,0,1,
		
						  0,0.5f,0, 0,0.5f,0, 0,0.5f,0, 0,0.5f,0,			// ground floor
						  0,0,1, 0,0,1, 0,0,1,							// roof
						  1,0,0, 1,0,0, 1,0,0, 1,0,0,
						  0,1,0, 0,1,0, 0,1,0, 0,1,0,
						  0,0,1, 0,0,1, 0,0,1,};
	
		// Set up the vertex data
		VertexData vertexData = renderContext.makeVertexData(42); //new VertexData(42);
	
		// Specify the elements of the vertex data:
		// - one element for vertex positions
		vertexData.addElement(vertices, VertexData.Semantic.POSITION, 3);
		// - one element for vertex colors
		vertexData.addElement(colors, VertexData.Semantic.COLOR, 3);
		// - one element for vertex normals
		vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(texcoord, VertexData.Semantic.TEXCOORD, 2);
		
		// The index data that stores the connectivity of the triangles
		int indices[] = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22,	// bottom face
		                 
						 24,26,27, 24,25,26,	// ground floor
						 28,29,30,				// roof
						 31,33,34, 31,32,33,
						 35,37,38, 35,36,37,
						 39,40,41};	
	
		vertexData.addIndices(indices);
	
		Shape house = new Shape(vertexData);
		
		return house;
	}
	
	public Shape createLandscape(int n, float roughness) {
		int size = (int)Math.pow(2, n) + 1;
		float seed = 0; //(float)Math.random();
		float maxHeight = -1000;
		float minHeight = 1000;
		
		float[][] terrain = diamondSquare(size, seed, roughness);
		
		// Number of vertex used
		final int VERTEX = size*size;
		
		// Calculate vertex positions
		float scale = 8;
		ArrayList<Float> vertex = new ArrayList<Float>();		
		for (int i=0; i < size; i++) {
			for (int j=0; j < size; j++) {
				vertex.add((float)(i-size/2)/size*scale);
				vertex.add((float)(j-size/2)/size*scale);
				vertex.add((float)terrain[i][j]);
				// Calculate max/min
				if (terrain[i][j] >= maxHeight) maxHeight = terrain[i][j];
				if (terrain[i][j] <= minHeight) minHeight = terrain[i][j];
			}
		}
		
		// Convert ArrayList to array
		float v[] = new float[vertex.size()];
		int k = 0;
		for (Float f : vertex) {
			v[k++] = f;
		}
		
		// Calculate normals
		float normals[] = new float[VERTEX*3];
		int counter = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Vector3f vector = new Vector3f(i, j, terrain[i][j]);
				
				Vector3f up = new Vector3f(), left = new Vector3f(), downLeft = new Vector3f(), 
						 down = new Vector3f(), right = new Vector3f(), upRight = new Vector3f();

				Vector3f temp = new Vector3f();
				Vector3f total = new Vector3f();
					
				// up
				if (i-1 >= 0) {
					up = new Vector3f(i-1, j, terrain[i-1][j]);
					up.sub(vector);
				}
				// left
				if (j-1 >= 0 && i-1 >= 0) {
					left = new Vector3f(i, j-1, terrain[i][j-1]);
					left.sub(vector);
					// up -> left
					temp.cross(up, left);
					if (temp.z < 0) temp.scale(-1);
					total.add(temp);
				}
				
				// downLeft
				if (i+1 < size && j-1 >= 0) {
					downLeft = new Vector3f(i+1, j-1, terrain[i+1][j-1]);
					downLeft.sub(vector);
					// left -> downLeft
					temp.cross(left, downLeft);
					if (temp.z < 0) temp.scale(-1);
					total.add(temp);
				}
				
				// down
				if (i+1 < size) {
					down = new Vector3f(i+1, j, terrain[i+1][j]);
					down.sub(vector);
					// downLeft -> down
					if (j-1 >= 0) {
						temp.cross(downLeft, down);
						if (temp.z < 0) temp.scale(-1);
						total.add(temp);
					}
				}
				
				// right
				if (j+1 < size) {
					right = new Vector3f(i, j+1, terrain[i][j+1]);
					right.sub(vector);
					// down -> right
					if (i+1 < size) {
						temp.cross(down, right);
						if (temp.z < 0) temp.scale(-1);
						total.add(temp);
					}
				}
				
				// upRight
				if (i-1 >= 0 && j+1 < size) {
					upRight = new Vector3f(i-1, j+1, terrain[i-1][j+1]);
					upRight.sub(vector);
					// right -> upRight
					temp.cross(right, upRight);
					if (temp.z < 0) temp.scale(-1);
					total.add(temp);
					// upRight -> up
					temp.cross(upRight, up);
					if (temp.z < 0) temp.scale(-1);
					total.add(temp);
				}
				
				total.normalize(); // average
				normals[counter] = total.x;
				normals[counter+1] = total.y;
				normals[counter+2] = total.z;
				counter += 3;
			}
		}
		 	
		// The vertex colors
		ArrayList<Float> height = new ArrayList<Float>();
		for (int i=0; i < size; i++) {
			for (int j=0; j < size; j++) {
				height.add(terrain[i][j]);
			}
		}
		
		float c[] = new float[VERTEX*3];
		for (int l=0; l < VERTEX*3; l+=3) {
			if (height.get(l/3) >= (maxHeight-roughness/3)) {
				c[l] = 1;
				c[l+1] = 1;
				c[l+2] = 1;
			}
			else if (height.get(l/3) >= (minHeight+roughness/3)) {
				c[l] = 0;
				c[l+1] = 0.6f;
				c[l+2] = 0;
			}
			else {
				c[l] = 0;
				c[l+1] = 0;
				c[l+2] = 1;
			}
		}
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = renderContext.makeVertexData(VERTEX);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
		
		// The triangles (three vertex indices for each triangle)
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		// Upper triangles
		for (int i=0; i <= size-2; i++) {
			for (int j=0; j <= size-2; j++) {
				index.add(size*(j+1)+i+1);
				index.add(size*j+i+1);
				index.add(size*j+i);
			}
		}
		
		// Lower triangles
		for (int i=0; i <= size-2; i++) {
			for (int j=0; j <= size-2; j++) {
				index.add(size*j+i);
				index.add(size*(j+1)+i);
				index.add(size*(j+1)+i+1);
			}
		}
		
		// Convert ArrayList to array
		int indices[] = new int[index.size()];
		int l = 0;
		for (Integer i : index) {
			indices[l++] = i;
		}
				
		vertexData.addIndices(indices);
		
		Shape landscape = new Shape(vertexData);
		
		return landscape;
	}
	
	private float[][] diamondSquare(int size, float seed, float roughness){
		float[][] terrain = new float[size][size];

		// corners
		terrain[0][0] = seed;
		terrain[0][size-1] = seed;
		terrain[size-1][0] = seed;
		terrain[size-1][size-1] = seed;

		for(int sideLength = size-1; sideLength >= 2; sideLength /=2, roughness/= 2){
			int halfSide = sideLength/2;

			// Squares
			for(int x=0;x<size-1;x+=sideLength){
				for(int y=0;y<size-1;y+=sideLength){
					float total = terrain[x][y] + 					// top left
							terrain[x+sideLength][y] +				// top right
							terrain[x][y+sideLength] + 				// lower left
							terrain[x+sideLength][y+sideLength];	// lower right
					float average = total/4;

					// center (average plus random offset)
					terrain[x+halfSide][y+halfSide] = (average + ((float)Math.random()*2*roughness) - roughness);
				}
			}

			// Diamonds
			for(int x=0; x<size-1; x+=halfSide){
				for(int y=(x+halfSide)%sideLength; y<size-1; y+=sideLength){
					float total = 
							terrain[(x-halfSide+size-1)%(size-1)][y] + 	// left of center
							terrain[(x+halfSide)%(size-1)][y] + 		// right of center
							terrain[x][(y+halfSide)%(size-1)] + 		// below center
							terrain[x][(y-halfSide+size-1)%(size-1)]; 	// above center
					float average = total/4;

					// center (average plus random offset)
					average = (average + ((float)Math.random()*2*roughness) - roughness);
					terrain[x][y] = (float) average;

					// wrap values on the edges
					if(x == 0)  terrain[size-1][y] = (float) average;
					if(y == 0)  terrain[x][size-1] = (float) average;
				}
			}
		}
		return terrain;
	}
	
	private ArrayList<Float> calculateNormalsCylinder(float[] v, int segments) {
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		for (int i = 0; i < v.length; i = i + 3) {
			vertices.add(new Vector3f(v[i], v[i+1], v[i+2]));
		}
		
		Vector3f a = new Vector3f(vertices.get(0));
		Vector3f b = new Vector3f(vertices.get(1));
		Vector3f c = new Vector3f(vertices.get(2));

		Vector3f v1 = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
		Vector3f v2 = new Vector3f(c.x - a.x, c.y - a.y, c.z - a.z);

		Vector3f norm = new Vector3f();

		norm.cross(v2, v1);
		norm.normalize();	

		normals.add(norm.x);
		normals.add(norm.y);
		normals.add(norm.z);

		for (int i = 1; i <= segments; i++) {
			if (i == 1) {
				a = new Vector3f(vertices.get(i));
				b = new Vector3f(vertices.get(segments));
				c = new Vector3f(vertices.get(i + 1));
			} else if (i == segments) {
				a = new Vector3f(vertices.get(segments));
				b = new Vector3f(vertices.get(1));
				c = new Vector3f(vertices.get(segments - 1));
			} else {
				a = new Vector3f(vertices.get(i));
				b = new Vector3f(vertices.get(i + 1));
				c = new Vector3f(vertices.get(i - 1));
			}
			v1 = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
			v2 = new Vector3f(c.x - a.x, c.y - a.y, c.z - a.z);

			v1.add(v2);
			v2 = new Vector3f(-v1.x, -v1.y, -v2.z);
			v2.normalize();

			v2.add(norm);
			v2.normalize();

			normals.add(v2.x);
			normals.add(v2.y);
			normals.add(v2.z);
		}
		
		a = new Vector3f(vertices.get(segments + 1));
		b = new Vector3f(vertices.get(segments + 2));
		c = new Vector3f(vertices.get(segments + 3));

		v1 = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
		v2 = new Vector3f(c.x - a.x, c.y - a.y, c.z - a.z);

		norm = new Vector3f();

		norm.cross(v2, v1);
		norm.normalize();
		normals.add(norm.x);
		normals.add(norm.y);
		normals.add(norm.z);

		for (int i = segments + 2; i <= 2 * segments + 1; i++) {
			if (i == segments + 2) {
				a = new Vector3f(vertices.get(i));
				b = new Vector3f(vertices.get(2 * segments + 1));
				c = new Vector3f(vertices.get(i + 1));
			} else if (i == 2 * segments + 1) {
				a = new Vector3f(vertices.get(2 * segments + 1));
				b = new Vector3f(vertices.get(segments + 2));
				c = new Vector3f(vertices.get(2 * segments));
			} else {
				a = new Vector3f(vertices.get(i));
				b = new Vector3f(vertices.get(i + 1));
				c = new Vector3f(vertices.get(i - 1));
			}
			v1 = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
			v2 = new Vector3f(c.x - a.x, c.y - a.y, c.z - a.z);

			v1.add(v2);
			v2 = new Vector3f(-v1.x, -v1.y, -v2.z);
			v2.normalize();

			v2.add(norm);
			v2.normalize();

			normals.add(v2.x);
			normals.add(v2.y);
			normals.add(v2.z);
		}
		
		return normals;
	}
}
