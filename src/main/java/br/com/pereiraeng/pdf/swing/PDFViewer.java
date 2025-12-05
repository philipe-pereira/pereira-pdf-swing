package br.com.pereiraeng.pdf.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PDFViewer {

	private final File file;

	private final PDFFile pdfFile;

	public PDFViewer(File file) throws IOException {
		this.file = file;
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		FileChannel channel = randomAccessFile.getChannel();
		ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

		this.pdfFile = new PDFFile(byteBuffer);
		randomAccessFile.close();
	}

	/**
	 * Função que retorna um imagem, compatível com {@link Icon Swing}, mostrando o
	 * conteúdo de uma página do PDF
	 * 
	 * @param pageNumber número da página do documento PDF
	 * @param factor     fator de zoom (1 para manter o tamanho original)
	 * @return imagem
	 */
	public Icon getPage(int pageNumber, float factor) {
		return getPDFlabel(pdfFile, pageNumber, factor);
	}

	/**
	 * Função que retorna um imagem, compatível com {@link Icon Swing}, mostrando o
	 * conteúdo de uma página do PDF
	 * 
	 * @param file   página PDF
	 * @param factor fator de zoom (1 para manter o tamanho original)
	 * @return imagem
	 */
	public static Icon getPDFlabel(PDFFile file, int pageNumber, float factor) {
		PDFPage page = file.getPage(pageNumber);
		Rectangle2D rectangle2d = page.getBBox();

		double width = rectangle2d.getWidth() * factor;
		double height = rectangle2d.getHeight() * factor;

		Image image = null;
		try {
			image = page.getImage((int) width, (int) height, rectangle2d, null, true, true);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		if (image != null)
			return new ImageIcon(image);
		else
			return new ErrorIcon();
	}

	/**
	 * Função que retorna o número de páginas do documento
	 * 
	 * @return número de páginas
	 */
	public int getNumPages() {
		return pdfFile.getNumPages();
	}

	public File getFile() {
		return file;
	}

	private static class ErrorIcon implements Icon {

		private static final int[] X_COORDINATES = { 0, 24, 64, 104, 128, 88, 128, 104, 64, 24, 0, 40 },
				Y_COORDINATES = { 24, 0, 40, 0, 24, 64, 104, 128, 88, 128, 104, 64 };

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(Color.RED);
			g.fillPolygon(X_COORDINATES, Y_COORDINATES, 12);
			g.setColor(Color.BLACK);
			g.drawPolygon(X_COORDINATES, Y_COORDINATES, 12);
		}

		@Override
		public int getIconWidth() {
			return 128;
		}

		@Override
		public int getIconHeight() {
			return 128;
		}
	}
}