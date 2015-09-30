package simple;

import java.util.Arrays;

import javax.vecmath.Vector3f;

import jrtr.RenderContext;
import jrtr.VertexData;

public class Cylinder {

	private RenderContext renderContext;
	private int segments;
	
	public Cylinder(RenderContext r) {
		this.renderContext = r;
	}
	
	// Segments = sides
	public VertexData createCylinder(int segments) {
		this.segments = segments;
		// The vertex positions of the cube
		
		float v[] = calculateAllCircleVertices(0);

		
		// The vertex colors

		float c[] = calculateColors(v.length);


		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = renderContext.makeVertexData(segments*2 + 2);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
	
		// The triangles (three vertex indices for each triangle)
		
		int indices[] = concatInt( calculateAllCircleIndices(), calculateBorderIndices(v) );
		
		vertexData.addIndices(indices);
		
		float n[] = calculateCircleNormals(v, 0, 1);
		
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		
		return vertexData;
	}
	
	public float[] calculateColors(int amount) {
		float[] res = new float[ amount ];
		int a = 0;
		
		// first circle
		for(int i = 0; i<segments+1; i++) {
			if(i % 2 == 0) {
				res[a++] = 0;
				res[a++] = 0;
				res[a++] = 0;
			} else {
				res[a++] = 1;
				res[a++] = 1;
				res[a++] = 1;
			}
		}
		
		// second circle
		for(int i = segments+1; i<segments*2 + 1; i++) {
			if(i % 2 != 0) {
				res[a++] = 0;
				res[a++] = 0;
				res[a++] = 0;
			} else {
				res[a++] = 1;
				res[a++] = 1;
				res[a++] = 1;
			}
		}
		return res;
	}
	
	
	public float[] calculateAllCircleVertices(int y) {
		return concatFloat( calculateCircleVertices(y), calculateCircleVertices(y + 4) );
	}
	

	public int[] calculateAllCircleIndices() {
		return concatInt( calculateCircleIndices(0, 1), calculateCircleIndices(segments+1, segments+2) );
	}
	
	public float[] calculateCircleNormals() {
		
		return null;
	}
	
	public int[] calculateBorderIndices(float vertices[]) {		
		int[] res = new int[ segments * 2 * 3 ];
		int a = 0;
		int secondCenter = segments + 1;
		
		for(int i = 1; i<segments; i++) {
			
			if(i != segments-1 && (i == secondCenter || i+segments+1 == secondCenter || i+segments+2 == secondCenter || i+1 == secondCenter)) continue;
			
			res[a++] = i;
			res[a++] = i + segments + 1;
			res[a++] = i + segments + 2;
			
			res[a++] = i;
			res[a++] = i + 1;
			res[a++] = i + segments + 2;
		
			// last two triangle which connect last with first points
			if(i == segments-1) {
				res[a++] = 1;
				res[a++] = segments + 2;
				res[a++] = i + segments + 2;
				
				res[a++] = 1;
				res[a++] = i + 1;
				res[a++] = i + 1 + segments + 1;
			}
		}
		
		return res;
	}
	
	public int[] calculateCircleIndices(int center, int start) {
		int[] res = new int[ (segments) * 3 ];
		int a = 0;
		
		for(int i = start; i<(start + segments); i++) {
			if(i == (start + segments - 1)) {
				// connect last with first point
				res[a++] = center;
				res[a++] = i;
				res[a++] = start;
			} else {
				// connect normal points
				res[a++] = center;
				res[a++] = i;
				res[a++] = i+1;
			}
		}

		return res;
	}
	
	
	public float[] calculateCircleNormals(float[] vertices, int center, int start) {
		float[] normals = new float[(segments+1)*3];
		int a = 0;
		//first circle center point
		
		/*normals[0] = 0;
		normals[1] = 1;
		normals[2] = 0;
		normals[1+segments] = 0;
		normals[1+segments+1] = 1 - 1*2;
		normals[1+segments+2] = 0;
		*/
		
		//float v1[], v2[] = new float[3];
		Vector3f v1 = new Vector3f(), v2 = new Vector3f(),
				v3 = new Vector3f(), v_1 = new Vector3f(),
				v_2 = new Vector3f(), v_n = new Vector3f();
		
		// top circle
		v1.set(vertices[center],vertices[center+1],vertices[center+2]);
		v2.set(vertices[center+3+3],vertices[center+3+4],vertices[center+3+5]);
		v3.set(vertices[center+3],vertices[center+4],vertices[center+5]);
		v_1.sub(v2,v1);
		v_2.sub(v3,v1);
		v_n.cross(v_1,v_2);
		v_n.normalize();
		normals[a++] = v_n.x;
		normals[a++] = v_n.y;
		normals[a++] = v_n.z;		
		
		for(int i = start+2; i<(start + segments)*3; i+=3) {
			v3.set(vertices[center],vertices[center+1],vertices[center+2]);
			v1.set(vertices[i],vertices[i+1],vertices[i+2]);
			v2.set(vertices[i+3],vertices[i+4],vertices[i+5]);
			
			v_1.sub(v2, v1);
			v_2.sub(v3, v1);
			v_n.cross(v_1, v_2);
			v_n.normalize();
			
			/*v1[0] = vertices[i] - vertices[center];
			v1[1] = vertices[i+1] - vertices[center+1];
			v1[2] = vertices[i+2] - vertices[center+2];
			
			v2[0] = vertices[i+3] - vertices[center];
			v2[0] = vertices[i+4] - vertices[center+1];
			v2[0] = vertices[i+5] - vertices[center+2];
			*/
			normals[a++] = v_n.x;
			normals[a++] = v_n.y;
			normals[a++] = v_n.z;
		}
		
		System.out.println(Arrays.toString(normals));
		
		return normals;
	}

	
	public float[] calculateCircleVertices(int y) {
		float[] res = new float[ (segments+1) * 3 ];
		int a = 3, r = 1;
		double phi = (double) (2*Math.PI / segments);

		res[0] = 0;
		res[1] = y;
		res[2] = 0;
		
		for(int i = 0; i<segments; i++) {
			res[a++] = (float) Math.cos( i * phi) * r;
			res[a++] = y;
			res[a++] = (float) Math.sin( i * phi) * r;
		}
		
		return res;
	}
	
	public static float[] concatFloat(float[] first, float[] second) {
		float[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static int[] concatInt(int[] first, int[] second) {
		int[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}