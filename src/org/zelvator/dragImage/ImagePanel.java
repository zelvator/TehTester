package org.zelvator.dragImage;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * ImagePanel is a class, which extends JPanel and let draw images in it.
 * 
 * @author zelvator
 * 
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -2695569499894323339L;
	private BufferedImage image;
	private String path = "";
	private boolean answerImage = false;

	/**
	 * Constructor for ImagePanel
	 * 
	 * @param path
	 *            is set, if the path of picture is known, e.g. if it is loaded from xml file, or Drag and Drop action is performed.
	 * @param answerImage
	 *            false if it is drawn into classic container, true if it is drawn in JList.
	 */
	public ImagePanel(String path, boolean answerImage) {
		setImage(path, answerImage);
	}

	/**
	 * Constructor for ImagePanel.
	 * If no picture is known, or there is error in loading files, empty image will be painted.
	 */
	public ImagePanel() {
		emptyImage();
	}

	/**
	 * This method overrides paintComponent of JPanel. Gets image's size, compare to window, if it's bigger than
	 * component which is drawn within, it will be scaled by the getScaledImage method, centered it and rendered.
	 * Else it will be just centered and rendered.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw image centered in the middle of the panel
		int imgX;
		int imgY;
		if (getImage().getWidth() > getWidth() || getImage().getHeight() > getHeight()) {
			BufferedImage imageBuff;
			try {
				imageBuff = getScaledImage(getImage(), getWidth(), getHeight());
				imgX = getWidth() / 2 - imageBuff.getWidth(this) / 2;
				imgY = getHeight() / 2 - imageBuff.getHeight(this) / 2;
				g.drawImage(imageBuff, imgX, imgY, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			imgX = getWidth() / 2 - getImage().getWidth(this) / 2;
			imgY = getHeight() / 2 - getImage().getHeight(this) / 2;
			g.drawImage(getImage(), imgX, imgY, this);
		}
	}

	/**
	 * Method takes BufferedImage and desired max width and height and scale it using AffineTransform
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}

	/**
	 * Method used for being rendered. If anwerImage is true, meant as if it is rendered in JList as answer's image,
	 * scale it to 200x150, else just return image.
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getImage() {
		if (answerImage) {
			try {
				if (image.getWidth() > 280 && image.getHeight() > 170) {
					return getScaledImage(image, 280, 170);
				}
				if (image.getWidth() > 280) {
					return getScaledImage(image, 280, image.getHeight());
				}
				if (image.getHeight() > 170) {
					return getScaledImage(image, image.getWidth(), 170);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	/**
	 * Method used for changing loaded image.
	 * 
	 * @param path
	 * @param answerImage
	 */
	public void setImage(String path, boolean answerImage) {
		try {
			image = ImageIO.read(new File(path));
			setPath(path);
			this.answerImage = answerImage;
		} catch (IOException ex) {
			emptyImage();
		} catch (NullPointerException npe) {
			emptyImage();
		}

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Method creates empty BufferedImage.
	 */
	public void emptyImage() {
		setImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
		this.path = "";
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
}