/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tc.fab.pdf.signer.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdesktop.application.LocalStorage;

import tc.fab.file.selector.IFileSelection;
import tc.fab.pdf.signer.application.PDFSignerApp;

import com.itextpdf.text.pdf.PdfReader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 
 * @author fabiano
 */

@XStreamAlias("options")
public class SignerOptions {

    public static SignerOptions newInstance() throws IOException {

	SignerOptions opts;
	LocalStorage lst = PDFSignerApp.getInstance().getContext()
		.getLocalStorage();

	File out = new File(lst.getDirectory(), "opt.xml");

	if (out.exists()) {

	    InputStream is = lst.openInputFile("opt.xml");

	    XStream xstream = new XStream();

	    xstream.alias("options", SignerOptions.class);

	    xstream.autodetectAnnotations(true);

	    try {
		opts = (SignerOptions) xstream.fromXML(is);
	    } catch (Exception e) {
		opts = new SignerOptions();
	    }

	} else {

	    FileUtils.touch(out);

	    opts = new SignerOptions();

	}

	return opts;

    }

    @XStreamOmitField
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static String propertiesFilePath = "resources/SignerOptions.properties";

    public static String EVENT_IMAGE = "evt-image";
    public static String EVENT_FILES = "evt-refresh";

    public static Integer POSITION_RELATIVE = 0;
    public static Integer POSITION_ABSOLUTE = 1;

    public enum RenderMode {
	GraphicAndDescription, NameAndDescription, Graphic, Description, Name
    }

    // public static Integer RenderGraphicAndDescription = 0;
    // public static Integer RenderNameAndDescription = 1;
    // public static Integer RenderGraphic = 2;
    // public static Integer RenderDescription = 3;
    // public static Integer RenderName = 4;

    private static String newLine = "\n";

    // Selection
    @XStreamOmitField
    private Set<File> files = new TreeSet<File>();

    // Graphics
    private File image = null;
    private Float imageScale = 0.5F;
    private Boolean imageName = false;
    private Boolean imageImport = false;
    private Boolean imageCustom = false;
    private String imageCustomText = null;

    // Position
    private String signatureAlign = "center";
    private Float signatureWidth = 100F;
    private Float signatureHeight = 100F;
    private Boolean signatureAutomaticSize = true;
    private Integer pageToSign = -1;
    private Float lastLineDistance = 2.5F;
    private Float footerDistance = 2.0F;
    private Float marginBottom = 5F;
    private Integer signaturePosition = POSITION_RELATIVE;
    private String referenceText = "";
    private Boolean referenceLastLine = true;

    // Description
    private Boolean textName = true;
    private Boolean textDate = true;
    private Boolean textLocal = false;
    private Boolean textReason = false;
    private Boolean textLabels = false;
    private String local = "";
    private String reason = "";

    // Output
    private Boolean outputOverwriteOriginal = false;
    private String outputPreffix = "";
    private String outputSuffix = " sgn'd";
    private Boolean outputMaskRegex = true;
    private String maskRegex = "";

    protected SignerOptions() {
    }

    public void addPropertyChangeListener(String eventName,
	    PropertyChangeListener evt) {
	getPcs().addPropertyChangeListener(eventName, evt);
    }

    public void addSelection(IFileSelection selection)
	    throws FileNotFoundException {
	getFiles().addAll(selection.getChilds());
	getPcs().firePropertyChange(EVENT_FILES, null, getFiles());
    }

    public Float getLastLineDistance() {
	return lastLineDistance;
    }

    public Vector<String> getFilenames() throws FileNotFoundException {

	Vector<String> inFiles = new Vector<String>();

	for (File file : getFiles()) {
	    inFiles.add(file.getAbsolutePath());
	}

	return inFiles;
    }

    public Set<File> getFiles() {

	if (files == null) {
	    files = new TreeSet<File>();
	}

	return files;

    }

    public void setFiles(Collection<Object> cFiles) {

	files.clear();

	for (Object file : cFiles) {
	    files.add((File) file);
	}

    }

    public File getImage() {
	return image;
    }

    public Boolean getImageImport() {
	return imageImport;
    }

    public Boolean getImageName() {
	return imageName;
    }

    public Float getImageScale() {
	return imageScale;
    }

    public String getLocal() {
	return local;
    }

    public Float getMarginBottom() {
	return marginBottom;
    }

    public Boolean getOutputOverwriteOriginal() {
	return outputOverwriteOriginal;
    }

    public String getOutputPreffix() {
	return outputPreffix;
    }

    public String getOutputSuffix() {
	return outputSuffix;
    }

    public Integer getPageToSign() {
	return pageToSign;
    }

