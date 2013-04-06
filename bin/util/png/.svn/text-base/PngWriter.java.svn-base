package util.png;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import com.keypoint.PngEncoder;

public class PngWriter {
	public static void write(PngSource source, Dimension size, OutputStream os)
			throws IOException {
		BufferedImage image = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_RGB);
		source.drawPng(image.getGraphics());
		PngEncoder encoder = new PngEncoder(image);
		byte[] bytes = encoder.pngEncode();

		os.write(bytes);
		os.flush();
	}
}
