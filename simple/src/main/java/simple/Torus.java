package simple;

import java.util.ArrayList;
import java.util.Arrays;

import jrtr.RenderContext;
import jrtr.Shape;
import jrtr.VertexData;


public class Torus {

	

	public static VertexData createTorus(int segments, double R, double d, int stripes, double red, double green, double blue) {
			// Number of vertex used
			final int seg = segments*segments;
			
			// Calculate vertex positions
			double angle = (2*Math.PI / segments);
			ArrayList<Float> vertex = new ArrayList<Float>();
			
			for (int i=1; i <= segments; i++) {
				for (int j=1; j <= segments; j++) {
					vertex.add((float) ((R+d*Math.cos(angle*i))*Math.cos(angle*j)));	// x
					vertex.add((float) ((R+d*Math.cos(angle*i))*Math.sin(angle*j)));	// y
					vertex.add((float) (d*Math.sin(angle*i)));			 				// z
				}
			}
			
			// Convert ArrayList to array
			float v[] = new float[vertex.size()];
			int k = 0;
			for (Float f : vertex) {
				v[k++] = f;
			}
			
			// The vertex colors
			float c[] = new float[seg*3];
			for (int i=0; i < seg*3; i+=3) {
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
						c[i] = (float) green;
						c[i+1] = (float) blue;
						c[i+2] = (float) red;
					}
				}
			}

			// Construct a data structure that stores the vertices, their
			// attributes, and the triangle mesh connectivity
			VertexData vertexData = simple.renderContext.makeVertexData(seg);
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
			
			return vertexData;
		}


	
}