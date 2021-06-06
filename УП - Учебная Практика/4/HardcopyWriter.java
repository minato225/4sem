import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class HardcopyWriter extends Writer {
    protected PrintJob job;
    protected Graphics page;
    protected String jobName;
    protected int fontSize;
    protected String time;
    protected Dimension pageSize;
    protected int pagedPi;
    protected Font font, headerFont;
    protected FontMetrics metrics;
    protected FontMetrics headerMetrics;
    protected int x0, y0;
    protected int width, height;            // Size (in dots) inside margins
    protected int headery;                  // Baseline of the page header
    protected int charWidth;                // The width of each character
    protected int lineHeight;               // The height of each line
    protected int lineAscent;
    protected int chars_per_line;           // Number of characters per line
    protected int lines_per_page;           // Number of lines per page
    protected int charNum = 0, lineNum = 0; // Current column and line position
    public int type;
    private boolean last_char_was_return = false;
    // A static variable that holds user preferences between print jobs
    protected static final Properties printProps = new Properties();
    static Dimension SIZE = new Dimension(600, 600);

    public HardcopyWriter(Frame frame, String name,int size, double lM, double rM, double tM, double bM) {
        Toolkit toolkit = frame.getToolkit();   // get Toolkit from Frame
        synchronized (printProps) {
            PageAttributes pa = new PageAttributes();
            pa.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
            JobAttributes ja = new JobAttributes();
            ja.setSides(JobAttributes.SidesType.TWO_SIDED_LONG_EDGE);
            job = toolkit.getPrintJob(frame, name, ja, pa);
            type = pa.getOrientationRequested() == PageAttributes.OrientationRequestedType.PORTRAIT ? 0 : 1;
        }

        pageSize = job.getPageDimension();      // query the page size
        pagedPi = job.getPageResolution();      // query the page resolution

        if (System.getProperty("os.name").regionMatches(true, 0, "windows", 0, 7)) {
            pagedPi = toolkit.getScreenResolution();
            pageSize = new Dimension((int)(8.5 * pagedPi), 11 * pagedPi);
            size = size * pagedPi / 72;
        }

        x0 = (int) (lM * pagedPi);
        y0 = (int) (tM * pagedPi);
        width = pageSize.width - (int) ((lM + rM) * pagedPi) - 200;
        height = pageSize.height - (int) ((tM + bM) * pagedPi);
        font = new Font("Arial", Font.PLAIN, size);
        metrics = frame.getFontMetrics(font);
        lineHeight = metrics.getHeight();
        lineAscent = metrics.getAscent();
        charWidth = metrics.charWidth('0');
        switch (type) {
            case 0:
                chars_per_line = width / charWidth - 1;
                lines_per_page = height / lineHeight - 10;
                break;
            case 1:
                chars_per_line = width / charWidth - 1;
                lines_per_page = height / lineHeight - 27;
                break;
        }
        headerFont = new Font("Arial", Font.ITALIC, size);
        headerMetrics = frame.getFontMetrics(headerFont);
        headery = y0 - (int) (0.125 * pagedPi) - headerMetrics.getHeight() + headerMetrics.getAscent();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        df.setTimeZone(TimeZone.getDefault());
        time = df.format(new Date());
        this.jobName = name;
        this.fontSize = size;
    }

    public void write(char[] buffer, int index, int len) {
        synchronized (this.lock) {
            for (int i = index; i < index + len; i++) {
                if (page == null)
                    newpage();

                if (buffer[i] == '\n') {
                    if (!last_char_was_return)
                        newline();
                    continue;
                }
                if (buffer[i] == '\r') {
                    newline();
                    last_char_was_return = true;
                    continue;
                } else
                    last_char_was_return = false;

                if (Character.isWhitespace(buffer[i]) && !Character.isSpaceChar(buffer[i]) && buffer[i] != '\t')
                    continue;
                if (charNum >= chars_per_line) {
                    newline();
                    if (page == null)
                        newpage();
                }
                if (Character.isSpaceChar(buffer[i]))
                    charNum++;
                else if (buffer[i] == '\t')
                    charNum += 8 - (charNum % 8);
                else {
                    page.drawChars(buffer, i, 1, x0 + charNum * charWidth, y0 + (lineNum * lineHeight) + lineAscent);
                    charNum++;
                }
            }
        }
    }

    public void flush() {  }

    public void close() {
        synchronized (this.lock) {
            if (page != null)
                page.dispose();
            job.end();
        }
    }

    protected void newpage() {
        page = job.getGraphics();
    }

    protected void newline() {
        charNum = 0;
        lineNum++;
        if (lineNum >= lines_per_page) {
            page.dispose();
            lineNum = 0;
            page = null;
        }
    }

    public static void printFile(FileReader in, HardcopyWriter out) {
        try {
            char[] buffer = new char[4096];
            int numChars;
            while ((numChars = in.read(buffer)) != -1)
                out.write(buffer, 0, numChars);
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }

    public void drawImage(BufferedImage img) {
        page.drawImage(img, pageSize.width / 2 - 200, y0, null);
        lineNum += SIZE.getHeight() / (lineHeight + lineAscent);
    }
}