    public Integer getPageToSign(PdfReader reader) {

	Integer inPageToSign;

	Integer totalPages = reader.getNumberOfPages();

	if (getPageToSign() < 0) {
	    inPageToSign = totalPages + getPageToSign() + 1;
	    if (inPageToSign < 0) {
		inPageToSign = 1;
	    }
	} else {
	    if (getPageToSign() > totalPages) {
		inPageToSign = totalPages;
	    } else {
		inPageToSign = getPageToSign();
	    }
	}

	return inPageToSign;

    }

    private PropertyChangeSupport getPcs() {
	if (pcs == null) {
	    pcs = new PropertyChangeSupport(this);
	}
	return pcs;
    }

    public String getReason() {
	return reason;
    }

    public String getSignatureAlign() {
	return signatureAlign;
    }

    public Boolean getSignatureAutomaticSize() {
	return this.signatureAutomaticSize;
    }

    public Float getSignatureHeight() {
	// TODO: Se uma imagem for setada, calcular automaticamente a largura da
	// assinatura
	return signatureHeight;
    }

    public Integer getSignaturePosition() {
	return signaturePosition;
    }

    public Float getSignatureWidth() {
	// TODO: Se uma imagem for setada, calcular automaticamente a altura da
	// assinatura
	return signatureWidth;
    }

    public Boolean getTextDate() {
	return textDate;
    }

    /**
     * @return the textLabels
     */
    public Boolean getTextLabels() {
	return textLabels;
    }

    /**
     * @return the textLocal
     */
    public Boolean getTextLocal() {
	return textLocal;
    }

    /**
     * @return the textName
     */
    public Boolean getTextName() {
	return textName;
    }

    /**
     * @return the textReason
     */
    public Boolean getTextReason() {
	return textReason;
    }

    /**
     * @return the signatureAutomaticSize
     */
    public Boolean isSignatureAutomaticSize() {
	return signatureAutomaticSize;
    }

    public void setLastLineDistance(Float lastLineDistance) {
	this.lastLineDistance = lastLineDistance;
    }

    public void setImage(File image) {
	this.image = image;
	getPcs().firePropertyChange(EVENT_IMAGE, null, getImage());
    }

    /**
     * @param imageImport
     *            the imageImport to set
     */
    public void setImageImport(Boolean imageImport) {
	if (imageImport)
	    setGraphicsOff();
	this.imageImport = imageImport;
    }

    /**
     * @param imageName
     *            the imageName to set
     */
    public void setImageName(Boolean imageName) {
	if (imageName)
	    setGraphicsOff();
	this.imageName = imageName;
    }

    public void setImageScale(Float imageScale) {
	this.imageScale = imageScale;
    }

    /**
     * @param local
     *            the local to set
     */
    public void setLocal(String local) {
	this.local = local;
    }

    public void setMarginBottom(Float marginBottom) {
	this.marginBottom = marginBottom;
    }

    /**
     * @param overwriteOriginal
     *            the overwriteOriginal to set
     */
    public void setOutputOverwriteOriginal(Boolean overwriteOriginal) {
	this.outputOverwriteOriginal = overwriteOriginal;
    }

    public void setOutputPreffix(String outputPreffix) {
	this.outputPreffix = outputPreffix;
    }

    public void setOutputSuffix(String outputSuffix) {
	this.outputSuffix = outputSuffix;
    }

    public void setPageToSign(Integer pageToSign) {
	this.pageToSign = pageToSign;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
	this.reason = reason;
    }

    /**
     * @param imageAlign
     *            the imageAlign to set
     */
    public void setSignatureAlign(String imageAlign) {
	this.signatureAlign = imageAlign;
    }

    /**
     * @param signatureAutomaticSize
     *            the signatureAutomaticSize to set
     */
    public void setSignatureAutomaticSize(Boolean signatureAutomaticSize) {
	this.signatureAutomaticSize = signatureAutomaticSize;
    }

    /**
     * @param signatureHeight
     *            the signatureHeight to set
     */
    public void setSignatureHeight(Float signatureHeight) {
	this.signatureHeight = signatureHeight;
    }

    /**
     * @param signaturePosition
     *            the signaturePosition to set
     */
    public void setSignaturePosition(Integer signaturePosition) {
	this.signaturePosition = signaturePosition;
    }

    /**
     * @param signatureWidth
     *            the signatureWidth to set
     */
    public void setSignatureWidth(Float signatureWidth) {
	this.signatureWidth = signatureWidth;
    }

    /**
     * @param textDate
     *            the textDate to set
     */
    public void setTextDate(Boolean textDate) {
	this.textDate = textDate;
    }

    /**
     * @param textLabels
     *            the textLabels to set
     */
    public void setTextLabels(Boolean textLabels) {
	this.textLabels = textLabels;
    }

    /**
     * @param textLocal
     *            the textLocal to set
     */
    public void setTextLocal(Boolean textLocal) {
	this.textLocal = textLocal;
    }

    /**
     * @param textName
     *            the textName to set
     */
    public void setTextName(Boolean textName) {
	this.textName = textName;
    }

