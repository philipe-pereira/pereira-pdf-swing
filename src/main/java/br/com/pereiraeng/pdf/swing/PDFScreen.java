package br.com.pereiraeng.pdf.swing;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sun.pdfview.PDFFile;

import br.com.pereiraeng.swing.button.PNpanel;
import br.com.pereiraeng.swing.button.ZoomInOutPanel;

public class PDFScreen extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String COMMAND_OPEN = "OPEN";

	private JLabel label;

	private File file;
	private PDFFile pdffile;

	private int page = -1;
	private float zoom = 1f;

	public PDFScreen() {
		super(new BorderLayout());

		JPanel panel = new JPanel();

		panel.add(new PNpanel(this, false));

		panel.add(new ZoomInOutPanel(this));

		JButton button = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/pdf24.png")));
		button.setActionCommand(COMMAND_OPEN);
		button.addActionListener(this);
		panel.add(button);

		add(panel, BorderLayout.NORTH);

		// ------------------------------------------------------------

		JScrollPane sp = new JScrollPane(label = new JLabel());
		add(sp, BorderLayout.CENTER);
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		getComponent(1).setPreferredSize(preferredSize);
	}

	public void setPDF(File file) {
		this.file = file;
		if (file != null) {
			try {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				FileChannel channel = raf.getChannel();
				ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

				pdffile = new PDFFile(buf);
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.page = 1;
			refresh();
		} else {
			label.setIcon(null);
			label.repaint();
		}
	}

	private void refresh() {
		label.setIcon(PDFViewer.getPDFlabel(this.pdffile, this.page, this.zoom));
		label.repaint();
	}

	// ------------------------------- LISTENER -------------------------------

	@Override
	public void actionPerformed(ActionEvent event) {
		if (pdffile != null) {
			switch (event.getActionCommand()) {
			case PNpanel.PREVIOUS:
				if (this.page > 1)
					this.page--;
				refresh();
				break;
			case PNpanel.NEXT:
				if (this.page < pdffile.getNumPages())
					this.page++;
				refresh();
				break;
			case ZoomInOutPanel.IN:
				zoom *= 1.25f;
				refresh();
				break;
			case ZoomInOutPanel.OUT:
				zoom *= 0.8f;
				refresh();
				break;
			case COMMAND_OPEN:
				// abrir arquivo no aplicativo apropriado
				if (file != null && Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.open(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}
}
