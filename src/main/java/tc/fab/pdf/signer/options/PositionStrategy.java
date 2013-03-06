package tc.fab.pdf.signer.options;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class PositionStrategy implements RenderListener {

	private final List<TextChunk> locationalResult = new ArrayList<TextChunk>();

	@Override
	public void renderText(TextRenderInfo renderInfo) {

		LineSegment segment = renderInfo.getBaseline();

		TextChunk location = new TextChunk(renderInfo.getText(), segment.getStartPoint(),
			segment.getEndPoint(), renderInfo.getSingleSpaceWidth(), segment.getBoundingRectange());

		locationalResult.add(location);

	}

	public String getReturnedText() {

		StringBuffer buffer = new StringBuffer();

		for (TextChunk chunk : locationalResult) {
			buffer.append(chunk.text);
		}

		return buffer.toString();

	}

	@Override
	public void renderImage(ImageRenderInfo renderInfo) {
	}

	@Override
	public void endTextBlock() {
	}

	@Override
	public void beginTextBlock() {
	}

	public Float getLastLinePosition(Float marginFooter) {

		Set<Float> ys = new TreeSet<Float>();

		ys.add(0f);

		for (TextChunk chunk : locationalResult) {
			Rectangle2D.Float r = chunk.boundingRectange;
			if ((r.y + r.height) < marginFooter) {
				if (chunk.text.trim().length() > 0) {
					ys.add(r.y);
				}
			}
		}

		return Collections.min(ys);

	}

	public Float getReferencePosition(Float marginFooter, String reference, Boolean lastOccurrence) {

		reference = Normalizer.normalize(reference, Normalizer.Form.NFD);
		reference = reference.replaceAll("[^\\p{ASCII}]", "");
		reference = reference.trim().toLowerCase().replaceAll("\\s+", "");

		StringBuffer buf = new StringBuffer();

		TreeSet<Float> encounters = new TreeSet<Float>();
		Set<Float> ys = new TreeSet<Float>();
		ys.add(0f);

		for (TextChunk chunk : locationalResult) {

			Rectangle2D.Float r = chunk.boundingRectange;

			if ((r.y + r.height) < marginFooter) {

				if (chunk.text.trim().length() > 0) {

					ys.add(r.y);

					String charFound = Normalizer.normalize(chunk.text, Normalizer.Form.NFD);
					charFound = charFound.replaceAll("[^\\p{ASCII}]", "");
					charFound = charFound.trim().toLowerCase().replaceAll("\\s+", "");

					buf.append(charFound);

					// TODO: optimize
					if (buf.lastIndexOf(reference) > -1) {
						encounters.add(r.y);
						buf = new StringBuffer();
					}

				}

			}

		}

		if (encounters.size() > 0) {
			if (lastOccurrence) {
				return Collections.min(encounters);
			} else {
				return Collections.max(encounters);
			}
		}

		return Collections.min(ys);

	}

	static class TextChunk implements Comparable<TextChunk> {
		/** the text of the chunk */
		final String text;
		/** the starting location of the chunk */
		final Vector startLocation;
		/** the ending location of the chunk */
		final Vector endLocation;
		/** unit vector in the orientation of the chunk */
		final Vector orientationVector;
		/** the orientation as a scalar for quick sorting */
		final int orientationMagnitude;
		/**
		 * perpendicular distance to the orientation unit vector (i.e. the Y
		 * position in an unrotated coordinate system) we round to the nearest
		 * integer to handle the fuzziness of comparing floats
		 */
		final int distPerpendicular;
		/**
		 * distance of the start of the chunk parallel to the orientation unit
		 * vector (i.e. the X position in an unrotated coordinate system)
		 */
		final float distParallelStart;
		/**
		 * distance of the end of the chunk parallel to the orientation unit
		 * vector (i.e. the X position in an unrotated coordinate system)
		 */
		final float distParallelEnd;
		/** the width of a single space character in the font of the chunk */
		final float charSpaceWidth;
		public final Rectangle2D.Float boundingRectange;

		public TextChunk(String string, Vector startLocation, Vector endLocation,
			float charSpaceWidth, Rectangle2D.Float float1) {

			this.text = string;
			this.startLocation = startLocation;
			this.endLocation = endLocation;
			this.charSpaceWidth = charSpaceWidth;
			this.boundingRectange = float1;

			orientationVector = endLocation.subtract(startLocation).normalize();
			orientationMagnitude = (int) (Math.atan2(orientationVector.get(Vector.I2),
				orientationVector.get(Vector.I1)) * 1000);

			// see
			// http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
			// the two vectors we are crossing are in the same plane, so the
			// result will be purely
			// in the z-axis (out of plane) direction, so we just take the I3
			// component of the result
			Vector origin = new Vector(0, 0, 1);
			distPerpendicular = (int) (startLocation.subtract(origin)).cross(orientationVector)
				.get(Vector.I3);

			distParallelStart = orientationVector.dot(startLocation);
			distParallelEnd = orientationVector.dot(endLocation);
		}

		/**
		 * @param as
		 *            the location to compare to
		 * @return true is this location is on the the same line as the other
		 */
		public boolean sameLine(TextChunk as) {
			if (orientationMagnitude != as.orientationMagnitude)
				return false;
			if (distPerpendicular != as.distPerpendicular)
				return false;
			return true;
		}

		/**
		 * Computes the distance between the end of 'other' and the beginning of
		 * this chunk in the direction of this chunk's orientation vector. Note
		 * that it's a bad idea to call this for chunks that aren't on the same
		 * line and orientation, but we don't explicitly check for that
		 * condition for performance reasons.
		 * 
		 * @param other
		 * @return the number of spaces between the end of 'other' and the
		 *         beginning of this chunk
		 */
		public float distanceFromEndOf(TextChunk other) {
			float distance = distParallelStart - other.distParallelEnd;
			return distance;
		}

		/**
		 * Compares based on orientation, perpendicular distance, then parallel
		 * distance
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(TextChunk rhs) {
			if (this == rhs)
				return 0; // not really needed, but just in case

			int rslt;
			rslt = compareInts(orientationMagnitude, rhs.orientationMagnitude);
			if (rslt != 0)
				return rslt;

			rslt = compareInts(distPerpendicular, rhs.distPerpendicular);
			if (rslt != 0)
				return rslt;

			// note: it's never safe to check floating point numbers for
			// equality, and if two chunks
			// are truly right on top of each other, which one comes first or
			// second just doesn't matter
			// so we arbitrarily choose this way.
			rslt = distParallelStart < rhs.distParallelStart ? -1 : 1;

			return rslt;
		}

		/**
		 * 
		 * @param int1
		 * @param int2
		 * @return comparison of the two integers
		 */
		private static int compareInts(int int1, int int2) {
			return int1 == int2 ? 0 : int1 < int2 ? -1 : 1;
		}

	}
}