    /**
     * @param textReason
     *            the textReason to set
     */
    public void setTextReason(Boolean textReason) {
	this.textReason = textReason;
    }

    /**
     * Salva as as propriedades do SignerOptions em um arquivo.
     * 
     * @param filepath
     *            Caminho absoluto do arquivo
     * @throws IOException
     * @throws URISyntaxException
     */
    public void store() throws IOException, URISyntaxException {

	// TODO: permitir vários arquivos de configuração
	LocalStorage lst = PDFSignerApp.getInstance().getContext()
		.getLocalStorage();

	File out = new File(lst.getDirectory(), "opt.xml");

	if (!out.exists()) {
	    FileUtils.touch(out);
	}

	OutputStream writer = lst.openOutputFile("opt.xml");

	XStream xstream = new XStream();
	xstream.autodetectAnnotations(true);

	String xml = xstream.toXML(this);

	IOUtils.write(xml, writer, "UTF-8");

	writer.close();

    }

    public void setFooterDistance(Float signatureAbsoluteTop) {
	this.footerDistance = signatureAbsoluteTop;
    }

    public Float getFooterDistance() {
	return footerDistance;
    }

    /**
     * Identifica o modo de Renderizar a assinatura no documento PDF
     * 
     * @return render mode
     */
    public RenderMode getRenderMode() {

	if (getImageImport() && getImage().exists()) {

	    if (getTextDate() || getTextLocal() || getTextName()
		    || getTextReason()) {
		return RenderMode.GraphicAndDescription;
	    } else {
		return RenderMode.Graphic;
	    }

	} else if (getImageName() || getImageCustom()) {

	    if (getTextDate() || getTextLocal() || getTextName()
		    || getTextReason()) {
		return RenderMode.NameAndDescription;
	    } else {
		return RenderMode.Name;
	    }

	} else {

	    return RenderMode.Description;

	}

    }

    public Boolean hasGraphic() {
	if (getRenderMode().equals(RenderMode.Graphic)
		|| getRenderMode().equals(RenderMode.GraphicAndDescription)) {
	    return true;
	} else {
	    return false;
	}
    }

    public Boolean hasDescription() {
	if (getRenderMode().equals(RenderMode.Description)
		|| getRenderMode().equals(RenderMode.GraphicAndDescription)
		|| getRenderMode().equals(RenderMode.NameAndDescription)) {
	    return true;
	} else {
	    return false;
	}
    }

    public PlainDescription getDescription() {

	PlainDescription pd = new PlainDescription(this);

	// TODO: ativar exibição de DN (distinguished name)

	StringBuffer description = new StringBuffer();

	if (getTextName()) {

	    if (getTextLabels()) {
		description.append("Assinado digitalmente por ");
	    }
	    description.append("$description.name$").append(newLine);

	}

	if (getTextDate()) {

	    if (getTextLabels()) {
		description.append("Data: ");
	    }
	    description.append("$description.date$").append(newLine);

	}

	if (getTextLocal()) {

	    if (getTextLabels()) {
		description.append("Local: ");
	    }
	    description.append("$description.local$").append(newLine);

	}

	if (getTextReason()) {

	    if (getTextLabels()) {
		description.append("Motivo: ");
	    }
	    description.append("$description.reason$").append(newLine);

	}

	pd.setTemplate(description.toString());

	return pd;

    }

    public Boolean isAbsolute() {
	return getSignaturePosition().equals(POSITION_ABSOLUTE);
    }

    public Boolean isRelative() {
	return getSignaturePosition().equals(POSITION_RELATIVE);
    }

    public void setImageCustom(Boolean imageCustom) {
	if (imageCustom)
	    setGraphicsOff();
	this.imageCustom = imageCustom;
    }

    private void setGraphicsOff() {
	setImageCustom(false);
	setImageImport(false);
	setImageName(false);
    }

    public Boolean getImageCustom() {
	if (imageCustom == null) {
	    return false;
	}
	return imageCustom;
    }

    public void setImageCustomText(String imageCustomText) {
	this.imageCustomText = imageCustomText;
    }

    public String getImageCustomText() {
	return imageCustomText;
    }

    public void setReferenceText(String referenceText) {
	this.referenceText = referenceText;
    }

    public String getReferenceText() {
	return referenceText;
    }

    public void setReferenceLastLine(Boolean referenceLastLine) {
	this.referenceLastLine = referenceLastLine;
    }

    public Boolean getReferenceLastLine() {
	if (referenceLastLine == null) {
	    return false;
	}
	return referenceLastLine;
    }

    public void setMaskRegex(String maskRegex) {
	this.maskRegex = maskRegex;
    }

    public String getMaskRegex() {
	return maskRegex;
    }

    public void setOutputMaskRegex(Boolean outputMaskRegex) {
	this.outputMaskRegex = outputMaskRegex;
    }

    public Boolean getOutputMaskRegex() {
	return outputMaskRegex == null ? false : outputMaskRegex;
    }

}
