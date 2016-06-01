import com.sun.glass.events.KeyEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI {

    JFrame frame = new JFrame("Resizer 1.0");
    BufferedImage img;
    Boolean rotated = false;
    Boolean resized = false;
    String filePath;
    JMenuItem menuItemSave, menuItemOpen, menuItemProperties, menuItemExit, menuItemCrop, menuItemResize, menuItemAbout;
    JFrame pictureFrame = new JFrame();
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    public JPanel canvasPanel;
    int counter = 0;
    boolean flagNewFolder = false;
    String newFolderName;
    String[] listForJCombo = {"px", "percent"};
    JComboBox list = new JComboBox(listForJCombo);
    int drag_status = 0, c1, c2, c3, c4;
    int w;
    int h;
    boolean cropSelected = false;
    ImageIcon cropIcon = new ImageIcon("src/Graphics/cropIcon.png");
    JButton crop = getButtonWithCommonProperties(cropIcon);
    ImageTransformation imageTransformation = new ImageTransformation();
    Boolean originalImage = false;

    public void GUI() {
        JLabel toolBackground = new JLabel(new ImageIcon("src/Graphics/iconMain.png"));
        frame.add(toolBackground);
        ImageIcon icon = new ImageIcon("src/Graphics/icon23.png");
        frame.setIconImage(icon.getImage());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setJMenuBar(createMenuBar());

        JButton open = getButtonWithCommonProperties(new ImageIcon("src/Graphics/openTool.png"));
        JButton save = getButtonWithCommonProperties(new ImageIcon("src/Graphics/saveTool.png"));
        JButton properties = getButtonWithCommonProperties(new ImageIcon("src/Graphics/propertiesIcon.png"));
        JButton resize = getButtonWithCommonProperties(new ImageIcon("src/Graphics/resizeIcon.png"));
        JButton rotate = getButtonWithCommonProperties(new ImageIcon("src/Graphics/rotateIcon.png"));
        JButton batch = getButtonWithCommonProperties(new ImageIcon("src/Graphics/batchIcon.png"));
        JButton exit = getButtonWithCommonProperties(new ImageIcon("src/Graphics/exitIcon.png"));

        save.addActionListener(new SaveListener());
        properties.addActionListener(new PropertiesListener());
        batch.addActionListener(new BatchListener());
        resize.addActionListener(new ResizeListener());
        crop.addActionListener(new CropListener());
        exit.addActionListener(new ExitListener());
        JLabel toolMenuIcon = new JLabel(new ImageIcon("C:\\Users\\Artur\\workspace\\KingSize\\src\\Graphics\\toolMenuIcon.png"));
        JPanel toolsPanel = new JPanel();
        toolMenuIcon.setSize(new Dimension(180, 80));
        JFrame toolsFrame = new JFrame("Tools");
        toolsFrame.add(toolMenuIcon);
        toolsFrame.add(toolsPanel);
        toolsFrame.setAlwaysOnTop(true);
        toolsPanel.setLayout(layout);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        toolsPanel.add(open, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        toolsPanel.add(save, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        toolsPanel.add(properties, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        toolsPanel.add(resize, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        toolsPanel.add(rotate, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        toolsPanel.add(batch, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        toolsPanel.add(crop, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        toolsPanel.add(exit, gbc);
        toolsFrame.setVisible(true);
        toolsFrame.setIconImage(icon.getImage());
        toolsFrame.setSize(new Dimension(50, 500));
        toolsFrame.isAlwaysOnTop();
        toolsFrame.setLocationRelativeTo(frame);
        toolsFrame.setLocation(1500, 150);
        SwingUtilities.updateComponentTreeUI(toolsFrame);
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private JButton getButtonWithCommonProperties(Icon icon) {
        JButton open = new JButton();
        open.setIcon(icon);
        open.setPreferredSize(new Dimension(50, 50));
        open.setOpaque(false);
        open.setContentAreaFilled(false);
        open.setBorderPainted(false);
        return open;
    }

    private class Canvas extends JPanel {
        private static final long serialVersionUID = 1L;

        public Canvas() {
            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    System.out.println(e);
                }
            });
            canvasPanel = new JPanel() {
                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, null);

                    Color c = new Color(0f, 0f, 0f, .5f);
                    g2.setColor(c);
                    g2.fillRect(c1, c2, w, h);
                    SwingUtilities.updateComponentTreeUI(canvasPanel);
                }

                public void paint(Graphics g) {
                    super.paint(g);
                    w = c1 - c3;
                    h = c2 - c4;
                    w = w * -1;
                    h = h * -1;
                    if (w < 0)
                        w = w * -1;
                    SwingUtilities.updateComponentTreeUI(canvasPanel);
                }


            };
            canvasPanel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
            canvasPanel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent arg0) {
                    System.out.println("CAShusca");
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                }

                @Override
                public void mousePressed(MouseEvent arg0) {
                    pictureFrame.repaint();
                    c1 = arg0.getX();
                    c2 = arg0.getY();
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                    pictureFrame.repaint();
                    if (drag_status == 1) {
                        c3 = arg0.getX();
                        c4 = arg0.getY();
                       /* shape = makeRectangle(c1, c2, c3,
                                c4);*/
                        if (cropSelected) {
                            img = img.getSubimage(c1, c2, w, h);

                            canvasPanel.repaint();
                            SwingUtilities.updateComponentTreeUI(canvasPanel);
                        }
                        try {
                            draggedScreen();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    canvasPanel.repaint();
                    SwingUtilities.updateComponentTreeUI(canvasPanel);
                }

                @Override
                public void mouseMoved(MouseEvent arg0) {

                }
            });
            canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent arg0) {
                    pictureFrame.repaint();
                    drag_status = 1;
                    c3 = arg0.getX();
                    c4 = arg0.getY();
                }
            });
            JScrollPane sp = new JScrollPane(canvasPanel);
            setLayout(new BorderLayout());
            add(sp, BorderLayout.CENTER);
        }

    }

    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu image = new JMenu("Image");
        JMenu about = new JMenu("About");
        file.setMnemonic(KeyEvent.VK_F);
        image.setMnemonic(KeyEvent.VK_I);
        menuItemProperties = new JMenuItem("Properties", new ImageIcon("src/Graphics/properties.png"));
        menuItemExit = new JMenuItem("Exit", new ImageIcon("src/Graphics/exit.png"));
        menuItemAbout = new JMenuItem("About Resize me 1.0", new ImageIcon("src/Graphics/about.png"));
        JMenuItem rotateClockWise = new JMenuItem("Rotate clockwise", KeyEvent.VK_R);
        rotateClockWise.setIcon(new ImageIcon("src/Graphics/clockWiseIcon.png"));
        JMenuItem rotateAntiClockWise = new JMenuItem("Rotate anti-clockwise", KeyEvent.VK_L);
        rotateAntiClockWise.setIcon(new ImageIcon("src/Graphics/antiClockWiseIcon.png"));
        JMenu menuItemRotate = new JMenu("Rotate");
        menuItemRotate.setIcon(new ImageIcon("src/Graphics/rotate.png"));
        menuItemRotate.add(rotateAntiClockWise);
        menuItemRotate.add(rotateClockWise);
        JMenuItem menuItemBatchResize = new JMenuItem("Bath resize", new ImageIcon("src/Graphics/batchResize.png"));
        menuItemOpen = new JMenuItem("Open...                O", new ImageIcon("src/Graphics/open.png"));
        menuItemOpen.setMnemonic(KeyEvent.VK_O);
        menuItemOpen.setToolTipText("Open image");
        menuItemOpen.addActionListener(new OpenListener());
        menuItemSave = new JMenuItem("Save As...             S", new ImageIcon("src/Graphics/save.png"));
        menuItemSave.setMnemonic(KeyEvent.VK_S);
        menuItemSave.setToolTipText("Save image");
        menuItemSave.addActionListener(new SaveListener());
        enableSaving(false);
        menuItemProperties.setMnemonic(KeyEvent.VK_P);
        menuItemProperties.setToolTipText("Get image exif");
        menuItemProperties.addActionListener(new PropertiesListener());
        menuItemExit.setMnemonic(KeyEvent.VK_E);
        menuItemExit.setToolTipText("Exit application");
        menuItemExit.addActionListener(new ExitListener());
        menuItemCrop = new JMenuItem("Crop", new ImageIcon("src/Graphics/crop.png"));
        menuItemCrop.setMnemonic(KeyEvent.VK_C);
        menuItemCrop.setToolTipText("Crop image");
        menuItemCrop.addActionListener(new CropListener());
        menuItemResize = new JMenuItem("Resize", new ImageIcon("src/Graphics/resize.png"));
        menuItemResize.setMnemonic(KeyEvent.VK_R);
        menuItemResize.setToolTipText("Resize image");
        menuItemResize.addActionListener(new ResizeListener());
        rotateClockWise.setMnemonic(KeyEvent.VK_O);
        rotateClockWise.setToolTipText("Rotate image");
        rotateClockWise.addActionListener(new RotateListener(true));
        rotateAntiClockWise.setMnemonic(KeyEvent.VK_O);
        rotateAntiClockWise.setToolTipText("Rotate image");
        rotateAntiClockWise.addActionListener(new RotateListener(false));
        menuItemBatchResize.setMnemonic(KeyEvent.VK_B);
        menuItemBatchResize.setToolTipText("Batch image resize");
        menuItemBatchResize.addActionListener(new BatchListener());
        file.add(menuItemOpen);
        file.add(menuItemSave);
        file.add(menuItemProperties);
        file.add(menuItemExit);
        image.add(menuItemCrop);
        image.add(menuItemResize);
        image.add(menuItemRotate);
        image.add(menuItemBatchResize);
        about.add(menuItemAbout);
        menubar.add(file);
        menubar.add(image);
        menubar.add(about);
        menuItemAbout.setMnemonic(KeyEvent.VK_A);
        menuItemAbout.setToolTipText("About Resize me 1.0");
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Resize me 1.0 is a simple image manipulation program.\n" +
                                "It is free of advanced options therefore you can\n quickly and easy resize and save" +
                                " your images.\n\n\n\n\nAuthor: Artur Kozyrski\nRelease Date: 03/02/2016\nAll rights reserved",
                        "About Resize me 1.0", JOptionPane.PLAIN_MESSAGE, new ImageIcon("src/Graphics/aboutIcon.png"));
            }
        });
        return menubar;
    }

    public String getImageFile() {
        File file;
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp"));
        int result = fc.showOpenDialog(frame);
        if (result == fc.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            return file.getPath();
        } else
            return null;
    }

    private BufferedImage resizeDialog() {
        JPanel resizePanel = new JPanel();
        JRadioButton keepProportion = new JRadioButton("Keep proportion?");
        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);
        resizePanel.setLayout((new BoxLayout(resizePanel, BoxLayout.PAGE_AXIS)));
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (originalImage) {
            imgWidth = img.getWidth();
            imgHeight = img.getHeight();
        }
        String width = "" + imgWidth;
        String height = "" + imgHeight;
        widthField.setText(width);
        heightField.setText(height);
        resizePanel.add(new JLabel("New width: "));
        resizePanel.add(widthField);
        resizePanel.add(new JLabel("New height: "));
        resizePanel.add(heightField);
        resizePanel.add(Box.createHorizontalStrut(15));
        resizePanel.add(keepProportion);
        int result = JOptionPane.showConfirmDialog(null, resizePanel,
                "Set new image size", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && keepProportion.isSelected()) {
            int widthInt = Integer.parseInt(widthField.getText());
            int heightInt = Integer.parseInt(widthField.getText());
            img = imageTransformation.getImageInScale(img, widthInt, heightInt);
        }
        if (result == JOptionPane.OK_OPTION && !keepProportion.isSelected()) {
            int widthInt = Integer.parseInt(widthField.getText());
            int heightInt = Integer.parseInt(heightField.getText());
            img = imageTransformation.getImageNotInScale(img, widthInt, heightInt);
        }
        return img;
    }

    private void WriteImage(BufferedImage image, File filesrc) {
        try {
            ImageIO.write(image, "JPG", filesrc);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }

    private void enableSaving(boolean f) {
        menuItemSave.setEnabled(f);
    }


    private void draggedScreen() throws Exception {
        int w = c1 - c3;
        int h = c2 - c4;
        w = w * -1;
        h = h * -1;
    }
    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                String filePath = fileToSave.getAbsolutePath();
                WriteImage(img, new File(filePath + ".jpg"));

            }
        }

    }

    private class OpenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (cropSelected) {
                crop.setIcon(cropIcon);
            }
            filePath = getImageFile();
            originalImage = true;
            rotated = false;
            resized = false;
            if (filePath != null) {

                try {

                    img = ImageIO.read(new File(filePath));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            if (img != null) {
                JPanel panel = new Canvas();
                if (img.getHeight() < 1200 && img.getWidth() < 1200) {
                    pictureFrame.setSize(new Dimension(img.getWidth() + 40, img.getHeight() + 40));
                    panel.setSize(new Dimension(pictureFrame.getWidth() + 40, pictureFrame.getHeight() + 40));
                } else {
                    pictureFrame.setSize(new Dimension(1200, 800));
                    panel.setSize(new Dimension(1200, 800));
                }
                pictureFrame.setVisible(true);
                pictureFrame.setAlwaysOnTop(true);
                pictureFrame.setLocation(150, 200);
                pictureFrame.add(panel);
                pictureFrame.setTitle(filePath);
                pictureFrame.setIconImage(img);
                pictureFrame.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent arg0) {
                    }

                    @Override
                    public void mouseExited(MouseEvent arg0) {
                    }

                    @Override
                    public void mousePressed(MouseEvent arg0) {
                        pictureFrame.repaint();
                        c1 = arg0.getX();
                        c2 = arg0.getY();
                    }

                    @Override
                    public void mouseReleased(MouseEvent arg0) {
                        pictureFrame.repaint();
                        if (drag_status == 1) {
                            c3 = arg0.getX();
                            c4 = arg0.getY();
                            try {
                                draggedScreen();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void mouseDragged(MouseEvent arg0) {
                        pictureFrame.repaint();
                        drag_status = 1;
                        c3 = arg0.getX();
                        c4 = arg0.getY();
                    }

                    @Override
                    public void mouseMoved(MouseEvent arg0) {
                    }
                });
                SwingUtilities.updateComponentTreeUI(panel);
                SwingUtilities.updateComponentTreeUI(pictureFrame);
            }
            enableSaving(true);
        }
    }

    private class BatchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            JFrame batchFrame = new JFrame("Batch");
            JPanel batchPanel = new JPanel();
            JPanel setDimensionPanel = new JPanel();
            JFileChooser fc = new JFileChooser();
            JPanel thumbNails = new JPanel(layout);
            File[] files = new File[fc.getSelectedFiles().length];
            BufferedImage[] img = new BufferedImage[fc.getSelectedFiles().length];
            thumbNails.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                    Color.lightGray, Color.lightGray));
            JRadioButton keepProportion = new JRadioButton("Keep proportion?");
            JTextField widthField = new JTextField(5);
            JTextField heightField = new JTextField(5);
            JTextField prefixName = new JTextField(5);
            JButton newFolderButton = new JButton("Create new Folder?");
            newFolderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newFolderName = JOptionPane.showInputDialog("Specify new Folder name: " + fc.getSelectedFile().getParent(), "");
                    if (newFolderName != null) {
                        new File(fc.getSelectedFile().getParent() + "\\" + newFolderName).mkdir();
                        flagNewFolder = true;
                    }
                }
            });
            batchPanel.setLayout(layout);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            batchPanel.add(setDimensionPanel);
            setDimensionPanel.setLayout(layout);
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            setDimensionPanel.add(new JLabel("New width: "), gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            setDimensionPanel.add(widthField, gbc);
            gbc.gridx = 2;
            gbc.gridy = 0;
            setDimensionPanel.add(new JLabel("New height: "), gbc);
            gbc.gridx = 3;
            gbc.gridy = 0;
            setDimensionPanel.add(heightField, gbc);
            gbc.gridx = 4;
            gbc.gridy = 0;
            setDimensionPanel.add(list, gbc);
            gbc.gridx = 5;
            gbc.gridy = 0;
            setDimensionPanel.add(keepProportion, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            batchPanel.add(thumbNails, gbc);
            gbc.gridx = 0;
            gbc.gridy = 8;
            setDimensionPanel.add(new JLabel("Specify new file prefix: "), gbc);
            gbc.gridx = 1;
            gbc.gridy = 8;
            setDimensionPanel.add(prefixName, gbc);
            gbc.gridx = 2;
            gbc.gridy = 8;
            setDimensionPanel.add(new JLabel("+0.jpg"), gbc);
            gbc.gridx = 2;
            gbc.gridy = 9;
            setDimensionPanel.add(newFolderButton, gbc);
            gbc.gridx = 3;
            gbc.gridy = 9;
            fc.setMultiSelectionEnabled(true);
            fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp"));
            fc.showOpenDialog(batchFrame);

            for (int i = 0; i < fc.getSelectedFiles().length; i++) {
                JLabel imgLabels = new JLabel();
                BufferedImage[] thumbNailImg = new BufferedImage[fc.getSelectedFiles().length];
                img = new BufferedImage[fc.getSelectedFiles().length];
                files = new File[fc.getSelectedFiles().length];
                files[i] = fc.getSelectedFiles()[i];
                try {
                    thumbNailImg[i] = ImageIO.read(new File(files[i].getPath()));
                    img[i] = ImageIO.read(new File(files[i].getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgLabels.setIcon(new ImageIcon(imageTransformation.getImageInScale(thumbNailImg[i], 100, 100)));
                thumbNails.add(imgLabels);
                int imgWidth = img[i].getWidth();
                int imgHeight = img[i].getHeight();
                String width = "" + imgWidth;
                String height = "" + imgHeight;
                widthField.setText(width);
                heightField.setText(height);

            }
            int result = JOptionPane.showConfirmDialog(null, batchPanel, "Batch conversion", JOptionPane.OK_CANCEL_OPTION);

            for (int i = 0; i < fc.getSelectedFiles().length; i++) {
                int widthInt = Integer.parseInt(widthField.getText());
                int heightInt = Integer.parseInt(widthField.getText());
                files[i] = fc.getSelectedFiles()[i];
                try {
                    img[i] = ImageIO.read(new File(files[i].getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (result == JOptionPane.OK_OPTION && keepProportion.isSelected()) {

                    img[i] = imageTransformation.getImageInScale(img[i], widthInt, heightInt);
                    if (flagNewFolder) {
                        WriteImage(img[i], new File(fc.getSelectedFiles()[i].getParent() + "/" + newFolderName + "/" + prefixName.getText() + counter + ".jpg"));

                    } else {
                        WriteImage(img[i], new File(fc.getSelectedFiles()[i].getParent() + "/" + prefixName.getText() + counter + ".jpg"));
                    }
                    counter++;

                }
                if (result == JOptionPane.OK_OPTION && !keepProportion.isSelected()) {
                    img[i] = imageTransformation.getImageNotInScale(img[i], widthInt, heightInt);
                    if (flagNewFolder) {
                        WriteImage(img[i], new File(fc.getSelectedFiles()[i].getParent() + "/" + newFolderName + "/" + prefixName.getText() + counter + ".jpg"));
                    } else {
                        WriteImage(img[i], new File(fc.getSelectedFiles()[i].getParent() + "/" + prefixName.getText() + counter + ".jpg"));
                    }
                    counter++;

                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        }
    }

    private class CropListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (!cropSelected) {
                cropSelected = true;
                crop.setIcon(new ImageIcon("src/Graphics/cropIconPressed.png"));
            } else {
                cropSelected = false;
                crop.setIcon(cropIcon);
            }
        }
    }

    private class RotateListener implements ActionListener {
        boolean rotateClockwise;
        RotateListener(boolean rotateClockwise) {
            this.rotateClockwise = rotateClockwise;
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            img = imageTransformation.rotateImage(rotateClockwise, resized, img).filter(img, null);
            SwingUtilities.updateComponentTreeUI(pictureFrame);
        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

    private class ResizeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            img = resizeDialog();
            SwingUtilities.updateComponentTreeUI(frame);
            SwingUtilities.updateComponentTreeUI(pictureFrame);
        }
    }

    private class PropertiesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            JPanel exifPanel = new JPanel();
            JTextPane exifTextPane = new JTextPane();
            exifPanel.setPreferredSize(new Dimension(450, 450));
            exifTextPane.setEditable(false);
            exifTextPane.setBackground(new Color(236, 236, 236, 236));
            exifTextPane.setText(String.valueOf((imageTransformation.properties(filePath))));
            exifTextPane.setPreferredSize(new Dimension(400, 400));
            exifPanel.add(exifTextPane);

            JOptionPane.showMessageDialog(frame, exifPanel,
                    "Image EXIF",
                    JOptionPane.PLAIN_MESSAGE);
            pictureFrame.setAlwaysOnTop(true);

        }
    }
}