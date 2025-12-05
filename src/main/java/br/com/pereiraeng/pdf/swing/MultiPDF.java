package br.com.pereiraeng.pdf.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MultiPDF extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;

	// dados

	private File[] pdfs;

	// parte gráfica

	private JSlider slider;

	private PDFScreen pdfScreen;

	public MultiPDF() {
		super(new BorderLayout());

		pdfScreen = new PDFScreen();
		pdfScreen.setPreferredSize(new Dimension(570, 520));
		this.add(pdfScreen, BorderLayout.CENTER);

		this.add(slider = new JSlider(SwingConstants.HORIZONTAL, 1, 1, 1), BorderLayout.SOUTH);
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
	}

	public void setPDFs(File directory) {
		this.setPDFs(directory.listFiles());
	}

	public void setPDFs(File[] pdfs) {
		this.pdfs = pdfs;

		this.slider.setEnabled(pdfs != null ? pdfs.length > 1 : false);
		if (this.slider.isEnabled()) {
			this.slider.setMaximum(pdfs.length);
			this.slider.setValue(1);
		}

		if (pdfs != null)
			this.pdfScreen.setPDF(pdfs[0]);
		else
			this.pdfScreen.setPDF(null);
	}

	public void clear() {
		setPDFs((File[]) null);
	}

	// -------------------------- LISTENER --------------------------

	@Override
	public void stateChanged(ChangeEvent event) {
		int i = slider.getValue();
		if (i >= 0 && pdfs != null)
			pdfScreen.setPDF(pdfs[i - 1]);
	}
}
